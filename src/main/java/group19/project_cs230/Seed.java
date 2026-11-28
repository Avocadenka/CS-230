package group19.project_cs230;

/**
 * Seeds grow when the Farmer waters them, and can be collected once fully grown
 * (and the Shelter has been built). Collecting drops the level back by one.
 */
public class Seed extends MainEntity {

    private boolean collected = false;

    /** Called when the Farmer collects the Seeds: level drops by one and the flag is set. */
    public void collect() {
        levelDown();
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    /** Seeds regrow after being collected, once the level is complete. */
    public void regrow() {
        if (collected) {
            levelUp();
        }
    }
}
