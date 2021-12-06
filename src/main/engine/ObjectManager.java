package main.engine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.openal.AL11;

import main.engine.objects.Balloom;
import main.engine.objects.Bomb;
import main.engine.objects.Brick;
import main.engine.objects.Player;
import main.engine.objects.Flame;
import main.engine.objects.Gate;
import main.engine.objects.Grass;
import main.engine.objects.Item;
import main.engine.objects.Oneal;
import main.engine.objects.Wall;
import main.engine.sound.SoundBuffer;
import main.engine.sound.SoundListener;
import main.engine.sound.SoundManager;
import main.engine.sound.SoundSource;
import main.engine.testGUI.Mesh;
import main.engine.testGUI.Texture;

public class ObjectManager {
    public static GameObject[][] tileMap;
    public static Brick[][] tileBrick;
    public static Bomb[][] tileBomb;
    public static Flame[][] tileFlame;
    public static Item[][] tileItem;
    public static Player player1;
    public static Balloom[] balloom;
    public static Oneal[] oneal;
    public static Gate gate;
    public static Player[] humanEnemy;

    public static int width;
    public static int height;
    public static float durationTimeBomb;
    public static float durationTimeFlame;

    public static float avgTimePerFrame = 1.0f/60;
    public static float totalTime = 1.0f/60;
    public static int totalDem = 1;

    public final static int BOMB = 1;
    public final static int SPEED = 2;
    public final static int POWER = 3;

    public static SoundManager soundMgr;

    public static void createMap(String path) throws Exception {
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
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2 };

        Texture grassTexture = new Texture("src/main/engine/testGUI/grassblock.png");
        Texture wallTexture = new Texture("src/main/engine/testGUI/square.jpg");
        Texture player1Texture = new Texture("src/main/engine/testGUI/deadpool.png");
        //Texture player2Texture = new Texture("src/main/engine/testGUI/capA.png");
        Texture bombTexture = new Texture("src/main/engine/testGUI/bomb.png");
        Texture brickTexture = new Texture("src/main/engine/testGUI/grassblock.png");
        Texture flameTexture = new Texture("src/main/engine/testGUI/flame.jpg");
        Texture balloomTexture = new Texture("src/main/engine/testGUI/balloom.jpg");
        Texture onealTexture = new Texture("src/main/engine/testGUI/oneal.jpg");
        Texture gateTexture = new Texture("src/main/engine/testGUI/gate.jpg");
        Texture bombItemTexture = new Texture("src/main/engine/testGUI/bombItem.jpg");
        Texture speedItemTexture = new Texture("src/main/engine/testGUI/speedItem.jpg");
        Texture powerItemTexture = new Texture("src/main/engine/testGUI/powerItem.jpg");

        Mesh grassMesh = new Mesh(positions, textCoordsOfGrass, indices, grassTexture);
        Mesh wallMesh = new Mesh(positions, textCoordsOfWall, indices, wallTexture);
        Mesh player1Mesh = new Mesh(positions, textCoordsOfPlayer, indices, player1Texture);
        //Mesh player2Mesh = new Mesh(positions, textCoordsOfPlayer, indices, player2Texture);
        Mesh bombMesh = new Mesh(positions, textCoordsOfPlayer, indices, bombTexture);
        Mesh brickMesh = new Mesh (positions, textCoordsOfBrick, indices, brickTexture);
        Mesh flameMesh = new Mesh (positions, textCoordsOfPlayer, indices, flameTexture);
        Mesh balloomMesh = new Mesh (positions, textCoordsOfPlayer, indices, balloomTexture);
        Mesh onealMesh = new Mesh (positions, textCoordsOfPlayer, indices, onealTexture);
        Mesh gateMesh = new Mesh(positions, textCoordsOfPlayer, indices, gateTexture);
        Mesh bombItemMesh = new Mesh(positions, textCoordsOfPlayer, indices, bombItemTexture);
        Mesh speedItemMesh = new Mesh(positions, textCoordsOfPlayer, indices, speedItemTexture);
        Mesh powerItemMesh = new Mesh(positions, textCoordsOfPlayer, indices, powerItemTexture);

