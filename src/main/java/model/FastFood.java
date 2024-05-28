package model;
public class FastFood {
    private int id;
    private String tenMon;
    private String moTa;
    private String loai;
    private byte[] hinhAnh;
    private int soLuong;

    public FastFood(int id, String tenMon, String moTa, String loai, byte[] hinhAnh, int soLuong) {
        this.id = id;
        this.tenMon = tenMon;
        this.moTa = moTa;
        this.loai = loai;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
    }

    public int getId() {
        return id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getLoai() {
        return loai;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
