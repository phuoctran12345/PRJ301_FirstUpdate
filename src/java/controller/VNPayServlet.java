package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.VNPayConfig;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/VNPayServlet")
public class VNPayServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy tham số
        String soTienStr = request.getParameter("amount");
        long soTien = Long.parseLong(soTienStr) * 100; // Chuyển đổi sang định dạng của VNPay (VND x 100)
        
        // Thiết lập các tham số VNPay
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String loaiDonHang = "other";
        String maNganHang = "NCB"; // Có thể thay đổi động nếu cần
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(soTien));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", maNganHang);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", loaiDonHang);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo chuỗi dữ liệu để mã hóa và chuỗi truy vấn
        List<String> tenTruong = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(tenTruong);
        StringBuilder duLieuHash = new StringBuilder();
        StringBuilder truyVan = new StringBuilder();
        
        Iterator<String> itr = tenTruong.iterator();
        while (itr.hasNext()) {
            String ten = itr.next();
            String giaTri = vnp_Params.get(ten);
            if ((giaTri != null) && (giaTri.length() > 0)) {
                duLieuHash.append(ten).append('=')
                    .append(URLEncoder.encode(giaTri, StandardCharsets.US_ASCII));
                truyVan.append(URLEncoder.encode(ten, StandardCharsets.US_ASCII))
                    .append('=').append(URLEncoder.encode(giaTri, StandardCharsets.US_ASCII));
                
                if (itr.hasNext()) {
                    truyVan.append('&');
                    duLieuHash.append('&');
                }
            }
        }

        String urlTruyVan = truyVan.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, duLieuHash.toString());
        urlTruyVan += "&vnp_SecureHash=" + vnp_SecureHash;
        String urlThanhToan = VNPayConfig.vnp_PayUrl + "?" + urlTruyVan;

        // Lưu thông tin cần thiết vào session để xử lý khi quay lại
        HttpSession session = request.getSession();
        session.setAttribute("vnp_TxnRef", vnp_TxnRef);
        session.setAttribute("vnp_Amount", soTien);

        // Chuyển hướng đến cổng thanh toán VNPay
        response.sendRedirect(urlThanhToan);
    }
}