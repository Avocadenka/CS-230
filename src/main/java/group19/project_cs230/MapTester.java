package group19.project_cs230;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class MapTester {

    public static void main(String[] args) {
        System.out.println("=== Starting MapLoader Test ===");

        // 1. Create a temporary test map file to load from
        String testFileName = "/GRASSLAND.txt";


        // 2. Instantiate the MapLoader
        MapLoader loader = new MapLoader();


        System.out.println("\n--- Loading Map ---");
        loader.loadMap("/GRASSLAND.txt");

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
        printSpawns("Farmer Spawns (H)", loader.getFarmerSpawns());
        printSpawns("Shelter Spawns (J)", loader.getShelterSpawn());
        printSpawns("Chicken Spawns (C)", loader.getChickenSpawns());
        printSpawns("Water Sources (W)", loader.getWaterSources());
        printSpawns("Seed Spawns (S)", loader.getSeedSpawns());

        System.out.println("\n=== MapLoader Test Complete ===");
    }


    private static void printSpawns(String label, java.util.List<int[]> spawns) {
        System.out.print(label + ": ");
        if (spawns.isEmpty()) {
            System.out.println("None found.");
            return;
        }
        for (int[] coord : spawns) {
            System.out.print(Arrays.toString(coord) + " ");
        }
        System.out.println();
    }
}