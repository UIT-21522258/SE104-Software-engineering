package utils;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityFunctions {
    public boolean isPhoneNumber(String input) {
        return input.matches("^0[0-9]{9}$|^\\+84[0-9]{9}$|^\\(\\+84\\)\\s?[0-9]{9}$");
    }

    public boolean isEmptyString(String input) {
        return input == null || input.isEmpty();
    }

    public boolean isNumericString(String input) {
        if (input == null || input.isEmpty()) {
            return false; // Input is null or empty
        }
        return input.matches("^[0-9]+$");
    }

    public  boolean isValidEmail(String email) {

        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void setVisibleNode(Node node, boolean is_visible) {
        node.setVisible(is_visible);
        node.setManaged(is_visible);

    }

    public void addColumnHeaderIcon(TableColumn<?, ?> column, String imagePath, String columnHeader) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        column.setGraphic(icon);
        column.setText(columnHeader);
    }

    public boolean setErrorMsg(Text errorLabel, String errorMsg) {
        setVisibleNode(errorLabel, true);
        errorLabel.setText(errorMsg);
        return true;
    }

    public boolean hideErrorMsg(Text errorLabel) {
        setVisibleNode(errorLabel, false);
        return false;
    }


}
