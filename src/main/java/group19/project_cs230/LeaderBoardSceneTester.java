package group19.project_cs230;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LeaderBoardSceneTester extends Application {

        @Override
        public void start(Stage primaryStage) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/group19/project_cs230/LeaderBoardScene.fxml"));
                Parent root = loader.load();

                primaryStage.setTitle("Leaderboard");
                primaryStage.setScene(new Scene(root, 800, 600));
                primaryStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            launch(args);
        }

}
