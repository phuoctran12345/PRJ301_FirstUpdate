<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Clothes - Shop</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="view\assets\home\img\favicon.png">
        <!-- all css here -->
        <%@include file="../../common/web/add_css.jsp"%>
        <style>
            .chat-bubble-modern {
                position: fixed;
                bottom: 30px;
                right: 30px;
                background-color: #018576;
                color: white;
                border: none;
                border-radius: 50px;
                padding: 10px 15px; /* Điều chỉnh padding để vừa với 2 emoji */
                display: flex;
                align-items: center;
                gap: 2px; /* Giảm gap để gấu trúc và bong bóng gần nhau hơn */
                cursor: pointer;
                box-shadow: 0 4px 15px rgba(0,0,0,0.2);
                transition: all 0.3s ease;
                z-index: 1000;
            }
            .chat-bubble-modern:hover {
                background-color: #016658;
                transform: scale(1.05);
            }
            .chat-icon {
                font-size: 24px; /* Kích thước icon */
            }
            @keyframes pulse {
                0% { transform: scale(1); }
                50% { transform: scale(1.05); }
                100% { transform: scale(1); }
            }
            .chat-bubble-modern {
                animation: pulse 2s infinite;
            }
        </style>
    </head>
    <body>
        <!--pos page start-->
        <div class="pos_page">
            <div class="container">
                <!--pos page inner-->
                <div class="pos_page_inner">  
                    <!--header area -->
                    <%@include file="../../common/web/header.jsp"%>
                    <!--header end -->

                    <!--pos home section-->
                    <form action="DispatchServlet" method="post">
                        <button type="submit" name="btnAction" value="Chat" class="chat-bubble-modern">
                            <span class="chat-icon">🐼</span> <!-- Icon gấu trúc -->
                            <span class="chat-icon">💬</span> <!-- Icon bong bóng chat -->
                        </button>
                    </form>
                    <div class="pos_home_section">
                        <div class="row pos_home">
                            <div class="col-lg-3 col-md-8 col-12">
                                <!--sidebar banner-->
                                <div class="sidebar_widget banner mb-35">
                                    <div class="banner_img mb-35">
                                        <a href="#"><img src="view\assets\home\img\banner\banner5.jpg" alt=""></a>
                                    </div>
                                    <div class="banner_img">
                                        <a href="#"><img src="view\assets\home\img\banner\banner6.jpg" alt=""></a>
                                    </div>
                                </div>
                                <!--sidebar banner end-->

                                <!--wishlist block start-->
                                <c:if test="${sessionScope.WISHLIST != null}">
                                    <div class="sidebar_widget wishlist mb-35" id="wishlist-small">
                                        <div class="block_title">
                                            <h3><a href="WishlistServlet">Wishlist</a></h3>
                                        </div>
                                        <c:forEach items="${sessionScope.WISHLIST}" var="p" varStatus="loop">
                                            <c:if test="${loop.index <= 2}">
                                                <div class="cart_item">
                                                    <div class="cart_img">
                                                        <a href="SingleProductServlet?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a>
                                                    </div>
                                                    <div class="cart_info">
                                                        <a href="SingleProductServlet?product_id=${p.id}">${p.name}</a>
                                                        <span class="cart_price">$${p.salePrice}</span>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                        <div class="block_content">
                                            <p>${sessionScope.WISHLIST.size()} products</p>
                                            <a href="WishlistServlet">» My wishlists</a>
                                        </div>
                                    </div>
                                </c:if>
                                <!--wishlist block end-->

                                <!--sidebar banner-->
                                <div class="sidebar_widget bottom">
                                    <div class="banner_img">
                                        <a href="#"><img src="view\assets\home\img\banner\banner9.jpg" alt=""></a>
                                    </div>
                                </div>
                                <!--sidebar banner end-->
                            </div>
                            <div class="col-lg-9 col-md-12">
                                <!--banner slider start-->
                                <div class="banner_slider slider_1">
                                    <!-- Static banner instead of carousel -->
                                    <div class="single_slider" style="background-image: url(view/assets/home/img/slider/slide_1.png)">
                                        <div class="slider_content">
                                            <div class="slider_content_inner">  
                                                <h1>Men's Fashion</h1>
                                                <p>Thời trang, phong cách trẻ trung.</p>
                                                <a href="ShopServlet">shop now</a>
                                            </div>     
                                        </div>    
                                    </div>
                                </div> 
                                <!--banner slider end-->

                                <!--new product area start-->
                                <div class="new_product_area">
                                    <div class="block_title">
                                        <h3>Mẫu mới 2024</h3>
                                    </div>
                                    <div class="row">
                                        <c:if test="${requestScope.LIST_PRODUCTS_NEW != null}">
                                            <c:forEach var="p" items="${requestScope.LIST_PRODUCTS_NEW}">
                                                <div class="col-lg-3 col-md-6">
                                                    <div class="single_product">
                                                        <div class="product_thumb">
                                                            <a href="SingleProductServlet?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a> 
                                                            <div class="img_icone">
                                                                <img src="view/assets/home/img/cart/span-new.png" alt="">
                                                            </div>
                                                            <c:if test="${p.discount != 0}">
                                                                <span class="discount">Up to ${p.discount * 100}%</span>
                                                            </c:if>
                                                            <div class="product_action">
                                                                <form action="CartServlet" method="post">
                                                                    <input type="hidden" name="action" value="Add">
                                                                    <input type="hidden" name="product_id" value="${p.id}">
                                                                    <input type="hidden" name="quantity" value="1">
                                                                    <button type="submit" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px;">
                                                                        <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                                    </button>
                                                                </form>
                                                            </div>
                                                        </div>
                                                        <div class="product_content">
                                                            <div style="display: flex; justify-content: center">
                                                                <c:if test="${p.price != p.salePrice}">
                                                                    <span style="margin-right: 10px; font-weight: 400;" class="old_price">Rs. ${p.price}đ</span>
                                                                </c:if>
                                                                <span class="current_price">Rs. ${p.salePrice}đ</span>
                                                            </div>
                                                            <h3 class="product_title"><a href="SingleProductServlet?product_id=${p.id}">${p.name}</a></h3>
                                                        </div>
                                                        <div class="product_info">
                                                            <ul>
                                                                <li>
                                                                    <form action="WishlistServlet" method="post">
                                                                        <input type="hidden" name="action" value="Add">
                                                                        <input type="hidden" name="product_id" value="${p.id}">
                                                                        <button type="submit" style="color: red; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">Yêu thích</button>
                                                                    </form>
                                                                </li>
                                                                <li><a href="SingleProductServlet?product_id=${p.id}" title="View Detail">Xem sản phẩm</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                    </div>       
                                </div> 
                                <!--new product area end-->  

                                <!--featured product start--> 
                                <div class="featured_product">
                                    <div class="block_title">
                                        <h3>Bán chạy nhất</h3>
                                    </div>
                                    <div class="row">
                                        <c:forEach items="${requestScope.LIST_PRODUCTS_SELLER}" var="p">
                                            <div class="col-lg-3 col-md-6">
                                                <div class="single_product">
                                                    <div class="product_thumb">
                                                        <a href="SingleProductServlet?product_id=${p.id}"><img src="${p.images[0]}" alt=""></a> 
                                                        <div class="hot_img">
                                                            <img src="view\assets\home\img\cart\span-hot.png" alt="">
                                                        </div>
                                                        <div class="product_action">
                                                            <form action="CartServlet" method="post">
                                                                <input type="hidden" name="action" value="Add">
                                                                <input type="hidden" name="product_id" value="${p.id}">
                                                                <input type="hidden" name="quantity" value="1">
                                                                <button type="submit" style="display: block; border: none; width: 100%; background: #018576; color: #fff; padding: 7px 0; text-transform: capitalize; font-size: 13px;">
                                                                    <i class="fa fa-shopping-cart"></i> Thêm vào giỏ
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                    <div class="product_content">
                                                        <div style="display: flex; justify-content: center">
                                                            <c:if test="${p.price != p.salePrice}">
                                                                <span style="margin-right: 10px; font-weight: 400;" class="old_price">Rs. ${p.price}đ</span>
                                                            </c:if>
                                                            <span class="current_price">Rs. ${p.salePrice}đ</span>
                                                        </div>
                                                        <h3 class="product_title"><a href="SingleProductServlet?product_id=${p.id}">${p.name}</a></h3>
                                                    </div>
                                                    <div class="product_info">
                                                        <ul>
                                                            <li>
                                                                <form action="WishlistServlet" method="post">
                                                                    <input type="hidden" name="action" value="Add">
                                                                    <input type="hidden" name="product_id" value="${p.id}">
                                                                    <button type="submit" style="color: red; border: none; border-radius: 4px; font-size: 13px; padding: 2px 11px; font-weight: 600;">Yêu thích</button>
                                                                </form>
                                                            </li>
                                                            <li><a href="SingleProductServlet?product_id=${p.id}" title="View Detail">Xem sản phẩm</a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div> 
                                </div>     
                                <!--featured product end--> 

                                <!--banner area start-->
                                <div class="banner_area mb-60">
                                    <div class="row">
                                        <div class="col-lg-6 col-md-6">
                                            <div class="single_banner">
                                                <a href="FilterServlet?discount=dis40"><img src="view\assets\home\img\banner\banner7.jpg" alt=""></a>
                                                <div class="banner_title">
                                                    <p>Up to <span>40%</span> off</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-lg-6 col-md-6">
                                            <div class="single_banner">
                                                <a href="FilterServlet?discount=dis25"><img src="view\assets\home\img\banner\banner8.jpg" alt=""></a>
                                                <div class="banner_title title_2">
                                                    <p>sale off <span>25%</span></p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>     
                                <!--banner area end--> 

                                <!--brand logo start--> 
                                <div class="brand_logo mb-60">
                                    <div class="block_title">
                                        <h3>Thương hiệu</h3>
                                    </div>
                                    <div class="row">
                                        <c:forEach items="${requestScope.LIST_SUPPLIERS}" var="s">
                                            <div class="col-lg-2 col-md-3">
                                                <div class="single_brand">
                                                    <a href="FilterServlet?btnAction=filterBySupplier&id_filter=${s.id}"><img src="${s.image}" alt=""></a>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>       
                                <!--brand logo end-->        
                            </div>
                        </div>  
                    </div>
                    <!--pos home section end-->
                </div>
                <!--pos page inner end-->
            </div>
        </div>
        <!--pos page end-->

        <!--footer area start-->
        <%@include file="../../common/web/footer.jsp"%>
        <!--footer area end-->

    </body>
</html>