        String tileMapFile = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(tileMapFile);

        width = obj.getInt("width");
        height = obj.getInt("height");
        tileMap = new GameObject[width + 1][height + 1];
        tileBrick = new Brick[width + 1][height + 1];
        tileFlame = new Flame[width + 1][height + 1];
        tileBomb = new Bomb[width + 1][height + 1];
        tileItem = new Item[width + 1][height + 1];
        oneal = new Oneal[0];
        balloom = new Balloom[0];
        humanEnemy = new Player[0];

        JSONArray array = obj.getJSONArray("map");
        for (int y = height; y >= 1; --y) {
            String s = array.getJSONObject(height - y).getString("0");
            for (int x = 1; x <= s.length(); ++x) {
                if (s.charAt(x - 1) == 'w' || s.charAt(x - 1) == 'b') {
                    tileMap[x][y] = new Wall(wallMesh);
                } else {
                    tileMap[x][y] = new Grass(grassMesh);
                }
                tileMap[x][y].setPosition(x, y, 0);

                tileBomb[x][y] = new Bomb(bombMesh, 2.0f);
                tileBomb[x][y].setPosition(x, y, 0);

                double func = Math.random()*3;
                if (func <= 1.0f) {
                    tileItem[x][y] = new Item(bombItemMesh, BOMB);
                }
                if (func <= 2.0f && func > 1.0f) {
                    tileItem[x][y] = new Item(speedItemMesh, SPEED);
                }
                if (func <= 3.0f && func> 2.0f) {
                    tileItem[x][y] = new Item(powerItemMesh, POWER);
                }
                tileItem[x][y].setPosition(x, y, 0);
                tileItem[x][y].setVisible(false);

                tileBrick[x][y] = new Brick(brickMesh);
                tileBrick[x][y].setVisible(false);
                if (s.charAt(x - 1) == 'c') {
                    tileBrick[x][y].setVisible(true);
                }
                if (s.charAt(x - 1) == ' ') {
                    if (Math.random() <= (float)(obj.getInt("wallGenPercent"))/100) {
                        tileBrick[x][y].setVisible(true);
                    }
                }
                tileBrick[x][y].setPosition(x, y, 0);

                tileFlame[x][y] = new Flame(flameMesh, 0.5f);
                tileFlame[x][y].setPosition(x, y, 0);
                tileFlame[x][y].setVisible(false);

                if (s.charAt(x - 1) == 'p') {
                    player1 = new Player(player1Mesh);
                    player1.setPosition(x, y, 0);
                    player1.setAutoMode(false);
                }

                if (s.charAt(x - 1) == '0' || s.charAt(x - 1) == '2' || s.charAt(x - 1) == '4' ) {
                    Balloom[] newBalloom = new Balloom[balloom.length + 1];
                    for (int i = 0; i < balloom.length; ++i) {
                        newBalloom[i] = balloom[i];
                    }
                    balloom = newBalloom;
                    balloom[balloom.length - 1] = new Balloom(balloomMesh);
                    balloom[balloom.length - 1].setPosition(x, y, 0);
                }

                if (s.charAt(x - 1) == '1' || s.charAt(x - 1) == '3' || s.charAt(x - 1) == '5' ) {
                    Oneal[] newOneal = new Oneal[oneal.length + 1];
                    for (int i = 0; i < oneal.length; ++i) {
                        newOneal[i] = oneal[i];
                    }
                    oneal = newOneal;
                    oneal[oneal.length - 1] = new Oneal(onealMesh);
                    oneal[oneal.length - 1].setPosition(x, y, 0);
                }

                if (s.charAt(x - 1) == '6') {
                    Player[] newHumanEnemy = new Player[humanEnemy.length + 1];
                    for (int i = 0; i < humanEnemy.length; ++i) {
                        newHumanEnemy[i] = humanEnemy[i];
                    }
                    humanEnemy = newHumanEnemy;
                    humanEnemy[humanEnemy.length - 1] = new Player(player1Mesh);
                    humanEnemy[humanEnemy.length - 1].setPosition(x, y, 0);
                }

                if (s.charAt(x - 1) == 'e') {
                    gate = new Gate(gateMesh);
                    gate.setPosition(x, y, 0);
                }
            }
            
            durationTimeBomb = 2.0f;
            durationTimeFlame = 0.5f;

            soundMgr = new SoundManager();
            soundMgr.init();
            soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
            setupSounds();
        }
    }

    public static void setupSounds() throws Exception {

        SoundBuffer bombSound = new SoundBuffer("src/main/engine/testGUI/bombsound.ogg");
        soundMgr.addSoundBuffer(bombSound);
        SoundSource sourceBeep = new SoundSource(false, true);
        sourceBeep.setBuffer(bombSound.getBufferId());
        soundMgr.addSoundSource("BOOM", sourceBeep);
        
        
        soundMgr.setListener(new SoundListener(new Vector3f()));
    }

    // Tile Map function
    public static boolean IsWallOfGameObject(int x, int y) {
        return (tileMap[x][y] instanceof Wall);
    }

    // Flame function
    public static void setTimeToBoomOfFlame(int x, int y, float timeToBoom) {
        tileFlame[x][y].setTimeToBoom(timeToBoom);
    }

    public static void setStartOfFlame(int x, int y) {
        tileFlame[x][y].start();
    }

    // Bomb function
    public static boolean isStartOfBomb(int x, int y) {
        return tileBomb[x][y].isStart();
    }

    public static float getTimeToBoomOfBomb(int x, int y) {
        return tileBomb[x][y].getTimeToBoom();
    }

    public static void startABomb(int x, int y, int power) {
        tileBomb[x][y].start(power);
        setUpBomb();
    }

    public static Bomb getBomb(int x, int y) {
        return tileBomb[x][y];
    }

    public static void linkBomb(int cx, int cy) {
        boolean[][] checked = new boolean[width + 1][height + 1];
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
                checked[i][j] = false;
            }
        }

        float timeMax = (float) (tileBomb[cx][cy].getTime());

        Queue<Vector3f> queue = new LinkedList<>();
        queue.add(tileBomb[cx][cy].getPosition());

        timeMax = tileBomb[cx][cy].getTime();

        while (!queue.isEmpty()) {
            Vector3f currentBomb = queue.remove();
            Util.round1(currentBomb);
            int x = (int) (currentBomb.x);
            int y = (int) (currentBomb.y);
            checked[x][y] = true;

            for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
                if (x + i <= width) {
                    if (tileMap[x + i][y] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[x + i][y].isStart()
                            && tileBomb[x + i][y].getTime() < timeMax) {
                        if (!checked[x + i][y])
                            queue.add(tileBomb[x + i][y].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
                if (x - i >= 1) {
                    if (tileMap[x - i][y] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[x - i][y].isStart()
                            && tileBomb[x - i][y].getTime() < timeMax) {
                        if (!checked[x - i][y])
                            queue.add(tileBomb[x - i][y].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
                if (y + i <= height) {
                    if (tileMap[x][y + i] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[x][y + i].isStart()
                            && tileBomb[x][y + i].getTime() < timeMax) {
                        if (!checked[x][y + i])
                            queue.add(tileBomb[x][y + i].getPosition());
                        break;
                    }
                }
            }
            for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
                if (y - i >= 1) {
                    if (tileMap[x][y - i] instanceof Wall) {
                        break;
                    }
                    if (tileBomb[x][y - i].isStart()
                            && tileBomb[x][y - i].getTime() < timeMax) {
                        if (!checked[x][y - i])
                            queue.add(tileBomb[x][y - i].getPosition());
                        break;
                    }
                }
            }
        }
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
                if (checked[i][j]) {
                    tileBomb[i][j].setTimeToBoom(timeMax);
                }
            }
        }
    }

    public static void setTimeFlameToBoomFromBomb(int x, int y, float timeToBoom) {

        tileFlame[x][y].setTimeToBoom(timeToBoom);

        for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
            if (x + i <= width) {
                if (tileMap[x + i][y] instanceof Wall) {
                    break;
                }
                if (tileBomb[x + i][y] .isStart()
                        && tileBomb[x + i][y].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
                    break;
                }
                tileFlame[x + i][y].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
            if (x - i >= 1) {
                if (tileMap[x - i][y] instanceof Wall) {
                    break;
                }
                if (tileBomb[x - i][y].isStart()
                        && tileBomb[x - i][y].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
                    break;
                }
                tileFlame[x - i][y].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
            if (y + i <= height) {
                if (tileMap[x][y + i] instanceof Wall) {
                    break;
                }
                if (tileBomb[x][y + i].isStart()
                        && tileBomb[x][y + i].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
                    break;
                }
                tileFlame[x][y + i].setTimeToBoom(timeToBoom);
            }
        }
        for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
            if (y - i >= 1) {
                if (tileMap[x][y - i] instanceof Wall) {
                    break;
                }
                if (tileBomb[x][y - i].isStart()
                        && tileBomb[x][y - i].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
                    break;
                }
                tileFlame[x][y - i].setTimeToBoom(timeToBoom);
            }
        }
    }

    public static void setUpBomb() {
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
                if (tileBomb[i][j].isStart()) {
                    linkBomb(i, j);
                }
            }
        }

        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null) flame.setNewTimeToBoom();
            }
        }
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
                if (tileBomb[i][j].isStart()) {
                    setTimeFlameToBoomFromBomb(i, j, tileBomb[i][j].getTimeToBoom());
                }
            }
        }
    }

    public static boolean lanFromBomb(int x, int y) {
        if (tileMap[x][y] instanceof Wall) {
            return false;
        }
        if (tileBrick[x][y].getVisible()) {
            tileFlame[x][y].start();
            tileBrick[x][y].onCollapse();
            return false;
        }
        if (tileBomb[x][y].isShow()) {
            tileBomb[x][y].boom();
            return false;
        }
        tileFlame[x][y].start();
        return true;
    }

    public static void createItem(int x, int y) {
        if (Math.random() <= 0.3f) {
            tileItem[x][y].setVisible(true);
        }
    }

    public static void checkIfEatItem(Player player) {
        float x = (float) Math.round(player.getPosition().x * 100) / 100;
        float y = (float) Math.round(player.getPosition().y * 100) / 100;
        for (int i = 1; i <= width; ++i)
            for (int j = 1; j <= height; ++j) {
                if (tileItem[i][j].isVisible()) {
                    Vector3f pos = tileMap[i][j].getPosition();
                    if (pos.x < x + 1 &&
                        x < pos.x + 1 &&
                        pos.y < y + 1 &&
                        y < pos.y + 1) {
                        if (tileItem[i][j].getFunction() == BOMB) {
                            player.increaseBomb();
                        }
                        if (tileItem[i][j].getFunction() == SPEED) {
                            player.increaseSpeed();
                        }
                        if (tileItem[i][j].getFunction() == POWER) {
                            player.increasePower();
                        }
                        tileItem[i][j].setVisible(false);
                    }
                }
            }
    }

    public static boolean isPotential(int cx, int cy, int x, int y, float timeSquare, float lastTimeToReach) {

        float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
        if (tileMap[cx][cy] instanceof Wall) {
            return false;
        }
        if (tileBrick[cx][cy].getVisible()) {
            return false;
        }
        if (tileBomb[cx][cy].isStart()) {
            if (timeToReach <= durationTimeBomb - tileBomb[cx][cy].getTimeToBoom() + durationTimeFlame) {
                return false;
            }
        }

        // for (int i = 0; i < tileFlame[cx][cy].timeToBoom.length; ++i) {
        //     if (timeToReach - timeSquare <= durationTimeBomb - tileFlame[cx][cy].timeToBoom[i] + durationTimeFlame) {
        //         return false;
        //     }
        // }
        if (tileFlame[cx][cy].timeToBoom.length > 0) {
            return false;
        }

        return true;
    }

    public static boolean isDangerous(int cx, int cy, int x, int y, float timeSquare, float lastTimeToReach) {
        float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
        if (tileMap[cx][cy] instanceof Wall) {
            return true;
        }
        if (tileBrick[cx][cy].getVisible()) {
            return true;
        }
        if (tileBomb[cx][cy].isStart()) {
            // System.out.println(tileBomb[y][x].getTimeToBoom());
            if (timeToReach - timeSquare <= durationTimeBomb - tileBomb[cx][cy].getTimeToBoom() + durationTimeFlame) {
                return true;
            }
        }
        for (int i = 0; i < tileFlame[cx][cy].timeToBoom.length; ++i) {
            if (timeToReach + timeSquare < durationTimeBomb - tileFlame[cx][cy].timeToBoom[i]
                || timeToReach - timeSquare > durationTimeBomb - tileFlame[cx][cy].timeToBoom[i] + durationTimeFlame) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    // check if Puting a bomb is save
    public static boolean testBomb(int x, int y, float timeSquare, float lastTimeToReach, int power) throws Exception {

        if (isDangerous(x, y, x, y, timeSquare, lastTimeToReach - timeSquare)) {
            return false;
        }

        Bomb oldBomb = new Bomb(tileBomb[x][y]);
        oldBomb.setPosition(x, y, 0.0f);

        tileBomb[x][y] = new Bomb(oldBomb.getMesh(), oldBomb.getDurationTime());
        tileBomb[x][y].setPosition(x, y, 0.0f);

        // tileBomb[x][y].start(power);

        boolean[][] checked = new boolean[width + 1][height + 1];
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
                checked[i][j] = false;
            }
        }

        tileBomb[x][y].start(power);
        setUpBomb();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(x, y));

        while (!queue.isEmpty()) {
            Pair current = (Pair) queue.remove();
            checked[current.x][current.y] = true;

            if (!(tileMap[current.x + 1][current.y] instanceof Wall)
                    && !isDangerous(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.x + 1][current.y]) {

                if (isPotential(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[x][y] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x + 1, current.y));
            }

            if (!(tileMap[current.x - 1][current.y] instanceof Wall)
                    && !isDangerous(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.x - 1][current.y]) {

                if (isPotential(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[x][y] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x - 1, current.y));
            }

            if (!(tileMap[current.x][current.y + 1] instanceof Wall)
                    && !isDangerous(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.x][current.y + 1]) {

                if (isPotential(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[x][y] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x, current.y + 1));
            }

            if (!(tileMap[current.x][current.y - 1] instanceof Wall)
                    && !isDangerous(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)
                    && !checked[current.x][current.y - 1]) {

                if (isPotential(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)) {
                    tileBomb[x][y] = oldBomb;
                    setUpBomb();
                    return true;
                }
                queue.add(new Pair(current.x, current.y -1));
            }
        }
        tileBomb[x][y] = oldBomb;
        setUpBomb();
        return false;
    }

    public static Pair findSafePos(int x, int y, float timeSquare) {

        boolean[][] checked = new boolean[width + 1][height + 1];
        for (int i = 1; i <= width; ++i) {
            for (int j = 1; j <= height; ++j) {
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
            checked[current.x][current.y] = true;

            if (!(tileMap[current.x + 1][current.y] instanceof Wall)
                    && !isDangerous(current.x + 1, current.y, x, y, timeSquare, 0.0f)
                    && !checked[current.x + 1][current.y]) {

                newPair = new Pair(current.x + 1, current.y,  current);
                queue.add(newPair);
                if (isPotential(current.x + 1, current.y, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.x - 1][current.y] instanceof Wall)
                    && !isDangerous(current.x - 1, current.y, x, y, timeSquare, 0.0f)
                    && !checked[current.x - 1][current.y]) {

                newPair = new Pair(current.x - 1, current.y,  current);
                queue.add(newPair);
                if (isPotential(current.x - 1, current.y, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.x][current.y + 1] instanceof Wall)
                    && !isDangerous(current.x, current.y + 1, x, y, timeSquare, 0.0f)
                    && !checked[current.x][current.y + 1]) {

                newPair = new Pair(current.x, current.y + 1,  current);
                queue.add(newPair);
                if (isPotential(current.x, current.y + 1, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }

            if (!(tileMap[current.x][current.y - 1] instanceof Wall)
                    && !isDangerous(current.x, current.y - 1, x, y, timeSquare, 0.0f)
                    && !checked[current.x][current.y - 1]) {

                newPair = new Pair(current.x, current.y - 1,  current);
                queue.add(newPair);
                if (isPotential(current.x, current.y - 1, x, y, timeSquare, 0.0f)) {
                    return newPair;
                }
            }
        }

        return newPair;
    }

    public static boolean isHaveBrick(int x2, int y2, float timeSquare, float lastTimeToReach, int power) throws Exception {
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick != null && brick.getVisible()) {

                    int x1 = (int) brick.getPosition().x;
                    int y1 = (int) brick.getPosition().y;

                    if (x1 == x2 && Math.abs(y1 - y2) == 1 && testBomb(x2, y2, timeSquare, lastTimeToReach, power)) {
                        return true;
                    } else if (y1 == y2 && Math.abs(x1 - x2) == 1 && testBomb(x2, y2, timeSquare, lastTimeToReach, power)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // check collision between x, y and map
    public static boolean checkCollision(float x, float y, boolean isInBomb, Bomb inBomb, GameObject obj) {
        x = (float) Math.round(x * 100) / 100;
        y = (float) Math.round(y * 100) / 100;

        for (int i = 1; i <= width; ++i)
            for (int j = 1; j <= height; ++j) {
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

        if (obj instanceof Oneal || obj instanceof Balloom) {
            Vector3f pos = gate.getPosition();
            if (pos.x < x + 1 &&
                x < pos.x + 1 &&
                pos.y < y + 1 &&
                y < pos.y + 1) {
                return false;
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

    public static boolean checkIfPlayerDead(float x, float y) {
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null && flame.isStart()) {
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
                if (tile != null && tile instanceof Wall) {
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
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick != null && brick.getVisible()) {
                    Vector3f pos = brick.getPosition();
                    if (pos.x < x + 1 &&
                        x < pos.x + 1 &&
                        pos.y < y + 1 &&
                        y < pos.y + 1) {
                        return true;
                    }
                }
            }
        }

        for (Balloom ball : balloom) {
            Vector3f pos = ball.getPosition();
            if (!ball.isDead()&&
                pos.x < x + 1 &&
                x < pos.x + 1 &&
                pos.y < y + 1 &&
                y < pos.y + 1) {
                return true;
            }
        }
        
        for (Oneal one : oneal) {
            Vector3f pos = one.getPosition();
            if (!one.isDead()&&
                pos.x < x + 1 &&
                x < pos.x + 1 &&
                pos.y < y + 1 &&
                y < pos.y + 1) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkIfEnemyDead(float x, float y) {
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null && flame.isStart()) {
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
                if (tile != null && tile instanceof Wall) {
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
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick != null && brick.getVisible()) {
                    Vector3f pos = brick.getPosition();
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
