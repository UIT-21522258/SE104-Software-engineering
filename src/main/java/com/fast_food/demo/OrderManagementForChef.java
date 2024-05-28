package com.fast_food.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.FastFood;
import model.Order;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class OrderManagementForChef implements Initializable {

    public boolean check_is_error() {

        boolean hasError = false;

        hasError |= cb_trang_thai.getValue() == null ? uf.setErrorMsg(text_error_tt, "Vui lòng điền thông tin") : uf.hideErrorMsg(text_error_tt);

        return hasError;
    }

    UtilityFunctions uf = new UtilityFunctions();
    DBHandler db = new DBHandler();

    @FXML
    private HBox button_submit;

    @FXML
    private ChoiceBox<String> cb_trang_thai;

    @FXML
    private TableView<Order> order_list;

    @FXML
    private TableColumn<?, ?> so_ban_dat;

    @FXML
    private TableView<FastFood> table_chi_tiet_hoa_don;

    @FXML
    private TableColumn<?, ?> tb_mon_an;

    @FXML
    private TableColumn<?, ?> tb_so_luong;

    @FXML
    private TableColumn<?, ?> ten_khach_hang;

    @FXML
    private Text text_error_tt;

    @FXML
    private TableColumn<Order, String> thoi_gian;

    @FXML
    private TableColumn<?, ?> ghi_chu;

    Order currentOrderClick = new Order();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        cb_trang_thai.getItems().addAll("Chưa xác nhận", "Đã Thanh Toán", "Đã Hoàn Thành", "Bị Hủy");
        button_submit.setDisable(true);
        uf.setVisibleNode(text_error_tt, false);

        ObservableList<Order> orderList = FXCollections.observableArrayList();
        ObservableList<FastFood> foodInOrder = FXCollections.observableArrayList();

        DBHandler db = new DBHandler();

        // init table order

        try {
            HashSet<Order> result = db.getAllOrders();
            for (Order or : result) {
                orderList.add(or);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ten_khach_hang.setCellValueFactory(new PropertyValueFactory<>("ten_kh"));
        so_ban_dat.setCellValueFactory(new PropertyValueFactory<>("so_ban_dat"));
        thoi_gian.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return new SimpleStringProperty(dateFormat.format(p.getValue().getNgay_dat()));
            }
        });

        ghi_chu.setCellValueFactory(new PropertyValueFactory<>("ghi_chu"));
        order_list.setItems(orderList);
        order_list.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                button_submit.setDisable(false);
                table_chi_tiet_hoa_don.getItems().clear();
                Order order = order_list.getSelectionModel().getSelectedItem();
                this.currentOrderClick = order;
                cb_trang_thai.setValue(order.getTrang_thai());
                if (order != null) {
                    try {
                        HashSet<FastFood> food = db.getFastFoodByIdOrder(order.getId());
                        for (FastFood x : food) {
                            foodInOrder.add(x);
                        }
                        tb_mon_an.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
                        tb_so_luong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
                        table_chi_tiet_hoa_don.setItems(foodInOrder);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
            button_submit.setOnMouseClicked(ex->{
                try {
                    db.updateChefOrder(currentOrderClick.getId(),cb_trang_thai.getValue());
                    orderList.clear();
                    HashSet<Order> result = db.getAllOrders();
                    for (Order or : result) {
                        orderList.add(or);
                    }
                } catch (SQLException ex1) {
                    throw new RuntimeException(ex1);
                }
            });
        });

    }
}