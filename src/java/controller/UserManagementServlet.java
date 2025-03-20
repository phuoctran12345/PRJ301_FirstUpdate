package controller;

import dao.UserDAO;
import model.UserDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UserManagementServlet", urlPatterns = {"/UserManagementServlet"})
public class UserManagementServlet extends HttpServlet {

    private static final String MANAGE_USER_PAGE = "view/jsp/admin/admin_users.jsp";
    private static final String INSERT_USER_PAGE = "view/jsp/admin/admin_user_insert.jsp";
    private static final String EDIT_USER_PAGE = "view/jsp/admin/admin_edit_user.jsp";
    private static final String UPLOAD_DIR = "view/assets/home/img/users/";

    // Regex patterns for validation
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_PATTERN = "^[0-9]{10}$"; // Assumes 10-digit phone number

    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    private boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = MANAGE_USER_PAGE;
        String action = request.getParameter("action");

        try {
            UserDAO userDao = new UserDAO();

             if ("Insert".equals(action)) {
                url = showInsertUserPage(request);
            } else if ("Edit".equals(action)) {
                url = showEditUserPage(request, userDao);
            } else if ("Update".equals(action)) {
                url = updateUser(request, userDao);
            } else if ("Delete".equals(action)) {
                url = deleteUser(request, userDao);
            } else if ("InsertUser".equals(action)) {
                url = insertNewUser(request, userDao);
            } else if ("Search".equals(action)) {  // Add search action
                url = searchUsers(request, userDao);
            } else if ("Restore".equals(action)) {  // Thêm action Restore
                url = restoreUser(request, userDao);
            } else if ("PermanentlyDelete".equals(action)) {  // Thêm action PermanentlyDelete
                url = permanentlyDeleteUser(request, userDao);
            } else {
                url = showUserList(request, userDao);
            }

        } catch (Exception ex) {
            log("UserManagementServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private String showUserList(HttpServletRequest request, UserDAO userDao)
            throws ServletException, IOException, SQLException {
        List<UserDTO> userList = userDao.getData();
        request.setAttribute("LISTUSERS", userList);
        request.setAttribute("CURRENTSERVLET", "User");
        return MANAGE_USER_PAGE;
    }

    private String showInsertUserPage(HttpServletRequest request)
            throws ServletException, IOException {
        return INSERT_USER_PAGE;
    }

    private String showEditUserPage(HttpServletRequest request, UserDAO userDao)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        UserDTO user = userDao.getUserByName(username);
        request.setAttribute("username", user.getUserName());
        request.setAttribute("firstname", user.getFirstName());
        request.setAttribute("lastname", user.getLastName());
        request.setAttribute("phone", user.getPhone());
        request.setAttribute("roleid", user.getRoleID());
        request.setAttribute("address", user.getAddress());
        request.setAttribute("email", user.getEmail());
        request.setAttribute("avatar", user.getAvatar());
        return EDIT_USER_PAGE;
    }

    private String deleteUser(HttpServletRequest request, UserDAO userDao)
            throws ServletException, IOException, SQLException {
        String uid = request.getParameter("uid");
        userDao.deleteUser(uid);
        request.setAttribute("mess", "Xóa người dùng thành công!");
        List<UserDTO> userList = userDao.getData();
        request.setAttribute("LISTUSERS", userList);
        request.setAttribute("CURRENTSERVLET", "User");
        return MANAGE_USER_PAGE;
    }

    private String insertNewUser(HttpServletRequest request, UserDAO userDao)
            throws ServletException, IOException, SQLException, Exception {
        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        String avatar = request.getParameter("avatar");

        if (userDao.checkUserNameDuplicate(username)) {
            setUserAttributes(request, username, fullName, "", phone, email, address, avatar, role);
            request.setAttribute("error", "Tên tài khoản đã tồn tại!");
            return INSERT_USER_PAGE;
        } else if (!isValidEmail(email)) {
            setUserAttributes(request, username, fullName, "", phone, email, address, avatar, role);
            request.setAttribute("error", "Email không hợp lệ!");
            return INSERT_USER_PAGE;
        } else if (!isValidPhone(phone)) {
            setUserAttributes(request, username, fullName, "", phone, email, address, avatar, role);
            request.setAttribute("error", "Số điện thoại không hợp lệ! (Cần 10 chữ số)");
            return INSERT_USER_PAGE;
        } else {
            String firstName = fullName.split(" ")[0];
            String lastName = String.join(" ", Arrays.copyOfRange(fullName.split(" "), 1, fullName.split(" ").length));
            int roleId = "admin".equals(role) ? 1 : 2;

            if (avatar != null && !avatar.isEmpty()) {
                avatar = UPLOAD_DIR + avatar;
            }
            UserDTO user = new UserDTO(0, firstName, lastName, email, avatar, username, password, address, phone, roleId, true);
            userDao.registerUser(user);
            request.setAttribute("mess", "Thêm người dùng thành công!");
            List<UserDTO> userList = userDao.getData();
            request.setAttribute("LISTUSERS", userList);
            request.setAttribute("CURRENTSERVLET", "User");
            return MANAGE_USER_PAGE;
        }
    }

    private String updateUser(HttpServletRequest request, UserDAO userDao)
            throws ServletException, IOException, SQLException, Exception {
        String username = request.getParameter("username");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String avatar = request.getParameter("avatar");

        // Validation email và phone
        if (!isValidEmail(email)) {
            setUserAttributes(request, username, firstname, lastname, phone, email, address, avatar, role);
            request.setAttribute("error", "Email không hợp lệ!");
            return EDIT_USER_PAGE;
        } else if (!isValidPhone(phone)) {
            setUserAttributes(request, username, firstname, lastname, phone, email, address, avatar, role);
            request.setAttribute("error", "Số điện thoại không hợp lệ! (Cần 10 chữ số)");
            return EDIT_USER_PAGE;
        } else {
            if (avatar != null && !avatar.isEmpty()) {
                avatar = UPLOAD_DIR + avatar;
            } else {
                avatar = userDao.getUserByName(username).getAvatar();
            }

            int roleId = "admin".equals(role) ? 1 : 2;
            userDao.updateUser(firstname, lastname, email, address, phone, username, avatar, roleId);
            request.setAttribute("mess", "Cập nhật người dùng thành công!");
            List<UserDTO> userList = userDao.getData();
            request.setAttribute("LISTUSERS", userList);
            request.setAttribute("CURRENTSERVLET", "User");
            return MANAGE_USER_PAGE;
        }
    }
        private String searchUsers(HttpServletRequest request, UserDAO userDao) 
            throws ServletException, IOException, SQLException, Exception {
        String searchQuery = request.getParameter("search");
        List<UserDTO> userList;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            userList = userDao.searchUsers(searchQuery);
            request.setAttribute("searchQuery", searchQuery);
        } else {
            userList = userDao.getData();
        }

        request.setAttribute("LISTUSERS", userList);
        request.setAttribute("CURRENTSERVLET", "User");
        return MANAGE_USER_PAGE;
    }
    // Thiết lập các thuộc tính để giữ dữ liệu khi có lỗi
    private void setUserAttributes(HttpServletRequest request, String username, String firstname, 
            String lastname, String phone, String email, String address, String avatar, String role) {
        request.setAttribute("username", username);
        request.setAttribute("firstname", firstname);
        request.setAttribute("lastname", lastname);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("avatar", avatar);
        request.setAttribute("role", role);
    }
    private String restoreUser(HttpServletRequest request, UserDAO userDao)
        throws ServletException, IOException, SQLException, Exception {
    String uid = request.getParameter("uid");
    userDao.restoreUser(uid);
    request.setAttribute("mess", "Khôi phục người dùng thành công!");
    List<UserDTO> userList = userDao.getData();
    request.setAttribute("LISTUSERS", userList);
    request.setAttribute("CURRENTSERVLET", "User");
    return MANAGE_USER_PAGE;
    }
    
    private String permanentlyDeleteUser(HttpServletRequest request, UserDAO userDao)
        throws ServletException, IOException, SQLException, Exception {
    String uid = request.getParameter("uid");
    userDao.permanentlyDeleteUser(uid);
    request.setAttribute("mess", "Xóa vĩnh viễn người dùng thành công!");
    List<UserDTO> userList = userDao.getData();
    request.setAttribute("LISTUSERS", userList);
    request.setAttribute("CURRENTSERVLET", "User");
    return MANAGE_USER_PAGE;
    }
    
    
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
        return "User Management Servlet";
    }
}