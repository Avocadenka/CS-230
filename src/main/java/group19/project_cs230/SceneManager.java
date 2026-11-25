package group19.project_cs230;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages scene transitions within the JavaFX application.
 * This class stores the primary stage and provides utility methods for loading and switching between FXML scenes.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class SceneManager {

    private static Stage stage;
    private static final int STAGE_WIDTH = 800;
    private static final int STAGE_HEIGHT = 600;

    /**
     * Sets the primary stage used throughout the application.
     *
     * @param primaryStage the application's primary stage
     */
    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    /**
     * Loads and switches to the specific FXML scene.
     * @param sceneFXMLFile the path of the FXML file to load
     */
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

    /**
     * Switches to the load game scene and refreshes the player selection list before displaying the screen.
     */
    public static void switchToLoadPlayers(){
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/group19/project_cs230/LoadGameScene.fxml"));
            Parent root = loader.load();

            LoadGameScene controller = loader.getController();

            controller.updatePlayerChoiceBox();

            stage.setScene(new Scene(root, STAGE_WIDTH, STAGE_HEIGHT));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
