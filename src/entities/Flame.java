package entities;


import engine.GameItem;
import engine.Input;
import engine.Timer;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

//Bomb Item
public class Flame extends GameItem {

  public float timeToBoom[];// calculate time to "BOOM"
  protected boolean visible;//is item visible?
  private Timer time; // calculate the time to stop "BOOM"
  private boolean isStart; //true if bomb is start
  private float durationTime;// duration time of flame
  private static Texture flameTexture;

  static {
    try {
      flameTexture = new Texture("resources/textures/flame.jpg");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, flameTexture);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Constructor
  public Flame(Mesh mesh, float durationTime) throws Exception {
    super(mesh);
    time = new Timer();
    this.durationTime = durationTime;
    timeToBoom = new float[0];
  }

  public Flame(float durationTime) throws Exception {
    super();
    time = new Timer();
    this.durationTime = durationTime;
    timeToBoom = new float[0];
  }

  //check if bomb is start?
  public boolean isStart() {
    return isStart;
  }

  public boolean getVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public void stop() {
    if (isStart) {
      if (time.getTime() >= durationTime + time.getLastLoopTime()) {
        isStart = false;
        setVisible(false);
        for (int i = 0; i < timeToBoom.length; ++i) {
          if (timeToBoom[i] >= 2.0f - 0.01f) {
            timeToBoom[i] = 0.0f;
          }
        }
      }
    }
  }

  //starting bomb
  public void start() {
    time.init();
    setVisible(true);
    isStart = true;
  }

  public void setNewTimeToBoom() {
    timeToBoom = new float[0];
  }

  public void setTimeToBoom(float time) {
    int n = timeToBoom.length;
    for (int i = 0; i < n; ++i) {
        if (time == timeToBoom[i]) {
            return;
        }
    }
    float[] newTimeToBoom = new float[n + 1];
    for (int i = 0; i < n; ++i) {
      newTimeToBoom[i] = timeToBoom[i];
    }
    newTimeToBoom[n] = time;
    timeToBoom = newTimeToBoom;
    n += 1;
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
