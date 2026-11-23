package group19.project_cs230;

import java.util.List;

public class Farmer extends MainEntity {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Farmer(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getFarmerSpawn() != null && !mapLoader.getFarmerSpawn().isEmpty()) {
            int[] spawn = mapLoader.getFarmerSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
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

    public void eatFood(Seed seed, MapLoader mapLoader) {
        if (seed == null || seed.getLevel() < 3 || mapLoader == null) {
            return;
        }

        List<int[]> seedSpawns = mapLoader.getSeedSpawn();
        if (seedSpawns == null || seedSpawns.isEmpty()) {
            return;
        }

        int[] targetSeed = seedSpawns.get(0);
        int bestDistance = Math.abs(targetSeed[0] - this.x) + Math.abs(targetSeed[1] - this.y);

        for (int i = 1; i < seedSpawns.size(); i++) {
            int[] candidate = seedSpawns.get(i);
            int dist = Math.abs(candidate[0] - this.x) + Math.abs(candidate[1] - this.y);
            if (dist < bestDistance) {
                bestDistance = dist;
                targetSeed = candidate;
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

        // 1. Move to seed source
        this.x = targetSeed[0];
        this.y = targetSeed[1];

        // 2. Level up
        this.level++;

        // 3. Return to spawn
        this.x = returnX;
        this.y = returnY;
    }

    public void waterSeeds() {

    }

    public void feedChickens(Chicken chicken, MapLoader mapLoader) {
        if (chicken == null || chicken.getLevel() < 1 || mapLoader == null) {
            return;
        }

        List<int[]> chickenSpawns = mapLoader.getChickenSpawn();
        if (chickenSpawns == null || chickenSpawns.isEmpty()) {
            return;
        }

        // Pick the closest chicken location ('C') to the farmer's current position
        int[] targetChicken = chickenSpawns.get(0);
        int bestDistance = Math.abs(targetChicken[0] - this.x) + Math.abs(targetChicken[1] - this.y);

        for (int i = 1; i < chickenSpawns.size(); i++) {
            int[] candidate = chickenSpawns.get(i);
            int dist = Math.abs(candidate[0] - this.x) + Math.abs(candidate[1] - this.y);
            if (dist < bestDistance) {
                bestDistance = dist;
                targetChicken = candidate;
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

        // 1. Move to chicken location
        this.x = targetChicken[0];
        this.y = targetChicken[1];

        // 2. Level up
        this.level++;
        chicken.level++;

        // 3. Return to spawn
        this.x = returnX;
        this.y = returnY;
    }

    public void expandShelter(Shelter shelter, MapLoader mapLoader){
        if (shelter == null || shelter.getLevel() < 1 || mapLoader == null) {
            return;
        }

        List<int[]> shelterSpawns = mapLoader.getShelterSpawn();
        if (shelterSpawns == null || shelterSpawns.isEmpty()) {
            return;
        }

        // Pick the closest chicken location ('C') to the farmer's current position
        int[] targetShelter = shelterSpawns.get(0);
        int bestDistance = Math.abs(targetShelter[0] - this.x) + Math.abs(targetShelter[1] - this.y);

        for (int i = 1; i < shelterSpawns.size(); i++) {
            int[] candidate = shelterSpawns.get(i);
            int dist = Math.abs(candidate[0] - this.x) + Math.abs(candidate[1] - this.y);
            if (dist < bestDistance) {
                bestDistance = dist;
                targetShelter = candidate;
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

        // 1. Move to shelter location
        this.x = targetShelter[0];
        this.y = targetShelter[1];

        // 2. Level up
        this.level++;
        shelter.level++;

        // 3. Return to spawn
        this.x = returnX;
        this.y = returnY;
    }

}
