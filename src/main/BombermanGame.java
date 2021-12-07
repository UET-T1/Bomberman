package main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

import engine.GameItem;
import engine.IGameLogic;
import engine.Input;
import engine.ObjectManager;
import engine.Window;
import engine.graphics.Camera;
import engine.graphics.Renderer;
import entities.Balloom;
import entities.Bomb;
import entities.Brick;
import entities.Flame;
import entities.Gate;
import entities.Item;
import entities.Oneal;
import entities.Player;

public class BombermanGame implements IGameLogic {

  public static int dem = 0;
  private static GameItem[][] tileMap;
  private static Brick[][] tileBrick;
  private static Bomb[][] tileBomb;
  private static Flame[][] tileFlame;
  private static Player player1;
  //private static Player player2;
  private static Balloom[] balloom;
  private static Oneal[] oneal;
  private static Gate gate;
  private static Item[][] tileItem;
  private static Player[] humanEnemy;
  private static int width;
  private static int height;
  private final Renderer renderer;
  private final Camera camera;

  public BombermanGame() {
    renderer = new Renderer();
    camera = new Camera();
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
    humanEnemy = ObjectManager.humanEnemy;

    width = ObjectManager.width;
    height = ObjectManager.height;

    player1.setSpeed(0.1f);
    for (int i = 0; i < humanEnemy.length; ++i) {
      humanEnemy[i].setSpeed(0.1f);
    }
  }

  @Override
  public void init(Window window) throws Exception {
    renderer.init(window);
    createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + "0.json");
    camera.setPosition(width / 2, height / 2, Math.max(width, height));
  }

  @Override
  public void input(Window window, Input input) throws Exception {
    //camera.update(input);
    if (!player1.isDead()) {
      if (!ObjectManager.checkCollision(player1.getPosition().x, player1.getPosition().y,
          gate.getPosition().x, gate.getPosition().y)) {
        dem++;
        createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
      }
    } else {
      createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
    }
    //camera.setPosition(width/2 + 1, height/2 + 10, Math.max(width, height)); //Math.max(width, height)
    //camera.setPosition(5.0f, 5.0f, 100f);

    if (!player1.isDead()) {
      if (input.isKeyDown(GLFW_KEY_A)) {
        player1.setAutoMode(true);
        player1.setTargetPosition(gate.getPosition());
      }
      player1.handleEvent(window, input);
      player1.takeBomb(window, input);
    }
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
    for (int i = 0; i < humanEnemy.length; ++i) {
      if (!humanEnemy[i].isDead()) {
        humanEnemy[i].setAutoMode(true);
        humanEnemy[i].setTargetPosition(gate.getPosition());
        humanEnemy[i].handleEvent(window, input);
        humanEnemy[i].takeBomb(window, input);
      }
    }

    ObjectManager.checkIfEatItem(player1);
    for (int i = 0; i < humanEnemy.length; ++i) {
      ObjectManager.checkIfEatItem(humanEnemy[i]);
    }

    ObjectManager.setUpBomb();
  }


  @Override
  public void update(float interval, Input input) {
    if (!player1.isDead()) {
      player1.dead();
    }
    for (int i = 0; i < humanEnemy.length; ++i) {
      if (!humanEnemy[i].isDead()) {
        humanEnemy[i].dead();
      }
    }

    for (Bomb[] bombs : tileBomb) {
      for (Bomb bomb : bombs) {
        if (bomb != null) {
          bomb.handleEvent();
        }
      }
    }

    for (Flame[] flames : tileFlame) {
      for (Flame flame : flames) {
        if (flame != null) {
          flame.stop();
        }
      }
    }

    for (int i = 0; i < balloom.length; ++i) {
      if (!balloom[i].isDead()) {
        balloom[i].setSpeed(0.05f);
        balloom[i].handleCollision();
      }
    }
    for (int i = 0; i < oneal.length; ++i) {
      if (!oneal[i].isDead()) {
        oneal[i].handleCollision();
      }
    }

    ObjectManager.setUpBomb();
  }

  @Override
  public void render(Window window) {
    renderer.render(window, camera, null, null);
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

    if (!player1.isDead()) {
      player1.render(renderer);
    }
    for (int i = 0; i < humanEnemy.length; ++i) {
      if (!humanEnemy[i].isDead()) {
        humanEnemy[i].render(renderer);
      }
    }

    for (int i = 0; i < balloom.length; ++i) {
      if (!balloom[i].isDead()) {
        balloom[i].render(renderer);
      }
    }
    for (int i = 0; i < oneal.length; ++i) {
      if (!oneal[i].isDead()) {
        oneal[i].render(renderer);
      }
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
    for (GameItem[] gameObjects : tileMap) {
      for (GameItem gameObject : gameObjects) {
        if (gameObject != null) {
          gameObject.render(renderer);
        }
      }
    }

    //renderer.finishRender();
  }


  @Override
  public void cleanup() {
    renderer.cleanup();
    if (tileMap != null) {
      for (GameItem[] gameObjects : tileMap) {
        for (GameItem gameObject : gameObjects) {
          if (gameObject != null && gameObject.getMesh() != null) {
            gameObject.getMesh().cleanUp();
          }
        }
      }
    }
    if (tileBomb != null) {
      for (Bomb[] bombs : tileBomb) {
        for (Bomb bomb : bombs) {
          if (bomb != null && bomb.getMesh() != null) {
            bomb.getMesh().cleanUp();
          }
        }
      }
    }
    if (tileFlame != null) {
      for (Flame[] flames : tileFlame) {
        for (Flame flame : flames) {
          if (flame != null && flame.getMesh() != null) {
            flame.getMesh().cleanUp();
          }
        }
      }
    }
    if (tileBrick != null) {
      for (Brick[] bricks : tileBrick) {
        for (Brick brick : bricks) {
          if (brick != null && brick.getMesh() != null) {
            brick.getMesh().cleanUp();
          }
        }
      }
    }
    if (tileItem != null) {
      for (Item[] items : tileItem) {
        for (Item item : items) {
          if (item != null && item.getMesh() != null) {
            item.getMesh().cleanUp();
          }
        }
      }
    }

    if (player1 != null) {
      player1.getMesh().cleanUp();
    }
    for (int i = 0; i < humanEnemy.length; ++i) {
      if (!humanEnemy[i].isDead()) {
        humanEnemy[i].getMesh().cleanUp();
      }
    }

    if (balloom != null) {
      for (int i = 0; i < balloom.length; ++i) {
        balloom[i].getMesh().cleanUp();
      }
    }
    if (oneal != null) {
      for (int i = 0; i < oneal.length; ++i) {
        oneal[i].getMesh().cleanUp();
      }
    }
  }

}
