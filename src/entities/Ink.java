package entities;

import engine.GameItem;
import engine.Input;
import engine.Movable;
import engine.ObjectManager;
import engine.Timer;
import engine.Utils;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Vector3f;

public class Ink extends GameItem implements Movable {

  private boolean isDead;
  private boolean chaseStat;
  private Vector3f nextPosition;
  private static Animation[] animationList;
  private Timer time = new Timer();
  private int animationListPos = 0;
  boolean dk = false;

  static {
    try {
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};

      animationList = new Animation[] {
        new Animation(5, 3, 10.0f, "resources/textures/ink/normal", positions, textCoords, indices),
        new Animation(3, 3, 10.0f, "resources/textures/ink/skill", positions, textCoords, indices)
      };

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Ink(Mesh mesh) throws Exception {
    super(mesh);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
  }

  public Ink(Animation animation) {
    super(animation);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
    time.init();
  }

  public Ink() throws Exception {
    this(animationList[0]);
  }

  @Override
  public void render(Renderer renderer) {
    this.meshItem = animationList[animationListPos].getCurrentMesh();
    renderer.render(this);
  }

  public boolean isDead() {
    return isDead;
  }

  @Override
  public void onCollapse() {
    isDead = true;
  }

  @Override
  public void onCollision() {

  }

  @Override
  public void handleCollision() {
    if (ObjectManager.checkIfEnemyDead(getPosition().x, getPosition().y)) {
      onCollapse();
    }
  }

  @Override
  public void move(Window window, Input input) {
    Vector3f pos = getPosition();
    pos = Utils.round100(pos);
    int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
    if (randomNum == 1 && ObjectManager.checkCollision(pos.x + 1, pos.y, false, null, this)) {
      nextPosition = new Vector3f(pos.x + 1, pos.y, pos.z);
      chaseStat = true;
      return;
    }
    if (randomNum == 2 && ObjectManager.checkCollision(pos.x - 1, pos.y, false, null, this)) {
      nextPosition = new Vector3f(pos.x - 1, pos.y, pos.z);
      chaseStat = true;
      return;
    }
    if (randomNum == 3 && ObjectManager.checkCollision(pos.x, pos.y + 1, false, null, this)) {
      nextPosition = new Vector3f(pos.x, pos.y + 1, pos.z);
      chaseStat = true;
      return;
    }
    if (randomNum == 4 && ObjectManager.checkCollision(pos.x, pos.y - 1, false, null, this)) {
      nextPosition = new Vector3f(pos.x, pos.y - 1, pos.z);
      chaseStat = true;
      return;
    }
  }

  @Override
  public void setSpeed(float speed) {
    this.speed = speed;
  }

  @Override
  public void handleEvent(Window window, Input input) {
    if (!chaseStat) {
      if (!dk && time.getTime() > time.getLastLoopTime() + 8.0f) {
          animationListPos = 1;
          createBrick();
          dk = true;
      }
      if (dk && time.getTime() > time.getLastLoopTime() + 5.0f) {
          time.init();
          animationListPos = 0;
          dk = false;
      }
      if (!dk) {
        nextPosition = null;
        move(window, input);
      }
    } else if (!dk) {
      chase();
    }
  }

  public void chase() {
    if (nextPosition != null) {
      Utils.round100(nextPosition);
      Utils.round100(getPosition());
      if (nextPosition.x == getPosition().x) {
        if (nextPosition.y > getPosition().y) {
          getPosition().y += speed;
        } else if (nextPosition.y < getPosition().y) {
          getPosition().y -= speed;
        } else {
          chaseStat = false;
        }
      }
      if (nextPosition.y == getPosition().y) {
        if (nextPosition.x > getPosition().x) {
          getPosition().x += speed;
        } else if (nextPosition.x < getPosition().x) {
          getPosition().x -= speed;
        } else {
          chaseStat = false;
        }
      }
      Utils.round100(nextPosition);
      Utils.round100(getPosition());
    }
  }

  public void createBrick() {
        Utils.round1(getPosition());
        int x = (int) getPosition().x;
        int y = (int) getPosition().y; 
        if (!ObjectManager.tileBomb[x + 1][y].isShow()
                 && !ObjectManager.tileBrick[x + 1][y].getVisible()
                 && !(ObjectManager.tileMap[x + 1][y] instanceof Wall)) {
                ObjectManager.tileBrick[x + 1][y].setVisible(true);
                time.init();
                return;
        }

        if (!ObjectManager.tileBomb[x - 1][y].isShow()
                 && !ObjectManager.tileBrick[x - 1][y].getVisible()
                 && !(ObjectManager.tileMap[x - 1][y] instanceof Wall)) {
                ObjectManager.tileBrick[x - 1][y].setVisible(true);
                time.init();
                return;
        }

        if (!ObjectManager.tileBomb[x][y + 1].isShow()
                 && !ObjectManager.tileBrick[x][y + 1].getVisible()
                 && !(ObjectManager.tileMap[x][y + 1] instanceof Wall)) {
                ObjectManager.tileBrick[x][y + 1].setVisible(true);
                time.init();
                return;
        }

        if (!ObjectManager.tileBomb[x][y - 1].isShow()
                 && !ObjectManager.tileBrick[x][y - 1].getVisible()
                 && !(ObjectManager.tileMap[x][y - 1] instanceof Wall)) {
                ObjectManager.tileBrick[x][y - 1].setVisible(true);
                time.init();
                return;
        }
  }

}
