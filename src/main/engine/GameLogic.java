package main.engine;

/**
 * Implement necessary APIs for game objects.
 */
public interface GameLogic {
  void create(Window window) throws Exception;
  void input(Window window, Input input);
  // update after each interval
  void update(float interval, Input input, Window window);
  void render(Window window);
  void destroy();
}
