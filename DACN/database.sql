-- ================================================================
-- HỆ THỐNG QUẢN LÝ HỌC SINH & HỒ SƠ HỌC VỤ
-- DATABASE: QuanLyHocSinhDB (SQL Server)
-- ================================================================

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyHocSinhDB')
BEGIN
    CREATE DATABASE QuanLyHocSinhDB;
END
GO

USE QuanLyHocSinhDB;
GO

-- 1. BẢNG TÀI KHOẢN (ĐĂNG NHẬP & PHÂN QUYỀN)
IF OBJECT_ID('TaiKhoan', 'U') IS NOT NULL DROP TABLE TaiKhoan;
GO

CREATE TABLE TaiKhoan (
    TenDangNhap VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(50) NOT NULL,
    VaiTro NVARCHAR(50) NOT NULL, -- 'Admin', 'Giáo viên'
    HoTen NVARCHAR(100) NOT NULL
);
GO

-- 2. BẢNG LỚP HỌC (QUẢN LÝ DANH MỤC LỚP & GVCN)
IF OBJECT_ID('HocSinh', 'U') IS NOT NULL DROP TABLE HocSinh;
IF OBJECT_ID('LopHoc', 'U') IS NOT NULL DROP TABLE LopHoc;
GO

CREATE TABLE LopHoc (
    MaLop VARCHAR(50) PRIMARY KEY,
    TenLop NVARCHAR(50) NOT NULL,
    GVCN NVARCHAR(100) NOT NULL,
    PhongHoc NVARCHAR(50)
);
GO

-- 3. BẢNG HỌC SINH (HỒ SƠ HỌC VỤ & KHÓA NGOẠI THAM CHIẾU LỚP HỌC)
CREATE TABLE HocSinh (
    MaSinhVien   VARCHAR(50) PRIMARY KEY,
    HoTen        NVARCHAR(100) NOT NULL,
    NgaySinh     VARCHAR(20) NOT NULL,
    GioiTinh     NVARCHAR(10) NOT NULL,
    DiaChi       NVARCHAR(255) NOT NULL,
    TenLop       VARCHAR(50) NOT NULL FOREIGN KEY REFERENCES LopHoc(MaLop),
    DiemTB       FLOAT NOT NULL CHECK (DiemTB >= 0 AND DiemTB <= 10),
    NgayNhapHoc  VARCHAR(20) NOT NULL,
    TrangThai    NVARCHAR(50) NOT NULL,
    SoCMND       VARCHAR(20)
);
GO

-- ================================================================
-- DỮ LIỆU MẪU ĐỂ SINH VIÊN BẢO VỆ ĐỒ ÁN
-- ================================================================

-- Chèn tài khoản mẫu
INSERT INTO TaiKhoan (TenDangNhap, MatKhau, VaiTro, HoTen) VALUES
('admin', '123', N'Admin', N'Quản Trị Viên Hệ Thống'),
('giaovien', '123', N'Giáo viên', N'Giáo Viên Chủ Nhiệm');

-- Chèn lớp học mẫu
INSERT INTO LopHoc (MaLop, TenLop, GVCN, PhongHoc) VALUES
('12A1', N'Lớp 12A1 (Chuyên Toán)', N'Thầy Nguyễn Văn Bình', N'Phòng 301'),
('12A2', N'Lớp 12A2 (Chuyên Lý)', N'Cô Trần Thu Hà', N'Phòng 302'),
('11B1', N'Lớp 11B1 (Cơ bản)', N'Thầy Lê Hoàng Nam', N'Phòng 201'),
('11B2', N'Lớp 11B2 (Ngoại ngữ)', N'Cô Phạm Mai Anh', N'Phòng 202');

-- Chèn học sinh mẫu
INSERT INTO HocSinh (MaSinhVien, HoTen, NgaySinh, GioiTinh, DiaChi, TenLop, DiemTB, NgayNhapHoc, TrangThai, SoCMND) VALUES
('HS001', N'Nguyễn Văn An', '15/08/2005', N'Nam', N'Hà Nội', '12A1', 9.2, '05/09/2023', N'Đang học', '001205001122'),
('HS002', N'Trần Thị Bích', '20/11/2005', N'Nữ', N'Hải Phòng', '12A1', 8.5, '05/09/2023', N'Đang học', '001205003344'),
('HS003', N'Lê Hoàng Cường', '10/03/2005', N'Nam', N'Đà Nẵng', '12A2', 7.8, '05/09/2023', N'Đang học', '001205005566'),
('HS004', N'Phạm Minh Dũng', '02/07/2005', N'Nam', N'Cần Thơ', '12A2', 6.2, '05/09/2023', N'Tạm nghỉ', '001205007788'),
('HS005', N'Hoàng Thu Dung', '14/09/2005', N'Nữ', N'Quảng Ninh', '11B1', 8.8, '05/09/2024', N'Đang học', '001205009900'),
('HS006', N'Vũ Quốc Đạt', '18/01/2005', N'Nam', N'Nam Định', '11B1', 4.5, '05/09/2024', N'Đang học', '001205002233'),
('HS007', N'Đặng Thanh Hà', '25/04/2005', N'Nữ', N'Bắc Ninh', '11B2', 9.0, '05/09/2024', N'Đang học', '001205004455'),
('HS008', N'Bùi Tiến Long', '30/12/2005', N'Nam', N'Hà Nội', '11B2', 7.0, '05/09/2024', N'Bảo lưu', '001205006677');
GO
