package engine;
import engine.network.*;

public class GameEngine implements Runnable {

  public static final int TARGET_FPS = 75;

  public static final int TARGET_UPS = 30;

  private final Window window;

  private final Timer timer;

  private final IGameLogic gameLogic;

  private final Input input;


  public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic)
      throws Exception {
    window = new Window(windowTitle, width, height, vSync);
    input = new Input();
    this.gameLogic = gameLogic;
    timer = new Timer();
  }

  @Override
  public void run() {
    try {
      init();
      gameLoop();
    } catch (Exception excp) {
      excp.printStackTrace();
    } finally {
      cleanup();
    }
  }

  protected void init() throws Exception {
    window.init();
    timer.init();
    input.init(window);
    gameLogic.init(window);
  }

  protected void gameLoop() throws Exception {
    float elapsedTime;
    float accumulator = 0f;
    float interval = 1f / TARGET_UPS;

    while (!window.windowShouldClose()) {
      elapsedTime = timer.getElapsedTime();
      accumulator += elapsedTime;

      input();

      while (accumulator >= interval) {
        update(interval);
        accumulator -= interval;
      }

      render();

      if (!window.isvSync()) {
        sync();
      }
    }

    Client.terminate();
  }

  protected void cleanup() {
    gameLogic.cleanup();
  }

  private void sync() {
    float loopSlot = 1f / TARGET_FPS;
    double endTime = timer.getLastLoopTime() + loopSlot;
    while (timer.getTime() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
      }
    }
  }

  protected void input() throws Exception {
    gameLogic.input(window, input);
  }

  protected void update(float interval) throws Exception {
    gameLogic.update(interval, input);
  }

  protected void render() {
    gameLogic.render(window);
    window.update();
  }
}