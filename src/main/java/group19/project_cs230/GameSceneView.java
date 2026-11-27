package group19.project_cs230;

import javafx.animation.AnimationTimer;
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
import java.util.function.BooleanSupplier;

public class GameSceneView {

    private static final int TILE_SIZE = 50;
    private static final int MAX_LEVEL = 5;
    private static final long STEP_INTERVAL_NANOS = 180_000_000L; // ~5.5 tiles/sec

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

    private Label farmerLevelLabel;
    private Label chickenLevelLabel;
    private Label shelterLevelLabel;
    private Label seedsLevelLabel;
    private Label waterLevelLabel;

    private AnimationTimer moveTimer;
    private long lastStepNanos = 0L;

    public Scene createScene(String levelResourcePath) {
        mapLoader.loadMap(levelResourcePath);
        resetSceneState();

        Button backButton = new Button("Main Menu");
        backButton.setOnAction(event -> {
            stopMovementTimer();
            SceneManager.switchScene("/group19/project_cs230/GameMenu.fxml");
        });

        Label themeLabel = new Label("Theme: " + mapLoader.getCurrentTheme());
        themeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        HBox topBar = new HBox(15, backButton, themeLabel);
        topBar.setStyle("-fx-padding: 10; -fx-background-color: #222222; -fx-alignment: CENTER_LEFT;");

        int widthPx = Math.max(mapLoader.getWidth() * TILE_SIZE, 240);
        int heightPx = Math.max(mapLoader.getHeight() * TILE_SIZE, 240);

        Canvas canvas = new Canvas(widthPx, heightPx);
        GraphicsContext gc = canvas.getGraphicsContext2D();
       // gc.setImageSmoothing(false);

        Label spawnHeader = new Label("Spawn Entities");
        spawnHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        farmerLevelLabel = createLevelLabel(farmer.getLevel());
        chickenLevelLabel = createLevelLabel(chickenEntity.getLevel());
        shelterLevelLabel = createLevelLabel(shelterEntity.getLevel());
        seedsLevelLabel = createLevelLabel(seedsEntity.getLevel());
        waterLevelLabel = createLevelLabel(waterEntity.getLevel());

        Button spawnFarmerBtn = createSpawnButton("Spawn Farmer", farmer, gc, () -> {
            showFarmer = true;
            onFarmerSpawned();
        });
        Button spawnChickenBtn = createSpawnButton("Spawn Chickens", chickenEntity, gc, () -> {
            showChickens = true;
            onChickensSpawned();
        });
        Button spawnShelterBtn = createSpawnButton("Spawn Shelter", shelterEntity, gc, () -> {
            showShelter = true;
            onShelterSpawned();
        });
        Button spawnSeedsBtn = createSpawnButton("Spawn Seeds", seedsEntity, gc, () -> {
            showSeeds = true;
            onSeedsSpawned();
        });
        Button spawnWaterBtn = createSpawnButton("Spawn Water", waterEntity, gc, () -> {
            showWater = true;
            onWaterSpawned();
        });

        VBox spawnPanel = new VBox(
                10,
                spawnHeader,
                createEntityControlRow(spawnFarmerBtn, farmerLevelLabel),
                createEntityControlRow(spawnChickenBtn, chickenLevelLabel),
                createEntityControlRow(spawnShelterBtn, shelterLevelLabel),
                createEntityControlRow(spawnSeedsBtn, seedsLevelLabel),
                createEntityControlRow(spawnWaterBtn, waterLevelLabel)
        );
        spawnPanel.setStyle("-fx-padding: 12; -fx-background-color: #2A2A2A; -fx-alignment: TOP_LEFT;");

        startMovementTimer(gc);
        redraw(gc);

        StackPane centerPane = new StackPane(canvas);
        centerPane.setStyle("-fx-padding: 15; -fx-background-color: #181818;");

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(spawnPanel);
        root.setCenter(centerPane);

        return new Scene(root);
    }

