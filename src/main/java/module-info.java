module org.example {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires java.persistence;
	requires javafx.base;
	requires net.bytebuddy;
    requires rt;
    requires java.mail;

    opens org.example to javafx.fxml;
    exports org.example;
}
