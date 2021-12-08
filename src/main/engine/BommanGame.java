package main.engine;

import org.joml.Vector3f;

import main.engine.objects.Bomb;
import main.engine.objects.Brick;
import main.engine.objects.Deadpool;
import main.engine.objects.Flame;
import main.engine.testGUI.Camera;

public class BommanGame implements GameLogic {
    private static GameObject[][] tileMap;
    private static Brick[][] tileBrick;
    private static Bomb[][] tileBomb;
    private static Flame[][] tileFlame;
    private static Deadpool deadpool;
    private static Deadpool deadpool2;
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
        deadpool = ObjectManager.deadpool;
        deadpool2 = ObjectManager.deadpool2;
    }

    @Override
    public void input(Window window, Input input) throws Exception {
        cameraInc.set(0, 0, 0);
        if (!deadpool.isDead()) {
            deadpool.handleEvent(window, input);
            deadpool.takeBomb(window, input);
        }

        if (!deadpool2.isDead()) {
            deadpool2.handleEvent(window, input);
            deadpool2.takeBomb(window, input);
        }
    }

    @Override
    public void update(float interval, Input input, Window window) {
        if (!deadpool.isDead()) {
            deadpool.setSpeed(0.2f);
            deadpool.setTargetPosition(new Vector3f(7, -5, -14.0f));
            deadpool.dead();
        }

        if (!deadpool2.isDead()) {
            deadpool2.setSpeed(0.2f);
            deadpool2.setTargetPosition(new Vector3f(7, -5, -14.0f));
            deadpool2.dead();
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
        if (!deadpool.isDead())
            renderer.render(deadpool);
        if (!deadpool2.isDead())
            renderer.render(deadpool2);
        
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

        deadpool.getMesh().cleanUp();
    }

}
