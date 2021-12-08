package main.engine;

import org.joml.Vector3f;

import main.engine.objects.Bomb;
import main.engine.objects.Brick;
import main.engine.objects.Player;
import main.engine.objects.Flame;
import main.engine.testGUI.Camera;

public class BommanGame implements GameLogic {
    private static GameObject[][] tileMap;
    private static Brick[][] tileBrick;
    private static Bomb[][] tileBomb;
    private static Flame[][] tileFlame;
    private static Player player1;
    private static Player player2;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;

    public BommanGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    // initial function
    @Override
    public void create(Window window) throws Exception {
        renderer.init(window);
        ObjectManager.createMap();
        tileMap = ObjectManager.tileMap;
        tileBrick = ObjectManager.tileBrick;
        tileBomb = ObjectManager.tileBomb;
        tileFlame = ObjectManager.tileFlame;
        player1 = ObjectManager.player1;
        player2 = ObjectManager.player2;
    }

    @Override
    public void input(Window window, Input input) throws Exception {
        cameraInc.set(0, 0, 0);
        if (!player1.isDead()) {
            player1.handleEvent(window, input);
            player1.takeBomb(window, input);
        }

        if (!player2.isDead()) {
            player2.handleEvent(window, input);
            player2.takeBomb(window, input);
        }
    }

    @Override
    public void update(float interval, Input input, Window window) {
        if (!player1.isDead()) {
            player1.setSpeed(0.2f);
            player1.setTargetPosition(new Vector3f(7, -5, -14.0f));
            player1.dead();
        }

        if (!player2.isDead()) {
            player2.setSpeed(0.2f);
            player2.setTargetPosition(new Vector3f(7, -5, -14.0f));
            player2.dead();
        }
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                bomb.handleEvent();
            }
        }

        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                flame.stop();
            }
        }

        ObjectManager.setUpBomb();
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera);
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                if (bomb.isShow()) {
                    renderer.render(bomb);
                }
            }
        }
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame.isStart()) {
                    renderer.render(flame);
                }
            }
        }
        if (!player1.isDead())
            renderer.render(player1);
        if (!player2.isDead())
            renderer.render(player2);
        
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick.getVisible()) {
                    renderer.render(brick);
                }
            }
        }
        for (GameObject[] gameObjects : tileMap) {
            for (GameObject gameObject : gameObjects) {
                renderer.render(gameObject);
            }
        }
        renderer.finishRender();
    }

    @Override
    public void destroy() {
        renderer.cleanup();
        for (GameObject[] gameObjects : tileMap) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getMesh() != null) {
                    gameObject.getMesh().cleanUp();
                }
            }
        }
        for (Bomb[] bombs : tileBomb) {
            for (Bomb bomb : bombs) {
                if (bomb.getMesh() != null) {
                    bomb.getMesh().cleanUp();
                }
            }
        }
        for (Flame[] flames : tileFlame) {
            for (Flame flame : flames) {
                if (flame.getMesh() != null) {
                    flame.getMesh().cleanUp();
                }
            }
        }
        for (Brick[] bricks : tileBrick) {
            for (Brick brick : bricks) {
                if (brick.getMesh() != null) {
                    brick.getMesh().cleanUp();
                }
            }
        }

        player1.getMesh().cleanUp();
        player2.getMesh().cleanUp();
    }

}
