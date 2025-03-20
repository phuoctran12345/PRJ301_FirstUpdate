CREATE DATABASE ClothesShop;
GO
USE ClothesShop;
GO


CREATE TABLE Users (
    id INT NOT NULL IDENTITY(1,1),
    firstname NVARCHAR(30) NOT NULL,
    lastname NVARCHAR(30) NOT NULL,
    email NVARCHAR(50) NOT NULL,
    avatar VARCHAR(200) NOT NULL,
    username VARCHAR(30) PRIMARY KEY NOT NULL,
    password VARCHAR(64) NOT NULL,
    address NVARCHAR(200) NOT NULL,
    phone NVARCHAR(15) NOT NULL,
    roleid INT NOT NULL,
    status BIT NOT NULL
);

CREATE TABLE Types (
    id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100)
);

CREATE TABLE Categories (
    categoryid INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    categoryname NVARCHAR(30),
    type_id INT FOREIGN KEY REFERENCES [dbo].[Types](id)
);

CREATE TABLE Suppliers (
    supplierid INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    suppliername NVARCHAR(100),
    supplierimage VARCHAR(255) NOT NULL
);

CREATE TABLE Products (
    id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    productname NVARCHAR(max) NOT NULL,
    supplierid INT FOREIGN KEY REFERENCES [dbo].[Suppliers](supplierid) ON DELETE SET NULL ON UPDATE CASCADE,
    categoryid INT FOREIGN KEY REFERENCES [dbo].[Categories](categoryid) ON DELETE SET NULL ON UPDATE CASCADE,
    size VARCHAR(40) NOT NULL,
    stock INT NOT NULL, 
    [description] NVARCHAR(max),
    [images] VARCHAR(255) NOT NULL,
    [colors] NVARCHAR(255) NOT NULL,
    releasedate DATE NOT NULL,
    discount FLOAT,
    unitSold INT,
    price MONEY NOT NULL,
    status BIT NOT NULL,
    typeid INT NOT NULL FOREIGN KEY REFERENCES [dbo].[Types](id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Payments (
    paymentid INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    payment_method NVARCHAR(30)
);

CREATE TABLE [Orders] (
    order_id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    orderdate DATETIME,
    totalprice DECIMAL(10,2),
    paymentid INT NOT NULL FOREIGN KEY REFERENCES [dbo].[Payments](paymentid),
    username VARCHAR(30) NOT NULL FOREIGN KEY REFERENCES [dbo].[Users]([username]),
    status BIT NOT NULL
);

CREATE TABLE OrderItem (
    order_item_id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    quantity INT,
    price DECIMAL(10,2),
    product_id INT NOT NULL FOREIGN KEY REFERENCES [dbo].[Products]([id]) ON DELETE CASCADE,
    order_id INT NOT NULL FOREIGN KEY REFERENCES [dbo].[Orders](order_id)
);
GO

-- Chèn dữ liệu vào bảng Users
INSERT INTO Users VALUES
(N'Jung', N'Kim', 'user@gmail.com', 'view/assets/home/img/users/user.jpg', 'user1', '12345', N'Ha Noi', '0981347469', 2, 0),
(N'admin', N'', 'admin@gmail.com', 'view/assets/home/img/users/user.jpg', 'admin', '12345', N'Quận 9', '0981347469', 1, 1),
(N'Phùng', N'Thành', 'thanh@gmail.com', 'view/assets/home/img/users/1.jpg', 'phuuthanh2003', '12345', N'60 Nguyễn Văn Trỗi, Phường 2, TP.Bảo Lộc', '0707064154', 1, 1),
(N'Bé', N'Moon', 'Moon123@gmail.com', 'view/assets/home/img/users/user1.jpg', 'user2', '12345', N'13 Hoàng Hữu Nam, Phường 2, TP.Bảo Lộc', '06868686868', 2, 1),
(N'User', N'3', 'user3@gmail.com', 'view/assets/home/img/users/user3.jpg', 'user3', '12345', N'USA', '06868686868', 2, 1),
(N'Lvh', N'Hoang', 'lvhhoangg171@gmail.com', 'view/assets/home/img/users/default.jpg', 'lvhhoangg171', '12345', N'Unknown', '0123456789', 2, 1);

-- Chèn dữ liệu vào bảng Types
INSERT INTO Types VALUES
(N'Áo'),
(N'Quần'),
(N'Phụ kiện');

-- Chèn dữ liệu vào bảng Categories
INSERT INTO Categories VALUES
(N'Áo sơ mi', 1),
(N'T-Shirt', 1),
(N'Sweatshirt', 1),
(N'Áo khoác', 1),
(N'Hoodies', 1),
(N'Quần short', 2),
(N'Quần thun', 2),
(N'Quần jean', 2),
(N'Áo Polo', 1),
(N'Mũ', 3),
(N'Balo', 3),
(N'Giày', 3),
(N'Áo bóng đá', 1),
(N'Kính', 3);

-- Chèn dữ liệu vào bảng Suppliers
INSERT INTO Suppliers VALUES
('Adidas', 'view/assets/home/img/suppliers/1.jpg'),
('Nike', 'view/assets/home/img/suppliers/2.jpg'),
('Louis Vuitton', 'view/assets/home/img/suppliers/3.jpg'),
('Channel', 'view/assets/home/img/suppliers/4.jpg'),
('BoBui', 'view/assets/home/img/suppliers/5.jpg'),
('4MEN', 'view/assets/home/img/suppliers/6.jpg');

-- Chèn dữ liệu vào bảng Products
INSERT INTO Products VALUES 
(N'ÁO KHOÁC REGULAR TECHNICAL', 6, 4, 'S,M', 5, N'Áo sơ mi khoác bằng cotton dệt chéo, có cổ, nẹp khuy liền và cầu vai phía sau. Túi ngực mở, tay dài có nẹp tay áo và măng sét cài khuy cùng vạt tròn.', 'view/assets/home/img/products/1-1.jpg view/assets/home/img/products/1-2.jpg', N'Đen', '2021-12-01', 0.4, 5, 249.000, 1, 1),
(N'ÁO SƠ MI TRƠN TAY NGẮN', 2, 1, 'S,M,L,XXL', 15, N'Áo Sơ Mi Tay Ngắn Nam Cotton Form Regular đem đến item tối giản với phong cách tràn đầy năng lượng, trẻ trung. Áo được làm từ chất liệu cotton với form áo suông, không ôm vào phần cơ thể đem đến sự thoải mái, nhẹ nhàng. Thân áo suông thẳng, thân sau áo có ly tạo nên điểm nổi bật cho áo.', 'view/assets/home/img/products/2-1.jpg view/assets/home/img/products/2-2.jpg', N'Trắng,Đen,Xám', '2022-02-01', 0.37, 76, 179.000, 1, 1),
(N'QUẦN JEANS XANH WASH LASER TÚI SAU FORM SLIM-CROPPED', 6, 8, 'S,M,L', 45, N'Một chiếc jeans xanh Wash Laser túi sau form slim-cropped 4MEN QJ092 trong tủ đồ có thể giúp các chàng trai mix được hàng chục, hàng trăm outfit khác nhau, từ thanh lịch đến bụi bặm cá tính, rồi năng động và tất nhiên luôn toát lên vẻ đẹp trẻ trung và hiện đại. Sở hữu ngay mẫu quần jeans xanh wash laser túi sau form slim-cropped 4MEN QJ092, chất vải mềm mịn và co giãn tốt sẽ rất thích hợp với ai yêu thích jeans.', 'view/assets/home/img/products/3-1.jpg view/assets/home/img/products/3-2.jpg', N'Xanh dương', '2023-11-01', 0, 72, 545.000, 1, 2),
(N'ÁO HOODIE MAY ĐẮP BASIC FORM REGULAR', 5, 4, 'S,M,L', 30, N'Áo nỉ có mũ, form Regular-Fit; Ngực trái áo có hình thêu chữ sử dụng kỹ thuật đắp vải con giống sắc nét ; 2 bên sườn áO may 2 mảng BO đảm bảo đúng form dáng thiết kế và tăng cảm giác thoải mái khi mặc; Áp dụng công nghệ giặt khô trước may hạn chế tình trạng co rút vải.', 'view/assets/home/img/products/4-1.jpg view/assets/home/img/products/4-2.jpg', N'Xanh dương', '2019-11-01', 0.31, 51, 399.000, 1, 1),
(N'ÁO THUN RÃ PHỐI IN HOME IS FORM REGULAR', 6, 2, 'S,M,L', 30, N'Thiết kế áo thun nam basic, cổ tròn form regular tay ngắn trẻ trung, hiện đại. Áo thun nam phối kẻ ngang nam tính, phong cách hiện đại.', 'view/assets/home/img/products/5-1.jpg view/assets/home/img/products/5-2.jpg', N'Nâu', '2019-11-01', 0.17, 21, 315.000, 1, 1),
(N'ÁO SWEATSHIRT BIG LOGO ADIDAS', 1, 3, 'S,M,L,XL', 10, N'Bất kể bạn chuẩn bị tập luyện buổi sáng hay nghỉ ngơi sau một ngày dài, đã có chiếc áo sweatshirt adidas này đồng hành cùng bạn. Chất liệu vải thun da cá siêu dễ chịu cùng cổ tay và gấu áo bo gân giúp bạn luôn thoải mái và duy trì nhiệt độ hoàn hảo trong mọi hoạt động. Hãy diện chiếc áo này và sẵn sàng cho tất cả.', 'view/assets/home/img/products/6-1.jpg view/assets/home/img/products/6-2.jpg', N'Xám,Trắng', '2022-11-01', 0.15, 11, 875.000, 1, 1),
(N'ÁO BÓNG ĐÁ NIKE LFC M NK SSL SWOOSH TEE NAM DZ3613-010', 2, 13, 'M,L', 30, N'Chất liệu cotton mềm, nhẹ. In đồ họa tương phản với mặt trước. Cổ thuyền với tay áo ngắn. In thương hiệu logo swoosh của Nike.', 'view/assets/home/img/products/7-1.jpg view/assets/home/img/products/7-2.jpg', N'Đen', '2022-11-01', 0, 21, 699.000, 1, 1),
(N'QUẦN JOGGER THUN RÃ PHỐI FORM REGULAR', 3, 7, 'S,M,L,XL', 30, N'Với xu hướng bùng nổ thời trang thể thao và thời trang đường phố hiện nay thì với Quần Jogger Nam – đại diện cho phong cách Street Style ngày càng được ưa chuộng. Đặc biệt, để phù hợp cho môi trường đi làm thì Kaki là chất liệu được đánh giá là lịch sự và trang trọng hơn hẳn chất liệu thun hay nỉ. Vì vậy mà bạn có thể tự tin diện Quần Jogger Kaki vừa để đi làm vừa để đi chơi.', 'view/assets/home/img/products/8-1.jpg view/assets/home/img/products/8-2.jpg', N'Vàng', '2024-09-01', 0.34, 31, 425.000, 1, 2),
(N'ÁO KHOÁC GOLF ADIDAS HYBRID-SPACER', 1, 4, 'S,M,L', 30, N'Bắt đầu buổi chơi trong phong cách thanh thoát với chiếc áo khoác golf adidas này. Cấu trúc hybrid kết hợp hai lớp vải được bố trí hợp lý để giữ ấm, cùng độ co giãn tăng cường tại những vị trí cần thiết nhất để bạn vận động tối ưu trên sân golf. Các túi giúp giữ ấm đôi tay giữa những cú đánh và cất các vật dụng nhỏ khi chơi.', 'view/assets/home/img/products/9-1.jpg view/assets/home/img/products/9-2.jpg', N'Đen, Nâu', '2024-11-01', 0.41, 51, 2660.000, 1, 1),
(N'ÁO BÓNG ĐÁ NIKE AS M NK DF FC LIBERO HOODIE', 2, 13, 'S,M,L,XL', 20, N'Áo bóng đá nike AS M NK DF FC LIBERO HOODIE nam DC9076-010', 'view/assets/home/img/products/10-1.jpg view/assets/home/img/products/10-2.jpg', N'Đen', '2022-12-01', 0, 5, 1053.000, 1, 1),
(N'TRAVIS SCOTT CACT.US CORP X NIKE U NRG BH LONG SLEEVE T', 2, 3, 'S', 15, N'Hoạt động trong nhà/ ngoài trời.', 'view/assets/home/img/products/11-1.jpg view/assets/home/img/products/11-2.jpg', N'Đen', '2024-02-01', 0.37, 76, 1100.000, 1, 1);

-- Chèn dữ liệu vào bảng Payments
INSERT INTO Payments VALUES 
(N'Tiền mặt'),
(N'Credit Card');

-- Chèn dữ liệu vào bảng Orders
INSERT INTO [Orders] VALUES 
('2024-03-10 12:30:00', 2241.00, 1, 'phuuthanh2003', 1), -- Order 1
('2018-05-19 11:30:00', 1167.00, 1, 'phuuthanh2003', 1), -- Order 2 
('2018-07-20 11:19:00', 4396.00, 1, 'phuuthanh2003', 1), -- Order 3
('2019-01-19 11:30:00', 2317.00, 1, 'user2', 1),      -- Order 4 
('2024-01-19 11:30:00', 2200.00, 1, 'user2', 1),      -- Order 5
('2023-06-10 12:30:00', 1190.00, 1, 'phuuthanh2003', 1), -- Order 6
('2023-02-10 12:30:00', 1053.00, 1, 'phuuthanh2003', 0), -- Order 7
('2020-02-10 12:30:00', 875.00, 1, 'phuuthanh2003', 1),  -- Order 8
('2019-02-14 12:30:00', 875.00, 1, 'admin', 1);        -- Order 9

-- Chèn dữ liệu vào bảng OrderItem (đảm bảo khớp với Orders)
INSERT INTO OrderItem VALUES 
(9, 249.00, 1, 1),       
(3, 179.00, 2, 2),        
(2, 315.00, 5, 2),        
(4, 699.00, 7, 3),        
(1, 1596.00, 9, 3),       
(2, 1569.40, 9, 4),       
(2, 315.00, 5, 4),        
(2, 179.00, 2, 4),        
(2, 1100.00, 11, 5),      
(1, 875.00, 6, 6),        
(1, 315.00, 5, 6),        
(1, 1053.00, 10, 7),      
(1, 875.00, 6, 8),        
(1, 875.00, 6, 9);        
GO