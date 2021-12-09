package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Wall extends GameItem {

  private static Texture wallTexture;
  private static Mesh mesh;
  private static Animation animation;

  static {
    try {
      wallTexture = new Texture("resources/textures/wall.png");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, wallTexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Wall(Animation animation) {
    super(animation);
  }

  public Wall() throws Exception {
    this(mesh);
    //this(animation);
  }

  public Wall(Mesh mesh) throws Exception {
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
