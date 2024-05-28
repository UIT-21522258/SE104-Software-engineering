package com.fast_food.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.LogedInUser;
import model.Order;
import model.SuperIngredient;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class IngredientManagementController implements Initializable {
    @FXML
    private HBox button_hoantat;

    @FXML
    private HBox button_themnguyenlieu;

    @FXML
    private HBox button_xoanguyenlieu;

    @FXML
    private Label button_yeucaunhapnl;
    @FXML
    private TextField textfield_tennl;
    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private TableView<SuperIngredient> table_nguyen_lieu;

    @FXML
    private TableColumn<?, ?> tablecolumn_dv;

    @FXML
    private TableColumn<SuperIngredient, String> tablecolumn_ngaynhapkho;

    @FXML
    private TableColumn<?, ?> tablecolumn_sltrongkho;

    @FXML
    private TableColumn<?, ?> tablecolumn_stt;

    @FXML
    private TableColumn<?, ?> tablecolumn_tennl;

    @FXML
    private Text texterror_validator_donvi;

    @FXML
    private Text texterror_validator_gia;

    @FXML
    private Text texterror_validator_ngaynhaptkho;

    @FXML
    private Text texterror_validator_soluongtrongkho;

    @FXML
    private Text texterror_validator_tennl;

    @FXML
    private Label label_ngaynhapkho;

    @FXML
    private Label label_sltrongkho;


    @FXML
    private TextField textfield_donvi;

    @FXML
    private TextField textfield_gia;

    @FXML
    private DatePicker textfield_ngaynhapkho;


    @FXML
    private TextField textfield_soluongtrongkho;

    @FXML
    private TextField textfield_xoa_nl;

    LogedInUser user = new LogedInUser();
    UtilityFunctions uf = new UtilityFunctions();

    SuperIngredient currentIngredientClick = new SuperIngredient();

    DBHandler db = new DBHandler();


    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    List<SuperIngredient> ingredientList = new ArrayList<>();

    public boolean check_nl() {
        boolean hasError = false;

        if (this.mode.equals("EDIT_NL")) {
            hasError |= textfield_ngaynhapkho.getValue() == null ? uf.setErrorMsg(texterror_validator_ngaynhaptkho, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_ngaynhaptkho);

            hasError |= uf.isEmptyString(textfield_soluongtrongkho.getText()) ? uf.setErrorMsg(texterror_validator_soluongtrongkho, "Vui lòng điền thông tin") :
                    (!uf.isNumericString(textfield_soluongtrongkho.getText()) ? uf.setErrorMsg(texterror_validator_soluongtrongkho, "Sai định dạng số") : uf.hideErrorMsg(texterror_validator_soluongtrongkho));

        }

        hasError |= uf.isEmptyString(textfield_tennl.getText()) ? uf.setErrorMsg(texterror_validator_tennl, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_tennl);

        hasError |= uf.isEmptyString(textfield_gia.getText()) ? uf.setErrorMsg(texterror_validator_gia, "Vui lòng điền thông tin") :
                (!uf.isNumericString(textfield_gia.getText()) ? uf.setErrorMsg(texterror_validator_gia, "Sai định dạng số") : uf.hideErrorMsg(texterror_validator_gia));


        hasError |= uf.isEmptyString(textfield_donvi.getText()) ? uf.setErrorMsg(texterror_validator_donvi, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_donvi);


        return hasError;
    }


    public String mode = "ADD_NL";

    void clearChiTiet() {
        textfield_ngaynhapkho.setValue(null);
        textfield_tennl.clear();
        textfield_donvi.clear();
        textfield_gia.clear();
        textfield_soluongtrongkho.clear();
    }

    public void renderDetailFollowingMode() {


        if (this.mode.equals("ADD_NL")) {
            uf.setVisibleNode(textfield_ngaynhapkho, false);
            uf.setVisibleNode(textfield_tennl, true);
            uf.setVisibleNode(textfield_gia, true);
            uf.setVisibleNode(textfield_donvi, true);
            uf.setVisibleNode(textfield_soluongtrongkho, false);
            uf.setVisibleNode(label_ngaynhapkho, false);
            uf.setVisibleNode(label_sltrongkho, false);


        } else {
            uf.setVisibleNode(textfield_ngaynhapkho, true);
            uf.setVisibleNode(textfield_tennl, true);
            uf.setVisibleNode(textfield_gia, true);
            uf.setVisibleNode(textfield_donvi, true);

            uf.setVisibleNode(label_ngaynhapkho, true);
            uf.setVisibleNode(label_sltrongkho, true);
            uf.setVisibleNode(textfield_soluongtrongkho, true);
        }
    }

    public boolean check_is_exit(String tennl) {
        return ingredientList.stream().filter(material1 -> material1.getTen().equals(tennl)).collect(Collectors.toList()).size() > 0 ? uf.setErrorMsg(texterror_validator_tennl, "nguyên liệu đã tồn tại") : uf.hideErrorMsg(texterror_validator_tennl);
    }


    void renderTableNguyenLieu() {
        table_nguyen_lieu.getItems().clear();
        //RENDER ALL TEN NL
        ObservableList<SuperIngredient> nguyenLieu = FXCollections.observableArrayList();
        for (SuperIngredient ingredient : ingredientList) {
            nguyenLieu.add(ingredient);
        }
        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_tennl.setCellValueFactory(new PropertyValueFactory<>("ten"));
        tablecolumn_stt.setCellValueFactory(new PropertyValueFactory<>("id"));
        tablecolumn_dv.setCellValueFactory(new PropertyValueFactory<>("donVi"));
        tablecolumn_sltrongkho.setCellValueFactory(new PropertyValueFactory<>("soLuongTrongKho"));

        tablecolumn_ngaynhapkho.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SuperIngredient, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SuperIngredient, String> p) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date ngayNhapKho = p.getValue().getNgayNhapKho();
                String formattedDate = (ngayNhapKho != null) ? dateFormat.format(ngayNhapKho) : "";
                return new SimpleStringProperty(formattedDate);
            }
        });


        table_nguyen_lieu.setItems(nguyenLieu);
    }


    void setCurrentUser(LogedInUser x) {
        this.user = x;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //RENDER DEFAULT VALUE
        uf.setVisibleNode(texterror_validator_donvi, false);
        uf.setVisibleNode(texterror_validator_tennl, false);
        uf.setVisibleNode(texterror_validator_gia, false);
        uf.setVisibleNode(texterror_validator_ngaynhaptkho, false);
        uf.setVisibleNode(texterror_validator_soluongtrongkho, false);

        renderDetailFollowingMode();

        try {
            ingredientList = db.getAllSuperIngredients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        renderTableNguyenLieu();

        table_nguyen_lieu.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra double click

                SuperIngredient selectedItem = table_nguyen_lieu.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    this.mode = "EDIT_NL";
                    label_them_chinh_sua_nl.setText("Chỉnh sửa hoàn tất");
                    renderDetailFollowingMode();

                    this.currentIngredientClick = selectedItem;

                    String tenNguyenLieu = selectedItem.getTen();
                    String donVi = selectedItem.getDonVi();
                    int soLuong = selectedItem.getSoLuongTrongKho();
                    Date ngayNhapKho = selectedItem.getNgayNhapKho();
                    int id = selectedItem.getId();
                    int gia = selectedItem.getGia();


                    textfield_tennl.setText(tenNguyenLieu);
                    textfield_donvi.setText(donVi);
                    textfield_gia.setText(String.valueOf(gia));
                    textfield_soluongtrongkho.setText(String.valueOf(soLuong));

                    if (ngayNhapKho != null) {
                        LocalDate ngayNhapKhoLocalDate = ngayNhapKho.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        // Ngày nhập kho khác null, tiếp tục xử lý
                        textfield_ngaynhapkho.setValue(ngayNhapKhoLocalDate);
                    } else {
                        textfield_ngaynhapkho.setValue(null);
                    }

                }
            }

        });

        button_themnguyenlieu.setOnMouseClicked(e -> {
            if (this.mode.equals("ADD_NL")) {
                if (!check_nl()) {

                    String ten_nl = textfield_tennl.getText();
                    int so_luong_trong_kho = 0;
                    String don_vi = textfield_donvi.getText();
                    int id = ingredientList.size() + 1;
                    Date ngay_nhap_kho = null;
                    int gia = Integer.parseInt(textfield_gia.getText());

                    SuperIngredient superIngredient = new SuperIngredient(id, ten_nl, don_vi, so_luong_trong_kho, gia, ngay_nhap_kho);
                    if (check_is_exit(ten_nl)) {
                        System.out.println("Da ton tai");
                        // xử lý đã tồn tại
                    } else {
                        ingredientList.add(superIngredient);
                        try {
                            db.InsIngre(id,ten_nl,don_vi,so_luong_trong_kho,gia);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        clearChiTiet();
                        renderTableNguyenLieu();
                    }
                }
            } else if (this.mode.equals("EDIT_NL")) {
                if (!check_nl()) {
                    String ten_nl = textfield_tennl.getText();
                    int so_luong_trong_kho = Integer.parseInt(textfield_soluongtrongkho.getText());
                    String don_vi = textfield_donvi.getText();
                    int id = currentIngredientClick.getId();
                    Date ngay_nhap_kho = Date.from(textfield_ngaynhapkho.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                    int gia = Integer.parseInt(textfield_gia.getText());

                    ingredientList.forEach(ingredient -> {
                        if (id == ingredient.getId()) {
                            System.out.println(ngay_nhap_kho);
                            ingredient.setGia(gia);
                            ingredient.setTen(ten_nl);
                            ingredient.setNgayNhapKho(ngay_nhap_kho);
                            ingredient.setSoLuongTrongKho(so_luong_trong_kho);
                            ingredient.setDonVi(don_vi);
                            try {
                                db.EditOfIngre(id,ten_nl,don_vi,so_luong_trong_kho,gia);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });

                    this.mode = "ADD_NL";
                    renderDetailFollowingMode();
                    clearChiTiet();
                    label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                    renderTableNguyenLieu();
                }
            }
        });

        button_yeucaunhapnl.setOnMouseClicked(e -> {
            //open new UI

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/fast_food/demo/RequestForIngredient.fxml"));

            Scene scene = null;

            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            RequestForIngredientController RFIC = fxmlLoader.getController();
                    RFIC.setCurrentUser(user);


            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

        });

        textfield_xoa_nl.setOnKeyPressed(event -> {
            int result = 0;
            if (event.getCode() == KeyCode.ENTER) {
                String nlCanXoa = textfield_xoa_nl.getText();

                // Lọc allMaterial và loại bỏ các phần tử giống với filterString
                Iterator<SuperIngredient> iterator = ingredientList.iterator();
                while (iterator.hasNext()) {
                    SuperIngredient nl = iterator.next();
                    if (nl.getTen().equalsIgnoreCase(nlCanXoa)) {
                        ++result;
                        iterator.remove();
                        try {
                            db.xoaNCC(nl.getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (result != 0) {
                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("đã xóa thành công");
                    renderTableNguyenLieu();
                    System.out.println("Enter key pressed");
                } else {
                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("Lỗi");
                }
            }
        });


    }
}