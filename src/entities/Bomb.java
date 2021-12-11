package entities;

import engine.GameItem;
import engine.Input;
import engine.ObjectManager;
import engine.Timer;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;


//Bomb Item
public class Bomb extends GameItem {

  private Timer time; // calculate the time to "BOOM"
  private boolean isStart; //true if bomb is start
  private int power; //power of the bomb
  private float durationTime; //duration time of bomb
  private float timeToBoom;// acuracy time to "BOOM"
  private boolean isShow;
  private static Texture bombTexture;
  private static Mesh mesh;
  private static Animation animation;

  static {
    try {
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      bombTexture = new Texture("resources/textures/bomb.png");
      mesh = new Mesh(positions, textCoords, indices, bombTexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Bomb(Mesh mesh, float durationTime) throws Exception {
    super(mesh);
    time = new Timer();
    power = 1;
    this.durationTime = durationTime;
    isStart = false;
    isShow = false;
  }

  public Bomb(float durationTime) throws Exception {
    this(mesh, durationTime);
    //this(animation, durationTime);
  }

  public Bomb(Bomb bomb) throws Exception {
    super(bomb.getMesh());
    time = bomb.time;
    power = bomb.power;
    this.durationTime = bomb.durationTime;
    this.isStart = bomb.isStart();
    this.isShow = bomb.isShow();
  }

  public Bomb(Animation animation, float durationTime) {
    super(animation);
    time = new Timer();
    power = 1;
    this.durationTime = durationTime;
    isStart = false;
    isShow = false;
  }

  public float getTime() {
    return (float) (time.getTime() - time.getLastLoopTime());
  }

  public float getTimeToBoom() {
    return timeToBoom;
  }

  public void setTimeToBoom(float timeToBoom) {
    this.timeToBoom = timeToBoom;
  }

  public int getPower() {
    return power;
  }

  //check if bomb is start?
  public boolean isStart() {
    return isStart;
  }

  public boolean isShow() {
    return isShow;
  }

  public void stop() {
    isStart = false;
  }

  public float getDurationTime() {
    return durationTime;
  }

  public void handleEvent() {
    if (isStart) {
      if (isShow && time.getTime() >= durationTime + time.getLastLoopTime()) {
        boom();
        time.init();
      }
      if (!isShow && time.getTime() >= time.getLastLoopTime() + 0.5f) {
        isStart = false;
      }
    }
  }

  //starting bomb
  public void start(int power) {
    this.power = power;
    time.init();
    isStart = true;
    isShow = true;
  }

  //"BOOM" function to break the broken wall or people
  public void boom() {
    ObjectManager.soundMgr.playSoundSource("BOOM");
    isShow = false;
    boomCenter();
    boomRight();
    boomLeft();
    boomUp();
    boomDown();
  }

  private void boomCenter() {
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    ObjectManager.setStartOfFlame(x, y);
  }

  private void boomRight() {
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    for (int i = 1; i <= power; ++i) {
      if (x + i <= ObjectManager.width) {
        if (!ObjectManager.lanFromBomb(x + i, y)) {
          return;
        }
      }
    }
  }

  private void boomLeft() {
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    for (int i = 1; i <= power; ++i) {
      if (x - i >= 1) {
        if (!ObjectManager.lanFromBomb(x - i, y)) {
          return;
        }
      }
    }
  }

  private void boomUp() {
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    for (int i = 1; i <= power; ++i) {
      if (y + i <= ObjectManager.height) {
        if (!ObjectManager.lanFromBomb(x, y + i)) {
          return;
        }
      }
    }
  }

  private void boomDown() {
    int x = (int) getPosition().x;
    int y = (int) getPosition().y;
    for (int i = 1; i <= power; ++i) {
      if (y - i >= 1) {
        if (!ObjectManager.lanFromBomb(x, y - i)) {
          return;
        }
      }
    }
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  @Override
  public void onCollapse() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onCollision() {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleCollision() {
    // TODO Auto-generated method stub

  }

  @Override
  public void handleEvent(Window window, Input input) {
    // TODO Auto-generated method stub

  }

}
