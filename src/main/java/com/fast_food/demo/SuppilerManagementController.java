package com.fast_food.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Ingredient;
import model.SuperIngredient;
import model.SuperSuppiler;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class SuppilerManagementController implements Initializable {
    @FXML
    private HBox button_themnguyenlieu;

    @FXML
    private HBox button_xoanguyenlieu;

    @FXML
    private ChoiceBox<String> choice_box_nlcc;

    @FXML
    private Label label_sltrongkho;

    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private TableView<SuperSuppiler> table_nha_cung_cap;

    @FXML
    private TableColumn<?, ?> tablecolumn_email;

    @FXML
    private TableColumn<?, ?> tablecolumn_nlcungcap;

    @FXML
    private TableColumn<SuperSuppiler, String> tablecolumn_sdt;

    @FXML
    private TableColumn<?, ?> tablecolumn_tenncc;

    @FXML
    private Text texterror_validator_emailncc;

    @FXML
    private Text texterror_validator_nlcc;

    @FXML
    private Text texterror_validator_sdt;

    @FXML
    private Text texterror_validator_tencc;


    @FXML
    private TextField textfield_diachi;

    @FXML
    private Text texterror_validator_diachi;


    @FXML
    private TableColumn<?, ?> tablecolumn_diachi;

    @FXML
    private TextField textfield_emailncc;

    @FXML
    private TextField textfield_ncc;

    @FXML
    private TextField textfield_sdt;

    @FXML
    private TextField textfield_xoa_nl;

    public String mode = "ADD_NCC";
    UtilityFunctions uf = new UtilityFunctions();

    SuperSuppiler currentSuppilerClick = new SuperSuppiler();
    DBHandler db = new DBHandler();

    List<SuperSuppiler> suppilerList = new ArrayList<>();
    List<Ingredient> ingredientList = new ArrayList<>();

    void clearChiTiet() {
        choice_box_nlcc.setValue(null);
        textfield_sdt.clear();
        textfield_emailncc.clear();
        textfield_diachi.clear();
        textfield_ncc.clear();

    }


    public boolean check_is_exit(String tenncc) {
        return suppilerList.stream().filter(x -> x.getTen().equals(tenncc)).collect(Collectors.toList()).size() > 0 ? uf.setErrorMsg(texterror_validator_nlcc, "Nhà cung cấp đã tồn tại") : uf.hideErrorMsg(texterror_validator_nlcc);
    }

    public boolean check() {
        boolean hasError = false;
        hasError |= uf.isEmptyString(choice_box_nlcc.getValue()) ? uf.setErrorMsg(texterror_validator_nlcc, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_nlcc);


        hasError |= uf.isEmptyString(textfield_ncc.getText()) ? uf.setErrorMsg(texterror_validator_tencc, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_tencc);
        hasError |= uf.isEmptyString(textfield_diachi.getText()) ? uf.setErrorMsg(texterror_validator_diachi, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_diachi);

        hasError |= choice_box_nlcc.getValue() == null ? uf.setErrorMsg(texterror_validator_tencc, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_nlcc);


        hasError |= uf.isEmptyString(textfield_sdt.getText()) ? uf.setErrorMsg(texterror_validator_sdt, "Vui lòng điền thông tin") :
                (!uf.isPhoneNumber(textfield_sdt.getText()) ? uf.setErrorMsg(texterror_validator_sdt, "Sai định dạng số điện thoại") : uf.hideErrorMsg(texterror_validator_sdt));

        hasError |= uf.isEmptyString(textfield_emailncc.getText()) ? uf.setErrorMsg(texterror_validator_emailncc, "Vui lòng điền thông tin") :
                (!uf.isValidEmail(textfield_emailncc.getText()) ? uf.setErrorMsg(texterror_validator_emailncc, "Sai định dạng email") : uf.hideErrorMsg(texterror_validator_emailncc));


        return hasError;
    }

    void renderTableNguyenLieu() {
        table_nha_cung_cap.getItems().clear();
        //RENDER ALL TEN NL
        ObservableList<SuperSuppiler> ncc = FXCollections.observableArrayList();

        for (SuperSuppiler superSuppiler : suppilerList) {
            ncc.add(superSuppiler);
        }
        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tablecolumn_tenncc.setCellValueFactory(new PropertyValueFactory<>("ten"));
        // Create a custom CellValueFactory for the "sdt" field
        tablecolumn_sdt.setCellValueFactory(new PropertyValueFactory<SuperSuppiler, String>("soDienThoai"));
        tablecolumn_diachi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        tablecolumn_nlcungcap.setCellValueFactory(new PropertyValueFactory<>("nguyenLieuTen"));

        table_nha_cung_cap.setItems(ncc);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uf.setVisibleNode(texterror_validator_sdt, false);
        uf.setVisibleNode(texterror_validator_tencc, false);
        uf.setVisibleNode(texterror_validator_nlcc, false);
        uf.setVisibleNode(texterror_validator_diachi, false);
        uf.setVisibleNode(texterror_validator_emailncc, false);


        try {
            suppilerList = db.getAllNhaCungCapWithNguyenLieu();
            ingredientList = db.getAllIngredients();
            renderTableNguyenLieu();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(Ingredient i : ingredientList) {
            choice_box_nlcc.getItems().add(i.getTen());
        }
        table_nha_cung_cap.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra double click

                SuperSuppiler selectedItem = table_nha_cung_cap.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    this.mode = "EDIT_NCC";
                    label_them_chinh_sua_nl.setText("Chỉnh sửa hoàn tất");


                    this.currentSuppilerClick = selectedItem;

                    String ten_ncc = selectedItem.getTen();
                    String sdt = selectedItem.getSoDienThoai();
                    String email = selectedItem.getEmail();
                    String nl = selectedItem.getNguyenLieuTen();
                    String diachi = selectedItem.getDiaChi();

                    int id = selectedItem.getId();


                    textfield_ncc.setText(ten_ncc);
                    textfield_emailncc.setText(email);
                    textfield_sdt.setText(sdt);
                    choice_box_nlcc.setValue(nl);
                    textfield_diachi.setText(diachi);

                }
            }

        });
        textfield_xoa_nl.setOnKeyPressed(e -> {
            int result = 0;
            if (e.getCode() == KeyCode.ENTER) {
                String nlCanXoa = textfield_xoa_nl.getText();


                Iterator<SuperSuppiler> iterator = suppilerList.iterator();
                while (iterator.hasNext()) {
                    SuperSuppiler nl = iterator.next();
                    if (nl.getTen().equalsIgnoreCase(nlCanXoa)) {
                        ++result;
                        iterator.remove();
                    }
                }
                if (result != 0) {
                    try {
                        System.out.println(textfield_xoa_nl.getText());
                        db.DelNcc(textfield_xoa_nl.getText());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
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

        table_nha_cung_cap.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                SuperSuppiler selectedRow = table_nha_cung_cap.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to delete this row?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        table_nha_cung_cap.getItems().remove(selectedRow);
                        try {
                            db.xoaNCC(selectedRow.getId());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        Iterator<SuperSuppiler> iterator = suppilerList.iterator();
                        while (iterator.hasNext()) {
                            SuperSuppiler x = iterator.next();
                            if (x.getId() == selectedRow.getId()) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        });


        button_themnguyenlieu.setOnMouseClicked(e -> {
            if (this.mode.equals("ADD_NCC")) {
                if (!check()) {

                    String ten_ncc = textfield_ncc.getText();
                    String sdt = textfield_sdt.getText();
                    String email = textfield_emailncc.getText();
                    String nl = choice_box_nlcc.getValue();
                    String diachi = textfield_diachi.getText();
                    int id = 0;
                    for (int i = 0; i < suppilerList.size(); i++) {
                        if(id<=suppilerList.get(i).getId()){
                            id=suppilerList.get(i).getId();
                        }
                    }
                    id++;
                    SuperSuppiler superSuppiler = new SuperSuppiler(id, ten_ncc, sdt, email, diachi, currentSuppilerClick.getNguyenLieuId(), choice_box_nlcc.getValue());
                    if (check_is_exit(ten_ncc)) {
                        System.out.println("Da ton tai");
                        // xử lý đã tồn tại
                    } else {
                        suppilerList.add(superSuppiler);
                        try {
                            ArrayList<Integer> ncc_nlID=db.getNcc_Nlid();
                            db.InsNCC(id, ten_ncc, sdt, email, diachi);
                            db.InsNCC_NL(ncc_nlID.get(ncc_nlID.size()-1)+1,id, db.find_idNl(choice_box_nlcc.getValue()));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        clearChiTiet();
                        renderTableNguyenLieu();
                    }
                }
            } else if (this.mode.equals("EDIT_NCC")) {
                if (!check()) {
                    int id = suppilerList.size() + 1;
                    String ten_ncc = textfield_ncc.getText();
                    String sdt = textfield_sdt.getText();
                    String email = textfield_emailncc.getText();
                    String diachi = textfield_diachi.getText();

                    suppilerList.forEach(suppiler -> {
                        if (currentSuppilerClick.getId() == suppiler.getId()) {
                            suppiler.setEmail(email);
                            suppiler.setTen(ten_ncc);
                            suppiler.setDiaChi(diachi);
                            suppiler.setSoDienThoai(sdt);
                            try {
                                db.EditOfNCC_NL(choice_box_nlcc.getValue(),currentSuppilerClick.getId());
                                db.EditOfNCC(currentSuppilerClick.getId(),ten_ncc,sdt,email,diachi);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                    this.mode = "ADD_NCC";
                    clearChiTiet();
                    label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                    renderTableNguyenLieu();
                }
            }
        });
    }

}