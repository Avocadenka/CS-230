package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class LoadGameScene {

    @FXML
    private ChoiceBox<PlayerProfile> selectPlayer;

    @FXML
    public void updatePlayerChoiceBox() {
        System.out.println("Choice box updating");
        ArrayList<PlayerProfile> players = Leaderboard.getPlayers();
        System.out.println(players);
        selectPlayer.setItems(FXCollections.observableArrayList(
                players));

    }

    @FXML
    private void backFromLoadScene (ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }
}
