package com.example.lost_and_found;

import javafx.scene.control.Button;

public class selectionUIHelper {
    public static void setSelected(Button selected, Button[] buttons) {
        for (Button b : buttons) {
            b.getStyleClass().remove("categoryBarSelect");
        }
        selected.getStyleClass().add("categoryBarSelect");
    }
}
