package model;

public class Material {
    private String name;
    private String unit;
    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Material(String name, String unit, int quantity) {
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    // Getters and setters


}