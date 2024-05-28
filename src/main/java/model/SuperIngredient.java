package model;

import java.time.LocalDate;
import java.util.Date;

public class SuperIngredient {
    private int id;
    private String ten;
    private String donVi;
    private int soLuongTrongKho;
    private int gia;
    private Date ngayNhapKho;

    public SuperIngredient() {
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

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getSoLuongTrongKho() {
        return soLuongTrongKho;
    }

    public void setSoLuongTrongKho(int soLuongTrongKho) {
        this.soLuongTrongKho = soLuongTrongKho;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public SuperIngredient(int id, String ten, String donVi, int soLuongTrongKho, int gia, Date ngayNhapKho) {
        this.id = id;
        this.ten = ten;
        this.donVi = donVi;
        this.soLuongTrongKho = soLuongTrongKho;
        this.gia = gia;
        this.ngayNhapKho = ngayNhapKho;
    }

    public Date getNgayNhapKho() {
        return ngayNhapKho;
    }

    public void setNgayNhapKho(Date ngayNhapKho) {
        this.ngayNhapKho = ngayNhapKho;
    }
}