    private void resetSceneState() {
        stopMovementTimer();
        lastStepNanos = 0L;

        showWater = false;
        showSeeds = false;
        showShelter = false;
        showChickens = false;
        showFarmer = false;

        farmer = new Farmer(mapLoader);
        chickenEntity = new Chicken(mapLoader);
        shelterEntity = new Shelter(mapLoader);
        seedsEntity = new Seed();
        waterEntity = new Water(mapLoader);
    }

    private void startMovementTimer(GraphicsContext gc) {
        moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastStepNanos < STEP_INTERVAL_NANOS) {
                    return;
                }
                lastStepNanos = now;

                boolean moved = false;

                if (showFarmer && farmer != null && farmer.isMoving()) {
                    farmer.stepOnce();
                    moved = true;
                }

                if (showChickens && chickenEntity instanceof Chicken) {
                    Chicken chicken = (Chicken) chickenEntity;
                    if (chicken.isMoving()) {
                        chicken.stepOnce();
                        moved = true;
                    }
                }

                if (moved) {
                    refreshAllLevelLabels();
                    redraw(gc);
                }
            }
        };
        moveTimer.start();
    }

    private void stopMovementTimer() {
        if (moveTimer != null) {
            moveTimer.stop();
            moveTimer = null;
        }
    }

    private Button createSpawnButton(
            String text,
            MainEntity entity,
            GraphicsContext gc,
            Runnable onSpawn
    ) {
        Button button = new Button(text);
        button.setOnAction(event -> {
            if (entity.getLevel() < MAX_LEVEL) {
                entity.levelUp();
            }
            onSpawn.run();
            button.setDisable(true);
            refreshAllLevelLabels();
            redraw(gc);
        });
        return button;
    }

    private void onWaterSpawned() {
        Water water = (waterEntity instanceof Water) ? (Water) waterEntity : null;
        if (water == null || water.getLevel() < 1) {
            return;
        }
        if (showChickens && chickenEntity instanceof Chicken) {
            ((Chicken) chickenEntity).drinkWater(water, mapLoader);
        }
        if (showFarmer && farmer != null) {
            farmer.drinkWater(water, mapLoader);
        }
    }

    private void onSeedsSpawned() {
        Seed seed = (seedsEntity instanceof Seed) ? (Seed) seedsEntity : null;
        if (seed == null) {
            return;
        }
        if (showChickens && chickenEntity instanceof Chicken && seed.getLevel() >= 1) {
            ((Chicken) chickenEntity).eatSeeds(seed, mapLoader);
        }
        if (showFarmer && farmer != null && seed.getLevel() >= 3) {
            farmer.eatFood(seed, mapLoader);
        }
    }

    private void onShelterSpawned() {
        if (showFarmer && farmer != null && shelterEntity instanceof Shelter && shelterEntity.getLevel() >= 1) {
            farmer.expandShelter((Shelter) shelterEntity, mapLoader);
        }
    }

    private void onChickensSpawned() {
        if (!(chickenEntity instanceof Chicken)) {
            return;
        }
        Chicken chicken = (Chicken) chickenEntity;
        Water water = (waterEntity instanceof Water) ? (Water) waterEntity : null;
        Seed seed = (seedsEntity instanceof Seed) ? (Seed) seedsEntity : null;

        if (water != null && water.getLevel() >= 1) {
            chicken.drinkWater(water, mapLoader);
        }
        if (showSeeds && seed != null && seed.getLevel() >= 1) {
            chicken.eatSeeds(seed, mapLoader);
        }
        if (showFarmer && farmer != null && chicken.getLevel() >= 1) {
            farmer.feedChickens(chicken, mapLoader);
        }
    }

    private void onFarmerSpawned() {
        if (farmer == null) {
            return;
        }
        Water water = (waterEntity instanceof Water) ? (Water) waterEntity : null;
        Seed seed = (seedsEntity instanceof Seed) ? (Seed) seedsEntity : null;

        if (showWater && water != null && water.getLevel() >= 1) {
            farmer.drinkWater(water, mapLoader);
        }
        if (showSeeds && seed != null && seed.getLevel() >= 3) {
            farmer.eatFood(seed, mapLoader);
        }
        if (showChickens && chickenEntity instanceof Chicken && chickenEntity.getLevel() >= 1) {
            farmer.feedChickens((Chicken) chickenEntity, mapLoader);
        }
        if (showShelter && shelterEntity instanceof Shelter && shelterEntity.getLevel() >= 1) {
            farmer.expandShelter((Shelter) shelterEntity, mapLoader);
        }
    }

    private void refreshAllLevelLabels() {
        updateLevelLabel(farmerLevelLabel, Math.min(MAX_LEVEL, farmer.getLevel()));
        updateLevelLabel(chickenLevelLabel, Math.min(MAX_LEVEL, chickenEntity.getLevel()));
        updateLevelLabel(shelterLevelLabel, Math.min(MAX_LEVEL, shelterEntity.getLevel()));
        updateLevelLabel(seedsLevelLabel, Math.min(MAX_LEVEL, seedsEntity.getLevel()));
        updateLevelLabel(waterLevelLabel, Math.min(MAX_LEVEL, waterEntity.getLevel()));
    }

    private Label createLevelLabel(int level) {
        Label label = new Label("Lv. " + Math.min(MAX_LEVEL, level));
        label.setStyle("-fx-text-fill: #A5D6A6; -fx-font-weight: bold;");
        return label;
    }

    private void updateLevelLabel(Label label, int level) {
        label.setText("Lv. " + Math.min(MAX_LEVEL, level));
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

        // Base ground ('-') across the grid
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

        // Static / spawn-list entities with level-aware sprites
        drawIfVisible(
                gc,
                theme,
                () -> showWater,
                resolveEntitySprite(theme, 'W', waterEntity.getLevel()),
                mapLoader.getWaterSpawn(),
                5
        );
        drawIfVisible(
                gc,
                theme,
                () -> showSeeds,
                resolveEntitySprite(theme, 'S', seedsEntity.getLevel()),
                mapLoader.getSeedSpawn(),
                5
        );
        drawIfVisible(
                gc,
                theme,
                () -> showShelter,
                resolveEntitySprite(theme, 'J', shelterEntity.getLevel()),
                mapLoader.getShelterSpawn(),
                2.5
        );

        if (showChickens && chickenEntity instanceof Chicken) {
            Chicken chicken = (Chicken) chickenEntity;
            Image chickenSprite = resolveEntitySprite(theme, 'C', chicken.getLevel());
            drawEntitySprite(gc, chickenSprite, chicken.getX(), chicken.getY(), 5);
        }

        if (showFarmer && farmer != null) {
            Image farmerSprite = resolveEntitySprite(theme, 'H', farmer.getLevel());
            drawEntitySprite(gc, farmerSprite, farmer.getX(), farmer.getY(), 5);
        }
    }

    private Image resolveEntitySprite(LevelTheme theme, char baseKey, int level) {
        if (baseKey == 'W' && level >= 3) {
            Image upgradedWater = spriteCache.getSprite(theme, 'W'); // Replace with level-3 water key if distinct
            if (upgradedWater != null) {
                return upgradedWater;
            }
        }
        if (baseKey == 'S' && level >= 3) {
            Image upgradedFlower = spriteCache.getSprite(theme, 'F'); // Replace with level-3 flower key if distinct
            if (upgradedFlower != null) {
                return upgradedFlower;
            }
        }
        return spriteCache.getSprite(theme, baseKey);
    }

    private void drawIfVisible(
            GraphicsContext gc,
            LevelTheme theme,
            BooleanSupplier visible,
            Image sprite,
            List<int[]> spawns,
            double scale
    ) {
        if (!visible.getAsBoolean()) {
            return;
        }
        drawSpawns(gc, sprite, spawns, scale);
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