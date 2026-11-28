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

public class GameSceneView {

    private static final int TILE_SIZE = 50;
    private static final int MAX_LEVEL = MainEntity.MAX_LEVEL;
    private static final long STEP_INTERVAL_NANOS = 180_000_000L; // ~5.5 tiles/sec

    private final MapLoader mapLoader = new MapLoader();
    private final ThemeSpriteCache spriteCache = new ThemeSpriteCache();

    // Entities start hidden until their spawn button is clicked
    private boolean showWater = false;
    private boolean showSeeds = false;
    private boolean showShelter = false;
    private boolean showChickens = false;
    private boolean showFarmer = false;

    private boolean levelTransitionTriggered = false;

    // Order the player pressed the spawn buttons, using the map keys (S, W, H, C, J)
    private final StringBuilder activationOrder = new StringBuilder();
    private String winSequence = "SWHCJ";

    private Farmer farmer;
    private Chicken chicken;
    private Shelter shelter;
    private Seed seeds;
    private Water water;

    private Label farmerLevelLabel;
    private Label chickenLevelLabel;
    private Label shelterLevelLabel;
    private Label seedsLevelLabel;
    private Label waterLevelLabel;

    private AnimationTimer moveTimer;
    private long lastStepNanos = 0L;

    public Scene createScene(String levelResourcePath) {
        mapLoader.loadMap(levelResourcePath);
        winSequence = mapLoader.getWinSequence();
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
        gc.setImageSmoothing(false);

        Label spawnHeader = new Label("Spawn Entities");
        spawnHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        farmerLevelLabel = createLevelLabel(farmer.getLevel());
        chickenLevelLabel = createLevelLabel(chicken.getLevel());
        shelterLevelLabel = createLevelLabel(shelter.getLevel());
        seedsLevelLabel = createLevelLabel(seeds.getLevel());
        waterLevelLabel = createLevelLabel(water.getLevel());

        Button spawnFarmerBtn = createSpawnButton("Spawn Farmer", farmer, gc, () -> {
            showFarmer = true;
            onActivated('H');
        });
        Button spawnChickenBtn = createSpawnButton("Spawn Chickens", chicken, gc, () -> {
            showChickens = true;
            onActivated('C');
        });
        Button spawnShelterBtn = createSpawnButton("Spawn Shelter", shelter, gc, () -> {
            showShelter = true;
            onActivated('J');
        });
        Button spawnSeedsBtn = createSpawnButton("Spawn Seeds", seeds, gc, () -> {
            showSeeds = true;
            onActivated('S');
        });
        Button spawnWaterBtn = createSpawnButton("Spawn Water", water, gc, () -> {
            showWater = true;
            onActivated('W');
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

        levelTransitionTriggered = false;
        activationOrder.setLength(0);

        farmer = new Farmer(mapLoader);
        chicken = new Chicken(mapLoader);
        shelter = new Shelter(mapLoader);
        seeds = new Seed();
        water = new Water(mapLoader);
    }

    private void startMovementTimer(GraphicsContext gc) {
        moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastStepNanos < STEP_INTERVAL_NANOS) {
                    return;
                }
                lastStepNanos = now;

                if (showFarmer && farmer.isMoving()) {
                    farmer.stepOnce();
                    refreshAllLevelLabels();
                    redraw(gc);
                    checkNextLevelProgression(gc);
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
            if (entity.getLevel() < 1) {
                entity.levelUp(); // activation only: 0 -> 1
            }
            onSpawn.run();
            button.setDisable(true);
            refreshAllLevelLabels();
            redraw(gc);
            checkNextLevelProgression(gc);
        });
        return button;
    }

    /** True once every entity has reached its final state. */
    private boolean isWon() {
        return seeds.isCollected()
                && seeds.getLevel() >= MAX_LEVEL
                && farmer.getLevel() >= MAX_LEVEL
                && chicken.getLevel() >= MAX_LEVEL
                && shelter.getLevel() >= MAX_LEVEL
                && water.getLevel() >= MAX_LEVEL;
    }

    private void checkNextLevelProgression(GraphicsContext gc) {
        if (levelTransitionTriggered || !isWon()) {
            return;
        }
        levelTransitionTriggered = true;
        stopMovementTimer();
        redraw(gc);
        advanceToNextGameLevel();
    }

    private void advanceToNextGameLevel() {
        // TODO: unlock the next map in the player profile, record the win on the leaderboard
    }

    private void onActivated(char key) {
        activationOrder.append(key);
        planNextFarmerTrip();
    }

    /** True while the buttons have been pressed in an order that is still a prefix of the winning one. */
    private boolean onTrack() {
        return winSequence.startsWith(activationOrder.toString());
    }

    private void sendFarmer(List<int[]> spawns, Runnable effect) {
        if (spawns == null || spawns.isEmpty()) {
            return;
        }
        int[] target = spawns.get(0);
        farmer.goTo(target[0], target[1], () -> {
            effect.run();
            planNextFarmerTrip(); // chain to the next rule that is now true
        });
    }

