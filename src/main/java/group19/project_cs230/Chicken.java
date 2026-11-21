package group19.project_cs230;

import java.util.List;

public class Chicken extends MainEntity{

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Chicken(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getChickenSpawn() != null && !mapLoader.getChickenSpawn().isEmpty()) {
            int[] spawn = mapLoader.getChickenSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
    }

    public void layEgg(){

    }

    public void eatSeed(){

    }

    public void drinkWater(Water water, MapLoader mapLoader) {
        if (water == null || water.getLevel() < 1 || mapLoader == null) {
            return;
        }

        List<int[]> waterSpawns = mapLoader.getWaterSpawn();
        if (waterSpawns == null || waterSpawns.isEmpty()) {
            return;
        }

        int[] targetWater = waterSpawns.get(0);
        int bestDistance = Math.abs(targetWater[0] - this.x) + Math.abs(targetWater[1] - this.y);

        for (int i = 1; i < waterSpawns.size(); i++) {
            int[] candidate = waterSpawns.get(i);
            int dist = Math.abs(candidate[0] - this.x) + Math.abs(candidate[1] - this.y);
            if (dist < bestDistance) {
                bestDistance = dist;
                targetWater = candidate;
            }
        }

        // Pull 'H' spawn from MapLoader, falling back to current position if none exists
        int returnX = this.x;
        int returnY = this.y;
        List<int[]> spawns = mapLoader.getFarmerSpawn();
        if (spawns != null && !spawns.isEmpty()) {
            returnX = spawns.get(0)[0];
            returnY = spawns.get(0)[1];
        }

        // 1. Move to water source
        this.x = targetWater[0];
        this.y = targetWater[1];

        // 2. Level up
        this.level++;

        // 3. Return to spawn
        this.x = returnX;
        this.y = returnY;
    }

    public void chickenPets(){

    }

}
