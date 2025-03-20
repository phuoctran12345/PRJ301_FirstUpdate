<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>My account</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="shortcut icon" type="image/x-icon" href="view\assets\home\img\favicon.png">
        <%@include file="../../common/web/add_css.jsp"%>
        <style type="text/css">
        </style>
    </head>
    <body>
        <div class="pos_page">
            <div class="container">  
                <div class="pos_page_inner">  
                    <%@include file="../../common/web/header.jsp"%>

                    <div class="breadcrumbs_area">
                        <div class="row">
                            <div class="col-12">
                                <div class="breadcrumb_content">
                                    <ul>
                                        <li><a href="DispatchServlet">home</a></li>
                                        <li><i class="fa fa-angle-right"></i></li>
                                        <c:choose>
                                            <c:when test="${requestScope.SHOW_ORDER_DETAIL}">
                                                <li><a href="ProfileServlet?tab=orders">my account</a></li>
                                                <li><i class="fa fa-angle-right"></i></li>
                                                <li>order details #${requestScope.ORDER_ID}</li>
                                            </c:when>
                                            <c:otherwise>
                                                <li>my account</li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <section class="main_content_area">
                        <div class="account_dashboard">
                            <div class="row">
                                <div class="col-sm-12 col-md-3 col-lg-3">
                                    <div class="dashboard_tab_button">
                                        <ul role="tablist" class="nav flex-column dashboard-list">
                                            <li style="margin-bottom: 20px"><img style="border: 5px solid #00BBA6; height: 255px" src="${sessionScope.account.avatar}" width="100%"></li>
                                            <li><a href="ProfileServlet" class="nav-link ${param.tab == null || param.tab == 'account' ? 'active' : ''}">Account details</a></li>
                                            <c:if test="${sessionScope.account != null && sessionScope.account.roleID == 2}">
                                                <li><a href="ProfileServlet?tab=orders" class="nav-link ${param.tab == 'orders' ? 'active' : ''}">Orders</a></li>
                                            </c:if>
                                        </ul>
                                    </div>    
                                </div>
                                <div class="col-sm-12 col-md-9 col-lg-9">
                                    <div class="tab-content dashboard_content">
                                        <c:choose>
                                            <c:when test="${requestScope.SHOW_ORDER_DETAIL}">
                                                <div class="tab-pane fade show active" id="order-details">
                                                    <h3>Order Details #${requestScope.ORDER_ID}</h3>
                                                    <div class="coron_table table-responsive">
                                                        <table class="table">
                                                            <thead>
                                                                <tr>
                                                                    <th>Product Name</th>
                                                                    <th>Quantity</th>
                                                                    <th>Price</th>
                                                                    <th>Total</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach items="${requestScope.ORDER_ITEMS}" var="item">
                                                                    <tr>
                                                                        <td>${item.product.name}</td>
                                                                        <td>${item.quantity}</td>
                                                                        <td>${item.price}</td>
                                                                        <td>${item.quantity * item.price}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <div class="save_button primary_btn default_button">
                                                        <a href="ProfileServlet?tab=orders" class="btn">Back to Orders</a>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${param.tab == 'orders'}">
                                                <div class="tab-pane fade show active" id="orders">
                                                    <h3>Orders</h3>
                                                    <div class="coron_table table-responsive">
                                                        <table class="table">
                                                            <thead>
                                                                <tr>
                                                                    <th>Order ID</th>
                                                                    <th>Date</th>
                                                                    <th>Status</th>
                                                                    <th>Total</th>
                                                                    <th>Actions</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach items="${requestScope.LISTORDERS}" var="o">
                                                                    <tr>
                                                                        <td>${o.orderID}</td>
                                                                        <td>${o.orderDate}</td>
                                                                        <td><span class="success">${o.status == true ? "Đã giao" : "Chưa giao"}</span></td>
                                                                        <td>${o.totalPrice}</td>
                                                                        <td><a href="ProfileServlet?orderID=${o.orderID}" class="view">view</a></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="tab-pane fade show active" id="account-details">
                                                    <h3>Account details</h3>
                                                    <c:if test="${requestScope.STATUS !=null }" >
                                                        <h6 style="color: green">${requestScope.STATUS}</h6>
                                                    </c:if>
                                                    <div class="login">
                                                        <div class="login_form_container">
                                                            <div class="account_login_form">
                                                                <form action="EditProfileServlet" method="${requestScope.EDIT_MODE ? 'post' : 'get'}">
                                                                    <label>Username</label>
                                                                    <input class="input_type" type="text" name="username" value="${sessionScope.account.userName}" readonly>
                                                                    <label>Role</label>
                                                                    <input class="input_type" type="text" name="role" value="${sessionScope.account.roleID == 1? 'Admin' : 'Customer'}" readonly>
                                                                    <label>First Name</label>
                                                                    <input class="input_type" type="text" name="first-name" value="${sessionScope.account.firstName}" ${requestScope.EDIT_MODE ? '' : 'readonly'}>
                                                                    <label>Last Name</label>
                                                                    <input class="input_type" type="text" name="last-name" value="${sessionScope.account.lastName}" ${requestScope.EDIT_MODE ? '' : 'readonly'}>
                                                                    <label>Email</label>
                                                                    <input class="input_type" type="text" name="email" value="${sessionScope.account.email}" ${requestScope.EDIT_MODE ? '' : 'readonly'}>
                                                                    <input class="input_type" type="hidden" name="avatar" value="${sessionScope.account.avatar}">
                                                                    <label>Address</label>
                                                                    <input class="input_type" type="text" name="address" value="${sessionScope.account.address}" ${requestScope.EDIT_MODE ? '' : 'readonly'}>
                                                                    <label>Phone</label>
                                                                    <input class="input_type" type="text" name="phone" value="${sessionScope.account.phone}" ${requestScope.EDIT_MODE ? '' : 'readonly'}>
                                                                    <div class="save_button primary_btn default_button">
                                                                        <c:choose>
                                                                            <c:when test="${requestScope.EDIT_MODE}">
                                                                                <button type="submit">Save</button>
                                                                                <a href="ProfileServlet" class="btn">Cancel</a>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <button type="submit" formmethod="get">Edit</button>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>      	
                    </section>
                </div>
            </div>
        </div>
        <%@include file="../../common/web/footer.jsp"%>
    </body>
</html>