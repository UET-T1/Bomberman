package main.engine.objects;

import java.io.File;
import java.util.Scanner;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.GameLogic;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Camera;
import main.engine.testGUI.Mesh;
import main.engine.testGUI.Texture;

//Bomman Game Logic
public class BommanGame implements GameLogic {

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[][] tileMap;
    private Bomb[][] tileBomb;
    private StaticItem[][] tileBrokenWall;

    private Deadpool deadpool;

    private CapA capA;

    //Constructor
    public BommanGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    //initial function
    @Override
    public void create(Window window) throws Exception {
        renderer.init(window);
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
        float[] textCoordsOfBrokenWall = new float[]{
                0.5f, 0.0f,
                1.0f, 0.0f,
                1.0f, 0.5f,
                0.5f, 0.5f };
        float[] textCoordsOfGrass = new float[]{
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 1.0f,
                0.0f, 1.0f };
        float[] textCoordsOfDeadpool = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f };
        float[] textCoordsOfCapA= new float[]{
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
        Texture deadpoolTexture = new Texture("src/main/engine/testGUI/deadpool.png");
        Texture capATexture = new Texture("src/main/engine/testGUI/capA.png");
        Texture bombTexture = new Texture("src/main/engine/testGUI/bomb.png");
        Texture brokenWallTexture = new Texture("src/main/engine/testGUI/grassblock.png");

        Mesh grassMesh = new Mesh(positions, textCoordsOfGrass, indices, grassTexture);
        Mesh wallMesh = new Mesh(positions, textCoordsOfWall, indices, wallTexture);
        Mesh deadpoolMesh = new Mesh(positions, textCoordsOfDeadpool, indices, deadpoolTexture);
        Mesh capAMesh = new Mesh(positions, textCoordsOfCapA, indices, capATexture);
        Mesh bombMesh = new Mesh(positions, textCoordsOfBomb, indices, bombTexture);
        Mesh brokenWallMesh = new Mesh (positions, textCoordsOfBrokenWall, indices, brokenWallTexture);

        File file = new File("src/main/engine/testGUI/tileMap.txt");
        Scanner in = new Scanner(file);
        tileMap = new GameItem[16][16];
        tileBomb = new Bomb[16][16];
        tileBrokenWall = new StaticItem[16][16];
        for (int y = 8 ; y >= -7; --y) {
            for (int x = -7; x <= 8; ++x) {
                int dk = in.nextInt();
                if (dk == 1) {
                    tileMap[y + 7][x + 7] = new StaticItem(wallMesh);
                } else {
                    tileMap[y + 7][x + 7] = new GameItem(grassMesh);
                }
                tileBomb[y + 7][x + 7] = new Bomb(bombMesh);
                tileMap[y + 7][x + 7].setPosition(x, y, -14.0f);
                tileBomb[y + 7][x + 7].setPosition(x, y, -14.0f);
                tileBrokenWall[y + 7][x + 7]= new StaticItem(brokenWallMesh);
                if (dk != 2) {
                    tileBrokenWall[y + 7][x + 7].setVisible(false);
                }
                tileBrokenWall[y + 7][x + 7].setPosition(x, y, -14.0f);
            }
        }
        in.close();
        deadpool = new Deadpool(deadpoolMesh);
        deadpool.setPosition(-6, 7, -14.0f);

        capA = new CapA(capAMesh);
        capA.setPosition(7, -5, -14.0f);
    }

    @Override
    public void input(Window window, Input input) {
        cameraInc.set(0, 0, 0);

        deadpool.setSpeed(0.2f);
        deadpool.setTargetPosition(capA.getPosition());
        deadpool.move(window, input, tileMap, tileBrokenWall, tileBomb);
        deadpool.takeBomb(window, input, tileMap, tileBrokenWall, tileBomb);

        capA.setSpeed(0.1f);
        capA.setTargetPosition(deadpool.getPosition());
        capA.move(window, input, tileMap, tileBrokenWall, tileBomb);

        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                bomb.boom(tileBrokenWall);
            }
        }
    }

    @Override
    public void update(float interval, Input input, Window window) {

    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera);
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                //bomb.boom();
                if (bomb.isStart()) {
                    renderer.render(bomb);
                }
            }
        }
        renderer.render(deadpool);
        renderer.render(capA);
        for (StaticItem[] brokenWalls : tileBrokenWall) {
            for (StaticItem brokenWall : brokenWalls) {
                if (brokenWall.getVisible()) {
                    renderer.render(brokenWall);
                }
            }
        }
        for (GameItem[] gameItems : tileMap) {
            for(GameItem gameItem : gameItems) {
                renderer.render(gameItem);
            }
        }
        renderer.finishRender();
    }

    @Override
    public void destroy() {
        renderer.cleanup();
        for (GameItem[] gameItems : tileMap) {
            for(GameItem gameItem : gameItems) {
                if (gameItem.getMesh() != null) {
                    gameItem.getMesh().cleanUp();
                }
            }
        }
        for (Bomb[] bombs : tileBomb) {
            for(Bomb bomb : bombs) {
                if (bomb.getMesh() != null) {
                    bomb.getMesh().cleanUp();
                }
            }
        }
        for (StaticItem[] brokenWalls : tileBrokenWall) {
            for(StaticItem brokenWall : brokenWalls) {
                if (brokenWall.getMesh() != null) {
                    brokenWall.getMesh().cleanUp();
                }
            }
        }
        deadpool.getMesh().cleanUp();
        capA.getMesh().cleanUp();
    }
    
}
