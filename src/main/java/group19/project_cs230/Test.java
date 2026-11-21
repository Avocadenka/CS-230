package group19.project_cs230;

/**
 * Test class used to verify functionality of implemented classes during development.
 *
 * @author Group 19
 * @version 1.0*/
public class Test {

    /** Runs simple tests for the current implementation.
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
//        Shelter house = new Shelter();
//        house.levelUp();
//        System.out.println(house.getLevel());

        PlayerProfile susan = new PlayerProfile("Susan", Title.LADY);
        System.out.println(susan.getFullName());
        susan.moveToNextMap(2); // finished level 1
        System.out.println(susan.getCurrentLevel()); // is on level 2
        susan.moveToNextMap(4);
        susan.moveToNextMap(6);
        System.out.println(susan.getHighScore());
    }
}
