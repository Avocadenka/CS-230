package group19.project_cs230;

import java.util.Arrays;
import java.util.List;

public class MapTester {
// DO NOT SUBMIT GEMINI MADE THIS
    private static final String TEST_MAP_RESOURCE = "/group19/project_cs230/Maps/GRASSLAND";

    public static void main(String[] args) {
        System.out.println("=== Starting MapLoader Test ===");

        // 1. Instantiate the MapLoader
        MapLoader loader = new MapLoader();

        // 2. Load map from the classpath (e.g., src/main/resources/group19/project_cs230/Maps/GRASSLAND)
        System.out.println("\n--- Loading Map ---");
        loader.loadMap(TEST_MAP_RESOURCE);

        // 3. Verify Theme
        System.out.println("\n--- Verifying Theme ---");
        System.out.println("Loaded Theme: " + loader.getCurrentTheme());

        // 4. Verify Dimensions
        System.out.println("\n--- Verifying Dimensions ---");
        System.out.println("Width: " + loader.getWidth());
        System.out.println("Height: " + loader.getHeight());

        // 5. Verify Grid Content
        System.out.println("\n--- Verifying Grid Content ---");
        if (loader.getMapGrid() != null && !loader.getMapGrid().isEmpty()) {
            int rowIdx = 0;
            for (char[] row : loader.getMapGrid()) {
                System.out.println("Row " + rowIdx++ + ": " + Arrays.toString(row));
            }
        } else {
            System.err.println("FAILED: Map grid is empty or null!");
        }

        // 6. Verify Spawn Coordinates
        System.out.println("\n--- Verifying Spawn Lists ---");
        printSpawns("Farmer Spawns (H)", loader.getFarmerSpawn());
        printSpawns("Shelter Spawns (J)", loader.getShelterSpawn());
        printSpawns("Chicken Spawns (C)", loader.getChickenSpawn());
        printSpawns("Water Sources (W)", loader.getWaterSpawn());
        printSpawns("Seed Spawns (S)", loader.getSeedSpawn());

        System.out.println("\n=== MapLoader Test Complete ===");
    }

    private static void printSpawns(String label, List<int[]> spawns) {
        System.out.print(label + ": ");
        if (spawns == null || spawns.isEmpty()) {
            System.out.println("None found.");
            return;
        }
        for (int[] coord : spawns) {
            System.out.print(Arrays.toString(coord) + " ");
        }
        System.out.println();
    }
}