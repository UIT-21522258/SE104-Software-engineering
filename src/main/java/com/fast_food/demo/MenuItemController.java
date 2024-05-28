package com.fast_food.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.MenuItem;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
public class MenuItemController implements Initializable {

    @FXML
    private Label description;

    @FXML
    private ImageView image;

    @FXML
    private Text name;

    @FXML
    private Text price;


    private model.MenuItem menuItem;

    public void setData(MenuItem menuItem) {
        this.menuItem = menuItem;
        this.updateDisplay();
    }

    public void updateDisplay() {
        this.name.setText(menuItem.getName());
        this.description.setText(menuItem.getDescription());
        this.price.setText("$" + menuItem.getPrice());
        this.image.setImage(new Image(new ByteArrayInputStream(menuItem.getImage())));
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle clip = new Rectangle(image.getFitWidth(), image.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        image.setClip(clip);
    }



}
