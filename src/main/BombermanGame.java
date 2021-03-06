package main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

import engine.GameItem;
import engine.IGameLogic;
import engine.Input;
import engine.ObjectManager;
import engine.Window;
import engine.graphics.Camera;
import engine.graphics.Renderer;
import entities.Pencil;
import entities.Bomb;
import entities.Brick;
import entities.Button;
import entities.Flame;
import entities.Gate;
import entities.Ink;
import entities.Item;
import entities.Menu;
import entities.PenA;
import entities.Player;
import entities.Witch;

public class BombermanGame implements IGameLogic {

  public static int dem = 0;
  private static GameItem[][] tileMap;
  private static Brick[][] tileBrick;
  private static Bomb[][] tileBomb;
  private static Flame[][] tileFlame;
  private static Player player1;
  private static Pencil[] pencil;
  private static PenA[] penA;
  private static Ink[] ink;
  private static Witch[] witch;
  private static Gate gate;
  private static Item[][] tileItem;
  private static Player[] humanEnemy;
  private static Menu menu;
  private static Button startButton;
  private static int width;
  private static int height;
  private final Renderer renderer;
  private final Camera camera;
  private static int status;
  private final int MENU = 0;
  private final int MAP = 1;

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
    pencil = ObjectManager.pencil;
    penA = ObjectManager.penA;
    gate = ObjectManager.gate;
    humanEnemy = ObjectManager.humanEnemy;
    ink = ObjectManager.ink;
    witch = ObjectManager.witch;

    width = ObjectManager.width;
    height = ObjectManager.height;

    player1.setSpeed(0.1f);
    for (int i = 0; i < humanEnemy.length; ++i) {
      humanEnemy[i].setSpeed(0.1f);
    }
    camera.setPosition((float)width/2, (float)height/2, Math.max(width, height));
  }

  @Override
  public void init(Window window) throws Exception {
    renderer.init(window, camera);
    // createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
    // status = MAP;

    menu = new Menu();
    menu.setPosition(0, 0, -1.5f);

    startButton = new Button("Start");
    startButton.setPosition(0, -4, -1.5f);

    status = MENU;
    camera.setPosition(0, 0, 14);
  }

  @Override
  public void input(Window window, Input input) throws Exception {
    if (status == MENU) {
      startButton.handleEvent(window, input);
    }
    else {
      //camera.update(input);
      if (!player1.isDead()) {
        if (!ObjectManager.checkCollision(player1.getPosition().x, player1.getPosition().y,
            gate.getPosition().x, gate.getPosition().y)) {
          dem++;
          createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
        }
        if (humanEnemy != null)
        for (int i = 0; i < humanEnemy.length; ++i) {
          if (!humanEnemy[i].isDead()) {
            if (!ObjectManager.checkCollision(humanEnemy[i].getPosition().x, humanEnemy[i].getPosition().y,
            gate.getPosition().x, gate.getPosition().y)) {
              createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
            }
          }
        }
  
      } else {
        createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
      }

      if (!player1.isDead()) {
        player1.setTargetPosition(gate.getPosition());
        player1.handleEvent(window, input);
        player1.takeBomb(window, input);
      }
      for (int i = 0; i < pencil.length; ++i) {
        if (!pencil[i].isDead()) {
          pencil[i].handleEvent(window, input);
        }
      }
      for (int i = 0; i < penA.length; ++i) {
        if (!penA[i].isDead()) {
          penA[i].setTargetPosition(player1.getPosition());
          penA[i].handleEvent(window, input);
        }
      }
      for (int i = 0; i < witch.length; ++i) {
        if (!witch[i].isDead()) {
          witch[i].setTargetPosition(player1.getPosition());
          witch[i].handleEvent(window, input);
        }
      }
      for (int i = 0; i < ink.length; ++i) {
        if (!ink[i].isDead()) {
          ink[i].handleEvent(window, input);
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
  }


  @Override
  public void update(float interval, Input input) throws Exception {

    if (status == MENU) {
      if (startButton.isClick) {
        createMap("resources/maps/level" + (int) (dem / 10) + (dem % 10) + ".json");
        status = MAP;
      }
    }
    else {
      if (!player1.isDead()) {
        player1.dead();
        player1.lureHandle();
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

      for (int i = 0; i < pencil.length; ++i) {
        if (!pencil[i].isDead()) {
          pencil[i].setSpeed(0.05f);
          pencil[i].handleCollision();
        }
      }
      for (int i = 0; i < penA.length; ++i) {
        if (!penA[i].isDead()) {
          penA[i].handleCollision();
        }
      }
      for (int i = 0; i < ink.length; ++i) {
        if (!ink[i].isDead()) {
          ink[i].setSpeed(0.05f);
          ink[i].handleCollision();
        }
      }
      for (int i = 0; i < witch.length; ++i) {
        if (!witch[i].isDead()) {
          witch[i].setSpeed(0.05f);
          witch[i].handleCollision();
        }
      }
      ObjectManager.lurePlayer(player1.getPosition().x, player1.getPosition().y);

      ObjectManager.setUpBomb();
    }
  }

  @Override
  public void render(Window window) {

    renderer.render(window, camera, null, null);

    if (status == MENU) {
      startButton.render(renderer);
      menu.render(renderer);
    }
    else {
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

      for (int i = 0; i < pencil.length; ++i) {
        if (!pencil[i].isDead()) {
          pencil[i].render(renderer);
        }
      }
      for (int i = 0; i < penA.length; ++i) {
        if (!penA[i].isDead()) {
          penA[i].render(renderer);
        }
      }
      for (int i = 0; i < ink.length; ++i) {
        if (!ink[i].isDead()) {
          ink[i].render(renderer);
        }
      }
      for (int i = 0; i < witch.length; ++i) {
        if (!witch[i].isDead()) {
          witch[i].render(renderer);
        }
      }

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

      gate.render(renderer);
      for (GameItem[] gameObjects : tileMap) {
        for (GameItem gameObject : gameObjects) {
          if (gameObject != null) {
            gameObject.render(renderer);
          }
        }
      }
    }
    

    renderer.finishRender();
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
    if (humanEnemy != null)
    for (int i = 0; i < humanEnemy.length; ++i) {
      if (!humanEnemy[i].isDead()) {
        humanEnemy[i].getMesh().cleanUp();
      }
    }

    if (pencil != null) {
      for (int i = 0; i < pencil.length; ++i) {
        pencil[i].getMesh().cleanUp();
      }
    }
    if (penA != null) {
      for (int i = 0; i < penA.length; ++i) {
        penA[i].getMesh().cleanUp();
      }
    }
    if (ink != null) {
      for (int i = 0; i < ink.length; ++i) {
        ink[i].getMesh().cleanUp();
      }
    }
    if (witch != null) {
      for (int i = 0; i < witch.length; ++i) {
        witch[i].getMesh().cleanUp();
      }
    }
    if (menu != null) {
      if (menu.getMesh() != null) {
        menu.getMesh().cleanUp();
      }
    }
    int x = 1;
  }

}
