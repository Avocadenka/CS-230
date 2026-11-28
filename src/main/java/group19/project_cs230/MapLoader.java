package group19.project_cs230;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapLoader {
    private static final String DEFAULT_SEQUENCE = "SWHCJ";

    private LevelTheme currentTheme = LevelTheme.GRASSLAND;
    private List<char[]> mapGrid = new ArrayList<>();
    private int width;
    private int height;

    /** Activation order that wins this map, e.g. "SWHCJ" (Seeds, Water, Human, Chicken, Shelter). */
    private String winSequence = DEFAULT_SEQUENCE;

    // Where things spawn: each int[] is {x, y}
    private final List<int[]> chickenSpawn = new ArrayList<>();
    private final List<int[]> waterSpawn = new ArrayList<>();
    private final List<int[]> seedSpawn = new ArrayList<>();
    private final List<int[]> shelterSpawn = new ArrayList<>();
    private final List<int[]> farmerSpawn = new ArrayList<>();

    public void loadMap(String resourcePath) {
        mapGrid = new ArrayList<>();
        chickenSpawn.clear();
        waterSpawn.clear();
        seedSpawn.clear();
        shelterSpawn.clear();
        farmerSpawn.clear();
        winSequence = DEFAULT_SEQUENCE;
        width = 0;
        height = 0;

        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            System.err.println("Invalid resource path.");
            return;
        }

        String normalizedPath = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        InputStream is = MapLoader.class.getResourceAsStream(normalizedPath);
        if (is == null) {
            System.err.println("File not found on classpath: " + normalizedPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String firstLine = reader.readLine();
            if (firstLine != null && !firstLine.trim().isEmpty()) {
                currentTheme = LevelTheme.fromString(firstLine.trim());
                System.out.println("Theme loaded: " + currentTheme);
            } else {
                currentTheme = LevelTheme.GRASSLAND;
            }

            String line;
            int y = 0;
            int maxWidth = 0;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                // Optional line: SEQUENCE SWHCJ  (must be checked before spaces are stripped)
                if (trimmed.toUpperCase().startsWith("SEQUENCE")) {
                    winSequence = trimmed.substring("SEQUENCE".length()).trim().replace(" ", "").toUpperCase();
                    //continue;
                }

                String cleanedLine = line.replace(" ", "").trim();
                if (cleanedLine.isEmpty()) {
                    continue;
                }

                char[] row = cleanedLine.toCharArray();
                mapGrid.add(row);
                if (row.length > maxWidth) {
                    maxWidth = row.length;
                }

                for (int x = 0; x < row.length; x++) {
                    char c = row[x];
                    switch (c) {
                        case 'H':
                            farmerSpawn.add(new int[]{x, y});
                            break;
                        case 'J':
                            shelterSpawn.add(new int[]{x, y});
                            break;
                        case 'C':
                            chickenSpawn.add(new int[]{x, y});
                            break;
                        case 'W':
                            waterSpawn.add(new int[]{x, y});
                            break;
                        case 'S':
                            seedSpawn.add(new int[]{x, y});
                            break;
                        case '-':
                        case 'X':
                            // Regular tiles and walls are part of the grid
                            break;
                        default:
                            // Ignore unrecognized characters
                            break;
                    }
                }
                y++;
            }

            height = mapGrid.size();
            width = maxWidth;
            System.out.println("Loaded map successfully. Dimensions: " + width + "x" + height);

        } catch (IOException e) {
            System.err.println("Error reading map file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** True if the tile is inside the map and is not a wall ('X'). */
    public boolean isWalkable(int x, int y) {
        if (y < 0 || y >= mapGrid.size()) {
            return false;
        }
        char[] row = mapGrid.get(y);
        return x >= 0 && x < row.length && row[x] != 'X';
    }

    /**
     * Shortest path (BFS) from start to target, excluding the start tile.
     * Returns an empty list if already there, or null if unreachable.
     */
    public List<int[]> findPath(int startX, int startY, int targetX, int targetY) {
        if (!isWalkable(startX, startY) || !isWalkable(targetX, targetY)) {
            return null;
        }
        int[][] previous = new int[height * width][];
        boolean[] seen = new boolean[height * width];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startX, startY});
        seen[startY * width + startX] = true;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == targetX && current[1] == targetY) {
                LinkedList<int[]> path = new LinkedList<>();
                int[] step = current;
                while (!(step[0] == startX && step[1] == startY)) {
                    path.addFirst(step);
                    step = previous[step[1] * width + step[0]];
                }
                return path;
            }
            for (int[] d : directions) {
                int nx = current[0] + d[0];
                int ny = current[1] + d[1];
                if (isWalkable(nx, ny) && !seen[ny * width + nx]) {
                    seen[ny * width + nx] = true;
                    previous[ny * width + nx] = current;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return null;
    }

    public String getWinSequence() { return winSequence; }
    public LevelTheme getCurrentTheme() { return currentTheme; }
    public List<char[]> getMapGrid() { return mapGrid; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<int[]> getFarmerSpawn() { return farmerSpawn; }
    public List<int[]> getShelterSpawn() { return shelterSpawn; }
    public List<int[]> getChickenSpawn() { return chickenSpawn; }
    public List<int[]> getWaterSpawn() { return waterSpawn; }
    public List<int[]> getSeedSpawn() { return seedSpawn; }
}
