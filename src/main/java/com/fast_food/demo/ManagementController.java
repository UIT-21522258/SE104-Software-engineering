package com.fast_food.demo;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Account;
import model.LogedInUser;
import utils.UtilityFunctions;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController implements Initializable {

    private LogedInUser userlogin;


    @FXML
    private VBox changepassword;

    @FXML
    private VBox control_menu;

    @FXML
    private HBox control_menu1;

    @FXML
    private VBox dashboard;

    @FXML
    private VBox employees;

    @FXML
    private VBox ingredient;

    @FXML
    private VBox ingredient1;

    @FXML
    private VBox ingredient11;

    @FXML
    private VBox menu;

    @FXML
    private VBox orders;

    @FXML
    private VBox scene_root;

    @FXML
    private VBox supplier;

    @FXML
    private Text text_name;

    @FXML
    private VBox voucher;

    @FXML
    private VBox button_logout;

    @FXML
    private VBox button_backtoquanly;


    public LogedInUser getUserlogin() {
        return userlogin;
    }


    public void setUserlogin(LogedInUser userlogin) {
        this.userlogin = userlogin;
        text_name.setText(userlogin.getCurentAcc().getName());
        renderLayout();

    }

    public void renderLayout() {

        Account currentAccout = userlogin.getCurentAcc();

        UtilityFunctions uf = new UtilityFunctions();
        uf.setVisibleNode(orders, true);

        uf.setVisibleNode(menu, true);
        uf.setVisibleNode(employees, true);
        uf.setVisibleNode(voucher, true);
        uf.setVisibleNode(supplier, true);
        uf.setVisibleNode(ingredient11, true);

        uf.setVisibleNode(ingredient, true);


        uf.setVisibleNode(ingredient1, true);



        //phan quyen
        if (currentAccout.getps().equals("Quản lý")) {
            uf.setVisibleNode(orders, false);
            uf.setVisibleNode(ingredient, false);
            uf.setVisibleNode(ingredient1, false);
        } else if (currentAccout.getps().equals("Đầu bếp")) {
            uf.setVisibleNode(menu, false);
            uf.setVisibleNode(employees, false);
            uf.setVisibleNode(voucher, false);
            uf.setVisibleNode(supplier, false);
            uf.setVisibleNode(ingredient11, false);
        } else if (currentAccout.getps().equals("Thu ngân")) {
            uf.setVisibleNode(supplier, false);
            uf.setVisibleNode(ingredient, false);

            uf.setVisibleNode(employees, false);
            uf.setVisibleNode(voucher, false);
            uf.setVisibleNode(ingredient1, false);
            uf.setVisibleNode(ingredient11, false);
            uf.setVisibleNode(menu, false);
        }
    }

    private void showLogoutConfirmation(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Bạn có chắc muốn đăng xuất?");
        alert.setContentText("Nhấn OK để đăng xuất.");

        ButtonType buttonTypeOK = new ButtonType("OK");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOK) {
                showLogoutAlert();
                primaryStage.close();
            }
        });
    }

    UtilityFunctions uf = new UtilityFunctions();

    private void showLogoutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Bạn đã đăng xuất thành công!");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uf.setVisibleNode(button_backtoquanly, false);

        button_logout.setOnMouseClicked(e -> {
            uf.setVisibleNode(button_backtoquanly, true);

            Stage stage = (Stage) button_logout.getScene().getWindow();
            showLogoutConfirmation(stage);



        });



        employees.setOnMouseClicked(e -> {
            control_menu.setManaged(false);
            control_menu.setVisible(false);
            uf.setVisibleNode(button_backtoquanly, true);


            FXMLLoader employee_management = new FXMLLoader();
            employee_management.setLocation(getClass().getResource("/com/fast_food/demo/EmployeesManagement.fxml"));

            try {
                scene_root.getStylesheets().add("/css/managementStyle.css");
                scene_root.getChildren().add(employee_management.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        voucher.setOnMouseClicked(e -> {
            control_menu.setManaged(false);
            control_menu.setVisible(false);
            uf.setVisibleNode(button_backtoquanly, true);


            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource("/com/fast_food/demo/VoucherManagement.fxml"));

            try {
                scene_root.getChildren().add(fxml.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        supplier.setOnMouseClicked(e -> {
            control_menu.setManaged(false);
            control_menu.setVisible(false);
            uf.setVisibleNode(button_backtoquanly, true);


            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource("/com/fast_food/demo/SuppilerManagement.fxml"));

            try {
                scene_root.getStylesheets().add("/css/managementStyle.css");
                scene_root.getChildren().add(fxml.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        dashboard.setOnMouseClicked(e -> {
            //open new UI


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/fast_food/demo/Dashboard.fxml"));

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

        ingredient.setOnMouseClicked(e -> {
            control_menu.setManaged(false);
            control_menu.setVisible(false);
            uf.setVisibleNode(button_backtoquanly, true);


            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource("/com/fast_food/demo/IngredientManagement.fxml"));


            try {
                scene_root.getChildren().add(fxml.load());
                IngredientManagementController IMC = fxml.getController();
                IMC.setCurrentUser(userlogin);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        orders.setOnMouseClicked(e -> {
            control_menu.setManaged(false);
            control_menu.setVisible(false);
            uf.setVisibleNode(button_backtoquanly, true);


            FXMLLoader employee_management = new FXMLLoader();
            if (userlogin.getCurentAcc().getps().equals("Thu ngân")) {
                employee_management.setLocation(getClass().getResource("/com/fast_food/demo/OrdersManagement.fxml"));
            } else {
                employee_management.setLocation(getClass().getResource("/com/fast_food/demo/OrdersManagementForChef.fxml"));
            }

            try {
                scene_root.getChildren().add(employee_management.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        menu.setOnMouseClicked(e -> {
            uf.setVisibleNode(button_backtoquanly, true);

            control_menu.setManaged(false);
            control_menu.setVisible(false);
            FXMLLoader employee_management = new FXMLLoader();
            employee_management.setLocation(getClass().getResource("/com/fast_food/demo/MenuManagement.fxml"));

            try {
                scene_root.getChildren().add(employee_management.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ingredient1.setOnMouseClicked(e -> {
            uf.setVisibleNode(button_backtoquanly, true);

            control_menu.setManaged(false);
            control_menu.setVisible(false);
            FXMLLoader employee_management = new FXMLLoader();
            employee_management.setLocation(getClass().getResource("/com/fast_food/demo/RequestForIngredient.fxml"));

            try {
                scene_root.getChildren().add(employee_management.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ingredient11.setOnMouseClicked(e -> {
            uf.setVisibleNode(button_backtoquanly, true);

            control_menu.setManaged(false);
            control_menu.setVisible(false);
            FXMLLoader employee_management = new FXMLLoader();
            employee_management.setLocation(getClass().getResource("/com/fast_food/demo/Admin.fxml"));

            try {
                scene_root.getChildren().add(employee_management.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        button_backtoquanly.setOnMouseClicked(e -> {
            scene_root.getChildren().remove(1);
            control_menu.setManaged(true);
            control_menu.setVisible(true);
           renderLayout();
        });

        changepassword.setOnMouseClicked(e -> {

            control_menu.setManaged(false);
            control_menu.setVisible(false);
            FXMLLoader employee_management = new FXMLLoader();
            employee_management.setLocation(getClass().getResource("/com/fast_food/demo/ChangePassword.fxml"));

            try {

                scene_root.getChildren().add(employee_management.load());
                Stage stage = (Stage) scene_root.getScene().getWindow();
                stage.setWidth(800);
                stage.setHeight(600);
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2;
                double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2;
                stage.setX(centerX);
                stage.setY(centerY);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}
