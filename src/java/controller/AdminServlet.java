/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.OrderDTO;
import model.OrderItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {

    private static final String ADMIN = "view/jsp/admin/admin_home.jsp";
    private final static String ORDER_DETAIL_PAGE = "view/jsp/admin/admin_order_detail.jsp";



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO pDao = new ProductDAO();
        OrderItemDAO oIDao = new OrderItemDAO();
        OrderDAO oDao = new OrderDAO();
        UserDAO uDao = new UserDAO();

        String url = ADMIN;
        try {
            double totalSale = oDao.getTotalSale();
            double totalSaleTD = oDao.getTotalSaleToday();
            int totalProducts = pDao.getTotalProducts();
            int numberProductsLowQuantity = pDao.getProductsLowQuantiry();
            int totalUsers = uDao.getTotalUsers();
            int totalOrders = oDao.getTotalOrders();
            List<OrderDTO> lastRecentOrders = oDao.getRecentOrders();

            request.setAttribute("TOTALSALE", totalSale);
            request.setAttribute("TOTALSALETODAY", totalSaleTD);
            request.setAttribute("TOTALPRODUCTS", totalProducts);
            request.setAttribute("PRODUCTSLOW", numberProductsLowQuantity);
            request.setAttribute("TOTALUSERS", totalUsers);
            request.setAttribute("TOTALORDERS", totalOrders);
            request.setAttribute("LAST_RECENT_ORDERS", lastRecentOrders);
            request.setAttribute("CURRENTSERVLET", "Dashboard");
            // tại đây nó cho CurrentServlet là Dashboarrd 
            // giúp thanh menu có CurrentServlet hoặt động và kích hoạt ở slibar.jsp
            // tại đó cái nào actice thì sẽ chạy css là app-menu__item đc cấu hình tại đường dẫn asset/admin/css/main.css
            String action = request.getParameter("showdetail");
            if ("showdetail".equals(action)) {// chi tiết đơn hàng
                url = ORDER_DETAIL_PAGE;
                String bill_id = request.getParameter("bill_id");
                OrderDTO order = oDao.getOrdersByID(bill_id);
                int id = order.getOrderID();
                List<OrderItem> list = oIDao.getOrderItemByOrderId(id);
                request.setAttribute("LIST_PRODUCTS_IN_ORDER", list);

            }

        } catch (Exception ex) {
            log("AdminServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(ADMIN).forward(request, response);
        }
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public String getServletInfo() {
        return "AdminServlet for managing the admin dashboard and order details.";
    }
}
