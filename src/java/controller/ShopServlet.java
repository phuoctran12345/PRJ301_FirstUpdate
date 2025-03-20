package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CategoryDTO;
import model.ProductDTO;

public class ShopServlet extends HttpServlet {

    // Paths for JSP files
    private static final String SHOP_PAGE = "view/jsp/home/shop-list.jsp";
    private static final String SORT_PAGE = "view/jsp/ajax/sortproducts.jsp";

    // Main method to handle both GET and POST requests
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String destinationPage = SHOP_PAGE; // Default page to show

        try {
            // Initialize DAOs for database access
            ProductDAO productDao = new ProductDAO();
            CategoryDAO categoryDao = new CategoryDAO();

            // Get all categories
            List<CategoryDTO> categories = categoryDao.getData();

            // Get product list (either from request or database)
            List<ProductDTO> products;
            if (request.getAttribute("LISTPRODUCTS") == null) {
                products = productDao.getData(); // Fetch all products from DB
            } else {
                products = (List<ProductDTO>) request.getAttribute("LISTPRODUCTS"); // Use existing list
            }

            // Handle sorting if requested
            String sortOption = request.getParameter("valueSort");
            if (sortOption != null) {
                switch (sortOption) {
                    case "1": // Sort option 1 (e.g., by name)
                    case "2": // Sort option 2 (e.g., by price low to high)
                    case "3": // Sort option 3 (e.g., by price high to low)
                        products = productDao.sortProduct(products, sortOption);
                        destinationPage = SORT_PAGE; // Change to sorting page
                        break;
                }
            }

            // Pagination logic
            int itemsPerPage = 9; // Number of products per page
            int totalProducts = products.size();
            int totalPages = (totalProducts % itemsPerPage == 0) 
                           ? (totalProducts / itemsPerPage) 
                           : (totalProducts / itemsPerPage) + 1;

            // Get current page number from request, default to 1
            String pageParam = request.getParameter("page");
            int currentPage = (pageParam == null) ? 1 : Integer.parseInt(pageParam);

            // Calculate start and end indices for the current page
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(currentPage * itemsPerPage, totalProducts);

            // Get the subset of products for the current page
            List<ProductDTO> productsForPage = productDao.getListByPage(products, startIndex, endIndex);

            // Set attributes for the JSP page
            request.setAttribute("DATA_FROM", "ShopServlet");
            request.setAttribute("NUMBERPAGE", totalPages);
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("LISTPRODUCTS", productsForPage);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortOption);
            request.setAttribute("CURRENTSERVLET", "Shop");

        } catch (Exception ex) {
            log("ShopServlet error: " + ex.getMessage()); // Log any errors
        } finally {
            // Forward to the appropriate JSP page
            request.getRequestDispatcher(destinationPage).forward(request, response);
        }
    }

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        return "Handles shop page functionality including product listing, sorting, and pagination";
    }
}
