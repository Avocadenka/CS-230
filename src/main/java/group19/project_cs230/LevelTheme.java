package group19.project_cs230;

import java.util.*;

public enum LevelTheme {
    GRASSLAND("Farmer.png","Chicken.png","Shelter.png", "Water.png", "Seeds.png", "Grass.png", "Wall.png");
    //CASTLE("");

    private final Map<Character, String> assetMap = new HashMap<>();

    LevelTheme(String farmer, String chicken, String shelter, String water, String seeds, String grass, String wall){
        assetMap.put('H', farmer); // Or shelter
        assetMap.put('C', chicken);
        assetMap.put('W', water);
        assetMap.put('S', seeds);
        assetMap.put('-', grass);
        assetMap.put('X', wall);
        assetMap.put('J', shelter);
    }

    public String getAsset(char character) {
        return assetMap.get(character);
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
