package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CategoryDTO;
import model.ProductDTO;

@WebServlet(name = "FilterServlet", urlPatterns = {"/FilterServlet"})
public class FilterServlet extends HttpServlet {

    private static final String SHOP_LIST_PAGE = "view/jsp/home/shop-list.jsp";
    private static final String SORT_AJAX_PAGE = "view/jsp/ajax/sortproducts.jsp";

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

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String targetPage = SHOP_LIST_PAGE;

        try {
            // Initialize DAOs and fetch initial data
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getData();
            List<ProductDTO> products = productDAO.getData();

            // Get request parameters
            String action = request.getParameter("btnAction");
            String sortGroup = request.getParameter("sort_group");
            String[] checkboxIds = request.getParameterValues("id_filter"); // Lấy tất cả checkbox đã chọn
            String queryString = request.getQueryString();

            if (action == null) {
                action = sortGroup;
            }

            // Handle category filtering
            Boolean[] categoryCheckboxes = new Boolean[categories.size() + 1];
            categoryCheckboxes[0] = true; // Mặc định chọn "Tất cả"

            if (checkboxIds != null && checkboxIds.length > 0) {
                int[] categoryIds = parseIntArray(checkboxIds);
                if (categoryIds[0] != 0) { // Nếu không chọn "Tất cả"
                    products = productDAO.searchByCheckBox(products, categoryIds);
                    categoryCheckboxes[0] = false; // Bỏ chọn "Tất cả"
                }
                // Cập nhật trạng thái checkbox
                for (int i = 0; i < categories.size(); i++) {
                    categoryCheckboxes[i + 1] = isChecked(categories.get(i).getId(), categoryIds);
                }
            }

            // Handle sorting
            String sortValue = request.getParameter("valueSort");
            if (sortValue != null && !sortValue.isEmpty()) {
                products = productDAO.sortProduct(products, sortValue);
                targetPage = SORT_AJAX_PAGE;
            }

            // Handle price filtering
            double priceFrom = parseDouble(request.getParameter("pricefrom"), 0);
            double priceTo = parseDouble(request.getParameter("priceto"), 0);
            if (priceFrom > 0 || priceTo > 0) {
                products = productDAO.searchByPrice(products, priceFrom, priceTo);
                request.setAttribute("price1", priceFrom);
                request.setAttribute("price2", priceTo);
            }

            // Handle color filtering
            String[] colors = request.getParameterValues("color");
            if (colors != null && colors.length > 0) {
                products = productDAO.searchByColors(products, colors);
                request.setAttribute("COLORS", colors);
            }

            // Handle discount filtering
            String discount = request.getParameter("discount");
            if (discount != null) {
                switch (discount) {
                    case "dis25": products = productDAO.searchByDiscount(products, 0.25); break;
                    case "dis40": products = productDAO.searchByDiscount(products, 0.4); break;
                    case "dis75": products = productDAO.searchByDiscount(products, 0.75); break;
                }
                request.setAttribute("DISCOUNT", discount);
            }

            // Handle pagination
            int itemsPerPage = 9;
            int totalItems = products.size();
            int totalPages = (totalItems + itemsPerPage - 1) / itemsPerPage;
            int currentPage = parseInt(request.getParameter("page"), 1);
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(currentPage * itemsPerPage, totalItems);
            List<ProductDTO> paginatedProducts = productDAO.getListByPage(products, startIndex, endIndex);

            // Set request attributes
            request.setAttribute("SORT_GROUP", action);
            request.setAttribute("DATA_FROM", "FilterServlet");
            request.setAttribute("NUMBERPAGE", totalPages);
            request.setAttribute("CURRENTPAGE", currentPage);
            request.setAttribute("chid", categoryCheckboxes);
            request.setAttribute("CURRENTSERVLET", "Shop");
            request.setAttribute("LISTPRODUCTS", paginatedProducts);
            request.setAttribute("LISTCATEGORIES", categories);
            request.setAttribute("VALUESORT", sortValue);
            request.setAttribute("QUERYSTRING", queryString);

        } catch (Exception ex) {
            log("FilterServlet error: " + ex.getMessage());
        }

        request.getRequestDispatcher(targetPage).forward(request, response);
    }

    private int[] parseIntArray(String[] values) {
        if (values == null) return new int[0];
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }

    private double parseDouble(String value, double defaultValue) {
        return (value != null && !value.isEmpty()) ? Double.parseDouble(value) : defaultValue;
    }

    private int parseInt(String value, int defaultValue) {
        return (value != null) ? Integer.parseInt(value) : defaultValue;
    }

    private boolean isChecked(int id, int[] checkedIds) {
        for (int checkedId : checkedIds) {
            if (checkedId == id) return true;
        }
        return false;
    }

    @Override
    public String getServletInfo() {
        return "Handles product filtering and sorting for the clothing store";
    }
}