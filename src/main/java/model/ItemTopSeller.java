package model;

public class ItemTopSeller {
    private String tenMon;
    private int gia;
    private byte[] hinhAnh;
    private int tongSl;

    public ItemTopSeller(String tenMon, int gia) {
        this.tenMon = tenMon;
        this.gia = gia;

    }
    public void setHinhAnh(byte[] hinhAnh){
        this.hinhAnh=hinhAnh;
    }

    public void setTongSl(int tongSl){
        this.tongSl=tongSl;
    }
    public int getTongSl(){
        return tongSl;
    }
    public String getTenMon() {
        return tenMon;
    }

    public int getGia() {
        return gia;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    // You can add additional methods or functionality as needed
}