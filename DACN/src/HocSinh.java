/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class HocSinh {
    private String maSinhVien;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String tenLop;
    private double diemTB;
    private String ngayNhapHoc;
    private String trangThai;
    private String soCMND;

    public HocSinh(String maSinhVien, String hoTen, String ngaySinh, String gioiTinh, 
                   String diaChi, String tenLop, double diemTB, String ngayNhapHoc, 
                   String trangThai, String soCMND) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.tenLop = tenLop;
        this.diemTB = diemTB;
        this.ngayNhapHoc = ngayNhapHoc;
        this.trangThai = trangThai;
        this.soCMND = soCMND;
    }

    public String getMaSinhVien() { return maSinhVien; }
    public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public double getDiemTB() { return diemTB; }
    public void setDiemTB(double diemTB) { this.diemTB = diemTB; }
    public String getNgayNhapHoc() { return ngayNhapHoc; }
    public void setNgayNhapHoc(String ngayNhapHoc) { this.ngayNhapHoc = ngayNhapHoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getSoCMND() { return soCMND; }
    public void setSoCMND(String soCMND) { this.soCMND = soCMND; }

    @Override
    public String toString() {
        return maSinhVien + "," + hoTen + "," + ngaySinh + "," + gioiTinh + "," + 
               diaChi + "," + tenLop + "," + diemTB + "," + ngayNhapHoc + "," + 
               trangThai + "," + soCMND;
    }

    public int tinhSoNamHoc() {
        try {
            java.time.LocalDate now = java.time.LocalDate.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate nhapHocDate = java.time.LocalDate.parse(ngayNhapHoc, formatter);
            return java.time.Period.between(nhapHocDate, now).getYears();
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean kiemTraDatHocBong() {
        return diemTB >= 8.0;
    }

    public String getXepLoai() {
        if (diemTB >= 9.0) return "Xuất sắc";
        if (diemTB >= 8.0) return "Giỏi";
        if (diemTB >= 6.5) return "Khá";
        if (diemTB >= 5.0) return "Trung bình";
        return "Yếu";
    }
}
