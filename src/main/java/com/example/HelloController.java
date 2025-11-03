package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel();

    @FXML
    private Label messageLabel;
    Timeline timeline;

    @FXML
    private void initialize() {
        messageLabel.textProperty().bind(
                Bindings.concat(model.pointsProperty().asString(), " points.")
        );
        startNextEvent();

    }

    private void startNextEvent() {
        new Timeline(
                new KeyFrame(Duration.millis(3000 * Math.random()), event -> {
                        model.changeImage(
                                (int) (3 * Math.random()),
                                (int) (4 * Math.random()));
                        startNextEvent();
                }
                )).play();
    }

    public HelloModel getModel() {
        return model;
    }

    public void image1 (MouseEvent mouseEvent) {
        model.smash(0);
    }

    public void image2 (MouseEvent mouseEvent) {
        model.smash(1);
    }

    public void image3 (MouseEvent mouseEvent) {
        model.smash(2);
    }
}
