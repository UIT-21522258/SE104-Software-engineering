package model;

public class Supplier {

        private int id;
        private String ten;
        private String soDienThoai;
        private String email;

    public Supplier(int id, String ten, String soDienThoai, String email, String diaChi) {
        this.id = id;
        this.ten = ten;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public Supplier() {
        this.id = 0;
        this.ten = "";
        this.soDienThoai = "";
        this.email = "";
        this.diaChi = "";
    }

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

    private String diaChi;
}
