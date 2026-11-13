package group19.project_cs230;

public class MainEntity {
    private int level;
  //  private Tile position;


    public MainEntity() {
        this.level = 0;
    }

    public void levelUp() {
        this.level++;
    }

    public int getLevel() {
        return level;
    }

    //    public Tile getPosition() {
//        return position;
//    }
//
//    public void setPosition(Tile position) {
//        this.position = position;
//    }
}
