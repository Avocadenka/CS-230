package group19.project_cs230;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;

/**
 * Test of the Main Menu for displaying and checking functionality.
 *
 * @author Andrii Hainiiev
 * @version 1.0
 */

public class MenuTester extends Application {

    /**
     * Starts and displays primary stage of the game menu.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("CS-230 Game Menu");
            SceneManager.setStage(primaryStage);
            SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
            // Paste background music path here:
            AudioManager.playMusic("/group19/project_cs230/audio/8-bit-bgm.mp3");

            // Paste SFX sound path here:
            primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (isButtonClick(event.getTarget())) {
                    AudioManager.playSFX("/group19/project_cs230/audio/click.wav");
                }
            });
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isButtonClick(Object target) {
        if (target instanceof Node) {
            Node node = (Node) target;
            while (node != null) {
                if (node instanceof ButtonBase) {
                    return true;
                }
                node = node.getParent();
            }
        }
        return false;
    }

    /**
     * Point of starting the test application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}