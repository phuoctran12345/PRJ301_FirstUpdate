/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;




import model.UserGoogleDTO;
import model.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import model.UserDTO;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private final String WELCOME = "DispatchServlet";
    private final String LOGIN = "view/jsp/home/login.jsp";
    private final String ADMIN_DASHBOARD = "AdminServlet";
    private final String REGISTER_CONTROLLER = "RegisterServlet";

    // Xử lý đăng nhập với Google
    private String getToken(String code) throws IOException {
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE)
                        .build())
                .execute().returnContent().asString();

        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    private UserGoogleDTO getUserInfo(String accessToken) throws IOException {
        String response = Request.Get(Constants.GOOGLE_LINK_GET_USER_INFO + accessToken).execute().returnContent().asString();
        return new Gson().fromJson(response, UserGoogleDTO.class);
    }

    // Xử lý HTTP GET request
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String url = WELCOME;
    try {
        String code = request.getParameter("code");

        if (code == null) {
            // Hiển thị trang login nếu không có mã từ Google
            handleCookies(request);
            url = LOGIN;
        } else {
            // Đăng nhập với Google
            handleGoogleLogin(request, response, code);
            return; // Thêm return để ngăn chặn forward sau khi redirect
        }
    } catch (Exception ex) {
        log("LoginServlet error:" + ex.getMessage());
    } finally {
        if (!response.isCommitted()) { // Kiểm tra nếu phản hồi chưa được cam kết
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}


    // Xử lý đăng nhập với Google
private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response, String code) throws IOException, SQLException, ServletException {
    String accessToken = getToken(code);
    UserGoogleDTO userGoogle = getUserInfo(accessToken);

    if (userGoogle != null) {
        UserDAO userDAO = new UserDAO();
        UserDTO account = userDAO.getUserByEmail(userGoogle.getEmail());

        if (account != null) {
            // Đăng nhập thành công, lưu thông tin vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            if (account.getRoleID() == 1) {
                response.sendRedirect(ADMIN_DASHBOARD);
            } else {
                response.sendRedirect(WELCOME);
            }
            return; // Thêm return để ngăn chặn forward sau khi redirect
        } else {
            // Nếu chưa có tài khoản, yêu cầu đăng ký
            handleGoogleRegistration(request, userGoogle);
            request.getRequestDispatcher(REGISTER_CONTROLLER).forward(request, response);
        }
    }
}

    // Xử lý đăng ký người dùng Google nếu chưa có tài khoản
    private void handleGoogleRegistration(HttpServletRequest request, UserGoogleDTO userGoogle) {
        request.setAttribute("msgRegisterGG", "You need to register your account!");
        request.setAttribute("emailGG", userGoogle.getEmail());
        request.setAttribute("firstNameGoogleAccount", userGoogle.getGiven_name());
        request.setAttribute("lastNameGoogleAccount", userGoogle.getFamily_name());
        request.setAttribute("avatar", userGoogle.getPicture());
    }
    // Xử lý cookie trong trang login


    // Xử lý HTTP POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = WELCOME;
        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");
            String remember = request.getParameter("remember");

            UserDAO userDAO = new UserDAO();
            UserDTO user = userDAO.checkLogin(username, password);

            if (user != null) {
                // Đăng nhập thành công, lưu thông tin vào session và cookie
                HttpSession session = request.getSession();
                session.setAttribute("account", user);
                handleRememberMe(request, response, username, password, remember);

                if (user.getRoleID() == 1) {
                    response.sendRedirect(ADMIN_DASHBOARD);
                } else {
                    response.sendRedirect(WELCOME);
                }
            } else {
                // Đăng nhập thất bại
                String error = "Invalid username or password!";
                request.setAttribute("msg", error);
                request.getRequestDispatcher(LOGIN).forward(request, response);
            }
        } catch (Exception ex) {
            log("LoginServlet error:" + ex.getMessage());
        }
    }
     private void handleRememberMe(HttpServletRequest request, HttpServletResponse response, String username, String password, String remember) {
    // Tạo các cookie để lưu thông tin đăng nhập
    Cookie u = new Cookie("cUName", username);
    Cookie p = new Cookie("cUPass", encryptPassword(password));
    Cookie r = new Cookie("reMem", remember);
    
    // Thiết lập đường dẫn cho cookies để có thể truy cập từ mọi nơi trong ứng dụng
    u.setPath("/");
    p.setPath("/");
    r.setPath("/");
    
    // Thiết lập các cờ bảo mật cho cookies
    // setSecure(true): Cookie chỉ được gửi qua HTTPS
    // setHttpOnly(true): Cookie không thể truy cập bằng JavaScript
    u.setSecure(true);
    u.setHttpOnly(true);
    p.setSecure(true);
    p.setHttpOnly(true);
    r.setSecure(true);
    r.setHttpOnly(true);
    
    // Kiểm tra xem người dùng có chọn "Ghi nhớ đăng nhập" không
    if (remember != null && "on".equals(remember.trim())) {
        // Thiết lập thời gian sống cho cookies (3 tháng)
        int maxAge = 60 * 60 * 24 * 30 * 3;
        u.setMaxAge(maxAge);
        p.setMaxAge(maxAge);
        r.setMaxAge(maxAge);
    } else {
        // Nếu không chọn "Ghi nhớ đăng nhập", xóa cookies
        u.setMaxAge(0);
        p.setMaxAge(0);
        r.setMaxAge(0);
    }

    // Thêm cookies vào response
    response.addCookie(u);
    response.addCookie(p);
    response.addCookie(r);
}

// Phương thức mã hóa mật khẩu trước khi lưu vào cookie
private String encryptPassword(String password) {
    try {
        // Sử dụng SHA-256 để mã hóa mật khẩu
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // Chuyển đổi mảng byte thành chuỗi Base64
        return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
        // Ghi log nếu có lỗi trong quá trình mã hóa
        log("Lỗi mã hóa: " + e.getMessage());
        return "";
    }
}

// Phương thức đọc cookies và thiết lập giá trị cho form đăng nhập
private void handleCookies(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            // Đọc và thiết lập giá trị username
            if ("cUName".equals(cookie.getName())) {
                request.setAttribute("uName", cookie.getValue());
            }
            // Đọc và thiết lập giá trị password đã được mã hóa
            if ("cUPass".equals(cookie.getName())) {
                request.setAttribute("uPass", cookie.getValue());
            }
            // Đọc và thiết lập trạng thái "Ghi nhớ đăng nhập"
            if ("reMem".equals(cookie.getName())) {
                request.setAttribute("reMem", cookie.getValue());
            }
        }
    }
}

    @Override
    public String getServletInfo() {
        return "Login Servlet";
    }
}
