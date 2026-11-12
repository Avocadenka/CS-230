package group19.project_cs230;

import java.util.*;
import java.io.*;

public class MapLoader {
        private LevelTheme currentTheme;
        private List<char[]> mapgrid;
        private int width;
        private int height;

        private final List<int[]> chickenSpawns = new ArrayList<>();
        private final List<int[]> waterSources = new ArrayList<>();
        private final List<int[]> seedSpawns = new ArrayList<>();
        private final List<int[]> shelterSpawn = new ArrayList<>();

        public void loadMap(String resourcePath){
            mapGrid = new ArrayList<>();
            chickenSpawns.clear();
            waterSources.clear();
            seedSpawns.clear();
            shelterSpawn.clear();

            if (is == null) {
                System.err.println("File not found!");
                return;
            }

            String firstLine = reader.readLine();
            if (firstLine != null){
                firstLine = firstLine.trim();
                System.out.println("Line 1 is: " + firstLine);

                if (firstLine  == "GRASSLAND"){
                    "H" = "Farmer.png";
                    "C" = "Chicken.png";
            }
        }

    }
}
