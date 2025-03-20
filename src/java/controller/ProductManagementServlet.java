package controller;

import dao.ProductDAO;
import dao.CategoryDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
import model.ProductDTO;
import model.CategoryDTO;
import model.SupplierDTO;
import model.TypeDTO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(name = "ProductManagementServlet", urlPatterns = {"/ProductManagementServlet"})
public class ProductManagementServlet extends HttpServlet {

    private static final String MANAGE_PRODUCT_PAGE = "view/jsp/admin/admin_products.jsp";
    private static final String INSERT_PRODUCT_PAGE = "view/jsp/admin/admin_products_insert.jsp";
    private static final String EDIT_PRODUCT_PAGE = "view/jsp/admin/admin_edit_product.jsp";
    private static final String UPLOAD_DIR = "view/assets/home/img/products/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = MANAGE_PRODUCT_PAGE;
        String action = request.getParameter("action");

        try {
            ProductDAO productDao = new ProductDAO();
            CategoryDAO categoryDao = new CategoryDAO();
            SupplierDAO supplierDao = new SupplierDAO();
            TypeDAO typeDao = new TypeDAO();

            if ("Insert".equals(action)) {
                url = showInsertProductPage(request, categoryDao, supplierDao, typeDao);
            } else if ("Update".equals(action)) {
                url = updateProduct(request, productDao);
            } else if ("Delete".equals(action)) {
                url = deleteProduct(request, productDao);
            } else if ("Edit".equals(action)) {
                url = showEditProductPage(request, productDao, categoryDao, supplierDao, typeDao);
            } else if ("InsertProduct".equals(action)) {
                url = insertNewProduct(request, productDao);
            } else if ("Search".equals(action)) {
                url = searchProducts(request, productDao);
            } else if ("Restore".equals(action)) {
                url = restoreProduct(request, productDao);
            } else if ("PermanentlyDelete".equals(action)) {
                url = permanentlyDeleteProduct(request, productDao);
            } else {
                url = showProductList(request, productDao);
            }

        } catch (Exception ex) {
            log("ProductManagementServlet error: " + ex.getMessage());
            request.setAttribute("error", "Đã xảy ra lỗi: " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private String showProductList(HttpServletRequest request, ProductDAO productDao)
            throws SQLException {
        List<ProductDTO> productList = productDao.getData();
        for (ProductDTO product : productList) {
            product.setSize(product.getSize() != null ? product.getSize() : new String[]{});
            product.setColors(product.getColors() != null ? product.getColors() : new String[]{});
            product.setImages(product.getImages() != null ? product.getImages() : new String[]{});
        }
        request.setAttribute("LISTPRODUCTS", productList);
        request.setAttribute("CURRENTSERVLET", "Product");
        return MANAGE_PRODUCT_PAGE;
    }

    private String showInsertProductPage(HttpServletRequest request, CategoryDAO categoryDao, SupplierDAO supplierDao, TypeDAO typeDao)
            throws SQLException {
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
        return INSERT_PRODUCT_PAGE;
    }

    private String showEditProductPage(HttpServletRequest request, ProductDAO productDao, CategoryDAO categoryDao, SupplierDAO supplierDao, TypeDAO typeDao)
            throws SQLException {
        String productId = request.getParameter("productId");
        ProductDTO product = productDao.getProductByID(Integer.parseInt(productId));
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();

        request.setAttribute("productId", product.getId());
        request.setAttribute("name", product.getName());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("stock", product.getStock());
        request.setAttribute("size", String.join(",", product.getSize() != null ? product.getSize() : new String[]{}));
        request.setAttribute("color", String.join(",", product.getColors() != null ? product.getColors() : new String[]{}));
        request.setAttribute("description", product.getDescription());
        request.setAttribute("image", String.join(" ", product.getImages() != null ? product.getImages() : new String[]{}));
        request.setAttribute("categoryId", product.getCategory().getId());
        request.setAttribute("supplierId", product.getSupplier().getId());
        request.setAttribute("typeId", product.getType().getId());
        request.setAttribute("releaseDate", product.getReleasedate());
        request.setAttribute("discount", product.getDiscount());
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
        return EDIT_PRODUCT_PAGE;
    }

    private String deleteProduct(HttpServletRequest request, ProductDAO productDao)
            throws SQLException {
        String productId = request.getParameter("productId");
        productDao.deleteProduct(productId);
        request.setAttribute("mess", "Xóa sản phẩm thành công!");
        return showProductList(request, productDao);
    }

    private String insertNewProduct(HttpServletRequest request, ProductDAO productDao)
            throws SQLException, Exception {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String[] images = request.getParameterValues("image");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String supplierId = request.getParameter("supplierId");
        String typeId = request.getParameter("typeId");
        String releaseDate = request.getParameter("releaseDate");
        String discount = request.getParameter("discount");

        String image = "";
        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            for (String img : images) {
                image += UPLOAD_DIR + img + " ";
            }
        }

        try {
            productDao.insertProduct(name, Integer.parseInt(categoryId), Integer.parseInt(supplierId),
                    Integer.parseInt(typeId), Double.parseDouble(price),
                    discount.isEmpty() ? 0 : Double.parseDouble(discount), size, color,
                    Integer.parseInt(stock), releaseDate, image.trim(), description);
            request.setAttribute("mess", "Thêm sản phẩm thành công!");
            return showProductList(request, productDao);
        } catch (SQLException e) {
            setProductAttributes(request, name, price, stock, size, color, description,
                    image, categoryId, supplierId, typeId, releaseDate, discount);
            request.setAttribute("error", e.getMessage());
            CategoryDAO categoryDao = new CategoryDAO();
            SupplierDAO supplierDao = new SupplierDAO();
            TypeDAO typeDao = new TypeDAO();
            List<CategoryDTO> categoryList = categoryDao.getData();
            List<SupplierDTO> supplierList = supplierDao.getData();
            List<TypeDTO> typeList = typeDao.getAllType();
            request.setAttribute("LISTCATEGORIES", categoryList);
            request.setAttribute("LISTSUPPLIERS", supplierList);
            request.setAttribute("LISTTYPES", typeList);
            return INSERT_PRODUCT_PAGE;
        }
    }

    private String updateProduct(HttpServletRequest request, ProductDAO productDao)
            throws SQLException, Exception {
        String productId = request.getParameter("productId");
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String[] images = request.getParameterValues("image");
        String description = request.getParameter("description");
        String categoryId = request.getParameter("categoryId");
        String supplierId = request.getParameter("supplierId");
        String typeId = request.getParameter("typeId");
        String releaseDate = request.getParameter("releaseDate");
        String discount = request.getParameter("discount");

        String image = "";
        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            for (String img : images) {
                image += UPLOAD_DIR + img + " ";
            }
        } else {
            image = productDao.getProductByID(Integer.parseInt(productId)).getImages().length > 0
                    ? String.join(" ", productDao.getProductByID(Integer.parseInt(productId)).getImages()) : "";
        }

        try {
            productDao.editProduct(Integer.parseInt(productId), name, description, Integer.parseInt(stock), image.trim(),
                    color, size, releaseDate, discount.isEmpty() ? 0 : Double.parseDouble(discount),
                    Double.parseDouble(price), Integer.parseInt(categoryId), Integer.parseInt(supplierId), Integer.parseInt(typeId));
            request.setAttribute("mess", "Cập nhật sản phẩm thành công!");
            return showProductList(request, productDao);
        } catch (SQLException e) {
            setProductAttributes(request, name, price, stock, size, color, description,
                    image, categoryId, supplierId, typeId, releaseDate, discount);
            request.setAttribute("error", e.getMessage());
            setEditPageAttributes(request, productDao);
            return EDIT_PRODUCT_PAGE;
        }
    }

