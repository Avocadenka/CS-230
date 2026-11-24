package group19.project_cs230;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.List;

public class GameController {

    private static final int TILE_SIZE = 48;

    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label themeLabel;

    private final MapLoader mapLoader = new MapLoader();
    private final ThemeSpriteCache spriteCache = new ThemeSpriteCache();

    @FXML
    public void initialize() {
        loadLevel("/group19/project_cs230/Maps/GRASSLAND");
    }

    public void loadLevel(String resourcePath) {
        mapLoader.loadMap(resourcePath);

        if (themeLabel != null) {
            themeLabel.setText("Theme: " + mapLoader.getCurrentTheme());
        }

        if (gameCanvas == null || mapLoader.getWidth() == 0 || mapLoader.getHeight() == 0) {
            return;
        }

        gameCanvas.setWidth(mapLoader.getWidth() * TILE_SIZE);
        gameCanvas.setHeight(mapLoader.getHeight() * TILE_SIZE);

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        drawLevel(gc);
    }

    @FXML
    private void handleBackToMenu() {
        SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
    }

    private void drawLevel(GraphicsContext gc) {
        List<char[]> grid = mapLoader.getMapGrid();
        if (grid == null || grid.isEmpty()) {
            return;
        }

        LevelTheme theme = mapLoader.getCurrentTheme();

        for (int y = 0; y < mapLoader.getHeight(); y++) {
            char[] row = grid.get(y);
            for (int x = 0; x < row.length; x++) {
                char baseChar = (row[x] == 'X') ? 'X' : '-';
                Image baseImg = spriteCache.getSprite(theme, baseChar);
                if (baseImg != null) {
                    gc.drawImage(baseImg, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        drawSpawns(gc, spriteCache.getSprite(theme, 'W'), mapLoader.getWaterSpawn());
        drawSpawns(gc, spriteCache.getSprite(theme, 'S'), mapLoader.getSeedSpawn());
        drawSpawns(gc, spriteCache.getSprite(theme, 'J'), mapLoader.getShelterSpawn());
        drawSpawns(gc, spriteCache.getSprite(theme, 'C'), mapLoader.getChickenSpawn());
        drawSpawns(gc, spriteCache.getSprite(theme, 'H'), mapLoader.getFarmerSpawn());
    }

    private void drawSpawns(GraphicsContext gc, Image sprite, List<int[]> spawns) {
        if (sprite == null || spawns == null) {
            return;
        }
        for (int[] xy : spawns) {
            gc.drawImage(sprite, xy[0] * TILE_SIZE, xy[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
}