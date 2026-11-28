package group19.project_cs230;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ThemeSpriteCache {

    private static final char[] SYMBOLS = {'-', 'H', 'J', 'C', 'W', 'S'};
    private static final String BASE_SPRITE_DIR = "/group19/project_cs230/Tiles/";

    private final Map<LevelTheme, Map<Character, Image>> cache = new EnumMap<>(LevelTheme.class);
    private final Map<String, Image> imageByAssetPath = new HashMap<>();

    public Image getSprite(LevelTheme theme, char symbol) {
        return getSprite(theme, symbol, 1);
    }

    public Image getSprite(LevelTheme theme, char symbol, int level) {
        LevelTheme baseTheme = (theme != null) ? theme : LevelTheme.GRASSLAND;
        LevelTheme resolvedTheme = LevelTheme.forLevel(level);

        String fileName = baseTheme.getAsset(symbol, level);
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = baseTheme.getAsset(symbol);
            resolvedTheme = baseTheme;
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }

        Image img = loadFromTheme(resolvedTheme, symbol, fileName.trim());
        if (img == null && resolvedTheme != LevelTheme.GRASSLAND) {
            String fallbackName = LevelTheme.GRASSLAND.getAsset(symbol);
            if (fallbackName != null && !fallbackName.trim().isEmpty()) {
                img = loadFromTheme(LevelTheme.GRASSLAND, symbol, fallbackName.trim());
            }
        }
        return img;
    }

    private Image loadFromTheme(LevelTheme theme, char symbol, String fileName) {
        String path = getThemeDirectory(theme) + fileName;
        if (imageByAssetPath.containsKey(path)) {
            return imageByAssetPath.get(path);
        }

        InputStream stream = ThemeSpriteCache.class.getResourceAsStream(path);
        if (stream == null) {
            System.err.println("Missing sprite for " + theme + " ['" + symbol + "']: " + path);
            imageByAssetPath.put(path, null);
            return null;
        }

        Image loaded = new Image(Objects.requireNonNull(stream));
        imageByAssetPath.put(path, loaded);
        cache.computeIfAbsent(theme, k -> new HashMap<>()).put(symbol, loaded);
        return loaded;
    }

    private String getThemeDirectory(LevelTheme theme) {
        if (theme == LevelTheme.CASTLE) {
            return BASE_SPRITE_DIR + "Castle/";
        }
        return BASE_SPRITE_DIR + "Grassland/";
    }

    public void clearCache() {
        cache.clear();
        imageByAssetPath.clear();
    }
}