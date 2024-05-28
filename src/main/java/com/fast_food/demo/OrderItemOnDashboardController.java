package com.fast_food.demo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Order;
import javafx.beans.Observable;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderItemOnDashboardController implements Initializable {

    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }


    @FXML
    private Text count;

    @FXML
    public Pane decrease;

    @FXML
    private ImageView image;

    @FXML
    public Pane increase;

    @FXML
    public Label name;

    @FXML
    private Text price;

    @FXML
    private HBox bt_delete;

    @FXML
    private HBox wrapper;


    public int count_number = 1;

    public String name_;
    public int price_;
    public byte[] image_;


    public void updateDisplay(String name_, int price_, byte[] image_, int count_) {
        this.name.setText(name_);
        this.count.setText(String.valueOf(count_));
        this.count_number = count_;
        this.price.setText("$" + price_);
        this.image.setImage(new Image(new ByteArrayInputStream(image_)));
        this.name_ = name_;
        this.price_ = price_;
        this.image_ = image_;
    }

    public void onIncreaseClick() {
        count_number = count_number + 1;
        callbacks.updateCounter(name_, count_number);
        updateDisplay(this.name_, this.price_, this.image_, count_number);

    }

    public void onDecreaseClick() {
        if (count_number >=2 ) {
            count_number = count_number - 1;
            callbacks.updateCounter(name_, count_number);
            updateDisplay(this.name_, this.price_, this.image_, count_number);
        }
    }

    public void onDeleteClick() {
        callbacks.deleteItem(this.name_, wrapper);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rectangle clip = new Rectangle(image.getFitWidth(), image.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        image.setClip(clip);
        decrease.setOnMouseClicked(e -> {
            this.onDecreaseClick();
        });
        increase.setOnMouseClicked(e -> {
            this.onIncreaseClick();
        });
        bt_delete.setOnMouseClicked(e -> {
            this.onDeleteClick();
        });
    }


}
