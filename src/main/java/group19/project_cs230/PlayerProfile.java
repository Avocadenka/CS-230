package group19.project_cs230;

import java.util.HashMap;

/**
 * Stores information relating to a player,
 * including their name, title, unlocked levels, number of attempts and high score.
 * The class is responsible for tracking player progression throughout the game.
 *
 * @author Lilian & Christianna
 * @version 1.0
 */
public class PlayerProfile {

    /** The player's name*/
    private String name;

    /** The title assigned to the player*/
    private Title title;

    /** The highest unlocked level.*/
    private int levelsUnlocked;

    /** Stores the number of attempts made for each level.*/
    private HashMap<Integer, Integer> numberOfAttempts;

    /**
     * The level number required to complete the game.
     * This should be one greater than the total number of maps
     */
    private static final int PLAYER_LEVEL_NEEDED_TO_FINISH = 4;

    /** The player's final high score */
    private int highScore;

    /**
     * Creates a new player profile.
     *
     * @param name the player's name
     * @param title the player's chosen title
     */
    public PlayerProfile(String name, Title title) {
        this.name = name;
        this.title = title;
        numberOfAttempts = new HashMap<>();
        this.levelsUnlocked = 1;
        // Leaderboard will have to deal with what to do if they have not finished the game.
        this.highScore = 0;
    }

    /**
     * Calculates the player's overall score by summing the attempts made across all completed levels.
     *
     * @return the calculated score
     */
    private int calculateScore() {
        int score = 0;
        for (int i = 1; i < PLAYER_LEVEL_NEEDED_TO_FINISH; i++) {
            score = score + numberOfAttempts.get(i);
        }
        return score;
    }

    /**
     * Return the player's full name including their title.
     *
     * @return the player's titled name
     */
    public String getFullName() {
        String title = "";
        switch (this.title) {
            case LORD:
                title = "Lord";
                break;
            case LADY:
                title = "Lady";
                break;
            default:
                title = "Liege";
                break;
        }
        return title + " " + this.name;
    }

    /**
     * Records the number of attempts made on the current level and unlocks the next level.
     * If the final level has been completed, the player's high score is calculated and stored.
     *
     * @param attempts the number of attempts made on the level
     */
    public void moveToNextMap(int attempts) {
        setAttempts(this.levelsUnlocked, attempts);
        this.levelsUnlocked++;

        if (isFinishedGame()) {
            this.highScore = calculateScore();
        }
    }

    /**
     * Returns the player's high score.
     *
     * @return the player's high score
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Returns the highest unlocked level.
     *
     * @return the highest unlocked level
     */
    public int getCurrentLevel() {
        return levelsUnlocked;
    }

    /**
     * Returns the number of attempts recorded for a level.
     *
     * @param level the level to retrieve attempts for
     * @return the number of attempts for the specified level
     */
    public int getAttempts(int level) {
        return numberOfAttempts.get(level);
    }

    /** Stores the number of attempts made for a level.
     *
     * @param level the level being updated
     * @param attempts the number of attempts made
     */
    private void setAttempts(int level, int attempts) {
        numberOfAttempts.put(level, attempts);
    }

    public boolean isFinishedGame() {
        boolean finished = false;
        if (this.levelsUnlocked == PLAYER_LEVEL_NEEDED_TO_FINISH) {
            finished = true;
        }
        return finished;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
