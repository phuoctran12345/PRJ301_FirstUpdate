<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>VNPAY Return</title>
        <link href="${pageContext.request.contextPath}/view/assets/home/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <h3 class="text-muted">Kết quả thanh toán</h3>
            </div>
            <div class="table-responsive">
                <div class="form-group">
                    <label>Mã đơn hàng:</label>
                    <label>${orderId}</label>
                </div>    
                <div class="form-group">
                    <label>Số tiền:</label>
                    <label><fmt:formatNumber type="number" pattern="###,###" value="${amount}"/> VNĐ</label>
                </div>  
                <div class="form-group">
                    <label>Kết quả:</label>
                    <label style="color: ${status == 'success' ? 'blue' : 'red'}">${message}</label>
                </div> 
                <div class="form-group">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Về trang chủ</a>
                </div>
            </div>
        </div>  
    </body>
</html>