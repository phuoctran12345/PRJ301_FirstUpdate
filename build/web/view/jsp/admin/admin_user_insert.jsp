<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Tạo mới tài khoản | Quản trị Admin</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="view/assets/admin/css/main.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
</head>
<body class="app sidebar-mini rtl">
    <%@include file="../../common/admin/sidebar.jsp"%>
    <main class="app-content">
        <div class="app-title">
            <ul class="app-breadcrumb breadcrumb">
                <li class="breadcrumb-item"><a href="UserManagementServlet">Danh sách người dùng</a></li>
                <li class="breadcrumb-item active">Tạo mới tài khoản</li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="tile">
                    <h3 class="tile-title">Tạo mới tài khoản</h3>
                    <div class="tile-body">
                        <h3 style="color: green; text-align: center; margin: 20px 0">${requestScope.mess}</h3>
                        <h3 style="color: red; text-align: center; margin: 20px 0">${requestScope.error}</h3>
                        <form class="row" action="UserManagementServlet" method="post">
                            <input type="hidden" name="action" value="InsertUser">
                            <div class="form-group col-md-3">
                                <label class="control-label">Ảnh đại diện</label>
                                <input type="file" name="avatar" accept="image/*" />
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Tên đầy đủ</label>
                                <input class="form-control" required name="fullname" type="text">
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Tên người dùng</label>
                                <input class="form-control" required name="username" type="text">
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Mật khẩu</label>
                                <input class="form-control" required name="password" type="text">
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Email</label>
                                <input class="form-control" type="email" name="email" value="${email}" placeholder="example@domain.com" >
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Địa chỉ</label>
                                <input class="form-control" required name="address" type="text">
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Số điện thoại</label>
                                <input class="form-control" required name="phone" type="tel" pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số">
                            </div>
                            <div class="form-group col-md-3">
                                <label class="control-label">Quyền quản trị</label>
                                <select name="role" class="form-control">
                                    <option value="admin">Admin</option>
                                    <option value="user">User</option>
                                </select>
                            </div>
                            <div class="form-group col-md-12">
                                <button class="btn btn-save" type="submit">Lưu lại</button>
                                <a class="btn btn-cancel" href="${pageContext.request.contextPath}/UserManagementServlet">Hủy bỏ</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>