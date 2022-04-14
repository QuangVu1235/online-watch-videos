use ONLINE_ENTERTAIMENT
go

-- Xoá table
drop table History
drop table Share
drop table [User]
drop table Video
go

-- Tạo lại table
create table Video(
	Id varchar(20) primary key,
	Title nvarchar(255) not null,
	Poster varchar(MAX) not null,
	[Views] int not null default 0,
	[Description] nvarchar(MAX) not null,
	UploadDate datetime not null,
	Active bit not null default 1
)

create table [User](
	Id varchar(20) primary key,
	[Password] varchar(50) not null,
	Fullname nvarchar(50) not null,
	Email varchar(50) unique not null,
	[Admin] bit not null default 0,
	Active bit not null default 1
)

create table Share(
	Id int identity(1,1) primary key,
	UserID varchar(20) foreign key references [User](Id) on delete cascade on update cascade,
	VideoID varchar(20) foreign key references [Video](Id) on delete cascade on update cascade,
	Email varchar(50) not null,
	ShareDate datetime not null default getdate()
)

create table History(
	Id int identity(1,1) primary key,
	UserId varchar(20) foreign key references [User](Id) on delete cascade on update cascade,
	VideoId varchar(20) foreign key references Video(Id) on delete cascade on update cascade,
	ViewedDate datetime not null default getdate(),
	IsLiked bit not null default 0,
	LikeDate datetime null
)

-- Nạp lại dữ liệu

insert into Video (Id, Title, Poster, [Views], [Description], UploadDate, Active) values 
(N'IryGw25Kgi0', N'Biết Bố Mày Là Ai Không', N'https://img.youtube.com/vi/IryGw25Kgi0/maxresdefault.jpg', 100, N'Cùng đi tìm nguồn gốc của câu nói huyền thoại: "BIẾT BỐ MÀY LÀ AI KHÔNG" | Hài Xuân Bắc, Tự Long.', getdate(), 1),
(N'Hi9eQnS7snc', N'Quan Trường - Trường Quan', N'https://img.youtube.com/vi/Hi9eQnS7snc/maxresdefault.jpg', 300, N'Phim hài dân gian | Hài Xuân Bắc, Tự Long, Trung Hiếu.', getdate(), 1),
(N'Zc1XepIV4-U', N'Cu Thóc Đi Tán Gái', N'https://img.youtube.com/vi/Zc1XepIV4-U/maxresdefault.jpg', 200, N'Phim Hài Hay Nhất | Cu Thóc, Cường Cá.', getdate(), 1),
(N'IChYNrudCWk', N'Phát Lộc Đầu Năm', N'https://img.youtube.com/vi/IChYNrudCWk/maxresdefault.jpg', 200, N'Hài Kịch Hoài Linh, Chí Tài | PBN 110.', getdate(), 1),
(N'AEjv3tFncGQ', N'Giải Hạn', N'https://img.youtube.com/vi/AEjv3tFncGQ/maxresdefault.jpg', 200, N'Phim Hài Trấn Thành - Anh Đức', getdate(), 1)
go

insert into [User] (Id, [Password], Fullname, Email, [Admin], Active) values 
(N'MinhNH', N'123456', N'Nguyễn Hoài Minh', N'hoaiminh4321@gmail.com', 1, 1),
(N'MaiNT', N'123456', N'Nguyễn Thị Mai', N'maint@gmail.com', 0, 1),
(N'TeoNV', N'123456', N'Nguyễn Văn Tèo', N'teonv@gmail.com', 0, 1),
(N'VanNTT', N'123456', N'Ngô Thị Thanh Vân', N'vanntt@gmail.com', 1, 1),
(N'KhoaDHD', N'123456', N'Đặng Hữu Đăng Khoa', N'khoadhd@gmail.com', 1, 1)
go

insert into History (UserId, VideoId,  ViewedDate, IsLiked, LikeDate) values 
(N'TeoNV', N'AEjv3tFncGQ', getdate(), 1, getdate()),
(N'TeoNV', N'Hi9eQnS7snc', getdate(), 1, getdate()),
(N'VanNTT', N'Hi9eQnS7snc', getdate(), 0, null),
(N'MaiNT', N'Zc1XepIV4-U', getdate(), 0, null)
go

insert into Share (UserID, VideoID, Email, ShareDate) values 
(N'MaiNT', N'IryGw25Kgi0', N'teonv@gmail.com', getdate())
go
