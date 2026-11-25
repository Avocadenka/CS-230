package group19.project_cs230;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Chicken extends MainEntity {

    private static final int MAX_LEVEL = 5;

    private int x;
    private int y;

    private final Queue<int[]> pathQueue = new ArrayDeque<>();
    private final Queue<Runnable> pendingActions = new ArrayDeque<>();
    private Runnable onArrivalCallback;

    public Chicken(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getChickenSpawn() != null && !mapLoader.getChickenSpawn().isEmpty()) {
            int[] spawn = mapLoader.getChickenSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMoving() {
        return !pathQueue.isEmpty();
    }

    public void layEgg() {
        // Implement egg laying logic when ready
    }

    public void eatSeeds(Seed seed, MapLoader mapLoader) {
        if (seed == null || seed.getLevel() < 1 || mapLoader == null) {
            return;
        }
        enqueueOrRunAction(() -> startEatSeeds(seed, mapLoader));
    }

    public void drinkWater(Water water, MapLoader mapLoader) {
        if (water == null || water.getLevel() < 1 || mapLoader == null) {
            return;
        }
        enqueueOrRunAction(() -> startDrinkWater(water, mapLoader));
    }

    public void chickenPets() {
        // Implement chicken pets / offspring logic when ready
    }

    public void stepOnce() {
        int[] next = pathQueue.poll();
        if (next == null) {
            runNextPendingActionIfIdle();
            return;
        }
        this.x = next[0];
        this.y = next[1];

        if (pathQueue.isEmpty()) {
            if (onArrivalCallback != null) {
                Runnable callback = onArrivalCallback;
                onArrivalCallback = null;
                callback.run();
            }
            runNextPendingActionIfIdle();
        }
    }

    private void startEatSeeds(Seed seed, MapLoader mapLoader) {
        if (seed == null || seed.getLevel() < 1 || mapLoader == null) {
            return;
        }
        List<int[]> seedSpawns = mapLoader.getSeedSpawn();
        if (seedSpawns == null || seedSpawns.isEmpty()) {
            return;
        }

        int[] target = seedSpawns.get(0);
        int[] home = getHomeSpawn(mapLoader);

        queueRoundTrip(target[0], target[1], home[0], home[1], () -> {
            this.level = Math.min(MAX_LEVEL, this.level + 1);
            seed.level = Math.min(MAX_LEVEL, seed.level + 1);
        });
    }

    private void startDrinkWater(Water water, MapLoader mapLoader) {
        if (water == null || water.getLevel() < 1 || mapLoader == null) {
            return;
        }
        List<int[]> waterSpawns = mapLoader.getWaterSpawn();
        if (waterSpawns == null || waterSpawns.isEmpty()) {
            return;
        }

        int[] target = waterSpawns.get(0);
        int[] home = getHomeSpawn(mapLoader);

        queueRoundTrip(target[0], target[1], home[0], home[1], () -> {
            this.level = Math.min(MAX_LEVEL, this.level + 1);
            water.level = Math.min(MAX_LEVEL, water.level + 1);
        });
    }

    private void enqueueOrRunAction(Runnable action) {
        if (isMoving()) {
            pendingActions.add(action);
        } else {
            action.run();
        }
    }

    private void runNextPendingActionIfIdle() {
        while (!isMoving() && !pendingActions.isEmpty()) {
            Runnable nextAction = pendingActions.poll();
            if (nextAction != null) {
                nextAction.run();
            }
        }
    }

    private int[] getHomeSpawn(MapLoader mapLoader) {
        List<int[]> spawns = mapLoader.getChickenSpawn();
        if (spawns != null && !spawns.isEmpty()) {
            return spawns.get(0);
        }
        return new int[]{this.x, this.y};
    }

    private void queueRoundTrip(int targetX, int targetY, int homeX, int homeY, Runnable onArrival) {
        pathQueue.clear();
        this.onArrivalCallback = onArrival;
        enqueueManhattanPath(this.x, this.y, targetX, targetY);
        enqueueManhattanPath(targetX, targetY, homeX, homeY);
    }

    private void enqueueManhattanPath(int fromX, int fromY, int toX, int toY) {
        int curX = fromX;
        int curY = fromY;

        while (curX != toX) {
            curX += Integer.compare(toX, curX);
            pathQueue.add(new int[]{curX, curY});
        }
        while (curY != toY) {
            curY += Integer.compare(toY, curY);
            pathQueue.add(new int[]{curX, curY});
        }
    }
}