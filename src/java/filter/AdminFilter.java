package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;

/**
 * Filter để kiểm tra quyền admin
 */
public class AdminFilter implements Filter {

    // Khởi tạo filter
    public AdminFilter() {}

    // Thực hiện kiểm tra trước khi tiếp tục chuỗi filter
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (session.getAttribute("account") == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            res.sendRedirect("DispatchServlet?btnAction=Login");
            return; // Dừng lại ở đây, không tiếp tục chuỗi filter
        }

        // Lấy đối tượng người dùng từ session
        UserDTO user = (UserDTO) session.getAttribute("account");

        // Kiểm tra xem người dùng có phải là admin không (roleID = 1)
        if (user.getRoleID() != 1) {
            // Nếu không phải admin, chuyển hướng đến trang lỗi
            res.sendRedirect("ErrorServlet");
            return; // Dừng lại ở đây, không tiếp tục chuỗi filter
        }

        // Nếu người dùng là admin, cho phép tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }

    // Khởi tạo filter (nếu cần)
    public void init(FilterConfig filterConfig) throws ServletException {}

    // Phá hủy filter khi không cần thiết nữa
    public void destroy() {}
}
