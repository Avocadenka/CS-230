package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Controller for the leaderboard screen.
 * Displays completed players and their scores in a table view and provides navigation back to the main menu.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class Leaderboard {

    @FXML
    private TableView<PlayerProfile> leaderboardTable;

    @FXML
    private TableColumn<PlayerProfile, String> userColumn;

    @FXML
    private TableColumn<PlayerProfile, Integer> highScoreColumn;

    private static ArrayList<PlayerProfile> players = new ArrayList<>();

    /**
     * Initialises the leaderboard table and populates it with all players who have completed the game.
     */
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

        PriorityQueue<PlayerProfile> topFive = new PriorityQueue<>(Comparator.comparingInt(PlayerProfile::getHighScore));
        int topScoresNum = 5;
        // find top 5
        for (int i = 0; i < players.size(); i++) {
            if (topFive.size() < topScoresNum) {
                System.out.println("add " + players.get(i));
                topFive.offer(players.get(i));
            }
            // else if (){ check if the last is equivalent then we can have 6
            else {
                topFive.poll();
                topFive.offer(players.get(i));
            }
        }


        while (!topFive.isEmpty()) {
            PlayerProfile currentPlayer = topFive.poll();
            if (currentPlayer.isFinishedGame()) {
                leaderboardData.add(currentPlayer);
            }
        }

        leaderboardTable.setItems(leaderboardData);

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

    /**
     * Adds a player profile to the leaderboard.
     *
     * @param player the player to add
     */
    public static void addPlayer(PlayerProfile player) {
        testAddingToLeaderboardWithRandomScores(player);
        players.add(player);
    }

    /**
     * Assigns random scores to a player for testing purposes before adding them to the leaderboard.
     *
     * @param player the player whose score data is updated
     */
    public static void testAddingToLeaderboardWithRandomScores(PlayerProfile player) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            player.moveToNextMap(random.nextInt(120));
        }
    }

    public static ArrayList<PlayerProfile> getPlayers() {
        return players;
    }
}
