package group19.project_cs230;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private static final int STAGE_WIDTH = 800;
    private static final int STAGE_HEIGHT = 600;

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    public static void switchScene(String sceneFXMLFile){
        try {
            Parent root = FXMLLoader.load(
                    SceneManager.class.getResource(sceneFXMLFile)
            );

            stage.setScene(new Scene(root, STAGE_WIDTH, STAGE_HEIGHT));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
