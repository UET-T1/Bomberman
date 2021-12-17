package entities;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import engine.Auto;
import engine.GameItem;
import engine.Input;
import engine.Movable;
import engine.Node;
import engine.ObjectManager;
import engine.Pair;
import engine.Timer;
import engine.Utils;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import org.joml.Vector3f;

public class Player extends GameItem implements Movable, Auto {

  public static Map<String, Integer> characters = new HashMap<>();
  public static Texture[] textures;
  private static Mesh[] meshes;
  private static Mesh mesh;
  private static Animation animation;
  private static Animation[] animationList;

  static {
    characters.put("Deadpool", 0);
  }

  static {
    try {
      textures = new Texture[]{
          new Texture("resources/textures/eraeser.png")
      };
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      meshes = new Mesh[]{
          new Mesh(positions, textCoords, indices, textures[0])
      };
      /*
      animationList = new Animation[] {
          new Animation(amount, fps, fileName1, positions, textCoords, indices);
      }
       */
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private final int PUTABOMB = 1;//SEARCH status for findWay function
  private final int GOTOSAFEPOS = 0;//BREAKWALL status for findWay function
  private final int FIND = 2;
  private final int STAY = 3;
  protected Vector3f targetPosition;// target position
  protected String chaseDiection = "";// chase direction
  protected int dem = 0;// the number of step to go full one square
  private boolean autoMode;// true if autoMode is ON
  private int status;// status to break wall or search the way
  private Bomb[] bombsOfMe; // bombs of this itemv
  private boolean isDead;
  private float timeSquare;
  private boolean isInBomb;// true if have bomb in leg
  private Bomb inBomb;// bomb in leg of object
  private int bombPower;
  private Timer time = new Timer();


  public Player(Mesh mesh) throws Exception {
    super(mesh);
    autoMode = true;
    bombsOfMe = new Bomb[1];
    bombPower = 1;
    isDead = false;
    status = FIND;
    isInBomb = false;
    targetPosition = new Vector3f(8, 8, 0);
  }

  public Player(String character) throws Exception {
    this(meshes[characters.get(character)]);
    //this(animationList[characters.get(character)]);
  }

  public Player(Animation animation) {
    super(animation);
    autoMode = true;
    bombsOfMe = new Bomb[1];
    bombPower = 1;
    isDead = false;
    status = FIND;
    isInBomb = false;
    targetPosition = new Vector3f(8, 8, 0);
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  @Override
  public void onCollapse() {

  }

  @Override
  public void onCollision() {

  }

  @Override
  public void handleCollision() {

  }

  @Override
  public void move(Window window, Input input) {
    if (input.isKeyDown(GLFW_KEY_LEFT)) {
      moveLeft();
    }
    if (input.isKeyDown(GLFW_KEY_RIGHT)) {
      moveRight();
    }
    if (input.isKeyDown(GLFW_KEY_UP)) {
      moveUp();
    }
    if (input.isKeyDown(GLFW_KEY_DOWN)) {
      moveDown();
    }
  }

  @Override
  public void setSpeed(float speed) {
    this.speed = speed;
  }

  @Override
  public void setTargetPosition(Vector3f targetPosition) {
    Vector3f addPos = new Vector3f(targetPosition);
    addPos = Utils.round1(addPos);
    this.targetPosition = new Vector3f(addPos);
  }

  public void setAutoMode(boolean autoMode) {
    this.autoMode = autoMode;
  }

  public void moveRight() {
    Vector3f pos = getPosition();
    pos = Utils.round100(pos);
    pos.x += speed;
    if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb, this)) {
      float res = (float) (pos.y - Math.floor(pos.y));
      if (res >= 0.5f && ObjectManager.checkCollision(pos.x, pos.y - res + 1, isInBomb, inBomb,
          this)) {
        pos.y = pos.y - res + 1;
      } else if (res <= 0.5f && ObjectManager.checkCollision(pos.x, pos.y - res, isInBomb, inBomb,
          this)) {
        pos.y = pos.y - res;
      } else {
        pos.x -= speed;
      }
    }
    pos = Utils.round100(pos);
    setPosition(pos.x, pos.y, pos.z);
  }

  public void moveLeft() {
    Vector3f pos = getPosition();
    pos = Utils.round100(pos);
    pos.x -= speed;
    if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb, this)) {
      float res = (float) (pos.y - Math.floor(pos.y));
      if (res >= 0.5f && ObjectManager.checkCollision(pos.x, pos.y - res + 1, isInBomb, inBomb,
          this)) {
        pos.y = pos.y - res + 1;
      } else if (res <= 0.5f && ObjectManager.checkCollision(pos.x, pos.y - res, isInBomb, inBomb,
          this)) {
        pos.y = pos.y - res;
      } else {
        pos.x += speed;
      }
    }
    pos = Utils.round100(pos);
    setPosition(pos.x, pos.y, pos.z);
  }

  public void moveUp() {
    Vector3f pos = getPosition();
    pos = Utils.round100(pos);
    pos.y += speed;
    if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb, this)) {
      float res = (float) (pos.x - Math.floor(pos.x));
      if (res >= 0.5f && ObjectManager.checkCollision(pos.x - res + 1, pos.y, isInBomb, inBomb,
          this)) {
        pos.x = pos.x - res + 1;
      } else if (res <= 0.5f && ObjectManager.checkCollision(pos.x - res, pos.y, isInBomb, inBomb,
          this)) {
        pos.x = pos.x - res;
      } else {
        pos.y -= speed;
      }
    }
    pos = Utils.round100(pos);
    setPosition(pos.x, pos.y, pos.z);
  }

  public void moveDown() {
    Vector3f pos = getPosition();
    pos = Utils.round100(pos);
    pos.y -= speed;
    if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb, this)) {
      float res = (float) (pos.x - Math.floor(pos.x));
      if (res >= 0.5f && ObjectManager.checkCollision(pos.x - res + 1, pos.y, isInBomb, inBomb,
          this)) {
        pos.x = pos.x - res + 1;
      } else if (res <= 0.5f && ObjectManager.checkCollision(pos.x - res, pos.y, isInBomb, inBomb,
          this)) {
        pos.x = pos.x - res;
      } else {
        pos.y += speed;
      }
    }
    pos = Utils.round100(pos);
    setPosition(pos.x, pos.y, pos.z);
  }

  public boolean isFullBomb() {
    for (int i = 0; i < bombsOfMe.length; ++i) {
      if (bombsOfMe[i] == null || !bombsOfMe[i].isStart()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void handleEvent(Window window, Input input) throws Exception {

    if (isInBomb) {//case bomb behind deadpool
      if (!inBomb.isStart()) {
        isInBomb = false;
      }
      if (ObjectManager.checkCollision(getPosition().x, getPosition().y, inBomb.getPosition().x,
          inBomb.getPosition().y)) {
        isInBomb = false;
      }
    }

    if (!autoMode) {//if auto mode is OFF

      move(window, input);

    } else if (autoMode) {//if auto mode is ON

      timeSquare = ObjectManager.avgTimePerFrame * (1.0f / speed);
      this.chase();
      if (!chaseStat) {
        Utils.round1(getPosition());
        nextPosition = null;
        if (search()) {
          if (status != FIND) {
            if (time.getTime() - time.getLastLoopTime() > 2.7f) {
              status = FIND;
            } else {
              status = STAY;
            }
          }
        } else {
          if (!isFullBomb()) {
            if (time.getTime() - time.getLastLoopTime() > 2.7f) {
              findBrick();
            } else {
              findSafePos();
            }
          } else {
            time.init();
            findSafePos();
          }
        }
      }
    }
  }

  public void dead() {
    float x = (float) Math.round(getPosition().x * 100) / 100;
    float y = (float) Math.round(getPosition().y * 100) / 100;
    if (ObjectManager.checkIfPlayerDead(x, y)) {
      isDead = true;
    }
  }

  public boolean isDead() {
    return isDead;
  }

  // take bomb in hand when press Space
  public void takeBomb(Window window, Input input) {
    Vector3f bombPos = new Vector3f(this.getPosition());
    bombPos = Utils.round1(bombPos);
    int bombX = (int) bombPos.x;
    int bombY = (int) bombPos.y;
    if (input.isKeyDown(GLFW_KEY_SPACE) && !ObjectManager.isStartOfBomb(bombX, bombY)
        && !autoMode) {
      if (!isFullBomb()) {
        putABomb();
      }
    }
  }

  // put a bomb to ground
  public void putABomb() {
    Vector3f bombPos = new Vector3f(this.getPosition());
    bombPos = Utils.round1(bombPos);
    int bombX = (int) bombPos.x;
    int bombY = (int) bombPos.y;
    if (!ObjectManager.isStartOfBomb(bombX, bombY)) {
      ObjectManager.startABomb(bombX, bombY, bombPower);

      ObjectManager.setUpBomb();

      isInBomb = true;
      inBomb = ObjectManager.getBomb(bombX, bombY);
      for (int i = 0; i < bombsOfMe.length; ++i) {
        if (bombsOfMe[i] == null || !bombsOfMe[i].isStart()) {
          bombsOfMe[i] = inBomb;
          return;
        }
      }
    }
  }

  protected void setNextPosition(Node current) {
    nextPosition = null;
    while (current.father != null) {
      nextPosition = current.value;
      current = current.father;
    }

    if (nextPosition != null) {
      dem = 0;
      chaseStat = false;
      float x = getPosition().x;
      float x1 = nextPosition.x;
      float y = getPosition().y;
      float y1 = nextPosition.y;
      if (Math.round(x) > Math.round(x1)) {
        chaseDiection = "LEFT";
      } else if (Math.round(x) < Math.round(x1)) {
        chaseDiection = "RIGHT";
      } else if (Math.round(y) > Math.round(y1)) {
        chaseDiection = "DOWN";
      } else if (Math.round(y) < Math.round(y1)) {
        chaseDiection = "UP";
      } else {
        chaseDiection = "STAY";
      }
    }
  }

  protected void setNextPosition(Pair current) {
    nextPosition = null;
    while (current.father != null) {
      nextPosition = current.value;
      current = current.father;
    }

    if (nextPosition != null) {
      dem = 0;
      chaseStat = false;
      float x = getPosition().x;
      float x1 = nextPosition.x;
      float y = getPosition().y;
      float y1 = nextPosition.y;
      if (Math.round(x) > Math.round(x1)) {
        chaseDiection = "LEFT";
      } else if (Math.round(x) < Math.round(x1)) {
        chaseDiection = "RIGHT";
      } else if (Math.round(y) > Math.round(y1)) {
        chaseDiection = "DOWN";
      } else if (Math.round(y) < Math.round(y1)) {
        chaseDiection = "UP";
      } else {
        chaseDiection = "STAY";
      }
    }
  }

  // check if find a way to target
  @Override
  public boolean search() {
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

    while (queue.size() > 0) {

      current = queue.poll();
      if (current.value.equals(targetPosition)) {
        break;
      }

      int numsOfSquare = 0;
      Node point = current;
      while (point.father != null) {
        point = point.father;
        numsOfSquare++;
      }
      float lastTimeToReach = timeSquare * numsOfSquare;

      int i = (int) current.value.x;
      int j = (int) current.value.y;
      checked[i][j] = true;

      if (!ObjectManager.IsWallOfGameObject(i + 1, j)
          && !ObjectManager.isDangerous(i + 1, j, i, j, timeSquare, lastTimeToReach)
          && !checked[i + 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i - 1, j)
          && !ObjectManager.isDangerous(i - 1, j, i, j, timeSquare, lastTimeToReach)
          && !checked[i - 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j + 1)
          && !ObjectManager.isDangerous(i, j + 1, i, j, timeSquare, lastTimeToReach)
          && !checked[i][j + 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j - 1)
          && !ObjectManager.isDangerous(i, j - 1, i, j, timeSquare, lastTimeToReach)
          && !checked[i][j - 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }
    }
    if (!current.value.equals(targetPosition)) {
      return false;
    }

    setNextPosition(current);

    return true;
  }

  @Override
  public void findSafePos() {
    status = GOTOSAFEPOS;
    int x = (int) (getPosition().x);
    int y = (int) (getPosition().y);
    Pair pair = ObjectManager.findSafePos(x, y, timeSquare);
    setNextPosition(pair);
  }

  //find brick when can not find target
  @Override
  public void findBrick() throws Exception {

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

      int numsOfSquare = 0;
      Node point = current;
      while (point.father != null) {
        point = point.father;
        numsOfSquare++;
      }
      float lastTimeToReach = timeSquare * numsOfSquare;

      if (ObjectManager.isHaveBrick((int) current.value.x, (int) current.value.y, timeSquare,
          lastTimeToReach, bombPower)) {
        setNextPosition(current);
        status = PUTABOMB;
        return;
      }

      int i = (int) current.value.x;
      int j = (int) current.value.y;
      checked[i][j] = true;

      if (!ObjectManager.IsWallOfGameObject(i + 1, j)
          && !ObjectManager.isDangerous(i + 1, j, i, j, timeSquare, lastTimeToReach)
          && !checked[i + 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i - 1, j)
          && !ObjectManager.isDangerous(i - 1, j, i, j, timeSquare, lastTimeToReach)
          && !checked[i - 1][j]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.x -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j + 1)
          && !ObjectManager.isDangerous(i, j + 1, i, j, timeSquare, lastTimeToReach)
          && !checked[i][j + 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y += 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }

      if (!ObjectManager.IsWallOfGameObject(i, j - 1)
          && !ObjectManager.isDangerous(i, j - 1, i, j, timeSquare, lastTimeToReach)
          && !checked[i][j - 1]) {

        Vector3f newPos = new Vector3f(current.value);
        newPos.y -= 1;
        newPos = Utils.round1(newPos);
        float h1 = Utils.distance(newPos, targetPosition);
        float g1 = current.g + 1;
        queue.add(new Node(h1 + g1, g1, h1, newPos, current));
      }
    }

    time.init();
    findSafePos();
  }

  @Override
  public void chase() {
    if (status == STAY) {
      return;
    }
    if (nextPosition != null) {

      Vector3f currentPosition = getPosition();
      if (dem < 1 / speed) {
        if (chaseDiection.equals("LEFT")) {
          currentPosition.x -= speed;
        } else if (chaseDiection.equals("RIGHT")) {
          currentPosition.x += speed;
        } else if (chaseDiection.equals("DOWN")) {
          currentPosition.y -= speed;
        } else if (chaseDiection.equals("UP")) {
          currentPosition.y += speed;
        }
        dem++;
        chaseStat = true;
      } else {
        dem = 0;
        chaseStat = false;
      }
    } else {
      if (status == PUTABOMB) {
        Vector3f currentPosition = getPosition();
        int x = Math.round(currentPosition.x);
        int y = Math.round(currentPosition.y);
        if (!ObjectManager.isStartOfBomb(x, y)) {
          putABomb();
          ObjectManager.setUpBomb();
        }
        status = GOTOSAFEPOS;
      } else {
        status = PUTABOMB;
      }
    }
  }

  public void increaseBomb() {
    if (bombsOfMe.length < 4) {
      Bomb[] newBomb = new Bomb[bombsOfMe.length + 1];
      for (int i = 0; i < bombsOfMe.length; ++i) {
        newBomb[i] = bombsOfMe[i];
      }
      bombsOfMe = newBomb;
    }
  }

  public void increasePower() {
    bombPower++;
  }

  public void increaseSpeed() {
    speed = (float) Math.round(speed * 100) / 100;
    if (speed < 0.2f) {
      speed = 0.2f;
      return;
    }
    if (speed < 0.25f) {
      speed = 0.25f;
      return;
    }
  }
}