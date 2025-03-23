package controller;

import service.IWishlistService;
import service.WishlistService; // Thay thế WishlistUtil bằng WishlistService
import service.CartService;  
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

@WebServlet(name = "CartServlet", urlPatterns = {"/CartServlet"})
public class CartServlet extends HttpServlet {

    private static final String CART_PAGE = "view/jsp/home/cart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = CART_PAGE;
        ProductDAO pDao = new ProductDAO();
        CartService cartService = new CartService();  // Use CartService instead of CartUtil
        List<CartItem> carts = null;
        HashMap<Integer, CartItem> listItem = null;

        IWishlistService wishlistService = new WishlistService(); // Thay WishlistUtil bằng IWishlistService
        List<ProductDTO> wishlists = null;
        HashMap<Integer, ProductDTO> listWishlist = null;

        try {
            HttpSession session = request.getSession();
            String action = request.getParameter("action");

            if (action == null) {
                url = CART_PAGE;
            } else {
                String product_id = request.getParameter("product_id");
                ProductDTO product = pDao.getProductByID(Integer.parseInt(product_id));

                if ("Add".equals(action)) {
                    String quantity = request.getParameter("quantity");
                    CartItem item = new CartItem(product, Integer.parseInt(quantity));

                    carts = (List<CartItem>) session.getAttribute("CART");

                    if (carts == null) {
                        listItem = cartService.createCart(item);
                    } else {
                        listItem = cartService.addItemToCart(item);
                    }

                    wishlists = (List<ProductDTO>) session.getAttribute("WISHLIST");
                    if (wishlists != null) {
                        listWishlist = wishlistService.removeItem(product); // Sử dụng wishlistService
                        wishlists = new ArrayList<>(listWishlist.values());
                        session.setAttribute("WISHLIST", wishlists);
                    }
                } else if ("Delete".equals(action)) {
                    String curPage = request.getParameter("curPage");
                    if ("cart.jsp".equals(curPage) || "header.jsp".equals(curPage)) {
                        url = CART_PAGE;
                    }

                    listItem = cartService.removeItem(product);
                } else if ("Update".equals(action)) {
                    url = CART_PAGE;
                    String quantity = request.getParameter("quantity");
                    CartItem item = new CartItem(product, Integer.parseInt(quantity));
                    listItem = cartService.updateItemToCart(item);
                }
            }

            carts = new ArrayList<>(listItem.values());
            session.setAttribute("CART", carts);

            String strCarts = cartService.convertToString();
            cartService.saveCartToCookie(request, response, strCarts);

            String strWishlist = wishlistService.convertToString(); // Sử dụng wishlistService
            wishlistService.saveWishlistToCookie(request, response, strWishlist); // Sử dụng wishlistService

        } catch (Exception ex) {
            log("CartServlet error: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        return "Servlet xử lý giỏ hàng và danh sách yêu thích";
    }
}