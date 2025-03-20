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

/**
 * Filter kiểm tra người dùng đã đăng nhập chưa
 */
public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    // Khởi tạo filter
    public LoginFilter() {}

    // Xử lý trước khi tiếp tục chuỗi filter
    private void doBeforeProcessing(ServletRequest request, ServletResponse response) {
        // Có thể thêm xử lý trước khi chuỗi filter tiếp theo được thực thi nếu cần
    }

    // Xử lý sau khi tiếp tục chuỗi filter
    private void doAfterProcessing(ServletRequest request, ServletResponse response) {
        // Có thể thêm xử lý sau khi chuỗi filter được thực thi nếu cần
    }

    /**
     * Kiểm tra người dùng đã đăng nhập chưa
     * @param request Servlet request
     * @param response Servlet response
     * @param chain Filter chain
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (session.getAttribute("account") == null) {
            res.sendRedirect("DispatchServlet?btnAction=Login");
            return; // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập và dừng chuỗi filter
        }

        // Nếu đã đăng nhập, tiếp tục chuỗi filter
        doBeforeProcessing(request, response);
        chain.doFilter(request, response);
        doAfterProcessing(request, response);
    }

    // Khởi tạo filter
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    // Phá hủy filter khi không cần thiết nữa
    public void destroy() {}
}
