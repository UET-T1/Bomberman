package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Menu extends GameItem {

  private static Texture menuTexture;
  private static Mesh mesh;
  private static Animation animation;
  static {
    try {
      menuTexture = new Texture("resources/textures/menu.png");
      textCoords = new float[]{
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f};
      mesh = new Mesh(menuPositions, textCoords, indices, menuTexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Menu(Animation animation) {
    super(animation);
  }

  public Menu() throws Exception {
    this(mesh);
    //this(animation);
  }

  public Menu(Mesh mesh) throws Exception {
    super(mesh);
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
