<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Shop Products</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/home/img/favicon.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .pos_page_inner {
            max-width: 1200px;
            margin: 0 auto;
        }
        .breadcrumbs_area {
            margin-bottom: 20px;
        }
        .breadcrumb_content ul {
            list-style: none;
            padding: 0;
            display: flex;
            gap: 10px;
        }
        .breadcrumb_content ul li a {
            color: #018576;
            text-decoration: none;
        }
        .breadcrumb_content ul li a:hover {
            text-decoration: underline;
        }
        .pos_home {
            display: flex;
            gap: 30px;
        }
        .col-lg-3 {
            flex: 0 0 25%;
            max-width: 25%;
        }
        .col-lg-9 {
            flex: 0 0 75%;
            max-width: 75%;
        }
        .sidebar_widget {
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        .sidebar_widget h2, .categorie__titile h4 {
            color: #333;
            margin-bottom: 15px;
        }
        .layere_categorie ul, .widget_color ul {
            list-style: none;
            padding: 0;
        }
        .layere_categorie ul li, .widget_color ul li {
            margin-bottom: 10px;
        }
        .layere_categorie ul li label, .widget_color ul li label {
            margin-left: 5px;
            cursor: pointer;
        }
        .shopee-price-range-filter__inputs {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .shopee-price-range-filter__inputs input {
            width: 90px;
            height: 35px;
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .shopee-price-range-filter__range-line {
            flex: 1;
            height: 1px;
            background: #bdbdbd;
        }
        .submit-price {
            display: inline-block;
            background: #018576;
            color: #fff;
            padding: 8px 20px;
            border-radius: 20px;
            text-decoration: none;
            transition: background 0.3s ease;
        }
        .submit-price:hover {
            background: #015d52;
        }
        .banner_slider img {
            width: 100%;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .shop_toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .list_button ul {
            list-style: none;
            padding: 0;
        }
        .list_button ul li a {
            color: #018576;
            font-size: 20px;
            text-decoration: none;
        }
        .select_option {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .select_option select {
            padding: 5px;
            border-radius: 5px;
        }
        .row {
            display: flex;
            flex-wrap: wrap;
            gap: 30px;
        }
        .col-lg-4 {
            flex: 0 0 calc(33.33% - 20px);
            max-width: calc(33.33% - 20px);
        }
        .single_product {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.3s ease;
        }
        .single_product:hover {
            transform: translateY(-5px);
        }
        .product_thumb {
            position: relative;
            text-align: center;
        }
        .product_thumb img {
            width: 100%;
            height: 250px;
            object-fit: cover;
        }
        .img_icone {
            position: absolute;
            top: 10px;
            left: 10px;
        }
        .discount {
            position: absolute;
            top: 10px;
            right: 10px;
            background: #ff4d4d;
            color: #fff;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
        }
        .product_action button {
            width: 100%;
            background: #018576;
            color: #fff;
            border: none;
            padding: 10px;
            font-size: 14px;
            text-transform: uppercase;
            cursor: pointer;
            transition: background 0.3s ease;
        }
        .product_action button:hover {
            background: #015d52;
        }
        .product_content {
            padding: 15px;
            text-align: center;
        }
        .old_price {
            color: #999;
            text-decoration: line-through;
            font-size: 14px;
        }
        .current_price {
            color: #018576;
            font-size: 18px;
            font-weight: bold;
        }
        .product_title a {
            color: #333;
            text-decoration: none;
            font-size: 16px;
            display: block;
            margin-top: 10px;
        }
        .product_title a:hover {
            color: #018576;
        }
        .product_info ul {
            list-style: none;
            padding: 0;
            margin: 10px 0 0;
            display: flex;
            justify-content: center;
            gap: 15px;
        }
        .product_info ul li a, .product_info ul li button {
            color: #666;
            text-decoration: none;
            font-size: 13px;
            background: none;
            border: none;
            cursor: pointer;
        }
        .product_info ul li button {
            color: red;
            font-weight: 600;
        }
        .product_info ul li a:hover, .product_info ul li button:hover {
            color: #018576;
        }
        .pagination_style {
            text-align: center;
            margin-top: 30px;
        }
        .pagination_style ul {
            list-style: none;
            padding: 0;
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .pagination_style ul li a {
            display: inline-block;
            padding: 8px 14px;
            background: #fff;
            color: #018576;
            text-decoration: none;
            border: 1px solid #ddd;
            border-radius: 5px;
            transition: background 0.3s ease;
        }
        .pagination_style ul li a.active {
            background: #018576;
            color: #fff;
            border-color: #018576;
        }
        .pagination_style ul li a:hover {
            background: #018576;
            color: #fff;
        }
    </style>
</head>
<body>
    <div class="pos_page">
        <div class="pos_page_inner">
            <!-- Header (Assuming it's included from an external file in the original) -->
            <!-- For simplicity, we'll simulate a basic header -->
            <header style="background: #fff; padding: 20px; text-align: center; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px;">
                <h1><a href="${pageContext.request.contextPath}/DispatchServlet" style="color: #018576; text-decoration: none;">Shop Name</a></h1>
            </header>

            <!-- Breadcrumbs -->
            <div class="breadcrumbs_area">
                <div class="breadcrumb_content">
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/DispatchServlet">home</a></li>
                        <li><i class="fa fa-angle-right"></i></li>
                        <li>shop</li>
                    </ul>
                </div>
            </div>

            <div class="pos_home">
                <!-- Sidebar Filter -->
                <form id="form-filter" action="${pageContext.request.contextPath}/FilterServlet" method="get" class="col-lg-3">
                    <div class="sidebar_widget shop_c">
                        <div class="categorie__titile">
                            <h4>Phân loại</h4>
                        </div>
                        <div class="layere_categorie">
                            <ul>
                                <li>
                                    <input id="defaultcate" value="0" name="id_filter" type="checkbox">
                                    <label for="defaultcate">Tất cả</label>
                                </li>
                                <c:forEach items="${requestScope.LISTCATEGORIES}" var="cat">
                                    <li>
                                        <input value="${cat.id}" id="${cat.id}" type="checkbox" name="id_filter">
                                        <label for="${cat.id}">${cat.name}</label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="sidebar_widget color">
                        <h2>Color</h2>
                        <div class="widget_color">
                            <ul>
                                <li><input type="checkbox" name="color" value="Đen"><label>Đen</label></li>
                                <li><input type="checkbox" name="color" value="Xanh lá"><label>Xanh lá</label></li>
                                <li><input type="checkbox" name="color" value="Cam"><label>Cam</label></li>
                                <li><input type="checkbox" name="color" value="Xanh dương"><label>Xanh dương</label></li>
                                <li><input type="checkbox" name="color" value="Vàng"><label>Vàng</label></li>
                                <li><input type="checkbox" name="color" value="Nâu"><label>Nâu</label></li>
                                <li><input type="checkbox" name="color" value="Trắng"><label>Trắng</label></li>
                                <li><input type="checkbox" name="color" value="Đỏ"><label>Đỏ</label></li>
                            </ul>
                        </div>
                    </div>
                    <div class="sidebar_widget price">
                        <h2>Price</h2>
                        <div class="shopee-price-range-filter__inputs">
                            <input type="number" name="pricefrom" placeholder="đ FROM" step="0.5" min="1">
                            <div class="shopee-price-range-filter__range-line"></div>
                            <input type="number" name="priceto" placeholder="đ TO" step="0.5" min="1">
                        </div>
                    </div>
                    <div class="sidebar_widget shop_c">
                        <div class="categorie__titile">
                            <h4>Discount</h4>
                        </div>
                        <div class="layere_categorie">
                            <ul>
                                <li><input id="dis25" type="radio" name="discount" value="dis25"><label for="dis25">Up to 25%</label></li>
                                <li><input id="dis40" type="radio" name="discount" value="dis40"><label for="dis40">Up to 40%</label></li>
                                <li><input id="dis75" type="radio" name="discount" value="dis75"><label for="dis75">Up to 75%</label></li>
                            </ul>
                        </div>
                    </div>
                    <button type="submit" class="submit-price">Apply</button>
                    <a href="${pageContext.request.contextPath}/ShopServlet" class="submit-price">Reset</a>
                </form>

                <!-- Main Content -->
                <div class="col-lg-9">
                    <div class="banner_slider">
                        <img src="${pageContext.request.contextPath}/assets/home/img/banner/banner10.jpg" alt="Banner">
                    </div>
                    <div class="shop_toolbar">
                        <div class="list_button">
                            <ul>
                                <li><a class="active" href="#"><i class="fa fa-th-large"></i></a></li>
                            </ul>
                        </div>
                        <div class="select_option">
                            <label>Sort By: </label>
                            <form action="${pageContext.request.contextPath}/ShopServlet" method="get">
                                <select name="valueSort" onchange="this.form.submit()">
                                    <option value="0">Nổi bật</option>
                                    <option value="1">Giá: Thấp đến Cao</option>
                                    <option value="2">Giá: Cao đến Thấp</option>
                                    <option value="3">Tên: A-Z</option>
                                </select>
                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <c:forEach items="${requestScope.LISTPRODUCTS}" var="p">
                            <div class="col-lg-4">
                                <div class="single_product">
                                    <div class="product_thumb">
                                        <a href="${pageContext.request.contextPath}/SingleProductServlet?product_id=${p.id}">
                                            <img src="${p.images[0]}" alt="${p.name}">
                                        </a>
                                        <c:if test="${p.releasedate.year == 124}">
                                            <div class="img_icone">
                                                <img src="${pageContext.request.contextPath}/assets/img/cart/span-new.png" alt="New">
                                            </div>
                                        </c:if>
                                        <c:if test="${p.discount != 0}">
                                            <span class="discount">Up to ${p.discount * 100}%</span>
                                        </c:if>
                                        <div class="product_action">
                                            <form action="${pageContext.request.contextPath}/CartServlet" method="post">
                                                <input type="hidden" name="action" value="Add">
                                                <input type="hidden" name="product_id" value="${p.id}">
                                                <input type="hidden" name="quantity" value="1">
                                                <button type="submit">
                                                    <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="product_content">
                                        <div style="display: flex; justify-content: center; align-items: center; gap: 10px;">
                                            <c:if test="${p.price != p.salePrice}">
                                                <span class="old_price">Rs. ${p.price}đ</span>
                                            </c:if>
                                            <span class="current_price">Rs. ${p.salePrice}đ</span>
                                        </div>
                                        <h3 class="product_title">
                                            <a href="${pageContext.request.contextPath}/SingleProductServlet?product_id=${p.id}">
                                                ${p.name}
                                            </a>
                                        </h3>
                                    </div>
                                    <div class="product_info">
                                        <ul>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/WishlistServlet" method="post">
                                                    <input type="hidden" name="action" value="Add">
                                                    <input type="hidden" name="product_id" value="${p.id}">
                                                    <button type="submit">Yêu thích</button>
                                                </form>
                                            </li>
                                            <li>
                                                <a href="${pageContext.request.contextPath}/SingleProductServlet?product_id=${p.id}" title="Quick view">View Detail</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${requestScope.NUMBERPAGE > 1}">
                        <div class="pagination_style">
                            <div class="page_number">
                                <span>Pages: </span>
                                <ul>
                                    <c:set var="page" value="${requestScope.CURRENTPAGE}"/>
                                    <c:if test="${page != 1}">
                                        <li><a href="${pageContext.request.contextPath}/FilterServlet?${requestScope.QUERYSTRING}&page=${page - 1}">«</a></li>
                                    </c:if>
                                    <c:forEach begin="1" end="${requestScope.NUMBERPAGE}" var="i">
                                        <li><a href="${pageContext.request.contextPath}/FilterServlet?${requestScope.QUERYSTRING}&page=${i}" class="${i == requestScope.CURRENTPAGE ? 'active' : ''}">${i}</a></li>
                                    </c:forEach>
                                    <c:if test="${page != requestScope.NUMBERPAGE}">
                                        <li><a href="${pageContext.request.contextPath}/FilterServlet?${requestScope.QUERYSTRING}&page=${page + 1}">»</a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Footer (Assuming it's included from an external file in the original) -->
            <!-- For simplicity, we'll simulate a basic footer -->
            <footer style="background: #fff; padding: 20px; text-align: center; box-shadow: 0 -2px 4px rgba(0,0,0,0.1); margin-top: 20px;">
                <p>&copy; 2025 Shop Name. All rights reserved.</p>
            </footer>
        </div>
    </body>
</html>