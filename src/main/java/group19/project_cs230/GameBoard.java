package group19.project_cs230;

import java.util.ArrayList;

public class GameBoard {

    private int width;
    private int height;
    private Tile[][] board;
    private ArrayList<MainEntity> entities;

    public GameBoard (int width, int height){
        this.width = width;
        this.height = height;
        board = new Tile[width][height];
        entities = new ArrayList<>();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public ArrayList getEntities(){
        return entities;
    }

    public void addEntity(MainEntity entity){
        entities.add(entity);
    }

    public void clearEntities(){
        entities.clear();
    }

    public void removeEntity(MainEntity entity){
        for (int i = 0; i < entities.size(); i++){
            MainEntity thisEntity = entities.get(i);
            if (entity == thisEntity){
                entities.remove(i);
            }
        }
    }

    public void addTile(int x, int y, Tile tile){
        board[x][y] = tile;
    }
}
