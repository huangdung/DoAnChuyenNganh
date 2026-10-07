
public class LopHoc {
    private String maLop;
    private String tenLop;
    private String gvcn;
    private String phongHoc;

    public LopHoc() {
    }

    public LopHoc(String maLop, String tenLop, String gvcn, String phongHoc) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.gvcn = gvcn;
        this.phongHoc = phongHoc;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getGvcn() {
        return gvcn;
    }

    public void setGvcn(String gvcn) {
        this.gvcn = gvcn;
    }

    public String getPhongHoc() {
        return phongHoc;
    }

    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }

    @Override
    public String toString() {
        return maLop + " - " + tenLop;
    }
}
