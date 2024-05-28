package com.fast_food.demo;

import javafx.scene.layout.HBox;

public interface Callbacks {
    void updateCounter(String ten_mon, int count);
    void deleteItem(String ten_mon, HBox box);
}
