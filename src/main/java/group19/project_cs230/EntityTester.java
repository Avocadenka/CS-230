package group19.project_cs230;

import java.util.ArrayList;
import java.util.List;

public class EntityTester {

    public static void main(String[] args) {
        String mapResource = "/group19/project_cs230/Maps/GRASSLAND";

        System.out.println("Resource URL: " + EntityTester.class.getResource(mapResource));

        MapLoader mapLoader = new MapLoader();
        mapLoader.loadMap(mapResource);

        List<char[]> grid = mapLoader.getMapGrid();
        if (grid == null || grid.isEmpty()) {
            System.err.println("Map failed to load or is empty: " + mapResource);
            return;
        }

        List<int[]> farmerSpawns = mapLoader.getFarmerSpawn();
        List<int[]> waterSources = mapLoader.getWaterSpawn();
        List<int[]> chickenCoords = findCoordsInGrid(grid, 'C');
        List<int[]> shelterSpawns = mapLoader.getShelterSpawn();

        if (farmerSpawns.isEmpty() || waterSources.isEmpty()) {
            System.err.println("Loaded map is missing 'H' (farmer spawn) or 'W' (water source).");
            return;
        }

        int[] spawn = farmerSpawns.get(0);
        int[] waterCoord = waterSources.get(0);

        System.out.println("Theme: " + mapLoader.getCurrentTheme());
        System.out.println("Dimensions: " + mapLoader.getWidth() + "x" + mapLoader.getHeight());
        System.out.println("Parsed Farmer Spawn ('H'): (" + spawn[0] + ", " + spawn[1] + ")");
        System.out.println("Parsed Water Source ('W'): (" + waterCoord[0] + ", " + waterCoord[1] + ")");
        if (!chickenCoords.isEmpty()) {
            System.out.println("Parsed Chicken Tile ('C'): (" + chickenCoords.get(0)[0] + ", " + chickenCoords.get(0)[1] + ")");
        }
        if (shelterSpawns != null && !shelterSpawns.isEmpty()) {
            System.out.println("Parsed Shelter Spawn ('S'): (" + shelterSpawns.get(0)[0] + ", " + shelterSpawns.get(0)[1] + ")");
        } else {
            System.out.println("No shelter spawns found on map.");
        }

        Water water = new Water(mapLoader);
        Farmer farmer = new Farmer(mapLoader);
        Chicken chicken = new Chicken(mapLoader);


        // --- drinkWater() tests ---
        water.level = 0;
        System.out.println("\n=== Test 1: drinkWater() with water.level = 0 ===");
        printState("Before drinkWater()", farmer, water, grid);
        farmer.drinkWater(water, mapLoader);
        printState("After drinkWater()", farmer, water, grid);

        water.level = 1;
        System.out.println("\n=== Test 2: drinkWater() with water.level = 1 ===");
        printState("Before drinkWater()", farmer, water, grid);
        farmer.drinkWater(water, mapLoader);
        printState("After drinkWater()", farmer, water, grid);

        // --- feedChickens() tests ---

        chicken.level = 0;
        System.out.println("\n=== Test 3: feedChickens() with chicken.level = 0 ===");
        printState("Before feedChickens()", farmer, water, grid);
        farmer.feedChickens(chicken, mapLoader);
        printState("After feedChickens()", farmer, water, grid);

        chicken.level = 1;
        System.out.println("\n=== Test 4: feedChickens() with chicken.level = 1 ===");
        printState("Before feedChickens()", farmer, water, grid);
        farmer.feedChickens(chicken, mapLoader);
        printState("After feedChickens()", farmer, water, grid);

        // --- expandShelter() tests ---
        Shelter shelter = new Shelter(mapLoader);

        // Test 5: shelter.level < 1 -> farmer stays at spawn and does NOT level up
        shelter.level = 0;
        System.out.println("\n=== Test 5: expandShelter() with shelter.level = 0 ===");
        printState("Before expandShelter()", farmer, water, grid);
        farmer.expandShelter(shelter, mapLoader);
        printState("After expandShelter()", farmer, water, grid);

        // Test 6: shelter.level >= 1 -> farmer visits shelter, levels up, and returns to 'H' spawn
        shelter.level = 1;
        System.out.println("\n=== Test 6: expandShelter() with shelter.level = 1 ===");
        printState("Before expandShelter()", farmer, water, grid);
        farmer.expandShelter(shelter, mapLoader);
        printState("After expandShelter()", farmer, water, grid);
    }

    private static List<int[]> findCoordsInGrid(List<char[]> grid, char target) {
        List<int[]> matches = new ArrayList<>();
        for (int y = 0; y < grid.size(); y++) {
            char[] row = grid.get(y);
            for (int x = 0; x < row.length; x++) {
                if (row[x] == target) {
                    matches.add(new int[]{x, y});
                }
            }
        }
        return matches;
    }

    private static void printState(String label, Farmer farmer, Water water, List<char[]> grid) {
        System.out.printf("[%s] Pos: (%d, %d) | Farmer Level: %d | Water Level: %d%n",
                label, farmer.getX(), farmer.getY(), farmer.level, water.level);
        printGrid(grid, farmer.getX(), farmer.getY());
    }

    private static void printGrid(List<char[]> grid, int fx, int fy) {
        for (int y = 0; y < grid.size(); y++) {
            char[] row = grid.get(y);
            StringBuilder sb = new StringBuilder("   ");
            for (int x = 0; x < row.length; x++) {
                sb.append((x == fx && y == fy) ? 'F' : row[x]);
                if (x < row.length - 1) {
                    sb.append(' ');
                }
            }
            System.out.println(sb);
        }
    }
}