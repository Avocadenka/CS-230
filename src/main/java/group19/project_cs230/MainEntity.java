package group19.project_cs230;

/**
 * The MainEntity class acts as the superclass for all core game entities.
 * It provides shared functionality such as level management that can be inherited by subclasses
 * including Farmer, Water, Seed, /chicken and Shelter
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class MainEntity {

    /**The current level of the entity. */
    public int level;

    //  private Tile position;

    /** Constructs a MainEntity object with an initial level of 0. */
    public MainEntity() {
        this.level = 0;
    }

    /** Increases the entity's level by one. */
    public static final int MAX_LEVEL = 5;

    public void levelUp() {
        if (this.level < MAX_LEVEL) {
            this.level++;
        }
    }

    /**
     * Returns the current level of the entity
     *
     * @return the current level of the entity
     */
    public int getLevel() {
        return level;
    }

    //    public Tile getPosition() {
    //        return position;
    //    }
    //
    //    public void setPosition(Tile position) {
    //        this.position = position;
    //    }
}
