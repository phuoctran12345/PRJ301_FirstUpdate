package controller;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import model.Email;
import model.OrderDTO;
import model.UserDTO;
import model.CartItem;
import utils.VNPayConfig;

@WebServlet("/VNPayReturn")
public class VNPayReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> vnp_Params = new HashMap<>();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                vnp_Params.put(fieldName, fieldValue);
            }
        }
        
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (vnp_SecureHash != null) {
            vnp_Params.remove("vnp_SecureHash");
            String signValue = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, vnp_Params.toString());
            if (signValue.equals(vnp_SecureHash)) {
                if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                    // Giao dịch thành công
                    HttpSession session = request.getSession();
                    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
                    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
                    
                    if (user != null && cartItems != null) {
                        try {
                            OrderDAO orderDAO = new OrderDAO();
                            double totalAmount = Double.parseDouble(vnp_Params.get("vnp_Amount")) / 100;
                            String shippingAddress = (String) session.getAttribute("shippingAddress");
                            String shippingPhone = (String) session.getAttribute("shippingPhone");
                            
                            // Create new VNPAY order
                            int orderId = orderDAO.createVNPayOrder(
                                totalAmount,
                                user.getUserName(),
                                shippingAddress,
                                shippingPhone
                            );
                            
                            if (orderId > 0) {
                                // Get the created order
                                OrderDTO savedOrder = orderDAO.getOrdersByID(String.valueOf(orderId));
                                
                                if (savedOrder != null) {
                                    // Gửi email xác nhận
                                    Email emailService = new Email();
                                    String emailContent = emailService.messageNewOrder(
                                        user.getUserName(),
                                        cartItems.size(),
                                        totalAmount,
                                        cartItems
                                    );
                                    emailService.sendEmail(
                                        emailService.subjectNewOrder(),
                                        emailContent,
                                        user.getEmail()
                                    );
                                    
                                    // Xóa giỏ hàng
                                    session.removeAttribute("cart");
                                    session.removeAttribute("shippingAddress");
                                    session.removeAttribute("shippingPhone");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        // Hiển thị kết quả
        request.setAttribute("code", request.getParameter("vnp_ResponseCode"));
        request.getRequestDispatcher("/view/jsp/home/vnpay_return.jsp").forward(request, response);
    }
}