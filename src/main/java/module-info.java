module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
	requires javafx.graphics;
	requires java.desktop;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
	requires javafx.base;

    opens org.example to javafx.fxml;
    exports org.example;
}