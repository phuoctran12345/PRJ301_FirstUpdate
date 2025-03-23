package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;

public class VNPayConfig {

    public static final String vnp_Version = "2.1.0";
    public static final String vnp_Command = "pay";
    public static final String vnp_TmnCode = "03ZW1F8X";
    public static final String vnp_HashSecret = "7QKB7FZT7BNEE7K2ZHM7IXEYQKCIWRL5";
    public static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "http://localhost:8080/ShopUpdate/VNPayReturn";
    public static final String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAddress = "Invalid IP:" + e.getMessage();
        }
        return ipAddress;
    }

    public static String getQueryUrl(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        
        String queryUrl = hashData.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        
        return queryUrl;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Testing VNPAY connection...");
            testConnection(vnp_ApiUrl, "API URL");
            testConnection(vnp_PayUrl, "Payment URL");
            testConnection(vnp_ReturnUrl, "Return URL");
            testHashSecret();
        } catch (Exception e) {
            System.out.println("Error testing VNPAY configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testConnection(String urlStr, String name) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (urlStr.equals(vnp_ApiUrl)) {
                String testData = "{\"orderId\":\"test123\"}";
                byte[] postData = testData.getBytes(StandardCharsets.UTF_8);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(postData);
                    os.flush();
                }
            } else {
                conn.setRequestMethod("GET");
            }

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            System.out.println(name + " Kiểm tra kết nối:");
            System.out.println("Mã phản hồi: " + responseCode);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode < 400 ? conn.getInputStream() : conn.getErrorStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                if (response.length() > 0) {
                    System.out.println("Phản hồi: " + response.toString());
                }
            }

            System.out.println("Trạng thái: " + (responseCode < 400 ? "THÀNH CÔNG" : "THẤT BẠI"));
            System.out.println("------------------------");

        } catch (Exception e) {
            System.out.println(name + " Kết nối THẤT BẠI:");
            System.out.println("Lỗi: " + e.getMessage());
            System.out.println("------------------------");
        }
    }

    private static void testHashSecret() {
        try {
            String testData = "test_" + new Random().nextInt(1000);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((testData + vnp_HashSecret).getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            System.out.println("Hash Secret Test:");
            System.out.println("Test Data: " + testData);
            System.out.println("Hashed Result: " + hexString.toString());
            System.out.println("Status: SUCCESS");
            System.out.println("------------------------");

        } catch (Exception e) {
            System.out.println("Hash Secret Test FAILED:");
            System.out.println("Error: " + e.getMessage());
            System.out.println("------------------------");
        }
    }
}