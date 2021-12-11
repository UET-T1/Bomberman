package entities;


import engine.GameItem;
import engine.Input;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;
import org.lwjgl.assimp.AIAnimation;

public class Gate extends GameItem {

  private static Texture gateTexture;
  private static Mesh mesh;
  private static Animation animation;

  static {
    try {
      gateTexture = new Texture("resources/textures/gate.png");
      textCoords = new float[]{
          0.0f, 0.0f,
          1.0f, 0.0f,
          1.0f, 1.0f,
          0.0f, 1.0f};
      mesh = new Mesh(positions, textCoords, indices, gateTexture);
      // remove mesh and active the following comment
      // hard-coded
      //animation = new Animation(amount, fps, fileName, positions, textureCoords, indices);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Gate(Animation animation) {
    super(animation);
  }


  public Gate(Mesh mesh) throws Exception {
    super(mesh);
    //TODO Auto-generated constructor stub
  }

  public Gate() throws Exception {
    this(mesh);
    //this(animation);
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
