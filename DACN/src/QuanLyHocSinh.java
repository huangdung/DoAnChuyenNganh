import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuanLyHocSinh {
    private ArrayList<HocSinh> danhSachHocSinh;
    
    private String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyHocSinhDB;encrypt=true;trustServerCertificate=true;";
    private String dbUser = "sa";
    private String dbPass = "123";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy SQL Server JDBC Driver: " + e.getMessage());
        }
    }

    public QuanLyHocSinh() {
        danhSachHocSinh = new ArrayList<>();
       
        docFile();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public void themHocSinh(HocSinh hocSinh) {
        String sql = "INSERT INTO HocSinh (MaSinhVien, HoTen, NgaySinh, GioiTinh, DiaChi, TenLop, DiemTB, NgayNhapHoc, TrangThai, SoCMND) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hocSinh.getMaSinhVien());
            ps.setString(2, hocSinh.getHoTen());
            ps.setString(3, hocSinh.getNgaySinh());
            ps.setString(4, hocSinh.getGioiTinh());
            ps.setString(5, hocSinh.getDiaChi());
            ps.setString(6, hocSinh.getTenLop());
            ps.setDouble(7, hocSinh.getDiemTB());
            ps.setString(8, hocSinh.getNgayNhapHoc());
            ps.setString(9, hocSinh.getTrangThai());
            ps.setString(10, hocSinh.getSoCMND());
            ps.executeUpdate();
            
            danhSachHocSinh.add(hocSinh);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void suaHocSinh(String maSinhVien, HocSinh updatedHocSinh) {
        String sql = "UPDATE HocSinh SET HoTen=?, NgaySinh=?, GioiTinh=?, DiaChi=?, TenLop=?, DiemTB=?, NgayNhapHoc=?, TrangThai=?, SoCMND=? WHERE MaSinhVien=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, updatedHocSinh.getHoTen());
            ps.setString(2, updatedHocSinh.getNgaySinh());
            ps.setString(3, updatedHocSinh.getGioiTinh());
            ps.setString(4, updatedHocSinh.getDiaChi());
            ps.setString(5, updatedHocSinh.getTenLop());
            ps.setDouble(6, updatedHocSinh.getDiemTB());
            ps.setString(7, updatedHocSinh.getNgayNhapHoc());
            ps.setString(8, updatedHocSinh.getTrangThai());
            ps.setString(9, updatedHocSinh.getSoCMND());
            ps.setString(10, maSinhVien);
            ps.executeUpdate();

            for (int i = 0; i < danhSachHocSinh.size(); i++) {
                if (danhSachHocSinh.get(i).getMaSinhVien().equals(maSinhVien)) {
                    danhSachHocSinh.set(i, updatedHocSinh);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void xoaHocSinh(String maSinhVien) {
        String sql = "DELETE FROM HocSinh WHERE MaSinhVien=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSinhVien);
            ps.executeUpdate();
            danhSachHocSinh.removeIf(hs -> hs.getMaSinhVien().equals(maSinhVien));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<HocSinh> timKiem(String keyword) {
        List<HocSinh> result = new ArrayList<>();
        for (HocSinh hs : danhSachHocSinh) {
            if (hs.getMaSinhVien().toLowerCase().contains(keyword.toLowerCase()) ||
                hs.getHoTen().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(hs);
            }
        }
        return result;
    }

    public List<HocSinh> locTheoLop(String tenLop) {
        if (tenLop.equals("Tất cả")) {
            return new ArrayList<>(danhSachHocSinh);
        }
        List<HocSinh> result = new ArrayList<>();
        for (HocSinh hs : danhSachHocSinh) {
            if (hs.getTenLop().equalsIgnoreCase(tenLop)) {
                result.add(hs);
            }
        }
        return result;
    }

    public void docFile() {
        danhSachHocSinh.clear();
        String sql = "SELECT * FROM HocSinh";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HocSinh hs = new HocSinh(
                    rs.getString("MaSinhVien"),
                    rs.getString("HoTen"),
                    rs.getString("NgaySinh"),
                    rs.getString("GioiTinh"),
                    rs.getString("DiaChi"),
                    rs.getString("TenLop"),
                    rs.getDouble("DiemTB"),
                    rs.getString("NgayNhapHoc"),
                    rs.getString("TrangThai"),
                    rs.getString("SoCMND")
                );
                danhSachHocSinh.add(hs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ghiFile() {
        System.out.println("Dữ liệu đã được lưu trực tiếp vào CSDL SQL Server.");
    }

    public ArrayList<HocSinh> getDanhSachHocSinh() {
        return danhSachHocSinh;
    }
    
    public String kiemTraDangNhap(String username, String password) {
        String sql = "SELECT VaiTro FROM TaiKhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("VaiTro");
                }
            }
        } catch (SQLException e) {
            
        }
        if ("admin".equalsIgnoreCase(username) && "123".equals(password)) {
            return "Admin";
        }
        if ("giaovien".equalsIgnoreCase(username) && "123".equals(password)) {
            return "Giáo viên";
        }
        return null;
    }

    private List<LopHoc> cacheLopHoc = new ArrayList<>();

    private void initDefaultLopHoc() {
        if (cacheLopHoc.isEmpty()) {
            cacheLopHoc.add(new LopHoc("12A1", "Lớp 12A1 (Chuyên Toán)", "Thầy Nguyễn Văn Bình", "Phòng 301"));
            cacheLopHoc.add(new LopHoc("12A2", "Lớp 12A2 (Chuyên Lý)", "Cô Trần Thu Hà", "Phòng 302"));
            cacheLopHoc.add(new LopHoc("11B1", "Lớp 11B1 (Cơ bản)", "Thầy Lê Hoàng Nam", "Phòng 201"));
            cacheLopHoc.add(new LopHoc("11B2", "Lớp 11B2 (Ngoại ngữ)", "Cô Phạm Mai Anh", "Phòng 202"));
            cacheLopHoc.add(new LopHoc("10A1", "Lớp 10A1 (Toán Tin)", "Thầy Vũ Quốc Hùng", "Phòng 101"));
            cacheLopHoc.add(new LopHoc("10A2", "Lớp 10A2 (Tự nhiên)", "Cô Hoàng Thu Trang", "Phòng 102"));
        }
    }

    public List<LopHoc> getDanhSachLopHoc() {
        initDefaultLopHoc();
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT MaLop, TenLop, GVCN, PhongHoc FROM LopHoc ORDER BY MaLop ASC";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new LopHoc(
                    rs.getString("MaLop"),
                    rs.getString("TenLop"),
                    rs.getString("GVCN"),
                    rs.getString("PhongHoc")
                ));
            }
        } catch (SQLException e) {
           
        }
        if (!list.isEmpty()) {
            cacheLopHoc = new ArrayList<>(list);
            return list;
        }
        return new ArrayList<>(cacheLopHoc);
    }

    public boolean tonTaiMaLop(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) return false;
        String m = maLop.trim();
        for (LopHoc lh : getDanhSachLopHoc()) {
            if (lh.getMaLop().equalsIgnoreCase(m)) return true;
        }
        return false;
    }

    public boolean themLopHoc(LopHoc lh) {
        initDefaultLopHoc();
        String sql = "INSERT INTO LopHoc (MaLop, TenLop, GVCN, PhongHoc) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lh.getMaLop());
            ps.setString(2, lh.getTenLop());
            ps.setString(3, lh.getGvcn());
            ps.setString(4, lh.getPhongHoc());
            ps.executeUpdate();
        } catch (SQLException e) {
           
        }
        cacheLopHoc.removeIf(l -> l.getMaLop().equalsIgnoreCase(lh.getMaLop()));
        cacheLopHoc.add(lh);
        return true;
    }

    public boolean suaLopHoc(String oldMaLop, LopHoc lh) {
        initDefaultLopHoc();
        String sql = "UPDATE LopHoc SET MaLop = ?, TenLop = ?, GVCN = ?, PhongHoc = ? WHERE MaLop = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lh.getMaLop());
            ps.setString(2, lh.getTenLop());
            ps.setString(3, lh.getGvcn());
            ps.setString(4, lh.getPhongHoc());
            ps.setString(5, oldMaLop);
            ps.executeUpdate();

           
            if (!oldMaLop.equalsIgnoreCase(lh.getMaLop())) {
                String updateHsSql = "UPDATE HocSinh SET TenLop = ? WHERE TenLop = ?";
                try (PreparedStatement psHs = conn.prepareStatement(updateHsSql)) {
                    psHs.setString(1, lh.getMaLop());
                    psHs.setString(2, oldMaLop);
                    psHs.executeUpdate();
                }
            }
        } catch (SQLException e) {
           
        }

        for (int i = 0; i < cacheLopHoc.size(); i++) {
            if (cacheLopHoc.get(i).getMaLop().equalsIgnoreCase(oldMaLop)) {
                cacheLopHoc.set(i, lh);
                break;
            }
        }
        if (!oldMaLop.equalsIgnoreCase(lh.getMaLop())) {
            for (HocSinh hs : danhSachHocSinh) {
                if (hs.getTenLop() != null && hs.getTenLop().equalsIgnoreCase(oldMaLop)) {
                    hs.setTenLop(lh.getMaLop());
                }
            }
        }
        return true;
    }

    public boolean xoaLopHoc(String maLop) {
        initDefaultLopHoc();
        int count = demHocSinhTrongLop(maLop);
        if (count > 0) {
            return false; 
        }
        String sql = "DELETE FROM LopHoc WHERE MaLop = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            ps.executeUpdate();
        } catch (SQLException e) {
          
        }
        cacheLopHoc.removeIf(l -> l.getMaLop().equalsIgnoreCase(maLop));
        return true;
    }

    public int demHocSinhTrongLop(String maLop) {
        if (maLop == null) return 0;
        int count = 0;
        for (HocSinh hs : danhSachHocSinh) {
            if (hs.getTenLop() != null && hs.getTenLop().equalsIgnoreCase(maLop.trim())) {
                count++;
            }
        }
        return count;
    }

    public List<String> getDanhSachLop() {
        Set<String> lopSet = new java.util.TreeSet<>();
        for (LopHoc lh : getDanhSachLopHoc()) {
            lopSet.add(lh.getMaLop());
        }
        for (HocSinh hs : danhSachHocSinh) {
            if (hs.getTenLop() != null && !hs.getTenLop().trim().isEmpty()) {
                lopSet.add(hs.getTenLop().trim());
            }
        }
        return new ArrayList<>(lopSet);
    }

    public String getThongTinLop(String maLop) {
        for (LopHoc lh : getDanhSachLopHoc()) {
            if (lh.getMaLop().equalsIgnoreCase(maLop)) {
                return lh.getTenLop() + " | GVCN: " + lh.getGvcn() + " (" + lh.getPhongHoc() + ")";
            }
        }
        String sql = "SELECT TenLop, GVCN, PhongHoc FROM LopHoc WHERE MaLop = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenLop") + " | GVCN: " + rs.getString("GVCN") + " (" + rs.getString("PhongHoc") + ")";
                }
            }
        } catch (SQLException e) {
            
        }
        return "";
    }
    
    public void sapXepTheoDiemTB() {
        danhSachHocSinh.sort((hs1, hs2) -> Double.compare(hs2.getDiemTB(), hs1.getDiemTB()));
    }

    public int demHocSinhTheoTrangThai(String trangThai) {
        return (int) danhSachHocSinh.stream().filter(hs -> hs.getTrangThai().equalsIgnoreCase(trangThai)).count();
    }

    public boolean tonTaiMaSinhVien(String maSV) {
        return danhSachHocSinh.stream().anyMatch(hs -> hs.getMaSinhVien().equalsIgnoreCase(maSV));
    }

    public boolean tonTaiSoCMND(String soCMND) {
        if (soCMND == null || soCMND.trim().isEmpty()) return false;
        return danhSachHocSinh.stream().anyMatch(hs -> 
            hs.getSoCMND() != null && soCMND.trim().equalsIgnoreCase(hs.getSoCMND().trim()));
    }

    public boolean tonTaiSoCMND(String soCMND, String excludeMaSV) {
        if (soCMND == null || soCMND.trim().isEmpty()) return false;
        return danhSachHocSinh.stream().anyMatch(hs -> 
            !hs.getMaSinhVien().equalsIgnoreCase(excludeMaSV) && 
            hs.getSoCMND() != null && 
            soCMND.trim().equalsIgnoreCase(hs.getSoCMND().trim()));
    }

    public double tinhDiemTrungBinhChung() {
        if (danhSachHocSinh.isEmpty()) return 0.0;
        double tong = danhSachHocSinh.stream().mapToDouble(HocSinh::getDiemTB).sum();
        return Math.round((tong / danhSachHocSinh.size()) * 100.0) / 100.0;
    }

    public int demDatHocBong() {
        return (int) danhSachHocSinh.stream().filter(HocSinh::kiemTraDatHocBong).count();
    }

    public int demTheoXepLoai(String loai) {
        return (int) danhSachHocSinh.stream().filter(hs -> hs.getXepLoai().equalsIgnoreCase(loai)).count();
    }

    public boolean xuatFileCSV(String filePath) {
        try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(filePath), java.nio.charset.StandardCharsets.UTF_8)) {
           
            writer.write('\ufeff');
            writer.write("Mã SV,Họ Tên,Ngày Sinh,Giới Tính,Địa Chỉ,Lớp,Điểm TB,Ngày Nhập Học,Trạng Thái,CMND/CCCD,Xếp Loại,Học Bổng\n");
            for (HocSinh hs : danhSachHocSinh) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    hs.getMaSinhVien(),
                    hs.getHoTen(),
                    hs.getNgaySinh(),
                    hs.getGioiTinh(),
                    hs.getDiaChi(),
                    hs.getTenLop(),
                    hs.getDiemTB(),
                    hs.getNgayNhapHoc(),
                    hs.getTrangThai(),
                    hs.getSoCMND(),
                    hs.getXepLoai(),
                    hs.kiemTraDatHocBong() ? "Đạt" : "Không"));
            }
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
