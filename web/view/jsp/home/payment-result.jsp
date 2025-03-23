<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/taglib.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kết quả thanh toán</title>
    </head>
    <body>
        <div class="container">
            <h2>Kết quả thanh toán</h2>
            <c:if test="${param.vnp_ResponseCode == '00'}">
                <div class="alert alert-success">
                    Thanh toán thành công!<br>
                    Mã đơn hàng: ${param.vnp_TxnRef}<br>
                    Số tiền: ${param.vnp_Amount/100}đ
                </div>
            </c:if>
            <c:if test="${param.vnp_ResponseCode != '00'}">
                <div class="alert alert-danger">
                    Thanh toán thất bại!<br>
                    Lý do: ${param.vnp_Message}
                </div>
            </c:if>
            <a href="DispatchServlet" class="btn btn-primary">Về trang chủ</a>
        </div>
    </body>
</html>