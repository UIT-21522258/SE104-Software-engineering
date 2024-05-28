package com.fast_food.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.LogedInUser;
import utils.DBHandler;
import utils.UtilityFunctions;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private HBox button_hoantat;

    @FXML
    private Label label_mode;

    @FXML
    private Text texterror_validator_mkcu;

    @FXML
    private Text texterror_validator_mkmoi;

    @FXML
    private Text texterror_validator_tentk;

    @FXML
    private Text texterror_validator_xnmk;

    @FXML
    private PasswordField textfield_mkcu;

    @FXML
    private PasswordField textfield_mkmoi;

    @FXML
    private TextField textfield_tentk;

    @FXML
    private PasswordField textfield_xnmk;

    //DUNG QUAN TAM TOI NO




    DBHandler db = new DBHandler();
    UtilityFunctions uf = new UtilityFunctions();

    public boolean check_is_error() {

        boolean hasError = false;

        hasError |= uf.isEmptyString(textfield_mkcu.getText()) ? uf.setErrorMsg(texterror_validator_mkcu, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_mkcu);
        hasError |= uf.isEmptyString(textfield_tentk.getText()) ? uf.setErrorMsg(texterror_validator_tentk, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_tentk);
        hasError |= uf.isEmptyString(textfield_mkmoi.getText()) ? uf.setErrorMsg(texterror_validator_mkmoi, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_mkmoi);
        hasError |= uf.isEmptyString(textfield_xnmk.getText()) ? uf.setErrorMsg(texterror_validator_xnmk, "Vui lòng điền thông tin") : uf.hideErrorMsg(texterror_validator_xnmk);


        return hasError;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uf.setVisibleNode(texterror_validator_mkcu, false);
        uf.setVisibleNode(texterror_validator_tentk, false);
        uf.setVisibleNode(texterror_validator_mkmoi, false);
        uf.setVisibleNode(texterror_validator_xnmk, false);




        button_hoantat.setOnMouseClicked(event -> {
            if (!check_is_error()) {
                String username = textfield_tentk.getText();
                String password = textfield_mkcu.getText();
                try {
                    LogedInUser userLogin = db.logIn(username, password);
                    if (userLogin == null) {
                        uf.setErrorMsg(texterror_validator_mkcu, "Sai thông tin đăng nhập");
                        uf.setErrorMsg(texterror_validator_tentk, "Sai thông tin đăng nhập");

                    } else {
                        uf.hideErrorMsg(texterror_validator_mkcu);
                        uf.hideErrorMsg(texterror_validator_tentk);


                        // continue
                        String newPassword = textfield_mkmoi.getText();
                        String confirmPassword = textfield_xnmk.getText();
                        if (newPassword.equals(confirmPassword)) {
                            uf.hideErrorMsg(texterror_validator_xnmk);
                            //DB
                            //
                            //
                            //
                            //
                            //
                            //
                            //
                            db.UpdatePassword(username, newPassword);
                            System.out.println("SUCCESS");

                        } else {
                            uf.setErrorMsg(texterror_validator_xnmk, "Mật khẩu xác nhận không trùng khớp");
                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
