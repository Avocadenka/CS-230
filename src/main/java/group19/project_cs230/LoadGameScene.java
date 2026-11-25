package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

/**
 * Controller for the load game screen.
 * Allows a player profile to be selected and loaded from the list of available player profiles.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class LoadGameScene {

    @FXML
    private ChoiceBox<PlayerProfile> selectPlayer;

    /**
     * Updates the player selection drop-down menu with all available player profiles stored in the leaderboard.
     */
    @FXML
    public void updatePlayerChoiceBox() {
        System.out.println("Choice box updating");
        ArrayList<PlayerProfile> players = Leaderboard.getPlayers();
        System.out.println(players);
        selectPlayer.setItems(FXCollections.observableArrayList(
                players));

    }

    /**
     * Returns the user to the main menu screen.
     *
     * @param event the button click event that triggered the scene change
     */
    @FXML
    private void returnToMainMenu(ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }

}
