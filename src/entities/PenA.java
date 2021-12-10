package entities;

import engine.GameItem;
import engine.Input;
import engine.Movable;
import engine.Node;
import engine.ObjectManager;
import engine.Utils;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Vector3f;

public class PenA extends GameItem implements Movable {

  private boolean isDead;
  private boolean chaseStat;
  private Vector3f targetPosition;

  private static Texture penATexture;
  private static Mesh mesh;
  private static Animation animation;

  static {
    try {
      penATexture = new Texture("resources/textures/penA.png");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, penATexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PenA(Animation animation) {
    super(animation);
    isDead = false;
    chaseStat = false;
  }

  public PenA() throws Exception {
    this(mesh);
  }

  public PenA(Mesh mesh) throws Exception {
    super(mesh);
    isDead = false;
    chaseStat = false;
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  @Override
  public void onCollapse() {
    isDead = true;
  }

  @Override
  public void onCollision() {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleCollision() {
    if (ObjectManager.checkIfEnemyDead(getPosition().x, getPosition().y)) {
      onCollapse();
    }
  }

  public void setTargetPosition(Vector3f targetPosition) {
    Vector3f addPos = new Vector3f(targetPosition);
    addPos = Utils.round1(addPos);
    this.targetPosition = new Vector3f(addPos);
  }

  @Override
  public void handleEvent(Window window, Input input) {
      if (!chaseStat) {
          int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
          if (randomNum == 1) {
              speed = 0.02f;
          }
          if (randomNum == 2) {
              speed = 0.05f;
          }
          if (randomNum == 3) {
              speed = 0.1f;
          }
          if (randomNum == 4) {
              speed = 0.2f;
          }
          nextPosition = null;
          move(window, input);
      } else {
          chase();
      }

  }

  @Override
  public void move(Window window, Input input) {
    search();
    chaseStat = true;
  }


  @Override
  public void setSpeed(float speed) {
    this.speed = speed;

  }

  private void chase() {
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

  public void search() {
    boolean[][] checked = new boolean[ObjectManager.width + 1][ObjectManager.height + 1];
    for (int i = 1; i <= ObjectManager.width; ++i) {
      for (int j = 1; j <= ObjectManager.height; ++j) {
        checked[i][j] = false;
      }
    }
    PriorityQueue<Node> queue = new PriorityQueue<>();
    float h = Utils.distance(this.getPosition(), targetPosition);
    Vector3f currentPos = new Vector3f(this.getPosition());
    currentPos = Utils.round1(currentPos);
    Node current = new Node(h, 0, h, currentPos, null);
    queue.add(current);

    while (!current.value.equals(targetPosition) && queue.size() > 0) {

      current = queue.poll();

      int i = (int) current.value.x;
      int j = (int) current.value.y;
      checked[i][j] = true;

      if (!ObjectManager.IsWallOfGameObject(i + 1, j)
          && ObjectManager.checkCollision(current.value.x + 1, current.value.y, false, null, this)
          && !checked[i + 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i - 1, j)
          && ObjectManager.checkCollision(current.value.x - 1, current.value.y, false, null, this)
          && !checked[i - 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j + 1)
          && ObjectManager.checkCollision(current.value.x, current.value.y + 1, false, null, this)
          && !checked[i][j + 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j - 1)
          && ObjectManager.checkCollision(current.value.x, current.value.y - 1, false, null, this)
          && !checked[i][j - 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }
    }

    setNextPosition(current);
  }

  private void setNextPosition(Node current) {
    nextPosition = null;
    while (current.father != null) {
      nextPosition = current.value;
      current = current.father;
    }
  }

  public boolean isDead() {
    return isDead;
  }

}
