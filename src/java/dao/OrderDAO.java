package dao;

import utils.DBContext;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoryRevenueDTO;
import model.OrderDTO;
import model.PaymentDTO;
import model.UserDTO;

public class OrderDAO extends DBContext {

    private UserDAO uDao = new UserDAO();
    private PaymentDAO pDao = new PaymentDAO();

    private static final String GET_TOTAL_SALE = "SELECT SUM(totalprice) AS TotalSale from [Orders]";
    private static final String GET_TOTAL_MONEY_YEAR = "SELECT SUM(totalprice) AS TotalSale from [Orders] where year([orderdate]) = ? AND Status = 1";
    private static final String GET_TOTAL_MONEY_MONTH = "SELECT SUM(totalprice) AS TotalSale from [Orders] where month([orderdate]) = ? AND Status = 1";
    private static final String GET_NUMBER_ORDERS = "SELECT COUNT(*) AS Total FROM [Orders] ";
    private static final String GET_TOTAL_ORDERS = "SELECT * FROM [Orders] ORDER BY orderdate DESC";
    private static final String GET_TOTAL_SALE_TODAY = "SELECT sum(totalprice) AS TotalSale FROM [Orders] "
            + " WHERE cast(orderdate as Date) = cast(getdate() as Date)";
    private static final String GET_ORDERS_USER = "SELECT * FROM [Orders] WHERE username = ?";
    private static final String GET_ORDERS_BYID = "SELECT * FROM [Orders] WHERE order_id = ?";
    private static final String GET_RECENT_ORDERS = "SELECT Top 10 * FROM Orders ORDER BY orderdate DESC";
    private static final String UPDATE_STATUS = "UPDATE [Orders] SET status = 1 WHERE order_id = ?";
    private static final String GET_LATEST_ORDER = "SELECT TOP 1 * FROM Orders ORDER BY order_id DESC";
    private static final String CREATE_ORDER = "INSERT INTO [dbo].[Orders]\n"
            + "           ([orderdate]\n"
            + "           ,[totalprice]\n"
            + "           ,[paymentid]\n"
            + "           ,[username]\n"
            + "           ,[status])\n"
            + "     VALUES(?,?,?,?, 0)";

