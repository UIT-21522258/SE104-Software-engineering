package com.fast_food.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.SuperIngredient;
import model.SuperSuppiler;
import model.Voucher;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
//import java.sql.Date;
import java.util.stream.Collectors;


public class VoucherManagementController implements Initializable {

    @FXML
    private HBox button_themnguyenlieu;

    @FXML
    private Label label_ngaynhapkho;

    @FXML
    private Label label_sltrongkho;

    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private TableView<Voucher> table_voucher;

    @FXML
    private TableColumn<?, ?> tablecolumn_magiamgia;

    @FXML
    private TableColumn<?, ?> tablecolumn_mota;

    @FXML
    private TableColumn<Voucher, String> tablecolumn_ngaybatdau;

    @FXML
    private TableColumn<Voucher, String> tablecolumn_ngayketthuc;

    @FXML
    private TableColumn<?, ?> tablecolumn_phantramgiamgia;

    @FXML
    private Text texterror_validator_magiamgia;

    @FXML
    private Text texterror_validator_mota;

    @FXML
    private Text texterror_validator_ngaybatdau;

    @FXML
    private Text texterror_validator_ngayketthuc;

    @FXML
    private Text texterror_validator_phantramgiamgia;

    @FXML
    private TextField textfield_magiamgia;

    @FXML
    private TextField textfield_mota;

    @FXML
    private DatePicker textfield_ngaybd;

    @FXML
    private DatePicker textfield_ngaykt;

    @FXML
    private TextField textfield_phantramgiamgia;


    UtilityFunctions uf = new UtilityFunctions();

    Voucher currentVoucherClick = new Voucher();

    DBHandler db = new DBHandler();

    List<Voucher> voucherList = new ArrayList<>();

    public String mode = "ADD_VC";

    public void clear() {
        textfield_ngaybd.setValue(null);
        textfield_ngaykt.setValue(null);
        textfield_phantramgiamgia.clear();
        textfield_magiamgia.clear();
        textfield_mota.clear();
    }

    public boolean check() {
        boolean hasError = false;

        hasError |= uf.isEmptyString(textfield_magiamgia.getText()) ? uf.setErrorMsg(texterror_validator_magiamgia, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_magiamgia);

        hasError |= uf.isEmptyString(textfield_phantramgiamgia.getText()) ? uf.setErrorMsg(texterror_validator_phantramgiamgia, "Vui lòng điền thông tin") :
                ((Integer.parseInt(textfield_phantramgiamgia.getText()) > 100 || (Integer.parseInt(textfield_phantramgiamgia.getText()) < 0) ? uf.setErrorMsg(texterror_validator_phantramgiamgia, "Sai định dạng ") : uf.hideErrorMsg(texterror_validator_phantramgiamgia)));


        hasError |= uf.isEmptyString(textfield_mota.getText()) ? uf.setErrorMsg(texterror_validator_mota, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_mota);
        hasError |= textfield_ngaybd.getValue() == null ? uf.setErrorMsg(texterror_validator_ngaybatdau, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_ngaybatdau);
        hasError |= textfield_ngaykt.getValue() == null ? uf.setErrorMsg(texterror_validator_ngayketthuc, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_ngayketthuc);


        return hasError;
    }


    public boolean check_is_exit(String tennl) {
        return voucherList.stream().filter(material1 -> material1.getMoTa().equals(tennl)).collect(Collectors.toList()).size() > 0 ? uf.setErrorMsg(texterror_validator_mota, "Voucher đã tồn tại") : uf.hideErrorMsg(texterror_validator_mota);
    }

