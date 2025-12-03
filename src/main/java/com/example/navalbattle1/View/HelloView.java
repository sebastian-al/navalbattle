package com.example.navalbattle1.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloView extends Stage {

    public HelloView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/example/navalbattle1/hello-view.fxml")
        );
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("Craps Game");
//        this.getIcons().add(new Image(
//                getClass().getResourceAsStream("/com/example/crapsgame02/images/favicon.png")
//        ));
    }

    public static HelloView getInstance() throws IOException {
        if (HelloView.HelloViewHolder.INSTANCE == null) {
            HelloView.HelloViewHolder.INSTANCE = new HelloView();
        }
        return HelloView.HelloViewHolder.INSTANCE;
    }

    private static class HelloViewHolder {
        private static HelloView INSTANCE = null;
    }
}
