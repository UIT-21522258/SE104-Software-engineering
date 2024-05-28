package model;

public class Employee {
    private int id;
    private String sdt;

    public int getId_quan_ly() {
        return id_quan_ly;
    }

    public Employee(int id, String sdt, int id_quan_ly, String chuc_vu, String ten, int luong, String nguoi_quan_ly) {
        this.id = id;
        this.sdt = sdt;
        this.id_quan_ly = id_quan_ly;
        this.chuc_vu = chuc_vu;
        this.ten = ten;
        this.luong = luong;
        this.nguoi_quan_ly = nguoi_quan_ly;
    }

    public void setId_quan_ly(int id_quan_ly) {
        this.id_quan_ly = id_quan_ly;
    }

    private int id_quan_ly;

    private String chuc_vu;
    private String ten;


    private int luong;
    private String nguoi_quan_ly;

    public Employee() {
    }

    public int getLuong() {
        return luong;
    }

    public void setLuong(int luong) {
        this.luong = luong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setNguoi_quan_ly(String nguoi_quan_ly) {
        this.nguoi_quan_ly = nguoi_quan_ly;
    }

    public String getChuc_vu() {
        return chuc_vu;
    }

    public void setChuc_vu(String chuc_vu) {
        this.chuc_vu = chuc_vu;
    }

    public String getTen() {
        return ten;
    }

    public String getNguoi_quan_ly() {
        return nguoi_quan_ly;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
