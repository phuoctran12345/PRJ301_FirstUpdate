package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CategoryDTO;
import model.ProductDTO;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    private static final String SEARCH_PAGE = "view/jsp/home/shop-list.jsp";
    private static final String SEARCH_IN_SHOPLIST = "view/jsp/ajax/sortproducts.jsp";
    private static final String SORT_PAGE = "view/jsp/ajax/sortproducts.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String destination = SEARCH_PAGE;

        try {
            // Get search and sort parameters
            String searchText = request.getParameter("txtSearch");
            String sortGroup = request.getParameter("sort_group");
            String scope = request.getParameter("scope");
            String sortValue = request.getParameter("valueSort");

            // Fetch products and categories
            ProductDAO productDAO = new ProductDAO();
            List<ProductDTO> products = productDAO.getProductBySearch(searchText);
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getData();

            // Adjust destination if coming from shop-list.jsp
            if ("shop-list.jsp".equals(scope)) {
                destination = SEARCH_IN_SHOPLIST;
            }

            // Default sortGroup to searchText if not provided
            if (sortGroup == null) {
                sortGroup = searchText;
            }

            // Sort products if a sort value is provided
            if (sortValue != null) {
                switch (sortValue) {
                    case "1":
                    case "2":
                    case "3":
                        products = productDAO.sortProduct(products, sortValue);
                        break;
                }
                destination = SORT_PAGE;
            }

            // Pagination logic
            int itemsPerPage = 9;
            int totalItems = products.size();
            int totalPages = (totalItems % itemsPerPage == 0) ? (totalItems / itemsPerPage) : (totalItems / itemsPerPage) + 1;

            String pageParam = request.getParameter("page");
            int currentPage = (pageParam == null) ? 1 : Integer.parseInt(pageParam);

            int start = (currentPage - 1) * itemsPerPage;
            int end = Math.min(currentPage * itemsPerPage, totalItems);
            List<ProductDTO> paginatedProducts = productDAO.getListByPage(products, start, end);

            // Set attributes for the JSP page
            request.setAttribute("DATA_FROM", "SearchServlet");
            request.setAttribute("SORT_GROUP", sortGroup);
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("LISTPRODUCTS", paginatedProducts);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortValue);

        } catch (Exception ex) {
            log("Error in SearchServlet: " + ex.getMessage());
        }

        // Forward to the appropriate page
        request.getRequestDispatcher(destination).forward(request, response);
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
        return "Handles product search and sorting";
    }
}