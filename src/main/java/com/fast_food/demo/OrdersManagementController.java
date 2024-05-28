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


public class OrdersManagementController implements Initializable {
    @FXML
    private ChoiceBox<String> cb_trang_thai;

    @FXML
    private ChoiceBox<String> hinh_thuc_thanh_toan;

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
    private TableColumn<Order, String> thoi_gian;

    @FXML
    private TableColumn<?, ?> tong_tien;

    @FXML
    private HBox button_submit;


    @FXML
    private Text text_error_httt;

    @FXML
    private Text text_error_tt;
    Order currentOrderClick = new Order();

    UtilityFunctions uf = new UtilityFunctions();



    private boolean check_is_error() {
        boolean is_ht_tt_error = hinh_thuc_thanh_toan.getValue() == null;
        boolean is_tt_error = cb_trang_thai.getValue() == null;

        uf.setVisibleNode(text_error_httt, is_ht_tt_error);
        uf.setVisibleNode(text_error_tt, is_tt_error);

        return is_ht_tt_error || is_tt_error;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hinh_thuc_thanh_toan.getItems().addAll("Tiền mặt", "Chuyển khoản");
        cb_trang_thai.getItems().addAll("Chưa xác nhận", "Đã Thanh Toán", "Đã Hoàn Thành", "Bị Hủy");
        button_submit.setDisable(true);
        uf.setVisibleNode(text_error_httt, false);
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
        tong_tien.setCellValueFactory(new PropertyValueFactory<>("tong_tien"));
        order_list.setItems(orderList);

        //

        button_submit.setOnMouseClicked(e -> {

            if (!check_is_error()) {
                System.out.println("submit");
                try {
                    db.updateOrders(currentOrderClick.getId(),hinh_thuc_thanh_toan.getValue(),cb_trang_thai.getValue());
                    orderList.clear();
                    HashSet<Order> result = db.getAllOrders();
                    for (Order or : result) {
                        orderList.add(or);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        //


        order_list.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                button_submit.setDisable(false);
                table_chi_tiet_hoa_don.getItems().clear();
                Order order = order_list.getSelectionModel().getSelectedItem();
                this.currentOrderClick = order;
                if (order != null) {
                    try {
                        HashSet<FastFood> food = db.getFastFoodByIdOrder(order.getId());
                        for (FastFood x: food) {
                            foodInOrder.add(x);
                        }

                        tb_mon_an.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
                        tb_so_luong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
                        cb_trang_thai.setValue("Chưa xác nhận");
                        hinh_thuc_thanh_toan.setValue(order.getHinh_thuc_thanh_toan());
                        table_chi_tiet_hoa_don.setItems(foodInOrder);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }


        });

    }


}
