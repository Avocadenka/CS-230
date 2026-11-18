package group19.project_cs230;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameMenu {

    @FXML
    private void NewGameStart(ActionEvent event) {
        System.out.println("New Game Started!");
        // here: add game board loading here

    }

    @FXML
    private void LoadGame(ActionEvent event) {
        System.out.println("Loading Game...");
        // here: add a loading procedure here
    }

    @FXML
    private void SettingsClick(ActionEvent event) {
        System.out.println("Opening Settings");
        // here: create settings menu
    }

    @FXML
    private void ExitClick(ActionEvent event) {
        System.out.println("Exiting...");
        System.exit(0);
    }
}

