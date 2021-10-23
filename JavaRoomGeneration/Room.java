package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Random;

public class Room {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    TETile[] doors;
    HashMap<String, TETile> corners;
    boolean validRoom;

    protected class Point{
        private int x;
        private int y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public Room(int x, int y, int length, int width, TETile[][] world){
        corners = new HashMap<>();
        corners.put("ll",world[x][y]);
        corners.put("ul",world[x][y + length]);
        corners.put("lr",world[x + width][y]);
        corners.put("ur",world[x + width][y + length]);
        validRoom = false;
        if (x + width < WIDTH && y + length < HEIGHT && x >= 0 && y >= 0){
            validRoom = true;
        }
        for (TETile t: corners.values()) {
            if (t.description().equals("wall") || t.description().equals("floor")){
                validRoom = false;
            }
        }
        if(validRoom){
            for (int curx = x; curx <= x + width; curx ++) {
                world[curx][y + length] = Tileset.WALL;
            }
            for (int curx = x; curx <= x + width; curx ++) {
                world[curx][y] = Tileset.WALL;
            }
            for (int cury = y + 1; cury < y + length; cury ++) {
                world[x][cury] = Tileset.WALL;
            }
            for (int cury = y + 1; cury < y + length; cury ++) {
                world[x + width][cury] = Tileset.WALL;
            }
            for (int cury = y + 1;cury < y + length; cury ++) {
                for (int curx = x + 1;curx < x + width; curx ++) {
                    world[curx][cury] = Tileset.FLOOR;
                }
            }
        }

    }


    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.FLOWER;
            }
        }
        Room m = new Room(49,9,20,10, world);
        ter.renderFrame(world);
        RandomUtils r = new RandomUtils();
    }
}
