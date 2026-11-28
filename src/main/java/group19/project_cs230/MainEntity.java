package group19.project_cs230;

/**
 * The MainEntity class acts as the superclass for all core game entities.
 * It provides shared functionality such as level management that can be inherited by subclasses
 * including Farmer, Water, Seed, Chicken and Shelter
 *
 * @author Lilian & Christianna
 * @version 1.1
 */
public class MainEntity {

    /** Highest level any entity can reach (per the functional spec). */
    public static final int MAX_LEVEL = 3;

    /** The current level of the entity. 0 = not activated yet. */
    public int level;

    /** Constructs a MainEntity object with an initial level of 0. */
    public MainEntity() {
        this.level = 0;
    }

    /** Increases the entity's level by one (up to MAX_LEVEL). */
    public void levelUp() {
        if (this.level < MAX_LEVEL) {
            this.level++;
        }
    }

    /** Decreases the entity's level by one (never below 0). Used when Seeds are collected. */
    public void levelDown() {
        if (this.level > 0) {
            this.level--;
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
}
