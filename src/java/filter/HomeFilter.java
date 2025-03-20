package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple filter to handle redirection for .jsp pages.
 */
@WebFilter(filterName = "HomeFilter", urlPatterns = {"/*"})
public class HomeFilter implements Filter {

    private FilterConfig filterConfig = null;

    public HomeFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getServletPath();

        // Redirect if the URL ends with .jsp
        if (url.endsWith(".jsp")) {
            res.sendRedirect("ErrorServlet");
            return;
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public String toString() {
        return (filterConfig == null) ? "HomeFilter()" : "HomeFilter(" + filterConfig + ")";
    }
}
