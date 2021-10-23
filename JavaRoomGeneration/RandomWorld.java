package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


import java.util.ArrayList;

import java.util.Random;

public class RandomWorld {
    private int WIDTH;
    private int HEIGHT;
    private static final int FRAME = 3;
    TETile[][] world;
    boolean validRoom;
    Random seed;
    ArrayList<RoomNode> rooms;
    private class RoomNode{
        Point middle;
        ArrayList corners;
        public RoomNode(Point m, ArrayList c){
            corners = c;
            middle = m;
        }
    }


    public RandomWorld(Random random, TETile[][] world, int widthWorld, int heightWorld) {
        WIDTH = widthWorld;
        HEIGHT = heightWorld;
        seed = random;
        rooms = new ArrayList<>();
        this.world = world;
        int height = roomHeight();
        int width = roomWidth();
        newRoom(RandomUtils.uniform(random, FRAME + 5, WIDTH - FRAME - width - 5),
                RandomUtils.uniform(random, FRAME + 5, HEIGHT - FRAME - height - 5), roomHeight(),
                roomWidth());
    }

    protected class Point {
        private int x;
        private int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean newRoom(int x, int y, int h, int w) {
        ArrayList<Point> corners = new <Point>ArrayList();
        corners.add(new Point(x, y));
        corners.add(new Point(x, y + h));
        corners.add(new Point(x + w, y));
        corners.add(new Point(x + w, y + h));
        Point middle = new Point(x + w / 2, y + h / 2);
        validRoom = false;
        if (x + w < WIDTH - FRAME && y + h < HEIGHT - FRAME && x >= FRAME && y >= FRAME) {
            validRoom = true;
        } else {
            return false;
        }
        for (Point t: corners) {
            if (world[t.x][t.y].description().equals("wall")
                    || world[t.x][t.y].description().equals("floor")) {
                return false;
            }
        }
        rooms.add(new RoomNode(middle, corners));
        for (Point t: corners) {
            world[t.x][t.y] = Tileset.WALL;
        }
        Point[] top = new Point[w - 1];
        for (int curx = x + 1; curx < x + w; curx++) {
            if (world[curx][y + h].description().equals("floor")) {
                world[curx][y + h] = Tileset.FLOOR;
            } else {
                world[curx][y + h] = Tileset.WALL;
            }
            top[curx - x - 1] = new Point(curx, y + h);
        }
        Point[] bottom = new Point[w - 1];
        for (int curx = x + 1; curx < x + w; curx++) {
            if (world[curx][y].description().equals("floor")) {
                world[curx][y] = Tileset.FLOOR;
            } else {
                world[curx][y] = Tileset.WALL;
            }
            bottom[curx - x - 1] = new Point(curx, y);
        }
        Point[] left = new Point[h - 1];
        for (int cury = y + 1; cury < y + h; cury++) {
            if (world[x][cury].description().equals("floor")) {
                world[x][cury] = Tileset.FLOOR;
            } else {
                world[x][cury] = Tileset.WALL;
            }
            left[cury - y - 1] = new Point(x, cury);
        }
        Point[] right = new Point[h - 1];
        for (int cury = y + 1; cury < y + h; cury++) {
            if (world[x + w][cury].description().equals("floor")) {
                world[x + w][cury] = Tileset.FLOOR;
            } else {
                world[x + w][cury] = Tileset.WALL;
            }
            right[cury - y - 1] = new Point(x + w, cury);
        }
        for (int cury = y + 1; cury < y + h; cury++) {
            for (int curx = x + 1; curx < x + w; curx++) {
                world[curx][cury] = Tileset.FLOOR;
            }
        }
        if (percentchance(80)) {
            RandomUtils.shuffle(seed, top);
            topHall(top[0], hallLength());
        }
        if (percentchance(80)) {
            RandomUtils.shuffle(seed, bottom);
            bottomHall(bottom[0], hallLength());
        }
        if (percentchance(80)) {
            RandomUtils.shuffle(seed, left);
            leftHall(left[0], hallLength());
        }
        if (percentchance(80)) {
            RandomUtils.shuffle(seed, right);
            rightHall(right[0], hallLength());
        }
        return true;
    }

    private boolean percentchance(int chance) {
        if (RandomUtils.uniform(seed, 0, 100) < chance) {
            return true;
        }
        return false;
    }

    private int hallLength() {
        return RandomUtils.uniform(seed, 2, HEIGHT / 4);
    }
    private int roomWidth() {
        return RandomUtils.uniform(seed, 4, WIDTH / 5);
    }
    private int roomHeight() {
        return RandomUtils.uniform(seed, 4, HEIGHT / 5);
    }

    private void topHall(Point p, int length) {
        boolean end = true;
        for (int i = 0; i < length; i++) {
            if (world[p.x][p.y + i].description().equals("floor")) {
                break;
            }
            if (i > 0 && world[p.x][p.y + i].description().equals("wall")) {
                if (world[p.x][p.y + i + 1].description().equals("floor")) {
                    world[p.x][p.y + i] = Tileset.FLOOR;
                    break;
                }
                world[p.x][p.y + i] = Tileset.WALL;
                break;
            }
            if (i == length - 1) {
                int height = roomHeight();
                int width = roomWidth();
                end = newRoom(RandomUtils.uniform(seed, p.x - width + 1,
                        p.x - 1), p.y + i, height, width);
            }

            if (!end) {
                world[p.x - 1][p.y + i] = Tileset.WALL;
                world[p.x][p.y + i] = Tileset.WALL;
                world[p.x + 1][p.y + i] = Tileset.WALL;
                break;
            }
            if (p.y + i >= HEIGHT - FRAME) {
                world[p.x - 1][p.y + i] = Tileset.WALL;
                world[p.x][p.y + i] = Tileset.WALL;
                world[p.x + 1][p.y + i] = Tileset.WALL;
                break;
            }

            if (i == 0) {
                world[p.x][p.y] = Tileset.FLOOR;
                continue;
            }

            world[p.x - 1][p.y + i] = Tileset.WALL;
            world[p.x][p.y + i] = Tileset.FLOOR;
            world[p.x + 1][p.y + i] = Tileset.WALL;
        }
    }

