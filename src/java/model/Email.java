package model;

import java.time.LocalDateTime;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class Email {
    // Các hằng số cho cấu hình SMTP
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private final String EMAIL_FROM = "phuocthde180577@fpt.edu.vn";
    private final String PASSWORD = "hbyh pjwc avng gdho";

    // Kiểm tra email hợp lệ
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Phương thức gửi email chung
    public void sendEmail(String subject, String message, String to) throws MessagingException {
        if (subject == null || message == null || to == null) {
            throw new IllegalArgumentException("Các tham số không được null");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setContent(message, CONTENT_TYPE);
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new MessagingException("Lỗi gửi email: " + e.getMessage(), e);
        }
    }

    // Các phương thức tạo tiêu đề email
    public String subjectContact(String name) {
        return "ClothesShop - Chào " + name + " cảm ơn bạn vì đã liên hệ với chúng tôi";
    }
    
    public String subjectNewOrder() {
        return "ClothesShop - Đặt hàng thành công";
    }

    public String subjectSubsribe() {
        return "ClothesShop - Bạn có thông báo mới";
    }

    public String subjectForgotPass() {
        return "ClothesShop - Mã xác nhận khôi phục mật khẩu";
    }

    // Các phương thức tạo nội dung email
    public String messageContact(String name) {
        return createEmailTemplate("Cảm ơn bạn đã liên hệ",
                "<p>Chào " + name + ",</p>" +
                "<p>Cảm ơn bạn đã liên hệ với ClothesShop.</p>" +
                "<p>Chúng tôi sẽ phản hồi yêu cầu của bạn trong thời gian sớm nhất.</p>" +
                "<p>Nếu cần hỗ trợ gấp, vui lòng gọi hotline: 1900 9090</p>");
    }
    
//    public String messageNewOrder(String name, int quantity, double total) {
//        return createEmailTemplate("Đặt hàng thành công",
//                "<p>Chào " + name + ",</p>" +
//                "<p>Đơn hàng của bạn đã được xác nhận thành công.</p>" +
//                "<p>Chi tiết đơn hàng:</p>" +
//                "<ul>" +
//                "<li>Số lượng sản phẩm: <strong>" + quantity + "</strong></li>" +
//                "<li>Tổng tiền: <strong>" + String.format("%,.0f", total) + " VNĐ</strong></li>" +
//                "</ul>" +
//                "<p>Thời gian giao hàng dự kiến: 3-7 ngày</p>");
//    }
public String messageNewOrder(String name, int quantity, double total) {
    // Format total price with thousands separator
    String formattedTotal = String.format("%,.0f", total);
    
    return createEmailTemplate("Đặt hàng thành công",
            "<p>Chào " + name + ",</p>" +
            "<p>Đơn hàng của bạn đã được xác nhận thành công.</p>" +
            "<p>Chi tiết đơn hàng:</p>" +
            "<ul>" +
            "<li>Số lượng sản phẩm: <strong>" + quantity + "</strong></li>" +
            "<li>Tổng tiền: <strong>" + formattedTotal + " VNĐ</strong></li>" +
            "</ul>" +
            "<p>Thời gian giao hàng dự kiến: 3-7 ngày</p>");
}

    public String messageFogot(String name, int code) {
        return createEmailTemplate("Mã xác nhận khôi phục mật khẩu",
                "<p>Chào " + name + ",</p>" +
                "<p>Mã xác nhận của bạn là:</p>" +
                "<p class='code'>" + code + "</p>" +
                "<p>Mã này có hiệu lực trong 5 phút.</p>" +
                "<p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>");
    }

    public String messageSubscribe() {
        return createEmailTemplate("Đăng ký nhận tin thành công",
                "<p>Xin chào,</p>" +
                "<p>Cảm ơn bạn đã đăng ký nhận tin từ ClothesShop.</p>" +
                "<p>Bạn sẽ nhận được các thông tin về:</p>" +
                "<ul>" +
                "<li>Khuyến mãi mới nhất</li>" +
                "<li>Sản phẩm mới</li>" +
                "<li>Tin tức thời trang</li>" +
                "</ul>");
    }

    public String messageExpiredCode(String name) {
        return createEmailTemplate("Mã xác nhận đã hết hạn",
                "<p>Chào " + name + ",</p>" +
                "<p>Mã xác nhận của bạn đã hết hạn.</p>" +
                "<p>Vui lòng thực hiện lại quá trình khôi phục mật khẩu để nhận mã mới.</p>");
    }

    public String messagePasswordChanged(String name) {
        return createEmailTemplate("Đổi mật khẩu thành công",
                "<p>Chào " + name + ",</p>" +
                "<p>Mật khẩu của bạn đã được thay đổi thành công.</p>" +
                "<p>Nếu bạn không thực hiện thay đổi này, vui lòng liên hệ ngay với chúng tôi.</p>" +
                "<p>Hotline: 1900 9090</p>");
    }

    // Phương thức tạo template email chung
    private String createEmailTemplate(String title, String content) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; }"
                + ".container { max-width: 600px; margin: auto; padding: 20px; }"
                + ".header { background-color: #e67e22; color: white; padding: 20px; text-align: center; }"
                + ".content { padding: 20px; border: 1px solid #ddd; }"
                + ".code { font-size: 24px; color: #e67e22; font-weight: bold; text-align: center; }"
                + ".footer { text-align: center; font-size: 12px; color: #666; margin-top: 20px; }"
                + "ul { margin: 10px 0; padding-left: 20px; }"
                + "li { margin: 5px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h1>" + title + "</h1>"
                + "</div>"
                + "<div class='content'>"
                + content
                + "</div>"
                + "<div class='footer'>"
                + "<p>ClothesShop - Hỗ trợ: 1900 9090</p>"
                + "<p>Email: clothesshop@gmail.com</p>"
                + "<p>Địa chỉ: Ho Chi Minh City</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    // Phương thức test
    public static void main(String[] args) {
        Email email = new Email();
        try {
            // Test gửi email
            String testEmail = "thebao122004@gmail.com";
            if (email.isValidEmail(testEmail)) {
                email.sendEmail(
                    email.subjectForgotPass(),
                    email.messageFogot("Test User", 123456),
                    testEmail
                );
                System.out.println("Email sent successfully!");
            } else {
                System.out.println("Invalid email address!");
            }
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendEmail(String string, String createEmailContent, String adminEmail, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}