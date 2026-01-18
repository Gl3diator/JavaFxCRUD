module com.esprit.studentcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.sql;
    requires org.mariadb.jdbc;
    // for whatsapp api
    requires java.net.http;


    // JavaFX controllers
    opens com.esprit.studentcrud.controller to javafx.fxml;

    // Export main app + controllers
    exports com.esprit.studentcrud;
    exports com.esprit.studentcrud.controller;
}
