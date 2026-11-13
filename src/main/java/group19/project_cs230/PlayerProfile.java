package group19.project_cs230;

import java.util.HashMap;

public class PlayerProfile {
    private String name;
    private Title title;
    private int levelsUnlocked;
    private HashMap<Integer, Integer> numberOfAttempts;
    private static final int PLAYER_LEVEL_NEEDED_TO_FINISH = 4; // 1 more than how many maps there are eg. 3 maps
    private int highScore;

    public PlayerProfile(String name, Title title) {
        this.name = name;
        this.title = title;
        numberOfAttempts = new HashMap<>();
        this.levelsUnlocked = 1;
        // Leaderboard will have to deal with what to do if they have not finished the game.
        this.highScore = 0;
    }

    private int calculateScore() {
        // score is total attempts of all maps
        int score = 0;
        for (int i = 1; i < PLAYER_LEVEL_NEEDED_TO_FINISH; i++) {
            score = score + numberOfAttempts.get(i);
        }
        return score;
    }

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

    public void moveToNextMap(int attempts) {
        setAttempts(this.levelsUnlocked, attempts);
        this.levelsUnlocked++;
        // check game is finished
        if (levelsUnlocked == PLAYER_LEVEL_NEEDED_TO_FINISH) {
            this.highScore = calculateScore();
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public int getCurrentLevel() {
        return levelsUnlocked;
    }

    public int getAttempts(int level) {
        return numberOfAttempts.get(level);
    }

    private void setAttempts(int level, int attempts) {
        numberOfAttempts.put(level, attempts);
    }
}
