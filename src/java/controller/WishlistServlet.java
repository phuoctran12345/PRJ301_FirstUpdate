package controller;

import utils.WishlistUtil;
import dao.ProductDAO;
import model.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/WishlistServlet"})
public class WishlistServlet extends HttpServlet {

    private static final String WISHLIST_PAGE = "view/jsp/home/wishlist.jsp";
    private static final String WISHLIST_AJAX = "view/jsp/home/wishlist.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Default URL is for AJAX requests
        String destination = WISHLIST_AJAX;

        // Initialize tools and variables
        ProductDAO productDAO = new ProductDAO();
        WishlistUtil wishlistUtil = new WishlistUtil();
        HttpSession session = request.getSession();
        List<ProductDTO> wishlist = null;
        HashMap<Integer, ProductDTO> wishlistItems = null;

        try {
            // Get the action parameter (Add, Delete, or null)
            String action = request.getParameter("action");

            // If no action, show the wishlist page
            if (action == null) {
                destination = WISHLIST_PAGE;
            } else {
                // Get product ID and fetch the product
                String productId = request.getParameter("product_id");
                ProductDTO product = productDAO.getProductByID(Integer.parseInt(productId));

                // Handle "Add" action
                if ("Add".equals(action)) {
                    wishlist = (List<ProductDTO>) session.getAttribute("WISHLIST");
                    if (wishlist == null) {
                        wishlistItems = wishlistUtil.createWishlist(product);
                    } else {
                        wishlistItems = wishlistUtil.addItemToWishlist(product);
                    }
                }
                // Handle "Delete" action
                else if ("Delete".equals(action)) {
                    wishlistItems = wishlistUtil.removeItem(product);
                    destination = WISHLIST_PAGE;
                }
            }

            // Update wishlist in session
            wishlist = new ArrayList<>(wishlistItems.values());
            session.setAttribute("WISHLIST", wishlist);

            // Save wishlist to cookie
            String wishlistString = wishlistUtil.convertToString();
            wishlistUtil.saveWishlistToCookie(request, response, wishlistString);

        } catch (Exception ex) {
            log("WishlistServlet error: " + ex.getMessage());
        } finally {
            // Forward to the destination page
            request.getRequestDispatcher(destination).forward(request, response);
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
        return "Handles wishlist actions like adding or removing products.";
    }
}