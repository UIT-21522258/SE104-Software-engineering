package model;

public class OrderDetail {
    public MenuItem getOrder() {
        return order;
    }

    public void setOrder(MenuItem order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private MenuItem order;
    private int count;

    public OrderDetail(MenuItem order, int count) {
        this.order = order;
        this.count = count;
    }
}
