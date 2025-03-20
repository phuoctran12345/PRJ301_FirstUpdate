package controller;

import utils.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    
    public static String generateMBBankQR(String accountNumber, double amount, String description) {
        // Tạo nội dung QR code
        String qrContent = "MB Bank|" + accountNumber + "|" + amount + "|" + description;
        // Tạo thư mục lưu QR code nếu chưa tồn tại
        String uploadPath = "/Users/tranhongphuoc/IdeaProjects/ShopUpdate/web/assets/qrcodes";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        // Tạo tên file QR code
        String fileName = "thanhtoan_" + System.currentTimeMillis() + ".png";
        String filePath = uploadPath + File.separator + fileName;
        // Tạo QR code
        createQRImage(filePath, qrContent, 300, 300);
        // Trả về đường dẫn tương đối
        return "assets/qrcodes/" + fileName;
}

    private static void generateQRCode(String data, String path) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, hints);

            BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            File qrFile = new File(path);
            qrFile.getParentFile().mkdirs();
            ImageIO.write(image, "PNG", qrFile);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void createQRImage(String filePath, String qrContent, int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}