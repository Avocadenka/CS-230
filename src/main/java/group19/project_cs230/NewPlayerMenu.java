package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Controller for the New Player menu.
 * This class manages the creation of new player profiles and handles navigation between menu screens.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class NewPlayerMenu {

    @FXML
    private ChoiceBox<Title> chooseTitle;

    @FXML
    private TextField userNameInput;

    /**
     * Initialises the controller and populates the title selection drop-down menu with all available titles.
     */
    @FXML
    public void initialize() {
        chooseTitle.setItems(
                FXCollections.observableArrayList(Title.values())
        );
    }

    /**
     * Creates a new player profile using the entered name and selected title.
     * The new profile is added to the leaderboard system.
     *
     * @param event the button click event that triggers player creation
     */
    @FXML
    private void createPlayer (ActionEvent event) {
        PlayerProfile newPlayer = new PlayerProfile(userNameInput.getText(), chooseTitle.getValue());
        System.out.println(newPlayer.getFullName());
        Leaderboard.addPlayer(newPlayer);

        // Gavin - this is where the scene will switch to the main game UI
        // SceneManager.switchScene("/group19/project_cs230/XXX.fxml");
    }

    /**
     * Returns the user to the main menu screen.
     *
     * @param event the button click event that triggers the scene change
     */
    @FXML
    private void returnToMainMenu (ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }
}
