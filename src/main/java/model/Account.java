package model;

public class Account {
    private String name;
    private String password;
    private String pos;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getIdManagement() {
        return idManagement;
    }

    public void setIdManagement(int idManagement) {
        this.idManagement = idManagement;
    }

    private int idEmployee;
    private int idManagement;

    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getps(){
        return pos;
    }
    public Account(String ten,String mk,String pos, int idNV, int idQL){
        name=ten;
        password=mk;
        this.pos=pos;
        this.idEmployee = idNV;
        this.idManagement = idQL;
    }
}
