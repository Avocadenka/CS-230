module group.project_cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens group19.project_cs230 to javafx.fxml;
    exports group19.project_cs230;
}