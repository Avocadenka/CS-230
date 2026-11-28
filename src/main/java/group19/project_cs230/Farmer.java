package group19.project_cs230;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * The Farmer is the only entity that moves. GameSceneView decides where he goes
 * (based on the game rules) and what happens when he arrives.
 */
public class Farmer extends MainEntity {

    private int x;
    private int y;

    private final MapLoader mapLoader;
    private final Queue<int[]> pathQueue = new ArrayDeque<>();
    private Runnable onArrival;
    private boolean carryingWater;

    public Farmer(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
        if (mapLoader != null && mapLoader.getFarmerSpawn() != null && !mapLoader.getFarmerSpawn().isEmpty()) {
            int[] spawn = mapLoader.getFarmerSpawn().get(0);
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

    /** True while walking, or while an arrival action is still pending. */
    public boolean isBusy() {
        return isMoving() || onArrival != null;
    }

    public boolean isCarryingWater() {
        return carryingWater;
    }

    public void setCarryingWater(boolean carryingWater) {
        this.carryingWater = carryingWater;
    }

    /**
     * Walk (around walls) to the given tile, then run onArrive.
     * If the tile cannot be reached the Farmer stays put and nothing happens.
     */
    public void goTo(int targetX, int targetY, Runnable onArrive) {
        if (mapLoader == null) {
            return;
        }
        List<int[]> path = mapLoader.findPath(x, y, targetX, targetY);
        if (path == null) {
            return;
        }
        pathQueue.clear();
        pathQueue.addAll(path);
        this.onArrival = onArrive;
        if (pathQueue.isEmpty()) {
            fireArrival(); // already standing on the tile
        }
    }

    /** Moves one tile along the current path. Called by the game timer. */
    public void stepOnce() {
        int[] next = pathQueue.poll();
        if (next == null) {
            return;
        }
        this.x = next[0];
        this.y = next[1];
        if (pathQueue.isEmpty()) {
            fireArrival();
        }
    }

    private void fireArrival() {
        Runnable callback = onArrival;
        onArrival = null; // cleared first so the callback can start a new trip
        if (callback != null) {
            callback.run();
        }
    }
}