    private String searchProducts(HttpServletRequest request, ProductDAO productDao)
            throws SQLException, Exception {
        String searchQuery = request.getParameter("search");
        List<ProductDTO> productList = (searchQuery != null && !searchQuery.trim().isEmpty())
                ? productDao.searchProducts(searchQuery)
                : productDao.getData();

        for (ProductDTO product : productList) {
            product.setSize(product.getSize() != null ? product.getSize() : new String[]{});
            product.setColors(product.getColors() != null ? product.getColors() : new String[]{});
            product.setImages(product.getImages() != null ? product.getImages() : new String[]{});
        }

        request.setAttribute("LISTPRODUCTS", productList);
        request.setAttribute("CURRENTSERVLET", "Product");
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            request.setAttribute("searchQuery", searchQuery);
        }
        return MANAGE_PRODUCT_PAGE;
    }

    private String restoreProduct(HttpServletRequest request, ProductDAO productDao)
            throws SQLException, Exception {
        String productId = request.getParameter("productId");
        productDao.restoreProduct(productId);
        request.setAttribute("mess", "Khôi phục sản phẩm thành công!");
        return showProductList(request, productDao);
    }

    private String permanentlyDeleteProduct(HttpServletRequest request, ProductDAO productDao)
            throws SQLException, Exception {
        String productId = request.getParameter("productId");
        productDao.permanentlyDeleteProduct(productId);
        request.setAttribute("mess", "Xóa vĩnh viễn sản phẩm thành công!");
        return showProductList(request, productDao);
    }

    private void setProductAttributes(HttpServletRequest request, String name, String price, String stock,
            String size, String color, String description, String image, String categoryId,
            String supplierId, String typeId, String releaseDate, String discount) {
        request.setAttribute("name", name);
        request.setAttribute("price", price);
        request.setAttribute("stock", stock);
        request.setAttribute("size", size);
        request.setAttribute("color", color);
        request.setAttribute("description", description);
        request.setAttribute("image", image);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("supplierId", supplierId);
        request.setAttribute("typeId", typeId);
        request.setAttribute("releaseDate", releaseDate);
        request.setAttribute("discount", discount);
        request.setAttribute("productId", request.getParameter("productId"));
    }

    private void setEditPageAttributes(HttpServletRequest request, ProductDAO productDao)
            throws SQLException {
        CategoryDAO categoryDao = new CategoryDAO();
        SupplierDAO supplierDao = new SupplierDAO();
        TypeDAO typeDao = new TypeDAO();
        List<CategoryDTO> categoryList = categoryDao.getData();
        List<SupplierDTO> supplierList = supplierDao.getData();
        List<TypeDTO> typeList = typeDao.getAllType();
        request.setAttribute("LISTCATEGORIES", categoryList);
        request.setAttribute("LISTSUPPLIERS", supplierList);
        request.setAttribute("LISTTYPES", typeList);
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
        return "Product Management Servlet";
    }
}