    private void bottomHall(Point p, int length) {
        boolean end = true;
        for (int i = 0; i < length; i++) {
            if (world[p.x][p.y - i].description().equals("floor")) {
                break;
            }
            if (i > 0 && world[p.x][p.y - i].description().equals("wall")) {
                if (world[p.x][p.y - i - 1].description().equals("floor")) {
                    world[p.x][p.y - i] = Tileset.FLOOR;
                    break;
                }
                world[p.x][p.y - i] = Tileset.WALL;
                break;
            }
            if (i == length - 1) {
                int height = roomHeight();
                int width = roomWidth();
                end = newRoom(RandomUtils.uniform(seed, p.x - width + 1, p.x - 1),
                        p.y - i - height, height, width);
            }

            if (!end) {
                world[p.x - 1][p.y - i] = Tileset.WALL;
                world[p.x][p.y - i] = Tileset.WALL;
                world[p.x + 1][p.y - i] = Tileset.WALL;
                break;
            }
            if (p.y - i <= FRAME) {
                world[p.x - 1][p.y - i] = Tileset.WALL;
                world[p.x][p.y - i] = Tileset.WALL;
                world[p.x + 1][p.y - i] = Tileset.WALL;
                break;
            }

            if (i == 0) {
                world[p.x][p.y] = Tileset.FLOOR;
                continue;
            }

            world[p.x - 1][p.y - i] = Tileset.WALL;
            world[p.x][p.y - i] = Tileset.FLOOR;
            world[p.x + 1][p.y - i] = Tileset.WALL;
        }
    }

    private void leftHall(Point p, int length) {
        boolean end = true;
        for (int i = 0; i < length; i++) {
            if (world[p.x - i][p.y].description().equals("floor")) {
                break;
            }
            if (i > 0 && world[p.x - i][p.y].description().equals("wall")) {
                if (world[p.x - i - 1][p.y].description().equals("floor")) {
                    world[p.x - i][p.y] = Tileset.FLOOR;
                    break;
                }
                world[p.x - i][p.y] = Tileset.WALL;
                break;
            }
            if (i == length - 1) {
                int height = roomHeight();
                int width = roomWidth();
                end = newRoom(p.x - i - width, RandomUtils.uniform(seed, p.y - height + 1, p.y - 1),
                        height, width);
            }
            if (!end) {
                world[p.x - i][p.y + 1] = Tileset.WALL;
                world[p.x - i][p.y] = Tileset.WALL;
                world[p.x - i][p.y - 1] = Tileset.WALL;
                break;
            }
            if (p.x - i <= FRAME) {
                world[p.x - i][p.y - 1] = Tileset.WALL;
                world[p.x - i][p.y] = Tileset.WALL;
                world[p.x - i][p.y + 1] = Tileset.WALL;
                break;
            }

            if (i == 0) {
                world[p.x][p.y] = Tileset.FLOOR;
                continue;
            }
            world[p.x - i][p.y + 1] = Tileset.WALL;
            world[p.x - i][p.y] = Tileset.FLOOR;
            world[p.x - i][p.y - 1] = Tileset.WALL;
        }
    }

    private void rightHall(Point p, int length) {
        boolean end = true;
        for (int i = 0; i < length; i++) {
            if (world[p.x + i][p.y].description().equals("floor")) {
                break;
            }
            if (p.x + i >= WIDTH - FRAME) {
                world[p.x + i][p.y - 1] = Tileset.WALL;
                world[p.x + i][p.y] = Tileset.WALL;
                world[p.x + i][p.y + 1] = Tileset.WALL;
                break;
            }
            if (i > 0 && world[p.x + i][p.y].description().equals("wall")) {
                if (world[p.x + i + 1][p.y].description().equals("floor")) {
                    world[p.x + i][p.y] = Tileset.FLOOR;
                    break;
                }
                world[p.x + i][p.y] = Tileset.WALL;
                break;
            }
            if (i == length - 1) {
                int height = roomHeight();
                int width = roomWidth();
                end = newRoom(p.x + i, RandomUtils.uniform(seed, p.y - height + 1, p.y - 1),
                        height, width);
            }
            if (!end) {
                world[p.x + i][p.y - 1] = Tileset.WALL;
                world[p.x + i][p.y] = Tileset.WALL;
                world[p.x + i][p.y + 1] = Tileset.WALL;
                break;
            }
            if (i == 0) {
                world[p.x][p.y] = Tileset.FLOOR;
                continue;
            }
            world[p.x + i][p.y - 1] = Tileset.WALL;
            world[p.x + i][p.y] = Tileset.FLOOR;
            world[p.x + i][p.y + 1] = Tileset.WALL;
        }
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        Engine e = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);
        for (int i = 0; i < 20 ; i++) {
            ter.renderFrame(e.interactWithInputString("n" + RandomUtils.uniform(new Random(),3000,70000) + "s"));
            StdDraw.pause(6000);
        }

    }
}
