package com.example.navalbattle1.View;

import com.example.navalbattle1.Controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Stage {

    private GameController gameController;

    public GameView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/com/example/navalbattle1/game-view.fxml")
        );
        Parent root = fxmlLoader.load();
        this.gameController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setTitle("batalla naval");
//        this.getIcons().add(new Image(
//                getClass().getResourceAsStream("/com/example/crapsgame02/images/favicon.png")
//        ));
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }
        return GameViewHolder.INSTANCE;
    }

    private static class GameViewHolder {
        private static GameView INSTANCE = null;
    }


}
