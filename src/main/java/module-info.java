module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens org.example to javafx.fxml;
    exports org.example;
}