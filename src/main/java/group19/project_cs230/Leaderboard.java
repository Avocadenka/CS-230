package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Random;

public class Leaderboard {

    @FXML
    private TableView<PlayerProfile> leaderboardTable;

    @FXML
    private TableColumn<PlayerProfile, String> userColumn;

    @FXML
    private TableColumn<PlayerProfile, Integer> highScoreColumn;

    private static ArrayList<PlayerProfile> players = new ArrayList<>();


    @FXML
    public void initialize() {

        userColumn.setCellValueFactory(
                new PropertyValueFactory<>("fullName"));

        highScoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("highScore"));

        // in future, this many need to be instance variable
        // with a method that adds a player to the leaderboard
        // which is called from congratulation screen at the end of game
        ObservableList<PlayerProfile> leaderboardData =
                FXCollections.observableArrayList();


        for (int i = 0; i < players.size(); i++) {
            PlayerProfile currentPlayer = players.get(i);
            if (currentPlayer.isFinishedGame()){
                leaderboardData.add(currentPlayer);
            }
        }

        leaderboardTable.setItems(leaderboardData);

    }

    @FXML
    private void returnToMainMenu(ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }

    public static void addPlayer(PlayerProfile player) {
        testAddingToLeaderboardWithRandomScores(player);
        players.add(player);
    }

    // for testing purposes - setting each player a random score
    public static void testAddingToLeaderboardWithRandomScores(PlayerProfile player){
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            player.moveToNextMap(random.nextInt(120));
        }
    }

    public static ArrayList<PlayerProfile> getPlayers() {
        return players;
    }
}