    void renderTableVoucher() {
        table_voucher.getItems().clear();
        ObservableList<Voucher> vouchers = FXCollections.observableArrayList();

        vouchers.addAll(voucherList);

        tablecolumn_magiamgia.setCellValueFactory(new PropertyValueFactory<>("maGiamGia"));
        tablecolumn_mota.setCellValueFactory(new PropertyValueFactory<>("moTa"));

        tablecolumn_ngaybatdau.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Voucher, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Voucher, String> p) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date x = p.getValue().getNgayBatDau();
                String formattedDate = (x != null) ? dateFormat.format(x) : "";
                return new SimpleStringProperty(formattedDate);
            }
        });
        tablecolumn_ngayketthuc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Voucher, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Voucher, String> p) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date x = p.getValue().getNgayKetThuc();
                String formattedDate = (x != null) ? dateFormat.format(x) : "";
                return new SimpleStringProperty(formattedDate);
            }
        });

        tablecolumn_phantramgiamgia.setCellValueFactory(new PropertyValueFactory<>("phanTramGiamGia"));

        table_voucher.setItems(vouchers);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uf.setVisibleNode(texterror_validator_ngayketthuc, false);
        uf.setVisibleNode(texterror_validator_ngaybatdau, false);
        uf.setVisibleNode(texterror_validator_mota, false);
        uf.setVisibleNode(texterror_validator_magiamgia, false);
        uf.setVisibleNode(texterror_validator_phantramgiamgia, false);
        try {
            this.voucherList = db.getAllVoucher();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        renderTableVoucher();

        table_voucher.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra double click

                Voucher selectedItem = table_voucher.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    this.mode = "EDIT_VC";
                    label_them_chinh_sua_nl.setText("Chỉnh sửa hoàn tất");


                    this.currentVoucherClick = selectedItem;
                    int id = selectedItem.getId();
                    String mota = selectedItem.getMoTa();
                    String maGiamGia = selectedItem.getMaGiamGia();
                    String phanTramGiamGia = String.valueOf(selectedItem.getPhanTramGiamGia());
                    Date ngayBatDau = selectedItem.getNgayBatDau();
                    Date ngayKetThuc = selectedItem.getNgayKetThuc();


                    textfield_mota.setText(mota);
                    textfield_phantramgiamgia.setText(phanTramGiamGia);
                    textfield_magiamgia.setText(maGiamGia);

                    if (ngayBatDau != null) {
                        LocalDate x = new java.util.Date(ngayBatDau.getTime())
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        textfield_ngaybd.setValue(x);
                    } else {
                        textfield_ngaybd.setValue(null);
                    }

                    if (ngayKetThuc != null) {
                        LocalDate x = new java.util.Date(ngayKetThuc.getTime())
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        // Ngày nhập kho khác null, tiếp tục xử lý
                        textfield_ngaykt.setValue(x);
                    } else {
                        textfield_ngaykt.setValue(null);
                    }

                }
            }

        });
        table_voucher.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                Voucher selectedRow = table_voucher.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this row?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        int id = 0;
                        table_voucher.getItems().remove(selectedRow);
                        Iterator<Voucher> iterator = voucherList.iterator();
                        while (iterator.hasNext()) {
                            Voucher x = iterator.next();
                            if (x.getId() == selectedRow.getId()) {
                                iterator.remove();
                                id=x.getId();
                            }
                        }
                        try {
                            db.xoaKM(id);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }
            }
        });
        button_themnguyenlieu.setOnMouseClicked(e -> {
            if (this.mode.equals("ADD_VC")) {
                if (!check()) {
                    int id = voucherList.size() + 1;
                    String mota = textfield_mota.getText();
                    String phanTramGiamGia = textfield_phantramgiamgia.getText();
                    String maGiamGia = textfield_magiamgia.getText();
                    Date ngayBD = Date.from(textfield_ngaybd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date ngayKT = Date.from(textfield_ngaykt.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Voucher voucher = new Voucher(id, mota, ngayBD, ngayKT, maGiamGia, Integer.parseInt(phanTramGiamGia));

                    if (check_is_exit(mota)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("error");
                        alert.setHeaderText(null);
                        alert.setContentText("Voucher already exist");
                    }
                    else {
                        voucherList.add(voucher);
                        try {
                            db.InsVC(id, mota, phanTramGiamGia, maGiamGia, ngayBD, ngayKT);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        clear();
                        renderTableVoucher();
                    }
                }
            } else if (this.mode.equals("EDIT_VC")) {
                if (!check()) {
                    int id = currentVoucherClick.getId();
                    String mota = textfield_mota.getText();
                    String phanTramGiamGia = textfield_phantramgiamgia.getText();
                    String maGiamGia = textfield_magiamgia.getText();
                    Date ngayBD = Date.from(textfield_ngaybd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Date ngayKT = Date.from(textfield_ngaykt.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    System.out.println(ngayBD);
                    System.out.println(ngayKT);
                    voucherList.forEach(vc -> {
                        if (id == vc.getId()) {
                            vc.setMoTa(mota);
                            vc.setPhanTramGiamGia(Integer.parseInt(phanTramGiamGia));
                            vc.setMaGiamGia(maGiamGia);
                            vc.setNgayKetThuc(ngayKT);
                            vc.setNgayBatDau(ngayBD);
                            try {
                                db.EditOfVC(id,mota,phanTramGiamGia,maGiamGia,ngayBD,ngayKT);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            };
                        }
                    });
                    //////////////////
                    /////////////////

                    this.mode = "ADD_VC";
                    clear();
                    label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                    renderTableVoucher();
                }
            }
        });
    }
}