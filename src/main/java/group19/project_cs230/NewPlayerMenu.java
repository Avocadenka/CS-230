package group19.project_cs230;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewPlayerMenu {

    @FXML
    private ChoiceBox<Title> chooseTitle;

    @FXML
    private TextField userNameInput;

    @FXML
    public void initialize() {
        chooseTitle.setItems(
                FXCollections.observableArrayList(Title.values())
        );
    }

    @FXML
    private void createPlayer (ActionEvent event) {
        PlayerProfile newPlayer = new PlayerProfile(userNameInput.getText(), chooseTitle.getValue());
        System.out.println(newPlayer.getFullName());
    }

    @FXML
    private void returnToMainMenu (ActionEvent event) {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }
}
