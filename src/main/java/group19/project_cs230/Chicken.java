package group19.project_cs230;

/**
 * The Chicken stays on its spawn tile. It grows when the Farmer visits it
 * (see GameSceneView.planNextFarmerTrip).
 */
public class Chicken extends MainEntity {

    private int x;
    private int y;

    public Chicken(MapLoader mapLoader) {
        if (mapLoader != null && mapLoader.getChickenSpawn() != null && !mapLoader.getChickenSpawn().isEmpty()) {
            int[] spawn = mapLoader.getChickenSpawn().get(0);
            this.x = spawn[0];
            this.y = spawn[1];
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
