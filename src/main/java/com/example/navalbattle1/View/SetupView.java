package com.example.navalbattle1.View;

import com.example.navalbattle1.Controller.SetupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupView extends Stage {

    private SetupController controller;

    private SetupView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/navalbattle1/setup-view.fxml")
        );

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Colocación de Flota");
    }

    /* ===================== SINGLETON ===================== */

    public static SetupView getInstance() throws IOException {
        if (SetupViewHolder.INSTANCE == null) {
            SetupViewHolder.INSTANCE = new SetupView();
        }
        return SetupViewHolder.INSTANCE;
    }

    private static class SetupViewHolder {
        private static SetupView INSTANCE = null;
    }

    /* ===================== CONTROLLER ===================== */

    public SetupController getController() {
        return controller;
    }
}

