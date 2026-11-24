package group19.project_cs230;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameSceneView {

    private static final int TILE_SIZE = 48;

    private final MapLoader mapLoader = new MapLoader();
    private final ThemeSpriteCache spriteCache = new ThemeSpriteCache();

    // Entities start hidden until their spawn button is clicked
    private boolean showWater = false;
    private boolean showSeeds = false;
    private boolean showShelter = false;
    private boolean showChickens = false;
    private boolean showFarmer = false;

    // Backing entity objects (initial level = 0 from MainEntity constructor)
    private Farmer farmer;
    private MainEntity chickenEntity;
    private MainEntity shelterEntity;
    private MainEntity seedsEntity;
    private MainEntity waterEntity;

    public Scene createScene(String levelResourcePath) {
        mapLoader.loadMap(levelResourcePath);

        // Reset entity visibility and instances whenever a scene is created
        showWater = false;
        showSeeds = false;
        showShelter = false;
        showChickens = false;
        showFarmer = false;

        farmer = new Farmer(mapLoader);
        chickenEntity = new MainEntity();
        shelterEntity = new MainEntity();
        seedsEntity = new MainEntity();
        waterEntity = new MainEntity();

        Button backButton = new Button("Main Menu");
        backButton.setOnAction(event ->
                SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml"));

        Label themeLabel = new Label("Theme: " + mapLoader.getCurrentTheme());
        themeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        HBox topBar = new HBox(15, backButton, themeLabel);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-alignment: CENTER_LEFT;");

        int widthPx = Math.max(mapLoader.getWidth() * TILE_SIZE, 240);
        int heightPx = Math.max(mapLoader.getHeight() * TILE_SIZE, 240);

        Canvas canvas = new Canvas(widthPx, heightPx);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        Label spawnHeader = new Label("Spawn Entities");
        spawnHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button spawnFarmerBtn = new Button("Spawn Farmer");
        Label farmerLevelLabel = createLevelLabel(farmer.getLevel());

        Button spawnChickenBtn = new Button("Spawn Chickens");
        Label chickenLevelLabel = createLevelLabel(chickenEntity.getLevel());

        Button spawnShelterBtn = new Button("Spawn Shelter");
        Label shelterLevelLabel = createLevelLabel(shelterEntity.getLevel());

        Button spawnSeedsBtn = new Button("Spawn Seeds");
        Label seedsLevelLabel = createLevelLabel(seedsEntity.getLevel());

        Button spawnWaterBtn = new Button("Spawn Water");
        Label waterLevelLabel = createLevelLabel(waterEntity.getLevel());

        spawnFarmerBtn.setOnAction(event -> {
            showFarmer = true;
            farmer.levelUp();
            spawnFarmerBtn.setDisable(true);
            triggerAvailableActionsAndRefresh(
                    farmerLevelLabel,
                    chickenLevelLabel,
                    shelterLevelLabel,
                    seedsLevelLabel,
                    waterLevelLabel
            );
            redraw(gc);
        });

        spawnChickenBtn.setOnAction(event -> {
            showChickens = true;
            chickenEntity.levelUp();
            spawnChickenBtn.setDisable(true);
            triggerAvailableActionsAndRefresh(
                    farmerLevelLabel,
                    chickenLevelLabel,
                    shelterLevelLabel,
                    seedsLevelLabel,
                    waterLevelLabel
            );
            redraw(gc);
        });

        spawnShelterBtn.setOnAction(event -> {
            showShelter = true;
            shelterEntity.levelUp();
            spawnShelterBtn.setDisable(true);
            triggerAvailableActionsAndRefresh(
                    farmerLevelLabel,
                    chickenLevelLabel,
                    shelterLevelLabel,
                    seedsLevelLabel,
                    waterLevelLabel
            );
            redraw(gc);
        });

        spawnSeedsBtn.setOnAction(event -> {
            showSeeds = true;
            seedsEntity.levelUp();
            spawnSeedsBtn.setDisable(true);
            triggerAvailableActionsAndRefresh(
                    farmerLevelLabel,
                    chickenLevelLabel,
                    shelterLevelLabel,
                    seedsLevelLabel,
                    waterLevelLabel
            );
            redraw(gc);
        });

        spawnWaterBtn.setOnAction(event -> {
            showWater = true;
            waterEntity.levelUp();
            spawnWaterBtn.setDisable(true);
            triggerAvailableActionsAndRefresh(
                    farmerLevelLabel,
                    chickenLevelLabel,
                    shelterLevelLabel,
                    seedsLevelLabel,
                    waterLevelLabel
            );
            redraw(gc);
        });

        HBox farmerRow = createEntityControlRow(spawnFarmerBtn, farmerLevelLabel);
        HBox chickenRow = createEntityControlRow(spawnChickenBtn, chickenLevelLabel);
        HBox shelterRow = createEntityControlRow(spawnShelterBtn, shelterLevelLabel);
        HBox seedsRow = createEntityControlRow(spawnSeedsBtn, seedsLevelLabel);
        HBox waterRow = createEntityControlRow(spawnWaterBtn, waterLevelLabel);

        VBox spawnPanel = new VBox(
                10,
                spawnHeader,
                farmerRow,
                chickenRow,
                shelterRow,
                seedsRow,
                waterRow
        );
        spawnPanel.setStyle("-fx-padding: 12; -fx-background-color: #2A2A2A; -fx-alignment: TOP_LEFT;");

        redraw(gc);

        StackPane centerPane = new StackPane(canvas);
        centerPane.setStyle("-fx-padding: 15; -fx-background-color: #181818;");

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(spawnPanel);
        root.setCenter(centerPane);

        return new Scene(root);
    }

    private void triggerAvailableActionsAndRefresh(
            Label farmerLabel,
            Label chickenLabel,
            Label shelterLabel,
            Label seedsLabel,
            Label waterLabel
    ) {
        if (showFarmer && farmer != null) {
            if (waterEntity.getLevel() >= 1) {
                if (waterEntity instanceof Water) {
                    farmer.drinkWater((Water) waterEntity, mapLoader);
                } else {
                    farmer.levelUp();
                }
            }

            if (seedsEntity.getLevel() >= 3) {
                if (seedsEntity instanceof Seed) {
                    farmer.eatFood((Seed) seedsEntity, mapLoader);
                } else {
                    farmer.levelUp();
                }
            }

            if (chickenEntity.getLevel() >= 1) {
                if (chickenEntity instanceof Chicken) {
                    farmer.feedChickens((Chicken) chickenEntity, mapLoader);
                } else {
                    farmer.levelUp();
                    chickenEntity.levelUp();
                }
            }

            if (shelterEntity.getLevel() >= 1) {
                if (shelterEntity instanceof Shelter) {
                    farmer.expandShelter((Shelter) shelterEntity, mapLoader);
                } else {
                    farmer.levelUp();
                    shelterEntity.levelUp();
                }
            }
        }

        refreshAllLevelLabels(farmerLabel, chickenLabel, shelterLabel, seedsLabel, waterLabel);
    }

    private void refreshAllLevelLabels(
            Label farmerLabel,
            Label chickenLabel,
            Label shelterLabel,
            Label seedsLabel,
            Label waterLabel
    ) {
        updateLevelLabel(farmerLabel, farmer.getLevel());
        updateLevelLabel(chickenLabel, chickenEntity.getLevel());
        updateLevelLabel(shelterLabel, shelterEntity.getLevel());
        updateLevelLabel(seedsLabel, seedsEntity.getLevel());
        updateLevelLabel(waterLabel, waterEntity.getLevel());
    }

    private Label createLevelLabel(int level) {
        Label label = new Label("Lv. " + level);
        label.setStyle("-fx-text-fill: #A5D6A7; -fx-font-weight: bold;");
        return label;
    }

    private void updateLevelLabel(Label label, int level) {
        label.setText("Lv. " + level);
    }

    private HBox createEntityControlRow(Button button, Label levelLabel) {
        HBox row = new HBox(8, button, levelLabel);
        row.setStyle("-fx-alignment: CENTER_LEFT;");
        return row;
    }

    private void redraw(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        drawLevel(gc);
    }

    private void drawLevel(GraphicsContext gc) {
        List<char[]> grid = mapLoader.getMapGrid();
        if (grid == null || grid.isEmpty()) {
            return;
        }

        LevelTheme theme = mapLoader.getCurrentTheme();

        // 1. Base ground ('-') and walls ('X') across the grid
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

        // 2. Entities & items at their spawn coordinates (only if spawned via button)
        if (showWater) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'W'), mapLoader.getWaterSpawn(), 5);
        }
        if (showSeeds) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'S'), mapLoader.getSeedSpawn(), 5);
        }
        if (showShelter) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'J'), mapLoader.getShelterSpawn(), 2.5);
        }
        if (showChickens) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'C'), mapLoader.getChickenSpawn(), 5);
        }
        if (showFarmer && farmer != null) {
            Image farmerSprite = spriteCache.getSprite(theme, 'H');
            drawEntitySprite(gc, farmerSprite, farmer.getX(), farmer.getY(), 5);
        }
    }

    private void drawSpawns(GraphicsContext gc, Image sprite, List<int[]> spawns, double scale) {
        if (sprite == null || spawns == null) {
            return;
        }
        for (int[] xy : spawns) {
            drawEntitySprite(gc, sprite, xy[0], xy[1], scale);
        }
    }

    private void drawEntitySprite(GraphicsContext gc, Image sprite, int col, int row, double scale) {
        if (sprite == null) {
            return;
        }
        double drawW = TILE_SIZE * scale;
        double drawH = TILE_SIZE * scale;

        // Centered on the tile:
        double x = col * TILE_SIZE - (drawW - TILE_SIZE) / 2.0;
        double y = row * TILE_SIZE - (drawH - TILE_SIZE) / 2.0;

        gc.drawImage(sprite, x, y, drawW, drawH);
    }
}