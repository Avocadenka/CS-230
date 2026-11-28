package group19.project_cs230;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ThemeSpriteCache {

    private static final String BASE_SPRITE_DIR = "/group19/project_cs230/Tiles/";

    private final Map<String, Image> imageByAssetPath = new HashMap<>();

    /** Base sprite for a symbol (ground tile, or the fallback for entities). */
    public Image getSprite(LevelTheme theme, char symbol) {
        LevelTheme t = (theme != null) ? theme : LevelTheme.GRASSLAND;
        String name = t.getAsset(symbol);
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        Image img = loadFromTheme(t, symbol, name.trim(), true);
        if (img == null && t != LevelTheme.GRASSLAND) {
            String fallback = LevelTheme.GRASSLAND.getAsset(symbol);
            if (fallback != null && !fallback.trim().isEmpty()) {
                img = loadFromTheme(LevelTheme.GRASSLAND, symbol, fallback.trim(), true);
            }
        }
        return img;
    }

    /**
     * Sprite for an entity at a given ENTITY level (1-3).
     * Looks for Name_<level>.png, then lower levels, then the plain Name.png.
     * The theme always comes from the map, never from the level.
     */
    public Image getSprite(LevelTheme theme, char symbol, int level) {
        LevelTheme t = (theme != null) ? theme : LevelTheme.GRASSLAND;
        for (int l = Math.max(level, 1); l >= 1; l--) {
            String name = t.getAsset(symbol, l);
            if (name != null) {
                Image img = loadFromTheme(t, symbol, name, false); // quiet: missing is fine
                if (img != null) {
                    return img;
                }
            }
        }
        return getSprite(t, symbol);
    }

    private Image loadFromTheme(LevelTheme theme, char symbol, String fileName, boolean warn) {
        String path = getThemeDirectory(theme) + fileName;
        if (imageByAssetPath.containsKey(path)) {
            return imageByAssetPath.get(path);
        }

        try (InputStream stream = ThemeSpriteCache.class.getResourceAsStream(path)) {
            if (stream == null) {
                if (warn) {
                    System.err.println("Missing sprite for " + theme + " ['" + symbol + "']: " + path);
                }
                imageByAssetPath.put(path, null);
                return null;
            }
            Image loaded = new Image(stream);
            imageByAssetPath.put(path, loaded);
            return loaded;
        } catch (IOException e) {
            imageByAssetPath.put(path, null);
            return null;
        }
    }

    private String getThemeDirectory(LevelTheme theme) {
        if (theme == LevelTheme.CASTLE) {
            return BASE_SPRITE_DIR + "Castle/";
        }
        return BASE_SPRITE_DIR + "Grassland/";
    }

}
