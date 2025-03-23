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
            String tenTruong = (String) params.nextElement();
            String giaTri = request.getParameter(tenTruong);
            if ((giaTri != null) && (giaTri.length() > 0)) {
                vnp_Params.put(tenTruong, giaTri);
            }
        }
        
        String vnp_SecureHash = vnp_Params.get("vnp_SecureHash");
        vnp_Params.remove("vnp_SecureHash");
        
        // Sắp xếp các tham số và tạo dữ liệu mã hóa
        List<String> tenTruong = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(tenTruong);
        StringBuilder duLieuHash = new StringBuilder();
        for (String ten : tenTruong) {
            String giaTri = vnp_Params.get(ten);
            if ((giaTri != null) && (giaTri.length() > 0)) {
                duLieuHash.append(ten).append('=').append(giaTri);
                if (tenTruong.indexOf(ten) < tenTruong.size() - 1) {
                    duLieuHash.append('&');
                }
            }
        }
        
        String giaTriKy = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, duLieuHash.toString());
        
        if (giaTriKy.equals(vnp_SecureHash)) {
            if ("00".equals(vnp_Params.get("vnp_ResponseCode"))) {
                HttpSession session = request.getSession();
                UserDTO nguoiDung = (UserDTO) session.getAttribute("LOGIN_USER");
                List<CartItem> gioHang = (List<CartItem>) session.getAttribute("cart");
                
                if (nguoiDung != null && gioHang != null) {
                    try {
                        OrderDAO orderDAO = new OrderDAO();
                        double tongTien = Double.parseDouble(vnp_Params.get("vnp_Amount")) / 100;
                        String diaChiGiaoHang = (String) session.getAttribute("shippingAddress");
                        String soDienThoaiGiaoHang = (String) session.getAttribute("shippingPhone");
                        
                        int maDonHang = orderDAO.createVNPayOrder(
                            tongTien,
                            nguoiDung.getUserName(),
                            diaChiGiaoHang,
                            soDienThoaiGiaoHang
                        );
                        
                        if (maDonHang > 0) {
                            OrderDTO donHangDaLuu = orderDAO.getOrdersByID(String.valueOf(maDonHang));
                            if (donHangDaLuu != null) {
                                Email dichVuEmail = new Email();
                                String noiDungEmail = dichVuEmail.messageNewOrder(
                                    nguoiDung.getUserName(),
                                    gioHang.size(),
                                    tongTien,
                                    gioHang
                                );
                                dichVuEmail.sendEmail(
                                    dichVuEmail.subjectNewOrder(),
                                    noiDungEmail,
                                    nguoiDung.getEmail()
                                );
                                
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
        
        request.setAttribute("code", vnp_Params.get("vnp_ResponseCode"));
        request.getRequestDispatcher("/view/jsp/home/vnpay_return.jsp").forward(request, response);
    }
}