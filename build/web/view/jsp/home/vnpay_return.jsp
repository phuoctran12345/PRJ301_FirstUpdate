<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Kết quả thanh toán</title>
    <style>
        .container {
            width: 100%;
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .success {
            color: green;
        }
        .error {
            color: red;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Kết quả thanh toán VNPAY</h2>
        
        <c:if test="${code == '00'}">
            <div class="success">
                <h3>Thanh toán thành công!</h3>
                <p>Đơn hàng của bạn đã được xác nhận.</p>
                <p>Chúng tôi sẽ gửi email xác nhận cho bạn.</p>
            </div>
        </c:if>
        
        <c:if test="${code != '00'}">
            <div class="error">
                <h3>Thanh toán thất bại!</h3>
                <p>Đã có lỗi xảy ra trong quá trình thanh toán.</p>
                <p>Vui lòng thử lại sau.</p>
            </div>
        </c:if>
        
        <p><a href="${pageContext.request.contextPath}/home">Quay về trang chủ</a></p>
    </div>
</body>
</html>