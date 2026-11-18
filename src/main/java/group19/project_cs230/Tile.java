package group19.project_cs230;

public class Tile {
    private int width;
    private final int DEFAULT_WIDTH = 50;
    private boolean navigable;
    private String filepath;

    public Tile(int width){
        this.width = width;
    }

    public Tile(){
        width = DEFAULT_WIDTH;
    }

    public int getWidth(){
        return width;
    }

    public boolean isNavigable() {
        return navigable;
    }

    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
    }

    public void setFilepath(String path){
        filepath = path;
    }

    public String getFilepath(){
        return filepath;
    }
}
