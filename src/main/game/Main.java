package main.game;

import main.engine.Window;
import org.lwjgl.glfw.GLFW;

public class Main implements Runnable{
  public Thread game;
  public Window window;
  public static final int width = 1280;
  public static final int height = 760;

  public void start() {
    game = new Thread(this, "game");
    game.start();
  }

  public void init() {
    window = new Window(width, height, "TEST");
    window.create();
  }

  @Override
  public void run() {
    init();

    // ESC button
    while (!window.isClose()) {
      update();
      render();
    }
    window.destroy();
  }

  private void update() {
    window.update();
  }
  private void render() { window.render();}


  public static void main(String[] args) {
    new Main().start();
  }
}
