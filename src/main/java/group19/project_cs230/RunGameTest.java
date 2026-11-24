package group19.project_cs230;

import javafx.application.Application;
import javafx.stage.Stage;

public class RunGameTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameSceneView gameSceneView = new GameSceneView();
        primaryStage.setTitle("CS230 Game Test");
        primaryStage.setScene(gameSceneView.createScene("/group19/project_cs230/Maps/GRASSLAND"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}