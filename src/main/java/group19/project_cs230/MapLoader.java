package group19.project_cs230;

import java.util.*;
import java.io.*;

public class MapLoader {
        private LevelTheme currentTheme;
        private List<char[]> mapGrid;
        private int width;
        private int height;

        // where things spawn
        private final List<int[]> chickenSpawns = new ArrayList<>();
        private final List<int[]> waterSources = new ArrayList<>();
        private final List<int[]> seedSpawns = new ArrayList<>();
        private final List<int[]> shelterSpawn = new ArrayList<>();
        private final List<int[]> farmerSpawns = new ArrayList<>();

        public void loadMap(String resourcePath){
            mapGrid = new ArrayList<>();
            chickenSpawns.clear();
            waterSources.clear();
            seedSpawns.clear();
            shelterSpawn.clear();
            farmerSpawns.clear();

            try (InputStream is = getClass().getResourceAsStream(resourcePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                //error case
                if (is == null) {
                    System.err.println("File not found: " + resourcePath);
                    return;
                }

                String firstLine = reader.readLine();
                if (firstLine != null) {
                    currentTheme = LevelTheme.fromString(firstLine);
                    System.out.println("Theme loaded: " + currentTheme);
                } else {
                    currentTheme = LevelTheme.GRASSLAND;
                }

                String line;
                int y = 0;

                while ((line = reader.readLine()) != null) {
                    String cleanedLine = line.replace(" ", "").trim();

                    if (cleanedLine.isEmpty()) {
                        continue;
                    }

                    char[] row = cleanedLine.toCharArray();
                    mapGrid.add(row);

                    for (int x = 0; x < row.length; x++) {
                        char c = row[x];
                        switch (c) {
                            case 'H':
                                farmerSpawns.add(new int[]{x, y});
                                break;
                            case 'J':
                                shelterSpawn.add(new int[]{x, y});
                                break;
                            case 'C':
                                chickenSpawns.add(new int[]{x, y});
                                break;
                            case 'W':
                                waterSources.add(new int[]{x, y});
                                break;
                            case 'S':
                                seedSpawns.add(new int[]{x, y});
                                break;
                            case '-':
                            case 'X':
                                // Regular tiles and walls are part of the grid
                                break;
                            default:
                                break;
                        }
                    }
                    y++;
                }

                height = mapGrid.size();
                width = height > 0 ? mapGrid.get(0).length : 0;
                System.out.println("Loaded map successfully. Dimensions: " + width + "x" + height);

            }catch (IOException e) {
                System.err.println("Error reading map file: " + e.getMessage());
                e.printStackTrace();
            }
    }
    public LevelTheme getCurrentTheme() { return currentTheme; }
    public List<char[]> getMapGrid() { return mapGrid; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<int[]> getFarmerSpawns() { return farmerSpawns; }
    public List<int[]> getShelterSpawn() { return shelterSpawn; }
    public List<int[]> getChickenSpawns() { return chickenSpawns; }
    public List<int[]> getWaterSources() { return waterSources; }
    public List<int[]> getSeedSpawns() { return seedSpawns; }
}