    public double getTotalSale() throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_SALE);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public double getTotalSaleToday() throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_SALE_TODAY);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public double getTotalMoneyByYear(int year) throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_MONEY_YEAR);
                ptm.setInt(1, year);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public double getTotalMoneyByMonth(int month) throws SQLException {
        double result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_MONEY_MONTH);
                ptm.setInt(1, month);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getDouble("TotalSale");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public List<OrderDTO> getRecentOrders() throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_RECENT_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    String userName = rs.getString("username");
                    UserDTO user = uDao.getUserByName(userName);
                    boolean status = rs.getBoolean("status");
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orders;
    }

    public OrderDTO getTheLatestOrder() throws SQLException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_LATEST_ORDER);
                rs = ptm.executeQuery();
                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    String userName = rs.getString("username");
                    UserDTO user = uDao.getUserByName(userName);
                    boolean status = rs.getBoolean("status");
                    order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return order;
    }

    public List<OrderDTO> getOrdersByUsername(String userName) throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ORDERS_USER);
                ptm.setString(1, userName);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    boolean status = rs.getBoolean("status");
                    UserDTO user = uDao.getUserByName(userName);
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orders;
    }

    public OrderDTO getOrdersByID(String id) throws SQLException {
        OrderDTO order = null;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_ORDERS_BYID);
                ptm.setString(1, id);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    boolean status = rs.getBoolean("status");
                    String userName = rs.getString("username");
                    UserDTO user = uDao.getUserByName(userName);
                    order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return order;
    }

    public int getTotalOrders() throws SQLException {
        int result = 0;
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_NUMBER_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    result = rs.getInt("Total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    public List<OrderDTO> getAllOrders() throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(GET_TOTAL_ORDERS);
                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    String userName = rs.getString("username");
                    UserDTO user = uDao.getUserByName(userName);
                    boolean status = rs.getBoolean("status");
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orders;
    }

    public void UpdateStatus(String orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(UPDATE_STATUS);
                ptm.setString(1, orderId);
                ptm.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean CreateNewOrder(String date, double total, PaymentDTO payment, UserDTO user) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_ORDER);
                ptm.setString(1, date);
                ptm.setDouble(2, total);
                ptm.setInt(3, payment.getPaymentID());
                ptm.setString(4, user.getUserName());
                ptm.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public List<OrderDTO> searchOrders(String searchUsername, String statusFilter) throws SQLException {
        List<OrderDTO> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn != null) {
                String query = "SELECT * FROM [Orders] WHERE 1=1";
                List<Object> params = new ArrayList<>();

                // Add username search condition
                if (searchUsername != null && !searchUsername.trim().isEmpty()) {
                    query += " AND username LIKE ?";
                    params.add("%" + searchUsername + "%");
                }

                // Add status filter condition
                if (statusFilter != null && !statusFilter.equals("all")) {
                    query += " AND status = ?";
                    params.add(statusFilter.equals("delivered") ? 1 : 0);
                }

                query += " ORDER BY orderdate DESC";

                ptm = conn.prepareStatement(query);

                // Set parameters
                for (int i = 0; i < params.size(); i++) {
                    ptm.setObject(i + 1, params.get(i));
                }

                rs = ptm.executeQuery();
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    Date orderDate = rs.getDate("orderdate");
                    double totalPrice = rs.getDouble("totalprice");
                    int paymentId = rs.getInt("paymentid");
                    PaymentDTO payment = pDao.getPaymentById(paymentId);
                    String userName = rs.getString("username");
                    UserDTO user = uDao.getUserByName(userName);
                    boolean status = rs.getBoolean("status");
                    OrderDTO order = new OrderDTO(orderId, orderDate, totalPrice, payment, user, status);
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orders;
    }

//
//    public static void main(String[] args) throws SQLException {
//        OrderDAO dao = new OrderDAO();
//        double list = dao.getTotalOrders();
//
//        OrderDTO order = dao.getOrdersByID("1");
//        System.out.println(list);
//    }
    public List<Object[]> getTopUsersByTotalSpent(int limit) throws SQLException {
        String sql = """
        SELECT 
            u.fullName,                                  -- Tên khách hàng
            COUNT(DISTINCT o.order_id) as order_count,   -- Số đơn hàng
            SUM(oi.price * oi.quantity) as total_spent   -- Tổng chi tiêu
        FROM Users u
        JOIN Orders o ON u.user_id = o.user_id
        JOIN OrderItems oi ON o.order_id = oi.order_id
        WHERE o.status = 'Completed'                     -- Chỉ tính đơn hàng đã hoàn thành
        GROUP BY u.user_id, u.fullName
        ORDER BY total_spent DESC                        -- Sắp xếp theo tổng chi tiêu giảm dần
        LIMIT ?
    """;

        List<Object[]> result = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit > 0 ? limit : 5);  // Mặc định lấy top 5 nếu không chỉ định
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("fullName"), // Tên khách hàng
                    rs.getInt("order_count"), // Số đơn hàng
                    rs.getDouble("total_spent") // Tổng chi tiêu
                };
                result.add(row);
            }
        }
        return result;
    }

    public List<CategoryRevenueDTO> getCategoryRevenue() throws SQLException {
        List<CategoryRevenueDTO> result = new ArrayList<>();
        String sql = """
        SELECT 
            c.name AS category_name,
            SUM(oi.price * oi.quantity) AS total_revenue,
            COUNT(DISTINCT o.order_id) AS total_orders
        FROM Categories c
        JOIN Products p ON c.category_id = p.category_id
        JOIN OrderItems oi ON p.product_id = oi.product_id
        JOIN Orders o ON oi.order_id = o.order_id
        WHERE o.status = 'Completed'
        GROUP BY c.category_id, c.name
        ORDER BY total_revenue DESC
    """;

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoryName = rs.getString("category_name");
                double revenue = rs.getDouble("total_revenue");
                int totalOrders = rs.getInt("total_orders");
                result.add(new CategoryRevenueDTO(categoryName, revenue, totalOrders));
            }
        }
        return result;
    }

    public List<Object[]> getTopProductRevenue(int limit) throws SQLException {
        String sql = """
        SELECT 
            p.name AS product_name,
            SUM(oi.price * oi.quantity) AS total_revenue,
            COUNT(DISTINCT o.order_id) AS order_count
        FROM Products p
        JOIN OrderItems oi ON p.product_id = oi.product_id
        JOIN Orders o ON oi.order_id = o.order_id
        WHERE o.status = 'Completed'
        GROUP BY p.product_id, p.name
        ORDER BY total_revenue DESC
        LIMIT ?
    """;

        List<Object[]> result = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("product_name"),
                        rs.getDouble("total_revenue"),
                        rs.getInt("order_count")
                    };
                    result.add(row);
                }
            }
        }
        return result;
    }

    public void updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE [Order] SET Status = ? WHERE OrderID = ?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=======================
    //THP 
    // Thêm vào class OrderDAO
    private static final String CREATE_VNPAY_ORDER = "INSERT INTO [Orders] "
            + "(orderdate, totalprice, paymentid, username, status, shipping_address, shipping_phone) "
            + "VALUES (GETDATE(), ?, 2, ?, 0, ?, ?)";

    public int createVNPayOrder(double totalPrice, String username, String shippingAddress, String shippingPhone) throws SQLException {
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        int orderId = 0;

        try {
            conn = getConnection();
            if (conn != null) {
                ptm = conn.prepareStatement(CREATE_VNPAY_ORDER, PreparedStatement.RETURN_GENERATED_KEYS);
                ptm.setDouble(1, totalPrice);
                ptm.setString(2, username);
                ptm.setString(3, shippingAddress);
                ptm.setString(4, shippingPhone);

                int rowsAffected = ptm.executeUpdate();
                if (rowsAffected > 0) {
                    rs = ptm.getGeneratedKeys();
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ptm != null) {
                ptm.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return orderId;
    }

}
