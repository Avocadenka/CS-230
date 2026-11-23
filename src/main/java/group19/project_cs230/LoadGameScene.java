package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class LoadGameScene {
    @FXML
    private ChoiceBox<Title> selectPlayer;

    @FXML
    public void initialize() {
        ArrayList<PlayerProfile> players = Leaderboard.getPlayers();

        selectPlayer = new ChoiceBox<>();

      //  selectPlayer.getItems().addAll(players);
    }

    @FXML
    private void backFromLoadScene (ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }
}
