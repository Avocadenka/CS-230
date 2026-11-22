package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Leaderboard {

    @FXML
    private TableView<PlayerProfile> leaderboardTable;

        @FXML
        private TableColumn<PlayerProfile, String> userColumn;

        @FXML
        private TableColumn<PlayerProfile, Integer> highScoreColumn;


        @FXML
        public void initialize() {

            System.out.println("init");

            userColumn.setCellValueFactory(
                    new PropertyValueFactory<>("fullName"));

            highScoreColumn.setCellValueFactory(
                    new PropertyValueFactory<>("highScore"));

            ObservableList<PlayerProfile> leaderboardData =
                    FXCollections.observableArrayList();

            // test data - using susan again
            PlayerProfile susan = new PlayerProfile("Susan", Title.LADY);
            susan.moveToNextMap(2);
            susan.moveToNextMap(4);
            susan.moveToNextMap(6);

            leaderboardData.add(susan);

            leaderboardTable.setItems(leaderboardData);
        }

    @FXML
    private void returnToMainMenu (ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }
}
