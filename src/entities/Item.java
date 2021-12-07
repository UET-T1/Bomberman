package entities;

import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Item extends GameItem {

  public static final int bombItem = 0;
  public static final int speedItem = 1;
  public static final int powerItem = 2;
  public int type;
  float[] textCoords = new float[]{
      0.0f, 0.0f,
      1.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f};
  private Texture[] textures = {
      new Texture("resources/textures/bombItem.jpg"),
      new Texture("resources/textures/speedItem.jpg"),
      new Texture("resources/textures/powerItem.jpg")
  };
  private int function;
  private boolean visible;

  public Item(Mesh mesh, int function) throws Exception {
    super(mesh);
    visible = false;
    this.function = function;
  }

  public Item(int type) throws Exception {
    super();
    mesh = new Mesh(positions, textCoords, indices, textures[type]);
    visible = false;
    this.function = type + 1;
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(this);
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public int getFunction() {
    return function;
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
