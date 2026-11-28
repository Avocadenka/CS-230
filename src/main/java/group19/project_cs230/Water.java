package group19.project_cs230;

/**
 * Represents the Water entity in the game.
 * Water is one of the core gameplay entities and can interact with the other entities
 * such as Seeds, Chickens and Shelters to support game progression.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class Water extends MainEntity {

    /** Creates a new Water entity. */
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Water(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getWaterSpawn() != null && !mapLoader.getWaterSpawn().isEmpty()) {
            int[] spawn = mapLoader.getWaterSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
    }
}
