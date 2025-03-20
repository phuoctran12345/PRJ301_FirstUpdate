package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDTO;
import java.io.IOException;

public class EditProfileServlet extends HttpServlet {

    private static final String PROFILE_PAGE = "view/jsp/home/my-account.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form chỉnh sửa
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("account");
        
        if (user != null) {
            request.setAttribute("EDIT_MODE", true); // Đánh dấu đang ở chế độ chỉnh sửa
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } else {
            response.sendRedirect("login"); // Redirect nếu chưa đăng nhập
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Get user input from the form
            String firstName = request.getParameter("first-name");
            String lastName = request.getParameter("last-name");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String avatar = request.getParameter("avatar");

            // Get current user from session
            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("account");

            // Update user in the database
            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(firstName, lastName, email, address, phone, user.getUserName(), avatar, user.getRoleID());

            // Refresh user session with updated data
            user = userDAO.checkLogin(user.getUserName(), user.getPassword());
            session.setAttribute("account", user);

            // Set success message
            request.setAttribute("STATUS", "Update successfully!!!");
            request.setAttribute("EDIT_MODE", false); // Quay về chế độ xem

        } catch (Exception ex) {
            log("Error in EditProfileServlet: " + ex.getMessage());
            request.setAttribute("STATUS", "Update failed: " + ex.getMessage());
        }

        // Forward to profile page
        request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles user profile updates";
    }
}