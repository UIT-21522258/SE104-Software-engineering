package com.fast_food.demo;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.*;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RequestForIngredientController implements Initializable {

    @FXML
    private HBox button_hoantat;

    @FXML
    private HBox button_themnguyenlieu;

    @FXML
    private HBox button_xoanguyenlieu;

    @FXML
    private ChoiceBox<String> choicebox_nhacungcap;

    @FXML
    private Label label_don_vi;

    @FXML
    private Label label_gia;

    @FXML
    private Label label_mode;

    @FXML
    private Label label_mode1;

    @FXML
    private Label label_ten_nl;

    @FXML
    private Label label_them_chinh_sua_nl;

    @FXML
    private Label label_tongtien;

    @FXML
    private TableView<Ingredient> table_nguyenlieu;

    @FXML
    private TableView<IngredientReceivedNote> table_phieunguyenlieu;

    @FXML
    private TableColumn<?, ?> tablecolumn_nhacungcap;

    @FXML
    private TableColumn<?, ?> tablecolumn_soluong;

    @FXML
    private TableColumn<?, ?> tablecolumn_tenlistnl;

    @FXML
    private TableColumn<?, ?> tablecolumn_tennl;

    @FXML
    private Label text_total;

    @FXML
    private Text texterror_validator_nhacungcap;

    @FXML
    private Text texterror_validator_tennl;

    @FXML
    private Text texterror_validator_soluong;

    @FXML
    private TableColumn<?, ?> tablecolumn_id;

    @FXML
    private TextField textfield_soluong;

    @FXML
    private TextField textfield_xoa_nl;

    List<Supplier> supplierList = new ArrayList<>();

    LogedInUser user = new LogedInUser();

    UtilityFunctions uf = new UtilityFunctions();
    DBHandler db = new DBHandler();

    public String mode = "ADD_NL";
    public String click_mode = "currentIngredientClick";

    Supplier currentSuppiler = new Supplier();
    IngredientReceivedNote currentIngredientReceivedNote = new IngredientReceivedNote();

    List<Ingredient> ingredientList = new ArrayList<>();
    List<IngredientReceivedNote> ingredientReceivedNoteList = new ArrayList<>();
    List<IngredientReceivedNote> ingredientReceivedNoteList_forDB = new ArrayList<>();

    Ingredient currentIngredientClick = new Ingredient();

    void setCurrentUser(LogedInUser x) {
        this.user = x;
    }

    void renderTienNguyenLieuTheoSoLuong(int sl, int gia) {
        label_tongtien.setText(String.valueOf(gia * sl));
    }

    void renderChoiceBoxNCCFollowNLID(int nl) throws SQLException {
        choicebox_nhacungcap.getItems().clear();
        supplierList = new ArrayList<>();
        supplierList = db.getNhaCungCapByNguyenLieuId(nl);

        for (Supplier x : supplierList) {
            choicebox_nhacungcap.getItems().add(x.getTen());
        }
    }

    public int getIdSuppiler(String name) {
        int i = 0;
        for (Supplier x: supplierList) {
            if(x.getTen().equals(name)){
                i = x.getId();
            }
        }
        return i;
    }

    public boolean check_is_error_nl() {

        boolean hasError = false;

        hasError |= uf.isEmptyString(choicebox_nhacungcap.getValue()) ? uf.setErrorMsg(texterror_validator_nhacungcap, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_nhacungcap);

        hasError |= uf.isEmptyString(textfield_soluong.getText()) ? uf.setErrorMsg(texterror_validator_soluong, "Vui lòng điền thông tin") :
                (!uf.isNumericString(textfield_soluong.getText()) ? uf.setErrorMsg(texterror_validator_soluong, "Sai định dạng số") : uf.hideErrorMsg(texterror_validator_soluong));


        return hasError;
    }

    public Ingredient getIngredientFollowingEditClick(int id_in) {
        Ingredient z = new Ingredient();
        z = ingredientList.stream()
                .filter(x -> x.getId() == id_in)
                .findFirst()
                .orElse(null);

        return z;
    }

    public boolean check_is_exit(String tennl) {
        boolean isExit = ingredientReceivedNoteList.stream()
                .filter(material1 -> material1.getIngredientName().equals(tennl))
                .collect(Collectors.toList())
                .size() > 0;

        if (isExit) {
            uf.setErrorMsg(texterror_validator_tennl, "Nguyên liệu đã tồn tại");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uf.hideErrorMsg(texterror_validator_tennl);
                }
            }, 2000); // 3 giây

            return true;
        }

        return false;
    }


    public void clearChiTietNguyenLieu() {
        label_gia.setText("");
        label_don_vi.setText("");
        label_tongtien.setText("");
        textfield_soluong.clear();
        choicebox_nhacungcap.setValue(null);
        label_ten_nl.setText("");
    }

    void renderTablePhieuNguyenLieu() {

        table_phieunguyenlieu.getItems().clear();
        ObservableList<IngredientReceivedNote> o = FXCollections.observableArrayList();

        for (IngredientReceivedNote x : ingredientReceivedNoteList) {
            o.add(x);
        }

        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_tennl.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        tablecolumn_soluong.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tablecolumn_nhacungcap.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        table_phieunguyenlieu.setItems(o);
    }

    public void renderTotal() {
        int total = 0;
        for (IngredientReceivedNote ingredientReceivedNote : ingredientReceivedNoteList) {
            Ingredient ingredient = getIngredientFollowingEditClick(ingredientReceivedNote.getIngredientId());
            total += ingredient.getGiaNL() * ingredientReceivedNote.getQuantity();
        }
        text_total.setText(String.valueOf(total));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //RENDER DEFAULT
        textfield_xoa_nl.setDisable(true);
        uf.setVisibleNode(texterror_validator_nhacungcap, false);
        uf.setVisibleNode(texterror_validator_soluong, false);
        uf.setVisibleNode(texterror_validator_tennl, false);


        //RENDER ALL TEN NL
        ObservableList<Ingredient> nguyenLieu = FXCollections.observableArrayList();
        try {
            ingredientList = db.getAllIngredients();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Ingredient ingredient : ingredientList) {
            nguyenLieu.add(ingredient);
        }

        //-----------THEM NGUYEN LIEU TUONG UNG VOI MON
        tablecolumn_tenlistnl.setCellValueFactory(new PropertyValueFactory<>("ten"));
        tablecolumn_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        table_nguyenlieu.setItems(nguyenLieu);


        table_nguyenlieu.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra double click
                textfield_xoa_nl.setDisable(false);
                this.mode="ADD_NL";
                label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                clearChiTietNguyenLieu();

                Ingredient selectedMaterial = table_nguyenlieu.getSelectionModel().getSelectedItem();
                if (selectedMaterial != null) {
                    textfield_soluong.setText("1");
                    currentIngredientClick = selectedMaterial;

                    String tenNguyenLieu = selectedMaterial.getTen();
                    String donVi = selectedMaterial.getDonVi();
                    int gia = selectedMaterial.getGiaNL();

                    label_ten_nl.setText(tenNguyenLieu);
                    label_gia.setText(String.valueOf(gia));
                    label_don_vi.setText(donVi);

                    renderTienNguyenLieuTheoSoLuong(1, gia);
                    try {
                        renderChoiceBoxNCCFollowNLID(selectedMaterial.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        table_phieunguyenlieu.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                IngredientReceivedNote ingredientReceivedNote = table_phieunguyenlieu.getSelectionModel().getSelectedItem();
                if (ingredientReceivedNote != null) {
                    currentIngredientReceivedNote = ingredientReceivedNote;

                    Ingredient ingredient = getIngredientFollowingEditClick(currentIngredientReceivedNote.getIngredientId());


                    label_ten_nl.setText(ingredient.getTen());
                    label_gia.setText(String.valueOf(ingredient.getGiaNL()));
                    label_don_vi.setText(ingredient.getDonVi());
                    textfield_soluong.setText(String.valueOf(currentIngredientReceivedNote.getQuantity()));
                    choicebox_nhacungcap.setValue(ingredientReceivedNote.getSupplierName());
                    renderTienNguyenLieuTheoSoLuong(currentIngredientReceivedNote.getQuantity(), ingredient.getGiaNL());

                    this.mode = "EDIT_NL";
                    label_them_chinh_sua_nl.setText("Chỉnh sửa nguyên liệu");


                }
            }
        });


        //XU LY KHI AN THEM NGUYEN LIEU
        button_themnguyenlieu.setOnMouseClicked(e -> {
            if (this.mode.equals("ADD_NL")) {
                if (!check_is_error_nl()) {
                    if (check_is_exit(currentIngredientClick.getTen())) {
                        System.out.println("Da ton tai");
                        // xử lý đã tồn tại
                    } else {
                        this.currentSuppiler = supplierList.stream()
                                .filter(x -> x.getTen().equals(choicebox_nhacungcap.getValue()))
                                .findFirst()
                                .orElse(null);

                        ingredientReceivedNoteList.add(new IngredientReceivedNote(currentIngredientClick.getId(),
                                currentIngredientClick.getTen(), Integer.parseInt(textfield_soluong.getText()),
                                currentSuppiler.getId(), currentSuppiler.getTen(), 1, 2, LocalDate.now()));

                        renderTablePhieuNguyenLieu();
                        clearChiTietNguyenLieu();
                    }
                }
            } else if (this.mode.equals("EDIT_NL")) {

                System.out.println(currentIngredientReceivedNote.getIngredientName());

                ingredientReceivedNoteList.forEach(ingredient -> {
                    if (ingredient.getIngredientName().equals(currentIngredientReceivedNote.getIngredientName())) {
                        ingredient.setQuantity(Integer.parseInt(textfield_soluong.getText()));
                        int si = getIdSuppiler(choicebox_nhacungcap.getValue());
                        ingredient.setSupplierId(si);
                        ingredient.setSupplierName(choicebox_nhacungcap.getValue());
                    }
                });

                ingredientReceivedNoteList.forEach(x -> {
                    System.out.println(x.getIngredientName() + ": " + x.getQuantity());
                });

                //////////////////


                /////////////////

                this.mode = "ADD_NL";
                label_them_chinh_sua_nl.setText("Thêm nguyên liệu");
                renderTablePhieuNguyenLieu();
                clearChiTietNguyenLieu();
            }

            renderTotal();

        });

        textfield_soluong.setOnKeyPressed(event -> {

            AtomicReference<String> previousText = new AtomicReference<>(textfield_soluong.getText());

            PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
            pause.setOnFinished(events -> {
                if (!textfield_soluong.getText().equals(previousText) && !textfield_soluong.getText().isEmpty()) {
                    renderTienNguyenLieuTheoSoLuong(Integer.parseInt(textfield_soluong.getText()), Integer.parseInt(label_gia.getText()));

                }
            });

            textfield_soluong.textProperty().addListener((observable, oldValue, newValue) -> {
                previousText.set(newValue);
                pause.playFromStart();
            });
        });

        textfield_xoa_nl.setOnKeyPressed(event -> {
            int result = 0;
            if (event.getCode() == KeyCode.ENTER) {
                String nl_can_xoa = textfield_xoa_nl.getText();

                // Lọc allMaterial và loại bỏ các phần tử giống với filterString
                Iterator<IngredientReceivedNote> iterator = ingredientReceivedNoteList.iterator();
                while (iterator.hasNext()) {
                    IngredientReceivedNote material = iterator.next();
                    if (material.getIngredientName().equalsIgnoreCase(nl_can_xoa)) {
                        ++result;
                        iterator.remove();
                    }
                }
                if (result != 0) {
                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("đã xóa thành công");
                    renderTablePhieuNguyenLieu();
                    System.out.println("Enter key pressed");
                    renderTotal();

                } else {
                    textfield_xoa_nl.clear();
                    textfield_xoa_nl.setPromptText("Lỗi");

                }
            }
        });
        button_hoantat.setOnMouseClicked(e->{
            for(int i=0;i<table_phieunguyenlieu.getItems().size();i++){
                try {
                    int id_nl= db.find_idNl(table_phieunguyenlieu.getItems().get(i).getIngredientName());
                    int id_ql=db.find_manager_id2(LoginController.id_nv);
                    int id_bep=LoginController.id_nv;
                    int soluong=table_phieunguyenlieu.getItems().get(i).getQuantity();

                    int id_ncc=db.findIdncc(table_phieunguyenlieu.getItems().get(i).getSupplierName());
                    int tongtien= Integer.parseInt(text_total.getText());
                    db.InsRequest(0,id_nl,id_ncc,id_ql,id_bep,tongtien,null,soluong);
                    table_phieunguyenlieu.getItems().clear();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thông báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Đã yêu cầu nhập thành công, đợi duyệt từ quản lý");
                    alert.showAndWait();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
}