    /**
     * The rules. Levels 1->2 react in any order (so entities visibly respond to each other);
     * every level-3 step needs onTrack(), so only the winning order can finish.
     */
    private void planNextFarmerTrip() {
        if (!showFarmer || farmer.isBusy() || levelTransitionTriggered) {
            return;
        }

        int s = seeds.getLevel();
        int w = water.getLevel();
        int c = chicken.getLevel();
        int j = shelter.getLevel();
        int f = farmer.getLevel();
        boolean track = onTrack();

        // Farmer finds Water; it develops and he picks it up (Seeds must exist to need it)
        if (showWater && showSeeds && w == 1) {
            sendFarmer(mapLoader.getWaterSpawn(), () -> {
                water.levelUp();
                farmer.setCarryingWater(true);
            });
        }
        // Delivers water: Seeds grow, Farmer grows
        else if (farmer.isCarryingWater() && s == 1) {
            sendFarmer(mapLoader.getSeedSpawn(), () -> {
                seeds.levelUp();
                farmer.levelUp();
                farmer.setCarryingWater(false);
            });
        }
        // Visits Chicken
        else if (showChickens && c == 1 && f >= 2) {
            sendFarmer(mapLoader.getChickenSpawn(), chicken::levelUp);
        }
        // Back to Seeds: ready to collect
        else if (s == 2 && c >= 2 && !seeds.isCollected() && track) {
            sendFarmer(mapLoader.getSeedSpawn(), seeds::levelUp);
        }
        // Back to Water: final stage
        else if (w == 2 && s >= 3 && c >= 2 && track) {
            sendFarmer(mapLoader.getWaterSpawn(), water::levelUp);
        }
        // Builds Shelter
        else if (showShelter && j == 1 && f >= 2) {
            sendFarmer(mapLoader.getShelterSpawn(), shelter::levelUp);
        }
        // Collects Seeds once Shelter is built: Farmer stronger, Seeds drop a level
        else if (s == 3 && j >= 2 && f == 2 && !seeds.isCollected() && track) {
            sendFarmer(mapLoader.getSeedSpawn(), () -> {
                seeds.collect();
                farmer.levelUp();
            });
        }
        // Chicken to final extent
        else if (c == 2 && f >= 3 && track) {
            sendFarmer(mapLoader.getChickenSpawn(), chicken::levelUp);
        }
        // Final Shelter upgrade; the Seeds regrow back to full level
        else if (j == 2 && f >= 3 && c == 3 && track) {
            sendFarmer(mapLoader.getShelterSpawn(), () -> {
                shelter.levelUp();
                seeds.regrow();
            });
        }
    }

    private void refreshAllLevelLabels() {
        updateLevelLabel(farmerLevelLabel, farmer.getLevel());
        updateLevelLabel(chickenLevelLabel, chicken.getLevel());
        updateLevelLabel(shelterLevelLabel, shelter.getLevel());
        updateLevelLabel(waterLevelLabel, water.getLevel());
        updateLevelLabel(seedsLevelLabel, seeds.getLevel());
        if (seeds.isCollected()) {
            seedsLevelLabel.setText(seedsLevelLabel.getText() + " (collected)");
        }
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

    /** Size in tiles for Water at each level. Tune these numbers. */
    private double waterScale(int level) {
        switch (level) {
            case 1:  return 5;   // Water.png
            default: return 3;   // Water_2.png (level 3 reuses it)
        }
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
        Image baseImg = spriteCache.getSprite(theme, '-');
        if (baseImg != null) {
            for (int y = 0; y < mapLoader.getHeight(); y++) {
                char[] row = grid.get(y);
                for (int x = 0; x < row.length; x++) {
                    gc.drawImage(baseImg, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Sprites change with the entity's level (Water_2.png, Seeds_2.png, ...)
        if (showWater) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'W', water.getLevel()),
                    mapLoader.getWaterSpawn(), waterScale(water.getLevel()));
        }
        if (showSeeds) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'S', seeds.getLevel()),
                    mapLoader.getSeedSpawn(), 5);
        }
        if (showShelter) {
            drawSpawns(gc, spriteCache.getSprite(theme, 'J', shelter.getLevel()),
                    mapLoader.getShelterSpawn(), 2.5);
        }
        if (showChickens) {
            drawEntitySprite(gc, spriteCache.getSprite(theme, 'C', chicken.getLevel()),
                    chicken.getX(), chicken.getY(), 5);
        }
        if (showFarmer) {
            drawEntitySprite(gc, spriteCache.getSprite(theme, 'H', farmer.getLevel()),
                    farmer.getX(), farmer.getY(), 5);
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

    /** Width comes from the scale; height follows the image's proportions so nothing is stretched. */
    private void drawEntitySprite(GraphicsContext gc, Image sprite, int col, int row, double scale) {
        if (sprite == null) {
            return;
        }
        double drawW = TILE_SIZE * scale;
        double drawH = drawW * (sprite.getHeight() / sprite.getWidth());

        // Centered on the tile
        double x = col * TILE_SIZE - (drawW - TILE_SIZE) / 2.0;
        double y = row * TILE_SIZE - (drawH - TILE_SIZE) / 2.0;

        gc.drawImage(sprite, x, y, drawW, drawH);
    }
}
