package com.example;

import javafx.scene.image.Image;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    Image noSmash;

    public HelloModel() {
        noSmash = new Image(getClass().getResource("/lantern.png").toExternalForm());
    }

    public Image getNoSmash() {
        return noSmash;
    }
}
