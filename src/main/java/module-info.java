module group19.project_cs230 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;


    opens group19.project_cs230 to javafx.fxml;
    exports group19.project_cs230;
}