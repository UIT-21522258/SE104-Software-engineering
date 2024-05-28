package model;

public class SuperSuppiler {
    private int id;
    private String ten;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private int nguyenLieuId;
    private String nguyenLieuTen;

    // Constructors
    public SuperSuppiler() {
    }

    public SuperSuppiler(int id, String ten, String soDienThoai, String email, String diaChi,
                                int nguyenLieuId, String nguyenLieuTen) {
        this.id = id;
        this.ten = ten;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.nguyenLieuId = nguyenLieuId;
        this.nguyenLieuTen = nguyenLieuTen;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getNguyenLieuId() {
        return nguyenLieuId;
    }

    public void setNguyenLieuId(int nguyenLieuId) {
        this.nguyenLieuId = nguyenLieuId;
    }

    public String getNguyenLieuTen() {
        return nguyenLieuTen;
    }

    public void setNguyenLieuTen(String nguyenLieuTen) {
        this.nguyenLieuTen = nguyenLieuTen;
    }
}

