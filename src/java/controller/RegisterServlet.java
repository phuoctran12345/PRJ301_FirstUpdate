package controller;

import dao.UserDAO;
import model.UserDTO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDTO;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Output simple HTML page for debugging (optional)
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward the request to the login page for registration
        request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // Get parameters from the registration form
            String fName = request.getParameter("firstname");
            String lName = request.getParameter("lastname");
            String uName = request.getParameter("username");
            String uPass = request.getParameter("password");
            String email = request.getParameter("email");
            String avatar = request.getParameter("avatar");

            // Action used for checking duplicate username
            String action = request.getParameter("action");

            // Instantiate DAO object for checking and registering users
            UserDAO ud = new UserDAO();
            String message;

            // Check if the action is to verify if the username is a duplicate
            if (action != null && action.equals("CheckDuplicate")) {
                PrintWriter out = response.getWriter();
                boolean isDuplicate = ud.checkUserNameDuplicate(uName);
                if (isDuplicate) {
                    out.println("<h6 style='color: red'>Username already exists!</h6>");
                } else {
                    out.println("<h6 style='color: green'>Username is available!</h6>");
                }
                return;
            }

            // Check if the username is already taken
            boolean isDup = ud.checkUserNameDuplicate(uName);
            if (isDup) {
                message = "Username already exists!";
                request.setAttribute("ERROR", message);
                request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
            } else {
                // Create a new UserDTO object for the new user
                UserDTO user = new UserDTO(0, fName, lName, email, 
                        (avatar == null ? "assets/img/users/user.jpg" : avatar), uName, uPass, "", "", 2, true);

                // Register the user in the database
                ud.registerUser(user);
                message = "Register successfully. Please Login!";

                // Set success message and forward to login page
                request.setAttribute("SUCCESS", message);
                request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            // Log the error for debugging purposes
            log("RegisterServlet error: " + ex.getMessage());
            request.setAttribute("ERROR", "There was an error during registration. Please try again.");
            request.getRequestDispatcher("view/jsp/home/login.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for user registration";
    }
}
