package com.fast_food.demo;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Employee;
import model.Ingredient;
import model.Material;
import model.MenuItem;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
public class MenuManagementController implements Initializable {
    @FXML
    private HBox button_chonanh;

    @FXML
    private HBox button_hoantat;

    @FXML
    private HBox button_themmon;

    @FXML
    private HBox button_themnguyenlieu;

    @FXML
    private HBox button_xoanguyenlieu;

    @FXML
    private ChoiceBox<String> choicebox_loai;

    @FXML
    private ChoiceBox<String> choicebox_tennguyenlieu;

    @FXML
    private Label label_mode;

    @FXML
    private TableView<MenuItem> table_mon_an;

    @FXML
    private TableView<Material> table_nguyen_lieu;

    @FXML
    private TableColumn<MenuItem, Image> tablecolumn_anh;

    @FXML
    private TableColumn<?, ?> tablecolumn_dv;

    @FXML
    private TableColumn<?, ?> tablecolumn_slnl;


    @FXML
    private TableColumn<?, ?> tablecolumn_tennl;

    @FXML
    private TableColumn<?, ?> tablecolumn_tenmon;


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
    private Text texterror_validator_tennl;

    @FXML
    private TextField text_field_gia;


    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private TextField textfield_donvi;


    @FXML
    private TextField textfield_xoa_nl;

    @FXML
    private TextField textfield_mota;

    @FXML
    private TextField textfield_soluong;

    @FXML
    private HBox button_xoamon;
    @FXML
    private TextField textfield_xoa_mon;
    @FXML
    private HBox HBox_refresh;


    @FXML
    private TextField textfield_tenmon;
    private Stage primaryStage;
    private String imagePath="";
    private String imageName="";
    private MenuItem currentMenuItemClick;
    private Material currentMaterielClick;
    public String mode = "ADD_NL";

    //private int originalPrice = 0;
    /*
    + EDIT_MON_AN (không sửa nguyên liệu)
    + EDIT_MON_AN_NL (có sửa nguyên liệu)
    + DELETE_NGUYEN_LIEU (xóa nguyên liệu)
    + ADD_NL
    + ADD_MON_AN
    + DELETE_MON_AN
    *
    */
    HashMap<Integer, Integer> allPrice = new HashMap<>();

    HashSet<MenuItem> menuList;

    List<Material> materialList;
    List<Ingredient> ingredientList;


    UtilityFunctions uf = new UtilityFunctions();
    DBHandler db = new DBHandler();

    public void clearChiTietMonAn() {
        textfield_mota.clear();
        textfield_tenmon.clear();
        choicebox_loai.setValue(null);
        text_field_gia.clear();
    }

    public void clearChiTietNguyenLieu() {
        choicebox_tennguyenlieu.setValue(null);
        textfield_soluong.clear();
        textfield_donvi.clear();
    }

    public void isDisableChiTietMonAn(boolean x) {
        textfield_mota.setDisable(x);
        textfield_tenmon.setDisable(x);
        choicebox_loai.setDisable(x);
        text_field_gia.setDisable(x);
    }

    public void isDisableChiTietNguyenLieu(boolean x) {
        choicebox_tennguyenlieu.setDisable(x);
        textfield_soluong.setDisable(x);
        textfield_donvi.setDisable(x);
    }

    public void renderTableNguyenLieu() {
        table_nguyen_lieu.getItems().clear();
        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        ObservableList<Material> nguyenLieu = FXCollections.observableArrayList();

        for (Material x : materialList) {
            nguyenLieu.add(x);
            System.out.println(x.getName());
        }


        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_tennl.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablecolumn_dv.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tablecolumn_slnl.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table_nguyen_lieu.setItems(nguyenLieu);
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
        return materialList.stream().filter(material1 -> material1.getName().equals(tennl)).collect(Collectors.toList()).size() > 0 ? uf.setErrorMsg(texterror_validator_tennl, "nguyên liệu đã tồn tại") : uf.hideErrorMsg(texterror_validator_tennl);
    }

