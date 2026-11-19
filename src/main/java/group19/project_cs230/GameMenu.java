package group19.project_cs230;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller which handles user interaction with the main menu
 *
 * @author Andrii Hainiiev
 * @version 25
 */

public class GameMenu {

    /**
     * Starting a new game session.
     *
     * @param event action triggered by the game control
     */
    @FXML
    private void NewGameStart(ActionEvent event) {
        System.out.println("New Game Started!");
        // here: add game board loading here

    }

    /**
     * Loads saved game session.
     *
     * @param event action triggered by the game control
     */
    @FXML
    private void LoadGame(ActionEvent event) {
        System.out.println("Loading Game...");
        // here: add a loading procedure here
    }

    /**
     * Opens game settings .
     *
     * @param event action triggered by the game control
     */
    @FXML
    private void SettingsClick(ActionEvent event) {
        System.out.println("Opening Settings");
        // here: create settings menu
    }

    /**
     * Exits the application.
     *
     * @param event action triggered by the game control
     */
    @FXML
    private void ExitClick(ActionEvent event) {
        System.out.println("Exiting...");
        System.exit(0);
    }
}

