package group19.project_cs230;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the sequence of entity interactions during a level
 * and checks if the player follows the expected progression sequence.
 *
 * @author Elnur
 * @version 1.0
 */
public class SequenceCheck {

    /** History of entities interacted with in the current attempt. */
    private final List<MainEntity> sequenceHistory;

    /** Expected target sequence of entities for the current level. */
    private final List<MainEntity> targetSequence;

    /**
     * Creates a new SequenceCheck with empty sequence lists.
     */
    public SequenceCheck() {
        this.sequenceHistory = new ArrayList<>();
        this.targetSequence = new ArrayList<>();
    }

    /**
     * Records an entity interaction in the sequence history.
     *
     * @param entity the entity interacted with on the game board
     */
    public void recordEntityInput(MainEntity entity) {

    }

    /**
     * Checks whether the current sequence of entity interactions is valid.
     *
     * @return true if valid, false otherwise
     */
    public boolean validateSequence() {
        return false;
    }

    /**
     * Checks whether a bonus condition has been triggered by the sequence.
     *
     * @return true if a bonus is triggered, false otherwise
     */
    public boolean checkBonusTrigger() {
        return false;
    }

    /**
     * Resets the recorded entity interaction history.
     */
    public void resetSequence() {

    }
}