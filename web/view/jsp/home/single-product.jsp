<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html class="no-js" lang="zxx">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Single product</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="view/assets/home/img/favicon.png">
    <%@include file="../../common/web/add_css.jsp"%>
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
                                    <li>single product</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="product_details">
                    <c:if test="${requestScope.PRODUCT != null}">
                        <form action="CartServlet" method="get">
                            <input type="hidden" name="product_id" value="${PRODUCT.id}">
                            <input type="hidden" name="action" value="Add">
                            <div class="row">
                                <div class="col-lg-5 col-md-6">
                                    <div class="product_tab fix">
                                        <div class="product_tab_button">
                                            <ul class="nav" role="tablist">
                                                <c:forEach items="${PRODUCT.images}" var="img" varStatus="loop">
                                                    <c:if test="${loop.index < 2}">
                                                        <li><img src="${img}" alt="photo-product"></li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                        <div class="tab-content produc_tab_c">
                                            <c:forEach items="${PRODUCT.images}" var="img" varStatus="loop">
                                                <div class="tab-pane fade show active" id="p_tab1" role="tabpanel">
                                                    <div class="modal_img">
                                                        <a href="#"><img style="margin-bottom: 10px; border: 1px solid #00BBA6" src="${img}" alt=""></a>
                                                        <div class="img_icone">
                                                            <img src="view/assets/home/img/cart/span-new.png" alt="">
                                                        </div>
                                                        <c:if test="${loop.index < 2}">
                                                            <div class="view_img">
                                                                <a class="large_view" href="${img}"><i class="fa fa-search-plus"></i></a>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-7 col-md-6">
                                    <div class="product_d_right">
                                        <h1>${PRODUCT.name}</h1>
                                        <div class="product_desc">
                                            <p>${PRODUCT.description}</p>
                                        </div>
                                        <div class="content_price mb-15">
                                            <span>${PRODUCT.getSalePrice()}đ</span>
                                            <span class="old-price">${PRODUCT.price}đ</span>
                                        </div>
                                        <div class="box_quantity mb-20">
                                            <label>Số lượng</label>
                                            <input name="quantity" min="1" max="${PRODUCT.stock}" value="1" type="number">
                                            <button type="submit"><i class="fa fa-shopping-cart"></i> thêm vào giỏ</button>
                                            <form action="WishlistServlet" method="get" style="display: inline;">
                                                <input type="hidden" name="action" value="Add">
                                                <input type="hidden" name="product_id" value="${PRODUCT.id}">
                                                <button type="submit"><i class="fa fa-heart"></i></button>
                                            </form>
                                        </div>
                                        <div class="sidebar_widget color">
                                            <h2>size</h2>
                                            <div class="widget_color">
                                                <ul>
                                                    <c:forEach items="${PRODUCT.size}" var="s">
                                                        <li><button type="button">${s}</button></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="sidebar_widget color">
                                            <h2>màu</h2>
                                            <div class="widget_color">
                                                <ul>
                                                    <c:forEach items="${PRODUCT.colors}" var="s">
                                                        <li><button type="button">${s}</button></li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="product_stock mb-20">
                                            <p>${PRODUCT.stock} sản phẩm</p>
                                            <span>In stock</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </c:if>
                </div>
                <div class="new_product_area product_page">
                    <div class="row">
                        <div class="col-12">
                            <div class="block_title">
                                <h3>${requestScope.LIST_PRODUCTS_SAME_CATEGORY.size()} sản phẩm cùng loại</h3>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <c:forEach items="${requestScope.LIST_PRODUCTS_SAME_CATEGORY}" var="p">
                            <div class="col-lg-3">
                                <div class="single_product">
                                    <div class="product_thumb">
                                        <a href="SingleProductServlet?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a>
                                        <div class="img_icone">
                                            <img src="view/assets/home/img/cart/span-new.png" alt="">
                                        </div>
                                        <div class="product_action">
                                            <form action="CartServlet" method="get">
                                                <input type="hidden" name="action" value="Add">
                                                <input type="hidden" name="product_id" value="${p.id}">
                                                <input type="hidden" name="quantity" value="1">
                                                <button type="submit" style="color: #00bba6; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">
                                                    <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="product_content">
                                        <div style="display: flex; justify-content: center">
                                            <c:if test="${p.price != p.salePrice}">
                                                <span style="margin-right: 10px; font-weight: 400;" class="old_price">${p.price}đ</span>
                                            </c:if>
                                            <span class="current_price">${p.getSalePrice()}đ</span>
                                        </div>
                                        <h3 class="product_title"><a href="SingleProductServlet?product_id=${p.id}">${p.name}</a></h3>
                                    </div>
                                    <div class="product_info">
                                        <ul>
                                            <li>
                                                <form action="WishlistServlet" method="get">
                                                    <input type="hidden" name="action" value="Add">
                                                    <input type="hidden" name="product_id" value="${p.id}">
                                                    <button type="submit" style="color: red; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">Yêu thích</button>
                                                </form>
                                            </li>
                                            <li><a href="SingleProductServlet?product_id=${p.id}">Xem sản phẩm</a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../../common/web/footer.jsp"%>
    
</body>
</html>