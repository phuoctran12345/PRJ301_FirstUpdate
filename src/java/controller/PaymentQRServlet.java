package controller;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet(name = "PaymentQRServlet", urlPatterns = {"/PaymentQR"})
public class PaymentQRServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String amount = request.getParameter("amount");
            double totalAmount = Double.parseDouble(amount.replace("đ", "").trim());
            
            // Tạo mã QR
            String qrPath = QRCodeGenerator.generateMBBankQR(
                "0987654321", // Thay bằng số tài khoản MB Bank thực
                totalAmount,
                "Thanh toan don hang " + System.currentTimeMillis()
            );
            
            // Trả về đường dẫn QR dưới dạng JSON
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("qrPath", qrPath);
            
            response.getWriter().write(jsonResponse.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Không thể tạo mã QR");
            response.getWriter().write(errorResponse.toString());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }
}