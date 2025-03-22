package controller;

import utils.WishlistUtil;
import utils.CartUtil;
import dao.ProductDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.ProductDTO;
import dao.ProductDAO;
import model.CartItem;
import model.ProductDTO;
import utils.CartUtil;
import utils.WishlistUtil;

/**
 * Servlet xử lý các chức năng liên quan đến giỏ hàng và danh sách yêu thích (wishlist)
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/CartServlet"})
public class CartServlet extends HttpServlet {

    private static final String DISPATCHSERVLET = "DispatchServlet";  // Không sử dụng
    private static final String CART_PAGE = "view/jsp/home/cart.jsp";  // Trang giỏ hàng
    

    /**
     * Phương thức xử lý yêu cầu từ người dùng
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = CART_PAGE;  // Mặc định trả về trang AJAX cho giỏ hàng
        ProductDAO pDao = new ProductDAO();  // Khởi tạo đối tượng DAO sản phẩm
        CartUtil cartUtil = new CartUtil();  // Khởi tạo công cụ giỏ hàng
        List<CartItem> carts = null;
        HashMap<Integer, CartItem> listItem = null;

        WishlistUtil wUtil = new WishlistUtil();  // Khởi tạo công cụ danh sách yêu thích
        List<ProductDTO> wishlists = null;
        HashMap<Integer, ProductDTO> listWishlist = null;
        
        try {
            HttpSession session = request.getSession();  // Lấy session
            String action = request.getParameter("action");  // Lấy hành động từ request
            
            // Nếu không có hành động, trả về trang giỏ hàng
            if (action == null) {
                url = CART_PAGE;
            } else {
                // Lấy thông tin sản phẩm từ ID
                String product_id = request.getParameter("product_id");
                ProductDTO product = pDao.getProductByID(Integer.parseInt(product_id));
                
                // Nếu hành động là "Add" (Thêm vào giỏ hàng)
                if ("Add".equals(action)) {
                    String quantity = request.getParameter("quantity");  // Lấy số lượng
                    CartItem item = new CartItem(product, Integer.parseInt(quantity));  // Tạo đối tượng CartItem
                    
                    // Lấy giỏ hàng từ session
                    carts = (List<CartItem>) session.getAttribute("CART");
                    
                    // Nếu giỏ hàng chưa tồn tại, tạo mới giỏ hàng
                    if (carts == null) {
                        listItem = cartUtil.createCart(item);
                    } else {
                        listItem = cartUtil.addItemToCart(item);  // Thêm sản phẩm vào giỏ hàng
                    }

                    // Xóa sản phẩm trong danh sách yêu thích nếu có
                    wishlists = (List<ProductDTO>) session.getAttribute("WISHLIST");
                    if (wishlists != null) {
                        listWishlist = wUtil.removeItem(product);  // Xóa sản phẩm khỏi wishlist
                        wishlists = new ArrayList<>(listWishlist.values());  // Cập nhật lại wishlist
                        session.setAttribute("WISHLIST", wishlists);  // Lưu wishlist vào session
                    }
                } 
                // Nếu hành động là "Delete" (Xóa khỏi giỏ hàng)
                else if ("Delete".equals(action)) {
                    String curPage = request.getParameter("curPage");  // Lấy trang hiện tại
                    // Xác định trang cần trả về dựa trên curPage
                    if ("cart.jsp".equals(curPage)) {
                        url = CART_PAGE;  // Nếu là trang cart.jsp, trả về AJAX cho giỏ hàng
                    } else if ("header.jsp".equals(curPage)) {
                        url = CART_PAGE;  // Nếu là header.jsp, trả về AJAX cho giỏ hàng
                    }

                    // Xóa sản phẩm khỏi giỏ hàng
                    listItem = cartUtil.removeItem(product);
                } 
                // Nếu hành động là "Update" (Cập nhật giỏ hàng)
                else if ("Update".equals(action)) {
                    url = CART_PAGE;  // Trả về trang AJAX cho giỏ hàng
                    String quantity = request.getParameter("quantity");  // Lấy số lượng mới
                    CartItem item = new CartItem(product, Integer.parseInt(quantity));  // Tạo đối tượng CartItem mới
                    listItem = cartUtil.updateItemToCart(item);  // Cập nhật giỏ hàng
                }
            }
            
            // Cập nhật giỏ hàng trong session
            carts = new ArrayList<>(listItem.values());
            session.setAttribute("CART", carts);

            // Lưu giỏ hàng vào cookie
            String strCarts = cartUtil.convertToString();
            cartUtil.saveCartToCookie(request, response, strCarts);

            // Lưu wishlist vào cookie
            String strWishlist = wUtil.convertToString();
            wUtil.saveWishlistToCookie(request, response, strWishlist);

        } catch (Exception ex) {
            log("CartServlet error: " + ex.getMessage());  // Log lỗi nếu có
        } finally {
            // Chuyển tiếp đến trang phù hợp
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // Phương thức xử lý yêu cầu GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Phương thức xử lý yêu cầu POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Phương thức trả về mô tả ngắn gọn của servlet
    @Override
    public String getServletInfo() {
        return "Servlet xử lý giỏ hàng và danh sách yêu thích";
    }
}
