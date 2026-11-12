package group19.project_cs230;

public enum LevelTheme {
    GRASSLAND("GRASSLAND");
    CASTLE("CASTLE");

    private final String folderName;

    LevelTheme(String folderName){
        this.folderName;
    }
    public String getFolderName() {
        return folderName;
    }

    public static LevelTheme fromString(String text){
        if (text == null) return GRASSLAND;
    }

}
