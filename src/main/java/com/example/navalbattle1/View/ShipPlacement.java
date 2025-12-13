package com.example.navalbattle1.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShipPlacement extends Stage {
    public ShipPlacement() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/example/navalbattle1/ship-placement.fxml")
        );
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("batalla naval");
//        this.getIcons().add(new Image(
//                getClass().getResourceAsStream("/com/example/crapsgame02/images/favicon.png")
//        ));
    }

    public static ShipPlacement getInstance() throws IOException {
        if (ShipPlacement.ShipHolder.INSTANCE == null) {
            ShipPlacement.ShipHolder.INSTANCE = new ShipPlacement();
        }
        return ShipPlacement.ShipHolder.INSTANCE;
    }

    private static class ShipHolder {
        private static ShipPlacement INSTANCE = null;
    }
}