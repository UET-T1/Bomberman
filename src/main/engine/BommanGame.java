package main.engine;

import org.joml.Vector3f;

import main.engine.objects.Balloom;
import main.engine.objects.Bomb;
import main.engine.objects.Brick;
import main.engine.objects.Player;
import main.engine.objects.Flame;
import main.engine.objects.Gate;
import main.engine.objects.Item;
import main.engine.objects.Oneal;
import main.engine.testGUI.Camera;

import static org.lwjgl.glfw.GLFW.*;

public class BommanGame implements GameLogic {
    private static GameObject[][] tileMap;
    private static Brick[][] tileBrick;
    private static Bomb[][] tileBomb;
    private static Flame[][] tileFlame;
    private static Player player1;
    //private static Player player2;
    private static Balloom[] balloom;
    private static Oneal[] oneal;
    private static Gate gate;
    private static Item[][] tileItem;

    private static int width;
    private static int height;

    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;

    public static int dem = 0;

    public BommanGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    // initial function
    @Override
    public void create(Window window) throws Exception {
        renderer.init(window);
        createMap("src/resources/map/level" + (int)(dem/10) + (dem%10) + ".json");
    }

    public void createMap(String path) throws Exception {
        ObjectManager.createMap(path);
        tileMap = ObjectManager.tileMap;
        tileBrick = ObjectManager.tileBrick;
        tileBomb = ObjectManager.tileBomb;
        tileFlame = ObjectManager.tileFlame;
        tileItem = ObjectManager.tileItem;
        player1 = ObjectManager.player1;
        //player2 = ObjectManager.player2;
        balloom = ObjectManager.balloom;
        oneal = ObjectManager.oneal;
        gate = ObjectManager.gate;

        width = ObjectManager.width;
        height = ObjectManager.height;

        player1.setSpeed(0.1f);
    }

    @Override
    public void input(Window window, Input input) throws Exception {
        if (!player1.isDead()) {
            if (!ObjectManager.checkCollision(player1.getPosition().x, player1.getPosition().y, 
                                             gate.getPosition().x, gate.getPosition().y)) {
                dem++;
                createMap("src/resources/map/level" + (int)(dem/10) + (dem%10) + ".json");
            }
        } else {
            createMap("src/resources/map/level" + (int)(dem/10) + (dem%10) + ".json");
        }
        cameraInc.set(0, 0, 0);
        camera.setPosition(width/2 + 1, height/2, Math.max(width, height));
        if (!player1.isDead()) {
            if (input.isKeyDown(GLFW_KEY_A)) {
                player1.setAutoMode(true);
                player1.setTargetPosition(gate.getPosition());
            }
            player1.handleEvent(window, input);
            player1.takeBomb(window, input);
        }

        // if (!player2.isDead()) {
        //     player2.handleEvent(window, input);
        //     player2.takeBomb(window, input);
        // }
        for (int i = 0; i < balloom.length; ++i) {
            if (!balloom[i].isDead()) {
                balloom[i].handleEvent(window, input);
            }
        }
        for (int i = 0; i < oneal.length; ++i) {
            if (!oneal[i].isDead()) {
                oneal[i].setTargetPosition(player1.getPosition());
                oneal[i].handleEvent(window, input);
            }
        }
        ObjectManager.checkIfEatItem(player1);

        ObjectManager.setUpBomb();
    }

    @Override
    public void update(float interval, Input input, Window window) {
        if (!player1.isDead()) {
            player1.dead();
        }

        // if (!player2.isDead()) {
        //     player2.setSpeed(0.2f);
        //     player2.setTargetPosition(new Vector3f(7, -5, -14.0f));
        //     player2.dead();
        // }
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                if (bomb != null)bomb.handleEvent();
            }
        }

        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null)flame.stop();
            }
        }

        for (int i = 0; i < balloom.length; ++i) {
            if (!balloom[i].isDead()) {
                balloom[i].setSpeed(0.05f);
                balloom[i].handleCollision();
            }
        }
        for (int i = 0; i< oneal.length; ++i) {
            if (!oneal[i].isDead()) {
                oneal[i].handleCollision();
            }
        }

        ObjectManager.setUpBomb();
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera);
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                if (bomb != null && bomb.isShow()) {
                    bomb.render(renderer);
                }
            }
        }
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null && flame.isStart()) {
                    flame.render(renderer);
                }
            }
        }
        if (!player1.isDead()) player1.render(renderer);

        //if (!player2.isDead()) player2.render(renderer);

        for (int i = 0; i < balloom.length; ++i) {
            if (!balloom[i].isDead()) balloom[i].render(renderer);
        }

        for (int i = 0; i < oneal.length; ++i) {
            if (!oneal[i].isDead()) oneal[i].render(renderer);
        }
        
        gate.render(renderer);

        for (Item[] items : tileItem) {
            for (Item item : items) {
                if (item != null && item.isVisible()) {
                    item.render(renderer);
                }
            }
        }
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick != null && brick.getVisible()) {
                    brick.render(renderer);
                }
            }
        }
        for (GameObject[] gameObjects : tileMap) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject != null )gameObject.render(renderer);
            }
        }

        renderer.finishRender();
    }

    @Override
    public void destroy() {
        renderer.cleanup();
        for (GameObject[] gameObjects : tileMap) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject != null && gameObject.getMesh() != null) {
                    gameObject.getMesh().cleanUp();
                }
            }
        }
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                if (bomb != null && bomb.getMesh() != null) {
                    bomb.getMesh().cleanUp();
                }
            }
        }
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame != null && flame.getMesh() != null) {
                    flame.getMesh().cleanUp();
                }
            }
        }
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick != null && brick.getMesh() != null) {
                    brick.getMesh().cleanUp();
                }
            }
        }
        for (Item[] items : tileItem) {
            for (Item item : items) {
                if (item != null && item.getMesh() != null) {
                    item.getMesh().cleanUp();
                }
            }
        }

        player1.getMesh().cleanUp();
        //player2.getMesh().cleanUp();
        for (int i = 0; i < balloom.length; ++i) {
            balloom[i].getMesh().cleanUp();
        }
        for (int i = 0; i < oneal.length; ++i) {
            oneal[i].getMesh().cleanUp();
        }
    }

}
