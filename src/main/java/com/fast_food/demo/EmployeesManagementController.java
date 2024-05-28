package com.fast_food.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Employee;
import utils.DBHandler;
import utils.UtilityFunctions;


import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class EmployeesManagementController implements Initializable {

    @FXML
    private TableView<Employee> employee_list;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> ten;

    @FXML
    private TableColumn<?, ?> chuc_vu;

    @FXML
    private TableColumn<?, ?> nguoi_quan_ly;

    @FXML
    private TableColumn<?, ?> sdt;

    @FXML
    private TableColumn<?, ?> luong;

    @FXML
    private Label text_tong_nv;

    @FXML
    private TextField textfield_tim_kiem_nv;

    @FXML
    private HBox button_submit;

    @FXML
    private HBox button_them_nv;

    @FXML
    private Label text_csua_them;

    @FXML
    private TextField textfiel_ho_va_ten;

    @FXML
    private Text text_ng_quan_ly;

    @FXML
    private TextField textfiel_luong;


    @FXML
    private TextField textfield_sdt;

    @FXML
    private ChoiceBox<String> choicebox_chuc_vu;

    @FXML
    private ChoiceBox<String> choicebox_nguoi_quan_ly;

    @FXML
    private HBox button_delete;

    @FXML
    private Text text_error_validator_chuc_vu;

    @FXML
    private Text text_error_validator_luong;

    @FXML
    private Text t_chuc_vu;

    @FXML
    private Text text_error_validator_name;

    @FXML
    private Text text_error_validator_nguoi_ql;

    @FXML
    private Text text_error_validator_sdt;

    @FXML
    private ChoiceBox<String> choicebox_searchtype;
    private int CountSelect = 0;

    public String searchStyle = "ID";

    public Employee currentEmpClick = new Employee();


    public String rule_button_submit;

    public void clearDetail() {
        textfield_sdt.clear();
        textfiel_luong.clear();
        textfiel_ho_va_ten.clear();
        choicebox_chuc_vu.setValue(null);
        choicebox_nguoi_quan_ly.setValue(null);
    }


    UtilityFunctions uf = new UtilityFunctions();
    DBHandler db = new DBHandler();

    Employee selectEm = new Employee();

    HashSet<Employee> employeeHashSet = new HashSet<Employee>();


    public EmployeesManagementController() {

    }


    public boolean check_is_error() {

        boolean hasError = false;

        hasError |= uf.isEmptyString(textfiel_ho_va_ten.getText()) ? uf.setErrorMsg(text_error_validator_name, "Vui lòng điền thông tin") : uf.hideErrorMsg(text_error_validator_name);
        hasError |= uf.isEmptyString(textfield_sdt.getText()) ? uf.setErrorMsg(text_error_validator_sdt, "Vui lòng điền thông tin") :
                (!uf.isPhoneNumber(textfield_sdt.getText()) ? uf.setErrorMsg(text_error_validator_sdt, "Sai định dạng số điện thoại") : uf.hideErrorMsg(text_error_validator_sdt));
        hasError |= uf.isEmptyString(textfiel_luong.getText()) ? uf.setErrorMsg(text_error_validator_luong, "Vui lòng điền thông tin") :
                (!uf.isNumericString(textfiel_luong.getText()) ? uf.setErrorMsg(text_error_validator_luong, "Sai định dạng lương") : uf.hideErrorMsg(text_error_validator_luong));
        hasError |= uf.isEmptyString(choicebox_chuc_vu.getValue()) ? uf.setErrorMsg(text_error_validator_chuc_vu, "Vui lòng điền thông tin") : uf.hideErrorMsg(text_error_validator_chuc_vu);
        if (CountSelect != 0) {
            if (!selectEm.getChuc_vu().equals("Quản lý")) {
                hasError |= uf.isEmptyString(choicebox_nguoi_quan_ly.getValue()) ? uf.setErrorMsg(text_error_validator_nguoi_ql, "Vui lòng điền thông tin") : uf.hideErrorMsg(text_error_validator_nguoi_ql);
            }
        }
        return hasError;
    }

    private String generateRandomString() {
        String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int length = random.nextInt(2) + 4; // Random length between 4 and 5

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARS.length());
            char randomChar = ALLOWED_CHARS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    HashSet<Employee> ds_nguoi_quan_ly;

    public void renderTableEmployee() {

        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String ten_ng_qly = choicebox_nguoi_quan_ly.getValue();
        choicebox_nguoi_quan_ly.getItems().clear();
        employee_list.getItems().clear();
        try {
            HashSet<Employee> em = db.getAllEmployees();
            for (Employee emp : em) {
                employeeList.add(emp);
                if ("Quản lý".equals(emp.getChuc_vu())) {
                    choicebox_nguoi_quan_ly.getItems().add(emp.getTen());
                }
            }
            employee_list.setItems(employeeList);
            text_tong_nv.setText(String.valueOf(em.size()));

        } catch (SQLException emp) {
            throw new RuntimeException(emp);
        }
        if (choicebox_chuc_vu.getValue().equals("Quản lý")) {
            uf.setVisibleNode(text_error_validator_nguoi_ql, false);
        }
        choicebox_nguoi_quan_ly.setValue(ten_ng_qly);
    }

    public void renderTableEmployeeFollowingName(String name) throws SQLException {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String ten_ng_qly = choicebox_nguoi_quan_ly.getValue();
        choicebox_nguoi_quan_ly.getItems().clear();
        employee_list.getItems().clear();
        HashSet<Employee> em = db.getAllEmployeesFollowingName(name);
        for (Employee emp : em) {
            employeeList.add(emp);
            if ("Quản lý".equals(emp.getChuc_vu())) {
                choicebox_nguoi_quan_ly.getItems().add(emp.getTen());
            }
        }

        for (Employee emp : employeeHashSet) {
            if ("Quản lý".equals(emp.getChuc_vu())) {
                choicebox_nguoi_quan_ly.getItems().add(emp.getTen());
            }
        }
        employee_list.setItems(employeeList);
        text_tong_nv.setText(String.valueOf(em.size()));
//
//        if (choicebox_chuc_vu.getValue().equals("Quản lý")) {
//            uf.setVisibleNode(text_error_validator_nguoi_ql, false);
//        }
//        choicebox_nguoi_quan_ly.setValue(ten_ng_qly);
    }

    public void renderTableEmployeeFollowingId(int id) throws SQLException {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        String ten_ng_qly = choicebox_nguoi_quan_ly.getValue();
        choicebox_nguoi_quan_ly.getItems().clear();
        employee_list.getItems().clear();
        HashSet<Employee> em = db.getAllEmployeesFollowingId(id);
        for (Employee emp : em) {
            employeeList.add(emp);
            if ("Quản lý".equals(emp.getChuc_vu())) {
                choicebox_nguoi_quan_ly.getItems().add(emp.getTen());
            }
        }

        for (Employee emp : employeeHashSet) {
            if ("Quản lý".equals(emp.getChuc_vu())) {
                choicebox_nguoi_quan_ly.getItems().add(emp.getTen());
            }
        }
        employee_list.setItems(employeeList);
        text_tong_nv.setText(String.valueOf(em.size()));
//
//        if (choicebox_chuc_vu.getValue().equals("Quản lý")) {
//            uf.setVisibleNode(text_error_validator_nguoi_ql, false);
//        }
//        choicebox_nguoi_quan_ly.setValue(ten_ng_qly);
    }

    public void clearError() {
        uf.setVisibleNode(text_error_validator_luong, false);
        uf.setVisibleNode(text_error_validator_name, false);
        uf.setVisibleNode(text_error_validator_chuc_vu, false);
        uf.setVisibleNode(text_error_validator_nguoi_ql, false);
        uf.setVisibleNode(text_error_validator_sdt, false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DBHandler db = new DBHandler();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList();
        UtilityFunctions uf = new UtilityFunctions();

        choicebox_chuc_vu.getItems().addAll("Đầu bếp", "Quản lý", "Thu ngân");
        uf.setVisibleNode(text_error_validator_luong, false);
        uf.setVisibleNode(text_error_validator_name, false);
        uf.setVisibleNode(text_error_validator_chuc_vu, false);
        uf.setVisibleNode(text_error_validator_nguoi_ql, false);
        uf.setVisibleNode(text_error_validator_sdt, false);

        choicebox_searchtype.getItems().add("ID");
        choicebox_searchtype.getItems().add("TÊN");
        choicebox_searchtype.setValue("ID");

        button_submit.setDisable(true);


        try {
            HashSet<Employee> em = db.getAllEmployees();
            for (Employee e : em) {
                employeeList.add(e);
                employeeHashSet.add(e);
                if ("Quản lý".equals(e.getChuc_vu())) {
                    choicebox_nguoi_quan_ly.getItems().add(e.getTen());
                }
            }


            text_tong_nv.setText(String.valueOf(em.size()));


            luong.setCellValueFactory(new PropertyValueFactory<>("luong"));
            ten.setCellValueFactory(new PropertyValueFactory<>("ten"));
            chuc_vu.setCellValueFactory(new PropertyValueFactory<>("chuc_vu"));
            sdt.setCellValueFactory(new PropertyValueFactory<>("sdt"));
            nguoi_quan_ly.setCellValueFactory(new PropertyValueFactory<>("nguoi_quan_ly"));


            uf.addColumnHeaderIcon(ten, "/images/text-dynamic-gradient.png", "Tên");
            uf.addColumnHeaderIcon(luong, "/images/money-dynamic-gradient.png", "Lương");
            uf.addColumnHeaderIcon(chuc_vu, "/images/star-dynamic-gradient.png", "Chức vụ");
            uf.addColumnHeaderIcon(sdt, "/images/phone-only-dynamic-gradient.png", "SĐT");
            uf.addColumnHeaderIcon(nguoi_quan_ly, "/images/flag-dynamic-gradient.png", "Người quản lý");


            employee_list.setItems(employeeList);
            text_tong_nv.setText(String.valueOf(em.size()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        button_them_nv.setOnMouseClicked(e -> {
            uf.setVisibleNode(text_error_validator_nguoi_ql, false);
            textfiel_ho_va_ten.setEditable(true);
            rule_button_submit = "add";
            text_csua_them.setText("Thêm nhân viên");
            textfield_sdt.clear();
            textfiel_luong.clear();
            textfiel_ho_va_ten.clear();

            uf.setVisibleNode(t_chuc_vu, true);
            uf.setVisibleNode(choicebox_chuc_vu, true);


            choicebox_chuc_vu.setValue(null);

            choicebox_chuc_vu.setOnAction(event -> {
                String selectedChucVu = choicebox_chuc_vu.getValue();
                if (selectedChucVu != null && selectedChucVu.equals("Quản lý")) {
                    uf.setVisibleNode(choicebox_nguoi_quan_ly, false);
                    uf.setVisibleNode(text_ng_quan_ly, false);
                } else {
                    uf.setVisibleNode(text_ng_quan_ly, true);
                    uf.setVisibleNode(choicebox_nguoi_quan_ly, true);
                }
            });

            choicebox_nguoi_quan_ly.setValue(null);
            button_submit.setDisable(false);

            uf.setVisibleNode(text_ng_quan_ly, true);


            uf.setVisibleNode(choicebox_nguoi_quan_ly, true);


        });

        button_submit.setOnMouseClicked(e -> {
            DBHandler DB = new DBHandler();
            if (!check_is_error() && rule_button_submit.equals("add")) {
                if (!choicebox_chuc_vu.getValue().equals("Quản lý") && choicebox_nguoi_quan_ly.getValue().isEmpty()) {
                } else {
                    if (choicebox_nguoi_quan_ly.getValue() == "Quản lý") {
                    }
                    int manager_id = 0;
                    try {
                        manager_id = DB.find_manager_id(choicebox_nguoi_quan_ly.getValue());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    String passwd = generateRandomString();
                    try {
                        DB.InsertEmp(textfiel_ho_va_ten.getText(), textfield_sdt.getText(), choicebox_chuc_vu.getValue(), Integer.parseInt(textfiel_luong.getText()), manager_id,passwd);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("Thêm nhân viên");
                    renderTableEmployee();
                    textfield_tim_kiem_nv.clear();
                }
            }
            if (!check_is_error() && rule_button_submit.equals("edit")) {


                // sửa những thay đổi
                try {
                    DB.UpdateEmp(textfiel_ho_va_ten.getText(), textfield_sdt.getText(), Integer.parseInt(textfiel_luong.getText()), choicebox_nguoi_quan_ly.getValue());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Sửa nhân viên");
                renderTableEmployee();
                clearDetail();


            }
        });

        button_delete.setOnMouseClicked(e -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác nhận xóa");
            confirmationAlert.setHeaderText("Bạn có chắc chắn muốn xóa nhân viên này?");
            confirmationAlert.setContentText("Hành động này không thể hoàn tác.");

            confirmationAlert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        db.DeleteEmp(currentEmpClick.getId());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    renderTableEmployee();
                    System.out.println("Nhân viên đã được xóa.");
                }
            });

        });

        employee_list.setOnMouseClicked(e -> {
            uf.setVisibleNode(text_error_validator_nguoi_ql, false);
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                clearError();
                CountSelect = 1;
                Employee employee = employee_list.getSelectionModel().getSelectedItem();
                this.currentEmpClick = employee;
                selectEm = employee;
                if (employee != null) {
                    textfiel_ho_va_ten.setEditable(false);
                    button_submit.setDisable(false);
                    rule_button_submit = "edit";
                    text_csua_them.setText("Chỉnh sửa nhân viên");
                    textfiel_ho_va_ten.setText(employee.getTen());
                    textfiel_luong.setText(String.valueOf(employee.getLuong()));
                    textfield_sdt.setText(employee.getSdt());

                    uf.setVisibleNode(text_ng_quan_ly, true);

                    uf.setVisibleNode(choicebox_chuc_vu, false);
                    uf.setVisibleNode(t_chuc_vu, false);
                    if (employee.getChuc_vu().equals("Quản lý")) {
                        uf.setVisibleNode(choicebox_nguoi_quan_ly, false);
                        uf.setVisibleNode(text_ng_quan_ly, false);


                    } else {

                        choicebox_nguoi_quan_ly.setValue(employee.getNguoi_quan_ly());
                        uf.setVisibleNode(choicebox_nguoi_quan_ly, true);
                    }


                    choicebox_chuc_vu.setValue(employee.getChuc_vu());
                }
            }

        });

        textfield_tim_kiem_nv.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (choicebox_searchtype.getValue().equals("ID")) {
                    int enterID = Integer.parseInt(textfield_tim_kiem_nv.getText());
                    System.out.println("Entered text: " + enterID);
                    try {
                        //
                        textfiel_luong.clear();
                        textfiel_ho_va_ten.clear();
                        choicebox_chuc_vu.setValue(null);
                        choicebox_nguoi_quan_ly.setValue(null);
                        textfield_sdt.clear();
                        //
                        renderTableEmployeeFollowingId(enterID);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {

                    String enteredText = textfield_tim_kiem_nv.getText();
                    System.out.println("Entered text: " + enteredText);
                    try {
                        //
                        textfiel_luong.clear();
                        textfiel_ho_va_ten.clear();
                        choicebox_chuc_vu.setValue(null);
                        choicebox_nguoi_quan_ly.setValue(null);
                        textfield_sdt.clear();
                        //
                        renderTableEmployeeFollowingName(enteredText);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });


    }
}
