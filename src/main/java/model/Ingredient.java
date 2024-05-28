package model;

public class Ingredient {
    private int id;
    private String ten;
    private String donVi;
    private int soLuongTrongKho;
    private int giaNL;

    public Ingredient(int id, String ten, String donVi, int soLuongTrongKho, int giaNL) {
        this.id = id;
        this.ten = ten;
        this.donVi = donVi;
        this.soLuongTrongKho = soLuongTrongKho;
        this.giaNL = giaNL;
    }

    public Ingredient() {
        this.id = 0;
        this.ten = "";
        this.donVi = "";
        this.soLuongTrongKho = 0;
        this.giaNL = 0;
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

    public int getGiaNL() {
        return giaNL;
    }

    public void setGiaNL(int giaNL) {
        this.giaNL = giaNL;
    }
}
