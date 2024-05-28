package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class IngredientReceivedNote {
    private int ingredientId;

    private String ingredientName;

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    private int quantity;
    private int supplierId;

    private String supplierName;
    private int managerId;
    private int kitchenId;
    private LocalDate receivedDate;

    public IngredientReceivedNote(int ingredientId, String ingredientName, int quantity, int supplierId, String supplierName, int managerId, int kitchenId, LocalDate receivedDate) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.supplierId = supplierId;
        this.managerId = managerId;
        this.kitchenId = kitchenId;
        this.ingredientName = ingredientName;
        this.supplierName = supplierName;
        this.receivedDate = LocalDate.now();
    }

    public IngredientReceivedNote() {

    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(int kitchenId) {
        this.kitchenId = kitchenId;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }
}

