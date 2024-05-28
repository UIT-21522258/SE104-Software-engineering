package model;

import java.util.Date;

public class Order1 {
    public Order1() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_kh() {
        return id_kh;
    }

    public void setId_kh(int id_kh) {
        this.id_kh = id_kh;
    }





    public int getId_tn() {
        return id_tn;
    }

    public void setId_tn(int id_tn) {
        this.id_tn = id_tn;
    }






    public int getSo_ban_dat() {
        return so_ban_dat;
    }

    public void setSo_ban_dat(int so_ban_dat) {
        this.so_ban_dat = so_ban_dat;
    }

    public int getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(int tong_tien) {
        this.tong_tien = tong_tien;
    }

    public String getHinh_thuc_thanh_toan() {
        return hinh_thuc_thanh_toan;
    }

    public void setHinh_thuc_thanh_toan(String hinh_thuc_thanh_toan) {
        this.hinh_thuc_thanh_toan = hinh_thuc_thanh_toan;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }

    public int getDat_online() {
        return dat_online;
    }

    public void setDat_online(int dat_online) {
        this.dat_online = dat_online;
    }

    public Date getNgay_dat() {
        return ngay_dat;
    }

    public void setNgay_dat(Date ngay_dat) {
        this.ngay_dat = ngay_dat;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    private int id;
    private int id_kh;
    private int id_km;
    private int id_tn;
    private int so_ban_dat;
    private int tong_tien;
    private String hinh_thuc_thanh_toan;
    private String trang_thai;
    private int dat_online;
    private Date ngay_dat;
    private String ghi_chu;

    public Order1(int id, int id_kh, int id_km, int id_tn, int so_ban_dat, int tong_tien, String hinh_thuc_thanh_toan, String trang_thai, int dat_online, Date ngay_dat, String ghi_chu) {
        this.id = id;
        this.id_kh = id_kh;
        this.id_km = id_km;
        this.id_tn = id_tn;
        this.so_ban_dat = so_ban_dat;
        this.tong_tien = tong_tien;
        this.hinh_thuc_thanh_toan = hinh_thuc_thanh_toan;
        this.trang_thai = trang_thai;
        this.dat_online = dat_online;
        this.ngay_dat = ngay_dat;
        this.ghi_chu = ghi_chu;
    }
}
