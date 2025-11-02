package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final StringProperty dateTimeProperty;

    public HelloModel() {
        dateTimeProperty = new SimpleStringProperty();
    }

    public void setDateTime(String dateTime) {
        dateTimeProperty.set(dateTime);
    }

    public String getDateTime() {
        return dateTimeProperty.get();
    }

    public StringProperty dateTimeProperty() {
        return dateTimeProperty;
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }
}