    public void renderTableMonAn() throws SQLException {
        table_mon_an.getItems().clear();
        HashSet<MenuItem> menu_list = db.getAllMenuItems();

        ObservableList<MenuItem> monan = FXCollections.observableArrayList();
        tablecolumn_anh.setCellFactory(column -> new TableCell<MenuItem, Image>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) {
                    setGraphic(null);
                } else {

                    imageView.setImage(image);
                    imageView.setFitWidth(50);  // Set the desired width of the image
                    imageView.setFitHeight(50); // Set the desired height of the image

                    Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                    clip.setArcWidth(40);
                    clip.setArcHeight(40);
                    imageView.setClip(clip);


                    setGraphic(imageView);
                }
            }
        });

        tablecolumn_anh.setCellValueFactory(cellData -> {
            byte[] imageData = cellData.getValue().getImage(); // Assuming "getHinhAnh()" returns the byte array
            if (imageData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = new Image(bis);
                return new SimpleObjectProperty<>(image);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });

        tablecolumn_tenmon.setCellValueFactory(new PropertyValueFactory<>("name"));
        for (MenuItem x : menu_list) {
            monan.add(x);
        }

        table_mon_an.setItems(monan);
    }

    public void renderTableMonAnFollowingUI() throws SQLException {
        table_mon_an.getItems().clear();

        ObservableList<MenuItem> monan = FXCollections.observableArrayList();
        tablecolumn_anh.setCellFactory(column -> new TableCell<MenuItem, Image>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) {
                    setGraphic(null);
                } else {

                    imageView.setImage(image);
                    imageView.setFitWidth(50);  // Set the desired width of the image
                    imageView.setFitHeight(50); // Set the desired height of the image

                    Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                    clip.setArcWidth(40);
                    clip.setArcHeight(40);
                    imageView.setClip(clip);


                    setGraphic(imageView);
                }
            }
        });

        tablecolumn_anh.setCellValueFactory(cellData -> {
            byte[] imageData = cellData.getValue().getImage(); // Assuming "getHinhAnh()" returns the byte array
            if (imageData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = new Image(bis);
                return new SimpleObjectProperty<>(image);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });

        tablecolumn_tenmon.setCellValueFactory(new PropertyValueFactory<>("name"));
        for (MenuItem x : menuList) {
            monan.add(x);
        }

        table_mon_an.setItems(monan);
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

        button_hoantat.setPrefHeight(75);
        button_hoantat.setMinHeight(50);
        button_hoantat.setMaxHeight(50);
        Platform.runLater(() -> {
            primaryStage = (Stage) button_chonanh.getScene().getWindow();
        });


        try {
            menuList = db.getAllMenuItems();
            //-------RENDER DEFAULT VALUE -----------------
            choicebox_loai.getItems().add("do an");
            choicebox_loai.getItems().add("do uong");

            ingredientList = db.getAllIngredients();


            for (Ingredient x : ingredientList) {
                String tenNguyenLieu = x.getTen();
                if (!choicebox_tennguyenlieu.getItems().contains(tenNguyenLieu)) {
                    choicebox_tennguyenlieu.getItems().add(tenNguyenLieu);
                }
            }
            isDisableChiTietMonAn(true);
            isDisableChiTietNguyenLieu(true);
            button_xoanguyenlieu.setDisable(true);

            button_themnguyenlieu.setDisable(true);
            for(MenuItem i : menuList){
                int name = i.getId();
                int price = i.getPrice();
                allPrice.put(name,price);
            }

            //-------RENDER DEFAULT VALUE -----------------


            // ------XU LY BUTTON-------------------
            //XU LY KHI AN THEM NGUYEN LIEU
            button_themnguyenlieu.setOnMouseClicked(e -> {
                choicebox_tennguyenlieu.setDisable(false);
                if (this.mode.equals("ADD_NL")) {
                    if (!check_is_error_nl()) {
                        String ten_nl = choicebox_tennguyenlieu.getValue();
                        String so_luong = textfield_soluong.getText();
                        String don_vi = textfield_donvi.getText();
                        Material material = new Material(ten_nl, don_vi, Integer.parseInt(so_luong));

                        if (check_is_exit(ten_nl)) {
                            System.out.println("Da ton tai");
                            // xử lý đã tồn tại
                        } else {
                            materialList.add(material);
                            try {
                                db.InsNl(textfield_tenmon.getText(),ten_nl,Integer.parseInt(so_luong));
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            clearChiTietNguyenLieu();
                            renderTableNguyenLieu();
                        }
                    }
                } else if (this.mode.equals("EDIT_NL")) {

                    // Lấy dữ liệu từ các TextField
                    String ten_nl = choicebox_tennguyenlieu.getValue();
                    String don_vi = textfield_donvi.getText();
                    String ten_mon = textfield_tenmon.getText();
                    int so_luong = Integer.parseInt(textfield_soluong.getText());


                    Material monAn = new Material(ten_nl, don_vi, so_luong);
                    materialList.forEach(material -> {
                        if (material.getName().equals(ten_nl)) {
                            material.setUnit(don_vi);
                            material.setQuantity(so_luong);
                        }
                    });

                    try {
                        int idMon = currentMenuItemClick.getId();
                        db.EditNlOfMon(idMon,ten_mon,ten_nl, so_luong);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    this.mode = "ADD_NL";
                    clearChiTietNguyenLieu();
                    label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                    renderTableNguyenLieu();
                }


            });
            textfield_xoa_mon.setOnKeyPressed(event->{
                int result =0;
                if(event.getCode()== KeyCode.ENTER){
                    String moncanxoa = textfield_xoa_mon.getText();
                    int id=0;
                    try {
                        id = db.findIdMon(moncanxoa);
                        result++;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if(result!=0){
                        try {
                            db.DelFood(id);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        textfield_xoa_mon.clear();
                        textfield_xoa_mon.setPromptText("Đã xóa thành công");
                        try {
                            renderTableMonAn();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            textfield_xoa_nl.setOnKeyPressed(event -> {
                int result = 0;
                if (event.getCode() == KeyCode.ENTER) {
                    String monCanXoa = textfield_xoa_nl.getText();

                    // Lọc allMaterial và loại bỏ các phần tử giống với filterString
                    Iterator<Material> iterator = materialList.iterator();
                    while (iterator.hasNext()) {
                        Material material = iterator.next();
                        if (material.getName().equalsIgnoreCase(monCanXoa)) {
                            ++result;
                            iterator.remove();
                        }
                    }
                    if (result != 0) {
                        try {
                            int idMon = currentMenuItemClick.getId();
                            db.DelNlOfMon(textfield_tenmon.getText(),monCanXoa);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
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


            button_themmon.setOnMouseClicked(e -> {
                //open new UI
                System.out.println("click");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/fast_food/demo/AddingFood.fxml"));


                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Stage stage = new Stage();

                stage.setScene(scene);
                stage.show();


            });
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
            button_hoantat.setOnMouseClicked(e -> {
                if(!check_is_error()) {
                    try {
                        int idMon = currentMenuItemClick.getId();
                        System.out.println(idMon);

                        int giaCu = allPrice.getOrDefault(idMon,-1);
                        if(giaCu==db.getFoodPrice(db.findIdMon(textfield_tenmon.getText()))) {
                            db.UpdateFood(idMon, textfield_tenmon.getText(), textfield_mota.getText(), choicebox_loai.getValue(), Integer.parseInt(text_field_gia.getText()), imagePath, imageName);
                            allPrice.put(idMon, Integer.parseInt(text_field_gia.getText()));
                            renderTableMonAn();
                        }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Lỗi");
                        alert.setHeaderText(null);
                        alert.setContentText("Đã xảy ra lỗi. Vui lòng reset hệ thống và thử lại.");
                        alert.show();
                    }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    imagePath = "";
                    imageName = "";
                    System.out.println(this.mode);
                }
            });

            HBox_refresh.setOnMouseClicked(e->{
                try {
                    renderTableMonAn();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            // ------XU LY BUTTON-------------------


            // ------RENDER TABLE MON AN-------------------
            ObservableList<MenuItem> monan = FXCollections.observableArrayList();
            tablecolumn_anh.setCellFactory(column -> new TableCell<MenuItem, Image>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if (empty || image == null) {
                        setGraphic(null);
                    } else {

                        imageView.setImage(image);
                        imageView.setFitWidth(50);  // Set the desired width of the image
                        imageView.setFitHeight(50); // Set the desired height of the image

                        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                        clip.setArcWidth(40);
                        clip.setArcHeight(40);
                        imageView.setClip(clip);


                        setGraphic(imageView);
                    }
                }
            });

            tablecolumn_anh.setCellValueFactory(cellData -> {
                byte[] imageData = cellData.getValue().getImage(); // Assuming "getHinhAnh()" returns the byte array
                if (imageData != null) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                    Image image = new Image(bis);
                    return new SimpleObjectProperty<>(image);
                } else {
                    return new SimpleObjectProperty<>(null);
                }
            });

            tablecolumn_tenmon.setCellValueFactory(new PropertyValueFactory<>("name"));
            for (MenuItem x : menuList) {
                monan.add(x);
            }

            table_mon_an.setItems(monan);
            // ------RENDER TABLE MON AN-------------------
            table_mon_an.setRowFactory(tv -> {


                TableRow<MenuItem> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        MenuItem selectedItem = row.getItem();
                        //-----------XU LY KHI DOUBLE CLICK VAO 1 HANG CUA TABLE MON AN
                        currentMenuItemClick = new MenuItem(selectedItem.getId(),selectedItem.getName(),selectedItem.getDescription(),selectedItem.getCategory(),selectedItem.getPrice(), selectedItem.getImage());
                        isDisableChiTietMonAn(false);
                        button_xoamon.setDisable(false);
                        button_themnguyenlieu.setDisable(false);
                        isDisableChiTietNguyenLieu(false);

                        textfield_tenmon.setText(selectedItem.getName());
                        int idMonvuaselect = selectedItem.getId();
                        int oldGia = allPrice.getOrDefault(idMonvuaselect,-1);
                        text_field_gia.setText(String.valueOf(oldGia));

                        textfield_mota.setText(selectedItem.getDescription());
                        choicebox_loai.setValue(selectedItem.getCategory());

                        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
                        ObservableList<Material> nguyenLieu = FXCollections.observableArrayList();

                        try {
                            materialList = db.getMaterialsForDish(selectedItem.getId());
                            for (Material x : materialList) {
                                nguyenLieu.add(x);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }


                        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
                        tablecolumn_tennl.setCellValueFactory(new PropertyValueFactory<>("name"));
                        tablecolumn_dv.setCellValueFactory(new PropertyValueFactory<>("unit"));
                        tablecolumn_slnl.setCellValueFactory(new PropertyValueFactory<>("quantity"));

                        table_nguyen_lieu.setItems(nguyenLieu);

                        //-----------XU LY KHI DOUBLE CLICK VAO 1 HANG CUA TABLE MON AN

                    }
                });
                return row;
            });

            table_nguyen_lieu.setRowFactory(tv -> {

                TableRow<Material> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        this.mode = "EDIT_NL";
                        label_them_chinh_sua_nl.setText("Chỉnh sửa hoàn tất");

                        Material selectedItem = row.getItem();
                        currentMaterielClick = new Material(selectedItem.getName(),selectedItem.getUnit(),selectedItem.getQuantity());
                        isDisableChiTietNguyenLieu(false);
                        button_xoanguyenlieu.setDisable(false);


                        //---------RENDER VALUE-------------
                        textfield_donvi.setText(selectedItem.getUnit());
                        textfield_soluong.setText(String.valueOf(selectedItem.getQuantity()));
                        choicebox_tennguyenlieu.setValue(selectedItem.getName());
                        choicebox_tennguyenlieu.setDisable(true);
                        //---------RENDER VALUE-------------
                    }
                });
                return row;

            });


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}