package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Email;

/**
 * Servlet xử lý việc gửi email liên hệ và đăng ký bản tin.
 */
@WebServlet(name = "EmailServlet", urlPatterns = {"/EmailServlet"})
public class EmailServlet extends HttpServlet {

    private static final String CONTACT_PAGE = "view/jsp/home/contact.jsp";  // Trang liên hệ
    private static final String HOME_PAGE = "view/jsp/home/home.jsp";        // Trang chủ (không sử dụng trong trường hợp này)
    private static final String NEWSLETTER_AJAX = "view/jsp/ajax/newsletter_ajax.jsp";  // Trang trả về khi đăng ký bản tin

    /**
     * Phương thức xử lý các yêu cầu HTTP.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");  // Đặt kiểu trả về là HTML với encoding UTF-8
        
        String url = CONTACT_PAGE;  // Mặc định trả về trang liên hệ
        Email handleEmail = new Email();  // Khởi tạo đối tượng Email để xử lý việc gửi email
        
        // Thông báo và trạng thái mặc định
        String message = "Có vẻ như một số thông tin cung cấp của bạn không hợp lệ, vui lòng cung cấp lại thông tin";
        String check = "fail";
        
        // Lấy các tham số từ request
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String txt = request.getParameter("message");
        
        try {
            if (action.equals("sendEmail")) {  // Nếu người dùng gửi email liên hệ
                // Kiểm tra tính hợp lệ của email
                if (handleEmail.isValidEmail(email)) {
                    message = "Cảm ơn bạn đã liên hệ với chúng tôi, chúng tôi sẽ kết nối với bạn trong thời gian sớm nhất";
                    check = "success";  // Nếu hợp lệ, trả về thông báo thành công
                    
                    // Tạo subject và message cho email
                    String sub = handleEmail.subjectContact(name);
                    String msg = handleEmail.messageContact(name);
                    
                    // Gửi email cho người dùng
                    handleEmail.sendEmail(sub, msg, email);
                }
            } else if (action.equals("subscribe")) {  // Nếu người dùng đăng ký nhận bản tin
                url = NEWSLETTER_AJAX;  // Trả về trang AJAX khi đăng ký
                
                // Kiểm tra tính hợp lệ của email
                if (handleEmail.isValidEmail(email)) {
                    message = "Cảm ơn bạn đã đăng ký nhận bản tin";
                    check = "success";  // Nếu hợp lệ, trả về thông báo thành công
                    
                    // Tạo subject và message cho email
                    String sub = handleEmail.subjectSubsribe();
                    String msg = handleEmail.messageSubscribe();
                    
                    // Gửi email cho người dùng
                    handleEmail.sendEmail(sub, msg, email);
                }
            }
        } catch (Exception ex) {
            log("EmailServlet error:" + ex.getMessage());  // Ghi lại lỗi nếu có
        } finally {
            // Gửi thông báo và dữ liệu về trang cần trả về
            request.setAttribute("MESSAGE", message);
            request.setAttribute("CHECK", check);
            request.setAttribute("NAME_CUSTOMER", name);
            request.setAttribute("EMAIL_CUSTOMER", email);
            request.setAttribute("TEXT", txt);
            request.getRequestDispatcher(url).forward(request, response);  // Chuyển hướng đến trang tương ứng
        }
    }

    // Phương thức xử lý yêu cầu GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Phương thức xử lý yêu cầu POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Phương thức trả về mô tả ngắn gọn của servlet
    @Override
    public String getServletInfo() {
        return "Servlet xử lý gửi email và đăng ký bản tin";
    }
}
