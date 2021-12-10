package entities;

import engine.GameItem;
import engine.Input;
import engine.Movable;
import engine.Node;
import engine.ObjectManager;
import engine.Timer;
import engine.Utils;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import java.util.PriorityQueue;
import org.joml.Vector3f;

public class Witch extends GameItem implements Movable {

  private Vector3f targetPosition;
  private boolean isDead;
  private boolean chaseStat;
  private Vector3f nextPosition;
  private static Animation[] animationList;
  boolean dk = false;

  static {
    try {
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};

      animationList = new Animation[] {
        new Animation(5, 6, 5.0f, "resources/textures/witch", positions, textCoords, indices),
      };

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Witch(Mesh mesh) throws Exception {
    super(mesh);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
  }

  public Witch(Animation animation) {
    super(animation);
    isDead = false;
    chaseStat = false;
    nextPosition = null;
  }

  public Witch() throws Exception {
    this(animationList[0]);
  }

  @Override
  public void render(Renderer renderer) {
    this.meshItem = animationList[0].getCurrentMesh();
    renderer.render(this);
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

  public void setTargetPosition(Vector3f targetPosition) {
    Vector3f addPos = new Vector3f(targetPosition);
    addPos = Utils.round1(addPos);
    this.targetPosition = new Vector3f(addPos);
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
