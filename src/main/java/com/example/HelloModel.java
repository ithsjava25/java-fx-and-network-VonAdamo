package com.example;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final Image noSmash;
    private final Image smash1;
    private final Image smash2;
    private final Image smash3;
    private final Image smash4;

    private final ObjectProperty<Image> first;
    private final ObjectProperty<Image> second;
    private final ObjectProperty<Image> third;

    private IntegerProperty points;


    public HelloModel() {
        noSmash = new Image(getClass().getResource("/lantern.png").toExternalForm());
        smash1 = new Image(getClass().getResource("/lantern1.png").toExternalForm());
        smash2 = new Image(getClass().getResource("/lantern2.png").toExternalForm());
        smash3 = new Image(getClass().getResource("/lantern3.png").toExternalForm());
        smash4 = new Image(getClass().getResource("/lantern4.png").toExternalForm());

        first = new SimpleObjectProperty<>(noSmash);
        second = new SimpleObjectProperty<>(noSmash);
        third = new SimpleObjectProperty<>(noSmash);

        points = new SimpleIntegerProperty(0);
    }

    public Image getFirst() {
        return first.get();
    }

    public ObjectProperty<Image> firstProperty() {
        return first;
    }

    public void setFirst(Image first) {
        this.first.set(first);
    }

    public Image getSecond() {
        return second.get();
    }

    public ObjectProperty<Image> secondProperty() {
        return second;
    }

    public void setSecond(Image second) {
        this.second.set(second);
    }

    public Image getThird() {
        return third.get();
    }

    public ObjectProperty<Image> thirdProperty() {
        return third;
    }

    public void setThird(Image third) {
        this.third.set(third);
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    public void smash(int lantern) {
        if (lantern == 0 && getFirst() != noSmash) {
            setPoints(getPoints() + 1);
            setFirst(noSmash);
        } else if (lantern == 1 && getSecond() != noSmash) {
            setPoints(getPoints() + 1);
            setSecond(noSmash);
        } else if (lantern == 2 && getThird() != noSmash) {
            setPoints(getPoints() + 1);
            setThird(noSmash);
        }
    }

    public void changeImage(int lantern, int imageId) {
        switch(lantern) {
            case 0 -> setFirst(getImage(imageId));
            case 1 -> setSecond(getImage(imageId));
            case 2 -> setThird(getImage(imageId));
        }
    }

    private Image getImage(int imageId) {
        return switch(imageId) {
            case 0 -> smash1;
            case 1 -> smash2;
            case 2 -> smash3;
            case 3 -> smash4;
            default -> noSmash;
        };
    }
}
