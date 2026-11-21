package group19.project_cs230;

/**
 * Represents a shelter entity within the game.
 * The Shelter can be built by the player as part of the progression sequence
 * and inherits common functionality such as level management from MainEntity
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class Shelter extends MainEntity {

    /** Indicates whether the shelter has been built.*/
    private boolean built;

    /** Creates a new Shelter object */
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Shelter(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getShelterSpawn() != null && !mapLoader.getShelterSpawn().isEmpty()) {
            int[] spawn = mapLoader.getShelterSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
    }

    /**
     * Returns whether the shelter has been built.
     *
     * @return true if the shelter has been built, otherwise false
     */
    public boolean isBuilt() {
        return built;
    }
}
