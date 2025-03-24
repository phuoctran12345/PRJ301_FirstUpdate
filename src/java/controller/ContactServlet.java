package controller;

import dao.UserDAO;
import model.Email;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/ContactServlet"}) // Định nghĩa URL pattern cho servlet
public class ContactServlet extends HttpServlet {

    // Đường dẫn tới trang contact.jsp
    private static final String CONTACT_PAGE = "view/jsp/home/contact.jsp";

    // Phương thức xử lý request chung cho cả GET và POST
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8"); // Thiết lập loại nội dung và encoding
        String url = CONTACT_PAGE; // URL mặc định để forward request

        try {
            String action = request.getParameter("action"); // Lấy tham số action từ request
            
            // Xử lý khi người dùng gửi form liên hệ
            if ("send".equals(action)) {
                // Lấy các thông tin từ form
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String subject = request.getParameter("subject");
                String message = request.getParameter("message");

                // Kiểm tra dữ liệu đầu vào
                if (validateInput(name, email, subject, message)) {
                    UserDAO userDAO = new UserDAO(); // Khởi tạo DAO để truy cập database
                    List<String> adminEmails = userDAO.getAdminEmails(); // Lấy danh sách email admin
                    
                    if (!adminEmails.isEmpty()) {
                        Email emailSender = new Email(); // Khởi tạo đối tượng gửi email
                        boolean sendSuccess = true;

                        // Gửi email cho từng admin
                        for (String adminEmail : adminEmails) {
                            try {
                                log("Đang gửi email đến admin: " + adminEmail); // Ghi log quá trình gửi
                                emailSender.sendEmail(
                                    "Thông tin liên hệ mới: " + subject, // Tiêu đề email
                                    createEmailContent(name, email, subject, message), // Nội dung email HTML
                                    adminEmail // Email người nhận
                                );
                                log("Gửi email thành công đến: " + adminEmail);
                            } catch (Exception e) {
                                sendSuccess = false;
                                log("Lỗi gửi email đến " + adminEmail + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        // Thiết lập thông báo kết quả
                        if (sendSuccess) {
                            request.setAttribute("CHECK", "success");
                            request.setAttribute("MESSAGE", "Gửi tin nhắn thành công!");
                        } else {
                            request.setAttribute("CHECK", "fail");
                            request.setAttribute("MESSAGE", "Gửi tin nhắn thất bại. Vui lòng thử lại.");
                        }
                    } else {
                        request.setAttribute("CHECK", "fail");
                        request.setAttribute("MESSAGE", "Không tìm thấy email admin.");
                        log("Không tìm thấy email admin trong database.");
                    }
                } else {
                    request.setAttribute("CHECK", "fail");
                    request.setAttribute("MESSAGE", "Vui lòng kiểm tra lại thông tin nhập.");
                }
            }
            
            request.setAttribute("CURRENTSERVLET", "Contact"); // Đánh dấu servlet hiện tại
        } catch (Exception ex) {
            // Xử lý lỗi chung
            log("Lỗi trong ContactServlet: " + ex.getMessage());
            ex.printStackTrace();
            request.setAttribute("CHECK", "fail");
            request.setAttribute("MESSAGE", "Đã xảy ra lỗi. Vui lòng thử lại.");
        }

        // Chuyển tiếp request tới trang JSP
        request.getRequestDispatcher(url).forward(request, response);
    }

    // Phương thức kiểm tra tính hợp lệ của dữ liệu đầu vào
    private boolean validateInput(String name, String email, String subject, String message) {
        // Kiểm tra null
        if (name == null || email == null || subject == null || message == null) {
            return false;
        }
        
        // Kiểm tra rỗng
        if (name.trim().isEmpty() || email.trim().isEmpty() 
            || subject.trim().isEmpty() || message.trim().isEmpty()) {
            return false;
        }
        
        // Regex kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    // Phương thức tạo nội dung email định dạng HTML
    private String createEmailContent(String name, String email, String subject, String message) {
        return String.format(
            "<html>" +
            "<head>" +
            "<meta charset='UTF-8'>" +
            "<style>" + // CSS để định dạng email
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }" +
            ".header { background: #1a237e; color: white; padding: 20px; border-radius: 5px 5px 0 0; text-align: center; }" +
            ".content { padding: 20px; background: #fff; }" +
            ".field { margin-bottom: 15px; padding: 10px; border-bottom: 1px solid #eee; }" +
            ".label { font-weight: bold; color: #1a237e; min-width: 100px; display: inline-block; }" +
            ".message-box { background: #f8f9fa; padding: 15px; border-radius: 5px; margin-top: 20px; border-left: 4px solid #1a237e; }" +
            ".footer { text-align: center; margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee; color: #666; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<div class='header'>" +
            "<h2 style='margin: 0;'>Thông Tin Liên Hệ Mới</h2>" +
            "</div>" +
            "<div class='content'>" +
            "<div class='field'><span class='label'>Người gửi:</span> %s</div>" +
            "<div class='field'><span class='label'>Email:</span> %s</div>" +
            "<div class='field'><span class='label'>Tiêu đề:</span> %s</div>" +
            "<div class='message-box'>" +
            "<div class='label'>Nội dung tin nhắn:</div>" +
            "<p>%s</p>" +
            "</div>" +
            "</div>" +
            "<div class='footer'>" +
            "<p>Email này được gửi tự động từ hệ thống ClothesShop</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>",
            name, email, subject, message.replace("\n", "<br/>") // Thay thế xuống dòng bằng thẻ HTML
        );
    }

    // Override phương thức doGet để xử lý request GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Override phương thức doPost để xử lý request POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}