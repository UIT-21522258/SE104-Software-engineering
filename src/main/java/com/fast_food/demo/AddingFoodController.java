package com.fast_food.demo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Ingredient;
import model.Material;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddingFoodController implements Initializable {
    @FXML
    private HBox button_chonanh;

    @FXML
    private HBox button_hoantat;

    @FXML
    private HBox button_themnguyenlieu;


    @FXML
    private TextField textfield_xoa_nl;

    @FXML
    private ChoiceBox<String> choicebox_loai;

    @FXML
    private ChoiceBox<String> choicebox_tennguyenlieu;

    @FXML
    private Label label_mode;

    @FXML
    private TableView<Material> table_nguyen_lieu;

    @FXML
    private TableColumn<?, ?> tablecolumn_dv;

    @FXML
    private TableColumn<?, ?> tablecolumn_slnl;

    @FXML
    private TableColumn<?, ?> tablecolumn_tennl;

    @FXML
    private TextField text_field_gia;

    @FXML
    private TextField textfield_donvi;

    @FXML
    private TextField textfield_mota;

    @FXML
    private TextField textfield_soluong;

    @FXML
    private TextField textfield_tenmon;


    @FXML
    private Text texterror_validator_donvi;

    @FXML
    private Text texterror_validator_gia;

    @FXML
    private Text texterror_validator_loai;

    @FXML
    private Text texterror_validator_mota;

    @FXML
    private Text texterror_validator_sl;

    @FXML
    private Text texterror_validator_tenmon;

    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private Text texterror_validator_tennl;
    private Stage primaryStage;
    private String imagePath;
    private String imageName;

    List<Material> allMaterial = new ArrayList<>(); // luu toan bo so luong, don vi, ten nguyen lieu de nau mon an them vo, se duoc re-render

    DBHandler db = new DBHandler();
    UtilityFunctions uf = new UtilityFunctions();
    List<Ingredient> ingredientList = new ArrayList<>();



    public String mode = "ADD_NL";

    public void clearChiTietNguyenLieu() {
        choicebox_tennguyenlieu.setValue(null);
        textfield_soluong.clear();
        textfield_donvi.clear();
    }

    public boolean check_is_error_nl() {

        boolean hasError = false;

        hasError |= uf.isEmptyString(choicebox_tennguyenlieu.getValue()) ? uf.setErrorMsg(texterror_validator_tennl, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_tennl);

        hasError |= uf.isEmptyString(textfield_soluong.getText()) ? uf.setErrorMsg(texterror_validator_sl, "Vui lòng điền thông tin") :
                (!uf.isNumericString(textfield_soluong.getText()) ? uf.setErrorMsg(texterror_validator_sl, "Sai định dạng số") : uf.hideErrorMsg(texterror_validator_sl));


        hasError |= uf.isEmptyString(textfield_donvi.getText()) ? uf.setErrorMsg(texterror_validator_donvi, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_donvi);


        return hasError;
    }

    public boolean check_is_error() {

        boolean hasError = false;

        hasError |= uf.isEmptyString(choicebox_loai.getValue()) ? uf.setErrorMsg(texterror_validator_loai, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_loai);

        hasError |= uf.isEmptyString(text_field_gia.getText()) ? uf.setErrorMsg(texterror_validator_gia, "Vui lòng điền thông tin") :
                (!uf.isNumericString(text_field_gia.getText()) ? uf.setErrorMsg(texterror_validator_gia, "Sai định dạng số") : uf.hideErrorMsg(texterror_validator_gia));


        hasError |= uf.isEmptyString(textfield_tenmon.getText()) ? uf.setErrorMsg(texterror_validator_tenmon, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_tenmon);
        hasError |= uf.isEmptyString(textfield_mota.getText()) ? uf.setErrorMsg(texterror_validator_mota, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_mota);


        return hasError;
    }

    public boolean check_is_exit(String tennl) {
        return allMaterial.stream().filter(material1 -> material1.getName().equals(tennl)).collect(Collectors.toList()).size() > 0 ? uf.setErrorMsg(texterror_validator_tennl, "nguyên liệu đã tồn tại") : uf.hideErrorMsg(texterror_validator_tennl);
    }

    public void renderTableMaterial() {
        table_nguyen_lieu.getItems().clear();
        ObservableList<Material> nguyenLieu = FXCollections.observableArrayList();

        for (Material material : allMaterial) {
            nguyenLieu.add(material);
        }

        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_tennl.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablecolumn_dv.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tablecolumn_slnl.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        table_nguyen_lieu.setItems(nguyenLieu);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // DEFAULT--------------------------------
        uf.setVisibleNode(texterror_validator_donvi, false);
        uf.setVisibleNode(texterror_validator_gia, false);
        uf.setVisibleNode(texterror_validator_loai, false);
        uf.setVisibleNode(texterror_validator_mota, false);
        uf.setVisibleNode(texterror_validator_tennl, false);
        uf.setVisibleNode(texterror_validator_tenmon, false);
        uf.setVisibleNode(texterror_validator_sl, false);

        uf.addColumnHeaderIcon(tablecolumn_tennl, "/images/link-dynamic-color.png", "Nguyên Liệu");
        uf.addColumnHeaderIcon(tablecolumn_dv, "/images/star-dynamic-color.png", "Đơn Vị");
        uf.addColumnHeaderIcon(tablecolumn_slnl, "/images/plus-dynamic-gradient.png", "Số Lượng");


        button_hoantat.setPrefHeight(75);
        button_hoantat.setMinHeight(50);
        Platform.runLater(() -> {
            primaryStage = (Stage) button_chonanh.getScene().getWindow();
        });

        choicebox_loai.getItems().add("do an");
        choicebox_loai.getItems().add("do uong");

        //RENDER CHOICEBOX CHON NGUYEN LIEU

        try {
            ingredientList = db.getAllIngredients();
            for (Ingredient ing : ingredientList) {
                choicebox_tennguyenlieu.getItems().add(ing.getTen());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        button_chonanh.setOnMouseClicked(e ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn ảnh");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Ảnh", "*.jpg", "*.jpeg", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String chuoi = selectedFile.getAbsolutePath();
                int viTriCuoi = chuoi.lastIndexOf("\\");
                if (viTriCuoi != -1) {
                    imagePath = chuoi.substring(0, viTriCuoi);
                }
                imageName = selectedFile.getName();
                System.out.println(imagePath);
                System.out.println(imageName);
                // Ứng dụng logic để sử dụng đường dẫn ảnh (imagePath) trong ứng dụng của bạn
            }
        });
        button_hoantat.setOnMouseClicked(event -> {

            if(!check_is_error()) {
                try {
                    db.AddFood(textfield_tenmon.getText(),textfield_mota.getText(),choicebox_loai.getValue(),Integer.parseInt(text_field_gia.getText()),imagePath,imageName);
                    for (Material material : allMaterial) {
                        db.InsNl(textfield_tenmon.getText(),material.getName(),material.getQuantity());
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setContentText("Đã thêm món thành công");
                    alert.show();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        table_nguyen_lieu.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra double click

                Material selectedMaterial = table_nguyen_lieu.getSelectionModel().getSelectedItem();
                if (selectedMaterial != null) {
                    this.mode = "EDIT_NL";
                    label_them_chinh_sua_nl.setText("Chỉnh sửa hoàn tất");

                    String tenNguyenLieu = selectedMaterial.getName();
                    String donVi = selectedMaterial.getUnit();
                    int soLuong = selectedMaterial.getQuantity();

                    textfield_donvi.setText(donVi);
                    textfield_soluong.setText(String.valueOf(soLuong));
                    choicebox_tennguyenlieu.setValue(tenNguyenLieu);

                }
            }
        });

        textfield_xoa_nl.setOnKeyPressed(event -> {
            int result = 0;
            if (event.getCode() == KeyCode.ENTER) {
                String monCanXoa = textfield_xoa_nl.getText();

                // Lọc allMaterial và loại bỏ các phần tử giống với filterString
                Iterator<Material> iterator = allMaterial.iterator();
                while (iterator.hasNext()) {
                    Material material = iterator.next();
                    if (material.getName().equalsIgnoreCase(monCanXoa)) {
                        ++result;
                        iterator.remove();
                    }
                }
                if(result != 0) {

                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("đã xóa thành công");
                    renderTableMaterial();
                    System.out.println("Enter key pressed");

                } else {
                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("Lỗi");

                }
            }
        });


        //------------------------------------------------------------------------------
        //XU LY KHI AN THEM NGUYEN LIEU
        button_themnguyenlieu.setOnMouseClicked(e -> {
            if(this.mode.equals("ADD_NL")){
                if (!check_is_error_nl()) {
                    String ten_nl = choicebox_tennguyenlieu.getValue();
                    String so_luong = textfield_soluong.getText();
                    String don_vi = textfield_donvi.getText();
                    Material material = new Material(ten_nl, don_vi, Integer.parseInt(so_luong));

                    if (check_is_exit(ten_nl)) {
                        System.out.println("Da ton tai");
                        // xử lý đã tồn tại
                    } else {
                        allMaterial.add(material);

                        //////////////////
                        /*
                         * XU LY VOI DB
                         *
                         * khong can
                         *
                         * */

                        /////////////////

                        clearChiTietNguyenLieu();
                        renderTableMaterial();
                    }
                }
            }
            else if(this.mode.equals("EDIT_NL")) {

                // Lấy dữ liệu từ các TextField
                String ten_nl = choicebox_tennguyenlieu.getValue();
                String don_vi = textfield_donvi.getText();
                int so_luong = Integer.parseInt(textfield_soluong.getText());


                Material monAn = new Material(ten_nl, don_vi, so_luong);
                allMaterial.forEach(material -> {
                    if (material.getName().equals(ten_nl)) {
                        material.setUnit(don_vi);
                        material.setQuantity(so_luong);
                    }
                });

                //////////////////
                /*
                 * XU LY VOI DB
                 *
                 * khong can
                 *
                 * */

                /////////////////

                this.mode = "ADD_NL";
                clearChiTietNguyenLieu();
                label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                renderTableMaterial();
            }
        });

    }
}
