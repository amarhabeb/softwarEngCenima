module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
	requires javafx.graphics;
	requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
}