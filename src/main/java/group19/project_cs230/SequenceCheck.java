package group19.project_cs230;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tracks the sequence of entity interactions during a level.
 * and checks if the player follows the expected progression sequence.
 * Entities are compared by their class name (Seed, Water, Farmer, Chicken, Shelter).
 *
 * @author Elnur
 * @version 2.0
 */
public class SequenceCheck {

    /**
     * The winning sequence for each level (index 0 is level 1).
     * Level 1 is the sequence from the functional spec
     * Levels 2 and 3 are our own.
     */
    private static final String[][] LEVEL_SEQUENCES = {
            {"Seed", "Water", "Farmer", "Chicken", "Shelter"},
            {"Water", "Farmer", "Seed", "Shelter", "Chicken"},
            {"Shelter", "Farmer", "Chicken", "Water", "Seed"}
    };

    /** Number of correct opening entities needed to trigger the bonus */
    private static final int BONUS_LENGTH = 3;

    /** History of entities interacted with in the current attempt */
    private final List<MainEntity> sequenceHistory;

    /** Expected target sequence of entity names for the current level */
    private final List<String> targetSequence;

    /** Number of finished attempts (all entities activated) on the current level */
    private int attempts;

    /**
     * Creates a new SequenceCheck for level 1.
     */
    public SequenceCheck() {
        this.sequenceHistory = new ArrayList<>();
        this.targetSequence = new ArrayList<>();
        setLevel(1);
    }

    /**
     * Loads the target sequence for a level and clears the history and the attempt count.
     *
     * @param level the level number, starting at 1
     */
    public void setLevel(int level) {
        sequenceHistory.clear();
        targetSequence.clear();
        attempts = 0;
        if (level < 1 || level > LEVEL_SEQUENCES.length) {
            System.err.println("No sequence defined for level " + level);
            return;
        }
        targetSequence.addAll(Arrays.asList(LEVEL_SEQUENCES[level - 1]));
    }

    /**
     * Records an entity interaction in the sequence history.
     * Each entity can only be activated once, so repeats are ignored
     * When the last entity is recorded, the attempt is counted.
     *
     * @param entity the entity interacted with on the game board
     */
    public void recordEntityInput(MainEntity entity) {
        if (entity == null || isComplete() || sequenceHistory.contains(entity)) {
            return;
        }
        sequenceHistory.add(entity);
        if (isComplete()) {
            attempts++;
        }
    }

    /**
     * Checks whether the current sequence of entity interactions is valid:
     * every entity has been activated and the order matches the target.
     *
     * @return true if valid, false otherwise
     */
    public boolean validateSequence() {
        return isComplete() && isOnTrack();
    }

    /**
     * Checks whether the entities recorded so far match the start of the target
     * sequence. A false result means the player has already made a mistake.
     *
     * @return true if no mistake has been made yet
     */
    public boolean isOnTrack() {
        for (int i = 0; i < sequenceHistory.size(); i++) {
            if (!matchesTarget(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether every entity of the level has been activated.
     *
     * @return true if the attempt is finished (whether right or wrong)
     */
    public boolean isComplete() {
        return !targetSequence.isEmpty() && sequenceHistory.size() >= targetSequence.size();
    }

    /**
     * Checks whether a bonus condition has been triggered by the sequence.
     * The bonus is triggered when the first three entities were activated in the correct order.
     *
     * @return true if a bonus is triggered, false otherwise
     */
    public boolean checkBonusTrigger() {
        if (targetSequence.size() < BONUS_LENGTH || sequenceHistory.size() < BONUS_LENGTH) {
            return false;
        }
        for (int i = 0; i < BONUS_LENGTH; i++) {
            if (!matchesTarget(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resets the recorded entity interaction history for a new attempt.
     * The attempt count is kept.
     */
    public void resetSequence() {
        sequenceHistory.clear();
    }

    /**
     * Returns the number of finished attempts on the current level.
     * Pass this to PlayerProfile.moveToNextMap(...) when the level is won.
     *
     * @return the number of attempts
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Checks whether the entity recorded at a position is the one the target expects there.
     *
     * @param index the position in the sequence
     * @return true if it matches the target at that position
     */
    private boolean matchesTarget(int index) {
        if (index >= targetSequence.size()) {
            return false;
        }
        String name = sequenceHistory.get(index).getClass().getSimpleName();
        return name.equals(targetSequence.get(index));
    }
}