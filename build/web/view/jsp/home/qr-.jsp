<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh Toán QR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/view/css/style.css">
    <style>
        /* ... giữ nguyên CSS ... */
    </style>
</head>
<body>
    <div class="qr-payment-container">
        <h2>Thanh Toán Qua Mã QR</h2>
        
        <div class="order-summary">
            <h3>Chi Tiết Đơn Hàng</h3>
            <c:forEach items="${cart}" var="item">
                <div class="item">
                    <span>${item.product.name} x ${item.quantity}</span>
                    <span class="price">
                        <fmt:formatNumber value="${item.product.salePrice * item.quantity}" type="currency" currencySymbol="đ"/>
                    </span>
                </div>
            </c:forEach>
            <div class="total">
                <strong>Tổng Tiền: </strong>
                <span><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="đ"/></span>
            </div>
        </div>

        <div class="timer">
            Thời gian còn lại: <span id="countdown">300</span> giây
        </div>

        <div class="qr-code-section">
            <img src="${qrPath}" alt="Mã QR Thanh Toán" width="300" height="300"/>
        </div>

        <div class="payment-instructions">
            <h3>Hướng Dẫn Thanh Toán:</h3>
            <ol>
                <li>Mở ứng dụng MB Bank trên điện thoại của bạn</li>
                <li>Chọn chức năng "Quét QR"</li>
                <li>Quét mã QR hiển thị ở trên</li>
                <li>Kiểm tra số tiền và xác nhận thanh toán</li>
                <li>Chờ xác nhận thanh toán thành công</li>
            </ol>
        </div>
    </div>

    <script>
        let timeLeft = 300;
        const countdownElement = document.getElementById('countdown');
        
        const timer = setInterval(() => {
            timeLeft--;
            countdownElement.textContent = timeLeft;
            
            if (timeLeft <= 0) {
                clearInterval(timer);
                alert('Mã QR đã hết hạn. Vui lòng thử lại.');
                window.location.href = 'CheckoutServlet';
            }
        }, 1000);
    </script>
</body>
</html>