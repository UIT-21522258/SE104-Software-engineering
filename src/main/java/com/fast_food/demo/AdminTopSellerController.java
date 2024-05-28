package com.fast_food.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminTopSellerController implements Initializable {

    @FXML
    private Label count;

    @FXML
    private ImageView image;

    @FXML
    private Label name;

    public void setData(String name_, String count_, byte[] image_) {
        count.setText("SL: " + count_);
        name.setText(name_);
        image.setImage(new Image(new ByteArrayInputStream(image_)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle clip = new Rectangle(image.getFitWidth(), image.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        image.setClip(clip);
    }
}
