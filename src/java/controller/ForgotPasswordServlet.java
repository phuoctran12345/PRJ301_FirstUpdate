package controller;

import dao.UserDAO;
import java.io.IOException;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Email;
import model.UserDTO;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPasswordServlet"})
public class ForgotPasswordServlet extends HttpServlet {

    private static final String FORGOT_PAGE = "view/jsp/home/forgot_password.jsp";  // Trang phục hồi mật khẩu

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = FORGOT_PAGE;
        HttpSession session = request.getSession();
        String emailInput = request.getParameter("txtEmail");  // Email người dùng nhập
        String txtCode = request.getParameter("txtCode");  // Mã xác nhận người dùng nhập
        String status = request.getParameter("status");  // Trạng thái hiện tại của quá trình
        String password = request.getParameter("txtPassword");  // Mật khẩu mới
        String confirm = request.getParameter("txtConfirm");  // Xác nhận mật khẩu mới
        UserDAO ud = new UserDAO();
        Email handleEmail = new Email();
        String message = "";
        String check = null;
        UserDTO user = null;
        String code_str = null;
        String emailsession = null;

        try {
            if ("forgot".equals(status)) {
                request.setAttribute("STATUS", status);
            }

            // Kiểm tra email khi người dùng nhập
            if (emailInput != null) {
                user = ud.getUserByEmail(emailInput);  // Tìm người dùng theo email
                if (user != null) {
                    Random random = new Random();
                    message = "EXIST - valid email, check your email to have resetcode";  // Thông báo email hợp lệ
                    check = "true";
                    status = "confirm";  // Cập nhật trạng thái
                    // Tạo mã xác nhận ngẫu nhiên
                    Integer code = 100000 + random.nextInt(900000);
                    code_str = code.toString();
                    // Gửi email chứa mã xác nhận
                    String subject = handleEmail.subjectForgotPass();
                    String msgEmail = handleEmail.messageFogot(user.getUserName(), code);
                    handleEmail.sendEmail(subject, msgEmail, emailInput);
                } else {
                    message = "NOT EXIST - Invalid email";  // Email không hợp lệ
                    check = "false";
                }
                emailsession = emailInput;
                session.setAttribute("email", emailsession);  // Lưu email vào session
            }

            // Kiểm tra mã xác nhận người dùng nhập
            if (txtCode != null) {
                code_str = (String) session.getAttribute("code");  // Lấy mã xác nhận từ session
                if (txtCode.equals(code_str)) {
                    message = "Valid code, enter your new password!";  // Mã hợp lệ, yêu cầu nhập mật khẩu mới
                    check = "true";
                    status = "enterpass";  // Cập nhật trạng thái
                } else {
                    message = "Invalid code, try again!";  // Mã không hợp lệ
                    check = "false";
                    status = "confirm";  // Quay lại màn hình xác nhận
                }
            }

            // Kiểm tra mật khẩu mới và xác nhận mật khẩu
            if (password != null && confirm != null) {
                if (password.equalsIgnoreCase(confirm)) {  // Kiểm tra mật khẩu và xác nhận
                    String email = (String) session.getAttribute("email");  // Lấy email từ session
                    user = ud.getUserByEmail(email);  // Lấy người dùng theo email
                    if (ud.updatePasswordUser(user, password)) {  // Cập nhật mật khẩu mới
                        message = "New password has been updated";  // Cập nhật thành công
                        check = "true";
                        status = "success";  // Cập nhật trạng thái thành công
                    } else {
                        message = "Error, please try again!";  // Lỗi khi cập nhật mật khẩu
                        check = "false";
                        status = "enterpass";  // Quay lại màn hình nhập mật khẩu
                    }
                } else {
                    message = "Passwords do not match, please try again!";  // Mật khẩu và xác nhận không trùng khớp
                    check = "false";
                    status = "enterpass";  // Quay lại màn hình nhập mật khẩu
                }
            }

            session.setAttribute("code", code_str);  // Lưu mã vào session
            request.setAttribute("check", check);
            request.setAttribute("message", message);
            request.setAttribute("STATUS", status);

        } catch (Exception ex) {
            log("ForgotPasswordServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);  // Chuyển tiếp đến trang đăng nhập
        }

    }

    // Các phương thức xử lý GET và POST
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý chức năng quên mật khẩu";
    }

}

