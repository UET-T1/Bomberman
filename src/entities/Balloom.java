package entities;

import engine.GameItem;
import engine.Input;
import engine.Movable;
import engine.ObjectManager;
import engine.Utils;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Vector3f;

public class Balloom extends GameItem implements Movable {

  private boolean isDead;
  private boolean chaseStat;
  private Vector3f nextPosition;
  private static Texture balloomTexture;
  private static Mesh mesh;
  private static Animation animation;

  static {
    try {
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      balloomTexture = new Texture("resources/textures/pencil.png");
      mesh = new Mesh(positions, textCoords, indices, balloomTexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Balloom(Mesh mesh) throws Exception {
    super(mesh);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
  }

  public Balloom(Animation animation) {
    super(animation);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
  }

  public Balloom() throws Exception {
    this(mesh);
    //this(animation);
  }

  @Override
  public void render(Renderer renderer) {
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
      nextPosition = null;
      move(window, input);
    } else {
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

}
