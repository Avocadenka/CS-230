package group19.project_cs230;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ThemeSpriteCache {
    private static final char[] SYMBOLS = {'-', 'X', 'H', 'J', 'C', 'W', 'S'};
    private static final String SPRITE_DIR = "/group19/project_cs230/Tiles/Grassland/";

    private final Map<LevelTheme, Map<Character, Image>> cache = new EnumMap<>(LevelTheme.class);

    public Image getSprite(LevelTheme theme, char symbol) {
        LevelTheme activeTheme = (theme != null) ? theme : LevelTheme.GRASSLAND;
        Map<Character, Image> themeSprites = cache.get(activeTheme);
        if (themeSprites == null) {
            themeSprites = loadTheme(activeTheme);
            cache.put(activeTheme, themeSprites);
        }

        Image img = themeSprites.get(symbol);
        if (img == null && activeTheme != LevelTheme.GRASSLAND) {
            img = getSprite(LevelTheme.GRASSLAND, symbol);
        }
        return img;
    }

    private Map<Character, Image> loadTheme(LevelTheme theme) {
        Map<Character, Image> map = new HashMap<>();
        for (char c : SYMBOLS) {
            String fileName = theme.getAsset(c);
            if (fileName == null || fileName.trim().isEmpty()) {
                continue;
            }
            String path = SPRITE_DIR + fileName.trim();
            InputStream stream = ThemeSpriteCache.class.getResourceAsStream(path);
            if (stream == null) {
                System.err.println("Missing sprite for " + theme + " ['" + c + "']: " + path);
                continue;
            }
            map.put(c, new Image(Objects.requireNonNull(stream)));
        }
        return map;
    }
}