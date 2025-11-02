module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example to javafx.fxml;
    exports com.example;
}