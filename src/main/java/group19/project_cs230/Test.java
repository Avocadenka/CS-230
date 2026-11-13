package group19.project_cs230;

public class Test {
    public static void main(String[] args){
        Shelter house = new Shelter();
        house.levelUp();
        System.out.println(house.getLevel());

        PlayerProfile susan = new PlayerProfile("Susan", Title.LADY);
        System.out.println(susan.getFullName());
        susan.moveToNextMap(2); // finished level 1
        System.out.println(susan.getCurrentLevel()); // is on level 2
        susan.moveToNextMap(4);
        susan.moveToNextMap(6);
        System.out.println(susan.getHighScore());
    }
}
