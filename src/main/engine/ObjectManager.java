package main.engine;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.joml.Vector3f;

import main.engine.objects.Bomb;
import main.engine.objects.Brick;
import main.engine.objects.Player;
import main.engine.objects.Flame;
import main.engine.objects.Grass;
import main.engine.objects.Wall;
import main.engine.testGUI.Mesh;
import main.engine.testGUI.Texture;

public class ObjectManager {
    public static GameObject[][] tileMap;
    public static Brick[][] tileBrick;
    public static Bomb[][] tileBomb;
    public static Flame[][] tileFlame;
    public static Player player1;
    public static Player player2;
    public static int width;
    public static int height;
    public static float durationTimeBomb;
    public static float durationTimeFlame;

    public static void createMap() throws Exception {
        // Create the Mesh
        float[] positions = new float[]{
                // V0
                1.0f, -1.0f, 0.0f,
                // V1
                1.0f,  0.0f, 0.0f,
                // V2
                0.0f,  0.0f, 0.0f,
                // V3
                0.0f, -1.0f, 0.0f };
        float[] textCoordsOfWall = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f };
        float[] textCoordsOfBrick = new float[]{
                0.5f, 0.0f,
                1.0f, 0.0f,
                1.0f, 0.5f,
                0.5f, 0.5f };
        float[] textCoordsOfGrass = new float[]{
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 1.0f,
                0.0f, 1.0f };
        float[] textCoordsOfPlayer = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f };
        float[] textCoordsOfBomb = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2 };

        Texture grassTexture = new Texture("src/main/engine/testGUI/grassblock.png");
        Texture wallTexture = new Texture("src/main/engine/testGUI/square.jpg");
        Texture player1Texture = new Texture("src/main/engine/testGUI/deadpool.png");
        Texture player2Texture = new Texture("src/main/engine/testGUI/capA.png");
        Texture bombTexture = new Texture("src/main/engine/testGUI/bomb.png");
        Texture brickTexture = new Texture("src/main/engine/testGUI/grassblock.png");
        Texture flameTexture = new Texture("src/main/engine/testGUI/flame.jpg");

        Mesh grassMesh = new Mesh(positions, textCoordsOfGrass, indices, grassTexture);
        Mesh wallMesh = new Mesh(positions, textCoordsOfWall, indices, wallTexture);
        Mesh player1Mesh = new Mesh(positions, textCoordsOfPlayer, indices, player1Texture);
        Mesh player2Mesh = new Mesh(positions, textCoordsOfPlayer, indices, player2Texture);
        Mesh bombMesh = new Mesh(positions, textCoordsOfBomb, indices, bombTexture);
        Mesh brickMesh = new Mesh (positions, textCoordsOfBrick, indices, brickTexture);
        Mesh flameMesh = new Mesh (positions, textCoordsOfBomb, indices, flameTexture);

        File file = new File("src/main/engine/testGUI/tileMap.txt");
        Scanner in = new Scanner(file);
        tileMap = new GameObject[16][16];
        tileBomb = new Bomb[16][16];
        tileBrick = new Brick[16][16];
        tileFlame = new Flame[16][16];

        for (int y = 8; y >= -7; --y) {
            for (int x = -7; x <= 8; ++x) {

                int dk = in.nextInt();
                if (dk == 1) {
                    tileMap[y + 7][x + 7] = new Wall(wallMesh);
                } else {
                    tileMap[y + 7][x + 7] = new Grass(grassMesh);
                }
                tileMap[y + 7][x + 7].setPosition(x, y, -14.0f);

                tileBomb[y + 7][x + 7] = new Bomb(bombMesh, 2.0f);
                tileBomb[y + 7][x + 7].setPosition(x, y, -14.0f);

                tileBrick[y + 7][x + 7] = new Brick(brickMesh);
                if (dk != 2) {
                    tileBrick[y + 7][x + 7].setVisible(false);
                }
                tileBrick[y + 7][x + 7].setPosition(x, y, -14.0f);

                tileFlame[y + 7][x + 7] = new Flame(flameMesh, 0.5f);
                tileFlame[y + 7][x + 7].setPosition(x, y, -14.0f);
                tileFlame[y + 7][x + 7].setVisible(false);
            }
        }
        in.close();

        player1 = new Player(player1Mesh);
        player1.setPosition(-6, 7, -14.0f);
        player1.setAutoMode(true);

        player2 = new Player(player2Mesh);
        player2.setPosition(-6, -3, -14.0f);
        player2.setAutoMode(false);
        width = 16;
        height = 16;
        durationTimeBomb = 2.0f;
        durationTimeFlame = 0.5f;
    }

    // Tile Map function
    public static boolean IsWallOfGameObject(int x, int y) {
        return (tileMap[y][x] instanceof Wall);
    }

    // Flame function
    public static void setTimeToBoomOfFlame(int x, int y, float timeToBoom) {
        tileFlame[y][x].setTimeToBoom(timeToBoom);
    }

    public static void setStartOfFlame(int x, int y) {
        tileFlame[y][x].start();
    }

    // Bomb function
    public static boolean isStartOfBomb(int x, int y) {
        return tileBomb[y][x].isStart();
    }

    public static float getTimeToBoomOfBomb(int x, int y) {
        return tileBomb[y][x].getTimeToBoom();
    }

    public static void startABomb(int x, int y) {
        tileBomb[y][x].start();
    }

    public static Bomb getBomb(int x, int y) {
        return tileBomb[y][x];
    }

    public static void linkBomb(int cx, int cy) {
        boolean[][] checked = new boolean[16][16];
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }

        float timeMax = (float) (tileBomb[cy][cx].getTime());

        Queue<Vector3f> queue = new LinkedList<>();
        queue.add(tileBomb[cy][cx].getPosition());

        timeMax = tileBomb[cy][cx].getTime();

        while (!queue.isEmpty()) {
            Vector3f currentBomb = queue.remove();
            Util.round1(currentBomb);
            int x = (int) (currentBomb.x + 7);
            int y = (int) (currentBomb.y + 7);
            checked[y][x] = true;

            for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
                if (y - i >= 0) {
                    if (tileMap[y - i][x] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[y - i][x].isStart()
                            && tileBomb[y - i][x].getTime() < timeMax) {
                        if (!checked[y - i][x])
                            queue.add(tileBomb[y - i][x].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
                if (y + i < tileMap.length) {
                    if (tileMap[y + i][x] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[y + i][x].isStart()
                            && tileBomb[y + i][x].getTime() < timeMax) {
                        if (!checked[y + i][x])
                            queue.add(tileBomb[y + i][x].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
                if (x + i < tileMap.length) {
                    if (tileMap[y][x + i] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[y][x + i].isStart()
                            && tileBomb[y][x + i].getTime() < timeMax) {
                        if (!checked[y][x + i])
                            queue.add(tileBomb[y][x + i].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
                if (x - i >= 0) {
                    if (tileMap[y][x - i] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[y][x - i].isStart()
                            && tileBomb[y][x - i].getTime() < timeMax) {
                        if (!checked[y][x - i])
                            queue.add(tileBomb[y][x - i].getPosition());
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (checked[i][j]) {
                    tileBomb[i][j].setTimeToBoom(timeMax);
                }
            }
        }
    }

    public static void setTimeFlameToBoomFromBomb(int x, int y, float timeToBoom) {

        tileFlame[y][x].setTimeToBoom(timeToBoom);

        for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
            if (y - i >= 0) {
                if (tileMap[y - i][x] instanceof Wall) {
                    break;
                }
                if (tileBomb[y - i][x].isStart()
                        && tileBomb[y - i][x].getTimeToBoom() <= tileBomb[y][x].getTimeToBoom()) {
                    break;
                }
                tileFlame[y - i][x].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
            if (y + i < tileMap.length) {
                if (tileMap[y + i][x] instanceof Wall) {
                    break;
                }
                if (tileBomb[y + i][x].isStart()
                        && tileBomb[y + i][x].getTimeToBoom() <= tileBomb[y][x].getTimeToBoom()) {
                    break;
                }
                tileFlame[y + i][x].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
            if (x + i < tileMap.length) {
                if (tileMap[y][x + i] instanceof Wall) {
                    break;
                }
                if (tileBomb[y][x + i].isStart()
                        && tileBomb[y][x + i].getTimeToBoom() <= tileBomb[y][x].getTimeToBoom()) {
                    break;
                }
                tileFlame[y][x + i].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[y][x].getPower(); ++i) {
            if (x - i >= 0) {
                if (tileMap[y][x - i] instanceof Wall) {
                    break;
                }
                if (tileBomb[y][x - i].isStart()
                        && tileBomb[y][x - i].getTimeToBoom() <= tileBomb[y][x].getTimeToBoom()) {
                    break;
                }
                tileFlame[y][x - i].setTimeToBoom(timeToBoom);
            }
        }
    }

    public static void setUpBomb() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (tileBomb[i][j].isStart()) {
                    linkBomb(j, i);
                }
            }
        }

        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                flame.setNewTimeToBoom();
            }
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (tileBomb[i][j].isStart()) {
                    setTimeFlameToBoomFromBomb(j, i, tileBomb[i][j].getTimeToBoom());
                }
            }
        }
    }

    public static boolean lanFromBomb(int x, int y) {
        if (tileMap[y][x] instanceof Wall) {
            return false;
        }
        if (tileBrick[y][x].getVisible()) {
            tileFlame[y][x].start();
            tileBrick[y][x].setVisible(false);
            return false;
        }
        if (tileBomb[y][x].isShow()) {
            tileBomb[y][x].boom();
            return false;
        }
        tileFlame[y][x].start();
        return true;
    }

    public static boolean isPotential(int cx, int cy, int x, int y, float timeSquare, float lastTimeToReach) {

        float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
        if (tileMap[cy][cx] instanceof Wall) {
            return false;
        }
        if (tileBrick[cy][cx].getVisible()) {
            return false;
        }
        if (tileBomb[cy][cx].isStart()) {
            if (timeToReach <= durationTimeBomb - tileBomb[cy][cx].getTimeToBoom() + durationTimeFlame) {
                return false;
            }
        }

        for (int i = 0; i < tileFlame[cy][cx].timeToBoom.length; ++i) {
            if (timeToReach - timeSquare <= durationTimeBomb - tileFlame[cy][cx].timeToBoom[i] + durationTimeFlame) {
                return false;
            }
        }

        return true;
    }

    public static boolean isDangerous(int cx, int cy, int x, int y, float timeSquare, float lastTimeToReach) {
        float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
        if (tileMap[cy][cx] instanceof Wall) {
            return true;
        }
        if (tileBrick[cy][cx].getVisible()) {
            return true;
        }
        if (tileBomb[cy][cx].isStart()) {
            // System.out.println(tileBomb[y][x].getTimeToBoom());
            if (timeToReach - timeSquare <= durationTimeBomb - tileBomb[cy][cx].getTimeToBoom() + durationTimeFlame) {
                return true;
            }
        }
        for (int i = 0; i < tileFlame[cy][cx].timeToBoom.length; ++i) {
            if (timeToReach + timeSquare <= durationTimeBomb - tileFlame[cy][cx].timeToBoom[i] + durationTimeFlame
                    && timeToReach >= durationTimeBomb - tileFlame[cy][cx].timeToBoom[i]) {
                return true;
            }
            if (timeToReach - timeSquare <= durationTimeBomb - tileFlame[cy][cx].timeToBoom[i] + durationTimeFlame
                    && timeToReach >= durationTimeBomb - tileFlame[cy][cx].timeToBoom[i]) {
                return true;
            }
        }

        return false;
    }

    // check if Puting a bomb is save
    public static boolean testBomb(int y, int x, float timeSquare, float lastTimeToReach) throws Exception {
        y += 7;
        x += 7;

        if (isDangerous(x, y, x, y, timeSquare, lastTimeToReach - timeSquare)) {
            return false;
        }

        Bomb oldBomb = new Bomb(tileBomb[y][x]);
        oldBomb.setPosition(x - 7, y - 7, -14.0f);

        tileBomb[y][x] = new Bomb(oldBomb.getMesh(), oldBomb.getDurationTime());
        tileBomb[y][x].setPosition(x - 7, y - 7, -14.0f);

        tileBomb[y][x].start();

        boolean[][] checked = new boolean[16][16];
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }

        tileBomb[y][x].start();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(x, y));

        while (!queue.isEmpty()) {
            Pair current = (Pair) queue.remove();
            checked[current.y][current.x] = true;
            // System.out.println((current.x - 7) + " " + (current.y - 7));

            if (!(tileMap[current.y][current.x + 1] instanceof Wall)
                    && !isDangerous(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.y][current.x + 1]) {

                if (isPotential(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[y][x] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x + 1, current.y));
            }

            if (!(tileMap[current.y][current.x - 1] instanceof Wall)
                    && !isDangerous(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.y][current.x - 1]) {

                if (isPotential(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[y][x] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x - 1, current.y));
            }

            if (!(tileMap[current.y + 1][current.x] instanceof Wall)
                    && !isDangerous(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.y + 1][current.x]) {

                if (isPotential(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[y][x] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x, current.y + 1));
            }

            if (!(tileMap[current.y - 1][current.x] instanceof Wall)
                    && !isDangerous(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.y - 1][current.x]) {

                if (isPotential(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[y][x] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x, current.y - 1));
            }
        }
        tileBomb[y][x] = oldBomb;
        setUpBomb();
        return false;
    }

    public static Pair findSafePos(int x, int y, float timeSquare) {
        boolean[][] checked = new boolean[16][16];
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }

        Queue<Pair> queue = new LinkedList<>();

        Pair newPair = new Pair(x, y);
        queue.add(newPair);
        if (isPotential(x, y, x, y, timeSquare, 0.0f)) {
            return newPair;
        }

        while (!queue.isEmpty()) {

            Pair current = (Pair) queue.remove();
            checked[current.y][current.x] = true;

            if (!(tileMap[current.y][current.x + 1] instanceof Wall)
                    && !isDangerous(current.x + 1, current.y, x, y, timeSquare, 0.0f)
                    && !checked[current.y][current.x + 1]) {

                newPair = new Pair(current.x + 1, current.y,  current);
                queue.add(newPair);
                if (isPotential(current.x + 1, current.y, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.y][current.x - 1] instanceof Wall)
                    && !isDangerous(current.x - 1, current.y, x, y, timeSquare, 0.0f)
                    && !checked[current.y][current.x - 1]) {

                newPair = new Pair(current.x - 1, current.y, current);
                queue.add(newPair);
                if (isPotential(current.x - 1, current.y, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.y + 1][current.x] instanceof Wall)
                    && !isDangerous(current.x, current.y + 1, x, y, timeSquare, 0.0f)
                    && !checked[current.y + 1][current.x]) {

                newPair = new Pair(current.x, current.y + 1, current);
                queue.add(newPair);
                if (isPotential(current.x, current.y + 1, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.y - 1][current.x] instanceof Wall)
                    && !isDangerous(current.x, current.y - 1, x, y, timeSquare, 0.0f)
                    && !checked[current.y - 1][current.x]) {

                newPair = new Pair(current.x, current.y - 1, current);
                queue.add(newPair);
                if (isPotential(current.x, current.y - 1, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }
        }

        return newPair;
    }

    public static boolean isHaveBrick(int x2, int y2, float timeSquare, float lastTimeToReach) throws Exception {
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick.getVisible()) {

                    int x1 = (int) brick.getPosition().x;
                    int y1 = (int) brick.getPosition().y;

                    if (x1 == x2 && Math.abs(y1 - y2) == 1 && testBomb(y2, x2, timeSquare, lastTimeToReach)) {
                        return true;
                    } else if (y1 == y2 && Math.abs(x1 - x2) == 1 && testBomb(y2, x2, timeSquare, lastTimeToReach)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // check collision between x, y and map
    public static boolean checkCollision(float x, float y, boolean isInBomb, Bomb inBomb) {
        x = (float) Math.round(x * 100) / 100;
        y = (float) Math.round(y * 100) / 100;

        for (int i = 0; i < tileMap.length; ++i)
            for (int j = 0; j < tileMap[i].length; ++j) {
                if (isInBomb && tileBomb[i][j].getPosition().equals(inBomb.getPosition())) {
                    continue;
                }
                if (tileBomb[i][j].isStart() || tileBrick[i][j].getVisible()
                        || tileMap[i][j] instanceof Wall) {
                    Vector3f pos = tileMap[i][j].getPosition();
                    if (pos.x < x + 1 &&
                            x < pos.x + 1 &&
                            pos.y < y + 1 &&
                            y < pos.y + 1) {
                        return false;
                    }
                }
            }
        return true;
    }

    // check collision between x, y and targetx, targetY
    public static boolean checkCollision(float x, float y, float targetX, float targetY) {
        x = (float) Math.round(x * 100) / 100;
        y = (float) Math.round(y * 100) / 100;
        targetX = (float) Math.round(targetX * 100) / 100;
        targetY = (float) Math.round(targetY * 100) / 100;
        if (targetX < x + 1 &&
                x < targetX + 1 &&
                targetY < y + 1 &&
                y < targetY + 1) {
            return false;
        }
        return true;
    }

    public static boolean checkIfDeadpoolDead(float x, float y) {
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame.isStart()) {
                    Vector3f pos = flame.getPosition();
                    if (pos.x < x + 1 &&
                            x < pos.x + 1 &&
                            pos.y < y + 1 &&
                            y < pos.y + 1) {
                        return true;
                    }
                }
            }
        }
        for (GameObject[] tiles : tileMap) {
            for (GameObject tile : tiles) {
                if (tile instanceof Wall) {
                    Vector3f pos = tile.getPosition();
                    if (pos.x < x + 1 &&
                            x < pos.x + 1 &&
                            pos.y < y + 1 &&
                            y < pos.y + 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
