package controller;

import service.CartService; // Replace CartUtil with CartService
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.PaymentDAO;
import dao.ProductDAO;
import model.CartItem;
import model.Email;
import model.OrderDTO;
import model.PaymentDTO;
import model.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/CheckoutServlet"})
public class CheckoutServlet extends HttpServlet {

    private static final String CHECKOUT_PAGE = "view/jsp/home/checkout.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleCheckout(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleCheckout(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        // Initialize DAOs and utilities
        PaymentDAO paymentDAO = new PaymentDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        CartService cartService = new CartService(); // Replace CartUtil with CartService
        Email emailSender = new Email();

        // Get session and basic data
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("account");
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("CART");
        String paymentId = request.getParameter("check_method");
        List<PaymentDTO> paymentMethods = paymentDAO.getPaymentData();

        // Variables to store results
        String message = "";
        String isSuccess = "false";
        double totalPrice = 0;
        int totalItems = 0;

        try {
            // Check if user is logged in, not an admin, and payment method is selected
            if (user == null) {
                message = "Please log in to checkout.";
            } else if (user.getRoleID() == 1) {
                message = "Admins cannot checkout.";
            } else if (paymentId == null) {
                message = "Please select a payment method.";
            } else {
                // Calculate total price and items
                totalPrice = calculateTotal(cartItems, productDAO);
                totalItems = calculateTotalQuantity(cartItems);

                // Get current date and time
                String currentDate = getFormattedDate();

                // Get selected payment method
                PaymentDTO payment = paymentDAO.getPaymentById(Integer.parseInt(paymentId));

                // Create new order
                if (orderDAO.CreateNewOrder(currentDate, totalPrice, payment, user)) {
                    message = "Order placed successfully!";
                    isSuccess = "true";

                    // Get the latest order
                    OrderDTO latestOrder = orderDAO.getTheLatestOrder();

                    // Send confirmation email
                    // Trong phương thức handleCheckout
// Send confirmation email
                    String emailSubject = emailSender.subjectNewOrder();
                    String emailMessage = emailSender.messageNewOrder(user.getFirstName(), totalItems, totalPrice, cartItems); // Thêm cartItems
                    emailSender.sendEmail(emailSubject, emailMessage, user.getEmail());

                    // Save order details and update stock
                    updateOrderDetailsAndStock(cartItems, latestOrder, orderItemDAO, productDAO);

                    // Clear cart
                    clearCart(session, request, response, cartService); // Use cartService instead of cartUtil
                } else {
                    message = "Failed to place order.";
                }
            }

            // Set attributes for the JSP page
            request.setAttribute("PAYMENTS", paymentMethods);
            request.setAttribute("MESSAGE", message);
            request.setAttribute("CHECK", isSuccess);

        } catch (Exception e) {
            log("CheckoutServlet Error: " + e.getMessage());
        }

        // Forward to checkout page
        request.getRequestDispatcher(CHECKOUT_PAGE).forward(request, response);
    }

    // Calculate total price of items in cart
    private double calculateTotal(List<CartItem> cartItems, ProductDAO productDAO) throws SQLException {
        double total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                int stock = productDAO.getStock(item.getProduct().getId());
                if (stock > 5 && item.getQuantity() <= stock) {
                    total += item.getQuantity() * item.getProduct().getSalePrice();
                }
            }
        }
        return total;
    }

    // Calculate total quantity of items in cart
    private int calculateTotalQuantity(List<CartItem> cartItems) {
        int total = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                total += item.getQuantity();
            }
        }
        return total;
    }

    // Get current date in formatted string
    private String getFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // Save order details and update product stock
    private void updateOrderDetailsAndStock(List<CartItem> cartItems, OrderDTO order,
            OrderItemDAO orderItemDAO, ProductDAO productDAO) throws SQLException {
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                orderItemDAO.createNewOrderDetail(item, order);
                productDAO.updateQuanityProduct(item);
            }
        }
    }

    // Clear the cart after successful checkout
    private void clearCart(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, CartService cartService) { // Replace CartUtil with CartService
        session.setAttribute("CART", null);
        Cookie cartCookie = cartService.getCookieByName(request, "Cart");
        cartService.saveCartToCookie(request, response, "[]");
    }

    @Override
    public String getServletInfo() {
        return "Handles checkout process for the clothing store.";
    }
}
