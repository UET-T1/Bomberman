package engine;

public interface IGameLogic {

  void init(Window window) throws Exception;

  void input(Window window, Input input) throws Exception;

  void update(float interval, Input input);

  void render(Window window);

  void cleanup();
}