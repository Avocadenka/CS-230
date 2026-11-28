package group19.project_cs230;

import java.util.*;

public enum LevelTheme {
    GRASSLAND(
            "Farmer.png",
            "Chicken.png",
            "Shelter.png",
            "Water.png",
            "Seeds.png",
            "Grass.png"
    ),
    CASTLE(
            "Knight.png",
            "Goose.png",
            "Throne.png",
            "Wine.png",
            "Flowers.png",
            "Stone.png"
    );

    private static final int CASTLE_UNLOCK_LEVEL = 2;

    private final Map<Character, String> assetMap = new HashMap<>();

    LevelTheme(
            String farmer,
            String chicken,
            String shelter,
            String water,
            String seeds,
            String grass
    ) {
        assetMap.put('H', farmer);
        assetMap.put('C', chicken);
        assetMap.put('W', water);
        assetMap.put('S', seeds);
        assetMap.put('-', grass);
        assetMap.put('J', shelter);
    }

    public static LevelTheme forLevel(int level) {
        return level >= CASTLE_UNLOCK_LEVEL ? CASTLE : GRASSLAND;
    }

    public String getAsset(char character) {
        return assetMap.get(character);
    }

    public String getAsset(char character, int level) {
        return forLevel(level).getAsset(character);
    }

    public static LevelTheme fromString(String text) {
        if (text == null) return GRASSLAND;
        try {
            return LevelTheme.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown theme '" + text + "', defaulting to GRASSLAND.");
            return GRASSLAND;
        }
    }
}