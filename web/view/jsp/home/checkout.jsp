<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Thanh toán</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="view\assets\home\img\favicon.png">

        <!-- all css here -->
        <%@include file="../../common/web/add_css.jsp"%>
        
        <!-- CSS cho phần thanh toán QR -->
        <style>
            .qr-payment-section {
                display: none;
                text-align: center;
                margin: 20px 0;
                padding: 15px;
                border: 1px solid #ddd;
                border-radius: 5px;
                background-color: #f9f9f9;
            }
            .timer {
                font-size: 18px;
                color: #e67e22;
                margin: 15px 0;
                font-weight: bold;
            }
            .payment-instructions {
                text-align: left;
                background: #fff;
                padding: 15px;
                margin: 15px 0;
                border-radius: 5px;
                border: 1px solid #eee;
            }
        </style>
    </head>
    <body>
        <!-- Add your site or application content here -->

        <!--pos page start-->
        <div class="pos_page">
            <div class="container">
                <!--pos page inner-->
                <div class="pos_page_inner">  
                    <!--header area -->
                    <%@include file="../../common/web/header.jsp"%>
                    <!--header end -->
                    <!--breadcrumbs area start-->
                    <div class="breadcrumbs_area">
                        <div class="row">
                            <div class="col-12">
                                <div class="breadcrumb_content">
                                    <ul>
                                        <li><a href="DispatchServlet">Trang chủ</a></li>
                                        <li><i class="fa fa-angle-right"></i></li>
                                        <li>Thanh toán</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--breadcrumbs area end-->

                    <!--Checkout page section-->
                    <div class="Checkout_section">
                        <div class="row">
                            <div class="col-12">
                                <div class="user-actions mb-20">
                                    <c:if test="${sessionScope.account == null}">
                                        <h3> 
                                            <i class="fa fa-file-o" aria-hidden="true"></i>
                                            Để thanh toán đơn hàng, bạn cần đăng nhập >>
                                            <a class="Returning" href="DispatchServlet?btnAction=Login">Nhấn vào đây để đăng nhập</a>     
                                        </h3>
                                    </c:if>
                                    <c:if test="${sessionScope.account != null}">
                                        <h3> 
                                            <i class="fa fa-file-o" aria-hidden="true"></i>
                                            Chỉnh sửa thông tin >>
                                            <a class="Returning" href="ProfileServlet">Tài khoản của tôi</a>     
                                        </h3>
                                    </c:if>
                                </div>
                            </div>
                            <div class="checkout_form">
                                <div class="row">
                                    <div class="col-lg-6 col-md-6" style="padding: 0 36px;">
                                        <c:if test="${requestScope.MESSAGE != null}">
                                            <h3 style="color: ${requestScope.CHECK == 'true' ? 'green': 'red'}">
                                                ${requestScope.MESSAGE}
                                            </h3>
                                        </c:if>
                                        <form style="padding: 20px; border: 1px black solid;" action="#">
                                            <h3>Thông tin thanh toán</h3>
                                            <div class="row">
                                                <div class="col-lg-6 mb-30">
                                                    <label>Họ <span>*</span></label>
                                                    <input type="text" value="${sessionScope.account != null ? sessionScope.account.firstName: ''}" readonly>    
                                                </div>
                                                <div class="col-lg-6 mb-30">
                                                    <label>Tên <span>*</span></label>
                                                    <input type="text" value="${sessionScope.account != null ? sessionScope.account.lastName: ''}" readonly> 
                                                </div>
                                                <div class="col-12 mb-30">
                                                    <label>Email</label>
                                                    <input type="email" value="${sessionScope.account != null ? sessionScope.account.email: ''}" readonly>     
                                                </div>
                                                <div class="col-12 mb-30">
                                                    <label>Địa chỉ <span>*</span></label>
                                                    <input placeholder="Số nhà và tên đường" type="text" value="${sessionScope.account != null ? sessionScope.account.address: ''}" readonly>     
                                                </div>
                                                <div class="col-lg-6 mb-30">
                                                    <label>Số điện thoại<span>*</span></label>
                                                    <input type="number" value="${sessionScope.account != null ? sessionScope.account.phone: ''}" readonly> 
                                                </div> 
                                                <div class="col-lg-6 mb-30">
                                                    <label>Email<span>*</span></label>
                                                    <input type="email" value="${sessionScope.account != null ? sessionScope.account.email: ''}" readonly> 
                                                </div> 
                                            </div>
                                        </form>    
                                    </div>
                                    <div class="col-lg-6 col-md-6" style="padding: 0 36px;">
                                        <c:if test="${sessionScope.CART != null && sessionScope.CART.size() > 0 }">
                                            <form action="CheckoutServlet" method="GET">    
                                                <h3>Đơn hàng của bạn</h3> 
                                                <div class="order_table table-responsive mb-30">
                                                    <table>
                                                        <thead>
                                                            <tr>
                                                                <th>Sản phẩm</th>
                                                                <th>Tổng tiền</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach items="${sessionScope.CART}" var="c">
                                                                <tr>
                                                                    <td>${c.product.name}<strong> × ${c.quantity}</strong></td>
                                                                    <td> ${c.product.getSalePrice() * c.quantity}đ</td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                        <tfoot>
                                                            <tr>
                                                                <th>Tổng giỏ hàng</th>
                                                                <td>
                                                                    <c:set var="totalPrice" value="0" />
                                                                    <c:forEach items="${sessionScope.CART}" var="c">
                                                                        <c:set var="productTotal" value="${c.product.getSalePrice() * c.quantity}" />
                                                                        <c:set var="totalPrice" value="${totalPrice + productTotal}" />
                                                                    </c:forEach>
                                                                    ${totalPrice}đ
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>Phí vận chuyển</th>
                                                                <td><strong>Miễn phí</strong></td>
                                                            </tr>
                                                            <tr class="order_total">
                                                                <th>Tổng thanh toán</th>
                                                                <td><strong>${totalPrice}đ</strong></td>
                                                            </tr>
                                                        </tfoot>
                                                    </table>     
                                                </div>
                                                <div class="payment_method">
                                                    <h3>PHƯƠNG THỨC THANH TOÁN</h3>
                                                    <c:forEach items="${requestScope.PAYMENTS}" var="p">
                                                        <div class="panel-default">
                                                            <input id="payment_${p.paymentID}" 
                                                                   name="check_method" 
                                                                   type="radio" 
                                                                   value="${p.paymentID}"
                                                                   onclick="handlePaymentSelection('${p.paymentMethod}')" 
                                                                   checked>
                                                            <label for="payment_${p.paymentID}">${p.paymentMethod}</label>
                                                        </div> 
                                                    </c:forEach>
                                                    
                                                    <!-- Phần hiển thị QR -->
                                                    <div id="qr-payment-section" class="qr-payment-section">
                                                        <h4>Quét mã QR để thanh toán</h4>
                                                        <div class="timer">
                                                            Thời gian còn lại: <span id="countdown">300</span> giây
                                                        </div>
                                                        <div class="qr-code">
                                                            <img id="qr-code-image" src="${requestScope.qrPath}" alt="QR Payment Code" style="max-width: 250px;">
                                                        </div>
                                                        <div class="payment-instructions">
                                                            <h5>Hướng dẫn thanh toán:</h5>
                                                            <ol>
                                                                <li>Mở ứng dụng MB Bank trên điện thoại</li>
                                                                <li>Chọn chức năng "Quét QR"</li>
                                                                <li>Quét mã QR hiển thị phía trên</li>
                                                                <li>Kiểm tra thông tin và xác nhận thanh toán</li>
                                                                <li>Đợi thông báo thanh toán thành công</li>
                                                            </ol>
                                                        </div>
                                                    </div>
                                                </div> 
                                                
                                                <c:if test="${sessionScope.CART != null && sessionScope.CART.size() > 0}">
                                                    <c:if test="${sessionScope.account != null && sessionScope.account.roleID == 2}">
                                                        <div class="order_button">
                                                            <button type="submit">Thanh toán</button> 
                                                        </div>    
                                                    </c:if>
                                                </c:if>
                                            </form>         
                                        </c:if>
                                        <c:if test="${sessionScope.CART == null || sessionScope.CART.size() == 0}">
                                            <div style="text-align: center;">
                                                <img src="view/assets/home/img/cart/emptycart1.png" alt="Giỏ hàng trống">
                                            </div>
                                            <div class="order_button">
                                                <form action="DispatchServlet" method="GET">
                                                    <button type="submit">Mua sắm ngay</button> 
                                                </form>
                                            </div>   
                                        </c:if>
                                    </div>
                                </div> 
                            </div>        
                        </div>
                    </div>
                </div>
            </div>

            <!--footer area start-->
            <%@include file="../../common/web/footer.jsp"%>
            <!--footer area end-->
            
            <!-- Script xử lý QR -->
            <script>
                let countdownTimer;
                
                window.onload = function() {
                    const showQR = ${requestScope.showQR != null ? requestScope.showQR : false};
                    if (showQR) {
                        document.getElementById('qr-payment-section').style.display = 'block';
                        startCountdown();
                    }
                };
                
                function handlePaymentSelection(paymentMethod) {
                    const qrSection = document.getElementById('qr-payment-section');
                    
                    if (paymentMethod === 'Credit Card') {
                        const totalPrice = ${totalPrice};
                        
                        fetch('PaymentQR?amount=' + totalPrice)
                            .then(response => response.json())
                            .then(data => {
                                document.getElementById('qr-code-image').src = data.qrPath;
                                qrSection.style.display = 'block';
                                startCountdown();
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert('Không thể tạo mã QR. Vui lòng thử lại.');
                            });
                    } else {
                        qrSection.style.display = 'none';
                        if (countdownTimer) {
                            clearInterval(countdownTimer);
                        }
                    }
                }
                
                function startCountdown() {
                    if (countdownTimer) {
                        clearInterval(countdownTimer);
                    }
                    
                    let timeLeft = 300;
                    const countdownElement = document.getElementById('countdown');
                    
                    countdownTimer = setInterval(() => {
                        timeLeft--;
                        countdownElement.textContent = timeLeft;
                        
                        if (timeLeft <= 0) {
                            clearInterval(countdownTimer);
                            alert('Mã QR đã hết hạn. Vui lòng tạo mã mới.');
                            document.getElementById('qr-payment-section').style.display = 'none';
                        }
                    }, 1000);
                }
            </script>
    </body>
</html>