package controller;

import utils.WishlistUtil;
import utils.CartUtil;
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.CategoryDTO;
import model.ProductDTO;
import model.SupplierDTO;
import model.TypeDTO;
@WebServlet(name = "DispatchServlet", urlPatterns = {"/DispatchServlet"})
public class DispatchServlet extends HttpServlet {

    // Page and servlet destinations
    private final String HOME_PAGE = "view/jsp/home/home.jsp";
    private final String LOGIN_SERVLET = "LoginServlet";
    private final String WISHLIST_SERVLET = "WishlistServlet";
    private final String REGISTER_SERVLET = "RegisterServlet";
    private final String SEARCH_SERVLET = "SearchServlet";

    // Button actions
    private final String LOGIN_ACTION = "Login";
    private final String SEARCH_ACTION = "Search";
    private final String LOGOUT_ACTION = "Logout";
    private final String REGISTER_ACTION = "Register";
    private final String ADD_TO_WISHLIST_ACTION = "AddToWishList";

    // Main request processing method
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response type to HTML with UTF-8 encoding
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String destination = HOME_PAGE; // Default destination
        
        try {
            String buttonClicked = request.getParameter("btnAction");
            HttpSession session = request.getSession();

            // If no button clicked, show home page
            if (buttonClicked == null) {
                loadHomePageData(request, response);
                request.setAttribute("CURRENTSERVLET", "Home");
            }
            // Handle logout
            else if (buttonClicked.equals(LOGOUT_ACTION)) {
                loadHomePageData(request, response);
                session.removeAttribute("account"); // Clear user session
                request.setAttribute("CURRENTSERVLET", "Home");
            }
            // Direct to login servlet
            else if (buttonClicked.equals(LOGIN_ACTION)) {
                destination = LOGIN_SERVLET;
            }
            // Direct to register servlet
            else if (buttonClicked.equals(REGISTER_ACTION)) {
                destination = REGISTER_SERVLET;
            }
            // Direct to search servlet
            else if (buttonClicked.equals(SEARCH_ACTION)) {
                destination = SEARCH_SERVLET;
            }
            // Direct to wishlist servlet
            else if (buttonClicked.equals(ADD_TO_WISHLIST_ACTION)) {
                destination = WISHLIST_SERVLET;
            }
        } catch (Exception error) {
            log("Error in DispatchServlet: " + error.getMessage());
        } finally {
            // Forward to the determined destination
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }

    // Load data for home page
    protected void loadHomePageData(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Create DAO objects
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            SupplierDAO supplierDAO = new SupplierDAO();
            TypeDAO typeDAO = new TypeDAO();

            // Get all necessary data
            List<ProductDTO> allProducts = productDAO.getData();
            List<CategoryDTO> categories = categoryDAO.getData();
            List<SupplierDTO> suppliers = supplierDAO.getData();
            List<ProductDTO> newProducts = productDAO.getProductNew();
            List<ProductDTO> bestSellers = productDAO.getProductsBestSeller();
            List<TypeDTO> types = typeDAO.getAllType();

            // Set data as request attributes
            request.setAttribute("LIST_PRODUCTS", allProducts);
            request.setAttribute("LIST_TYPES", types);
            request.setAttribute("LIST_CATEGORIESS", categories);
            request.setAttribute("LIST_SUPPLIERS", suppliers);
            request.setAttribute("LIST_PRODUCTS_NEW", newProducts);
            request.setAttribute("LIST_PRODUCTS_SELLER", bestSellers);
        } catch (Exception error) {
            log("Error loading home page data: " + error.getMessage());
        }
    }

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CartUtil cartUtil = new CartUtil();
        WishlistUtil wishlistUtil = new WishlistUtil();

        try {
            HttpSession session = request.getSession();
            List<CartItem> cartItems = null;
            List<ProductDTO> wishlistItems = null;

            // Load cart from session or cookie
            if (session.getAttribute("CART") == null) {
                Cookie cartCookie = cartUtil.getCookieByName(request, "Cart");
                if (cartCookie != null) {
                    cartItems = cartUtil.getCartFromCookie(cartCookie);
                }
            } else {
                cartItems = (List<CartItem>) session.getAttribute("CART");
            }

            // Load wishlist from session or cookie
            if (session.getAttribute("WISHLIST") == null) {
                Cookie wishlistCookie = wishlistUtil.getCookieByName(request, "Wishlist");
                if (wishlistCookie != null) {
                    wishlistItems = wishlistUtil.getWishlistFromCookie(wishlistCookie);
                }
            } else {
                wishlistItems = (List<ProductDTO>) session.getAttribute("WISHLIST");
            }

            // Update session with cart and wishlist
            session.setAttribute("CART", cartItems);
            session.setAttribute("WISHLIST", wishlistItems);

        } catch (Exception error) {
            log("Error in doGet: " + error.getMessage());
        }
        
        processRequest(request, response);
    }

    // Handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Servlet description
    @Override
    public String getServletInfo() {
        return "Main dispatch controller for clothing store";
    }
}