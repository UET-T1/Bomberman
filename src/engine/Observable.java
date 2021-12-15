package engine;

import engine.graphics.Renderer;

public interface Observable {

  void render(Renderer renderer);

  void onCollapse();
}
