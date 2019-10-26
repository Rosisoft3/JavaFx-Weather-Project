/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rosisoft.weatherproject.app.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;






/**
 *
 * @author Rosisse
 */
public class Main extends Application {
    
@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().
        getResource("/com/rosisoft/weatherproject/app/resources/views/"
                        + "weather.view.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMouseDragged(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX());
                primaryStage.setY(event.getScreenY());
            }
        });

        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        primaryStage.setTitle("Current Weather!:: Version 1.0");
        primaryStage.getIcons().add(new Image("/com/rosisoft/weatherproject/app/resources/icons/logo.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
