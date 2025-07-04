package controller;

// Import các service cần thiết
import service.IWishlistService;
import service.WishlistService;
import service.CartService;
// Import các DAO để tương tác với database
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
// Import các thư viện Java và Servlet
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// Import các model
import model.CartItem;
import model.CategoryDTO;
import model.ProductDTO;
import model.SupplierDTO;
import model.TypeDTO;
import model.UserDTO;

/**
 * DispatchServlet - Servlet chính điều khiển luồng xử lý của ứng dụng
 * Xử lý các request từ người dùng và điều hướng đến các trang/servlet tương ứng
 */
@WebServlet(name = "DispatchServlet", urlPatterns = {"/DispatchServlet"})
public class DispatchServlet extends HttpServlet {

    // Định nghĩa các đường dẫn trang và servlet
    private final String HOME_PAGE = "view/jsp/home/home.jsp";
    private final String LOGIN_SERVLET = "LoginServlet";
    private final String WISHLIST_SERVLET = "WishlistServlet";
    private final String REGISTER_SERVLET = "RegisterServlet";
    private final String SEARCH_SERVLET = "SearchServlet";

    // Định nghĩa các action từ button
    private final String LOGIN_ACTION = "Login";
    private final String SEARCH_ACTION = "Search";
    private final String LOGOUT_ACTION = "Logout";
    private final String REGISTER_ACTION = "Register";
    private final String ADD_TO_WISHLIST_ACTION = "AddToWishList";

    /**
     * Phương thức xử lý request chính
     * Xác định action và điều hướng đến trang/servlet tương ứng
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập encoding UTF-8 cho response
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String destination = HOME_PAGE; // Trang mặc định là trang chủ

        try {
            String buttonClicked = request.getParameter("btnAction");
            HttpSession session = request.getSession();

            // Xử lý các action khác nhau
            if (buttonClicked == null) {
                // Không có action -> hiển thị trang chủ
                loadHomePageData(request, response);
                request.setAttribute("CURRENTSERVLET", "Home");
            } else if (buttonClicked.equals(LOGOUT_ACTION)) {
                // Xử lý đăng xuất
                loadHomePageData(request, response);
                session.removeAttribute("account");
                request.setAttribute("CURRENTSERVLET", "Home");
            } else if (buttonClicked.equals(LOGIN_ACTION)) {
                // Chuyển đến trang đăng nhập
                destination = LOGIN_SERVLET;
            } else if (buttonClicked.equals(REGISTER_ACTION)) {
                // Chuyển đến trang đăng ký
                destination = REGISTER_SERVLET;
            } else if (buttonClicked.equals(SEARCH_ACTION)) {
                // Chuyển đến trang tìm kiếm
                destination = SEARCH_SERVLET;
            } else if (buttonClicked.equals(ADD_TO_WISHLIST_ACTION)) {
                // Chuyển đến trang wishlist
                destination = WISHLIST_SERVLET;
            } else if (buttonClicked.equals("Chat")) {
                // Xử lý chat dựa vào role của user
                if (session.getAttribute("account") != null) {
                    UserDTO user = (UserDTO) session.getAttribute("account");
                    destination = user.getRoleID() == 1 ? "chat_admin.jsp" : "chat_user.jsp";
                } else {
                    destination = LOGIN_SERVLET;
                }
            }
        } catch (Exception error) {
            log("Error in DispatchServlet: " + error.getMessage());
        } finally {
            // Chuyển hướng đến trang đích
            request.getRequestDispatcher(destination).forward(request, response);
        }
    }

    /**
     * Phương thức tải dữ liệu cho trang chủ
     * Lấy danh sách sản phẩm, danh mục, nhà cung cấp và loại sản phẩm
     */
    protected void loadHomePageData(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Khởi tạo các DAO
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            SupplierDAO supplierDAO = new SupplierDAO();
            TypeDAO typeDAO = new TypeDAO();

            // Lấy dữ liệu từ database
            List<ProductDTO> allProducts = productDAO.getData();
            List<CategoryDTO> categories = categoryDAO.getData();
            List<SupplierDTO> suppliers = supplierDAO.getData();
            List<ProductDTO> newProducts = productDAO.getProductNew();
            List<ProductDTO> bestSellers = productDAO.getProductsBestSeller();
            List<TypeDTO> types = typeDAO.getAllType();

            // Set dữ liệu vào request để hiển thị
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

    /**
     * Xử lý GET request
     * Kiểm tra và tải giỏ hàng và wishlist từ session hoặc cookie
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CartService cartService = new CartService();
        IWishlistService wishlistService = new WishlistService();

        try {
            HttpSession session = request.getSession();
            List<CartItem> cartItems = null;
            List<ProductDTO> wishlistItems = null;

            // Kiểm tra và tải giỏ hàng từ session hoặc cookie
            if (session.getAttribute("CART") == null) {
                Cookie cartCookie = cartService.getCookieByName(request, "Cart");
                if (cartCookie != null) {
                    cartItems = cartService.getCartFromCookie(cartCookie);
                }
            } else {
                cartItems = (List<CartItem>) session.getAttribute("CART");
            }

            // Kiểm tra và tải wishlist từ session hoặc cookie
            if (session.getAttribute("WISHLIST") == null) {
                Cookie wishlistCookie = wishlistService.getCookieByName(request, "Wishlist");
                if (wishlistCookie != null) {
                    wishlistItems = wishlistService.getWishlistFromCookie(wishlistCookie);
                }
            } else {
                wishlistItems = (List<ProductDTO>) session.getAttribute("WISHLIST");
            }

            // Cập nhật session với giỏ hàng và wishlist
            session.setAttribute("CART", cartItems);
            session.setAttribute("WISHLIST", wishlistItems);

        } catch (Exception error) {
            log("Error in doGet: " + error.getMessage());
        }

        processRequest(request, response);
    }

    /**
     * Xử lý POST request
     * Chuyển tiếp đến processRequest
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Trả về thông tin mô tả servlet
     */
    @Override
    public String getServletInfo() {
        return "Main dispatch controller for clothing store";
    }
}
