
package model;

import java.sql.Date;
import java.util.List;

public class OrderDTO {
    private int orderID;
    private Date orderDate;
    private double totalPrice;
    private PaymentDTO paymentMethod;
    private UserDTO user;
    private boolean status;

    public OrderDTO() {
    }
    

    public OrderDTO(int orderID, Date orderDate, double totalPrice, PaymentDTO paymentMethod, UserDTO user, boolean status) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.user = user;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PaymentDTO getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentDTO paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPaymentId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setCartItems(List<CartItem> cartItems) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
