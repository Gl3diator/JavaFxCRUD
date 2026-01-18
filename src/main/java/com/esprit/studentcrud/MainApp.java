package com.esprit.studentcrud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/esprit/studentcrud/students-view.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Student CRUD");
        stage.setScene(scene);
        stage.show();
    }
}
