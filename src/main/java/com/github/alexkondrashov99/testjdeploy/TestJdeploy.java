package com.github.alexkondrashov99.testjdeploy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class TestJdeploy extends Application {

    @Override
    public void start(Stage stage) {
        try {
            var fxmlLoader = new FXMLLoader(TestJdeploy.class.getResource("/hello-view.fxml"));
            var scene = new Scene(fxmlLoader.load(), 320.0, 240.0);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();

            var controller = (HelloController)fxmlLoader.getController();
            controller.btFile.setOnAction(
                    actionEvent -> FileOpenHelper.INSTANCE.openFile(stage, fxmlLoader)
            );
        } catch (Exception e) {
            var javaVersion = SystemInfo.javaVersion();
            var javafxVersion = SystemInfo.javafxVersion();

            var label = new Label("error:" + e.getMessage());
            var scene = new Scene(new StackPane(label), 640, 480);
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}