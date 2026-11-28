package group19.project_cs230;

import java.util.HashMap;
import java.util.Map;

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

    /** Base sprite file name, e.g. "Water.png". */
    public String getAsset(char character) {
        return assetMap.get(character);
    }

    /** Per-level sprite file name, e.g. 'W' + level 2 -> "Water_2.png". */
    public String getAsset(char character, int level) {
        String base = assetMap.get(character);
        if (base == null) {
            return null;
        }
        int dot = base.lastIndexOf('.');
        if (dot < 0) {
            return base + "_" + level;
        }
        return base.substring(0, dot) + "_" + level + base.substring(dot);
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
