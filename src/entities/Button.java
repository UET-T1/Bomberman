package entities;


import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.GameItem;
import engine.Input;
import engine.ObjectManager;
import engine.Window;
import engine.graphics.Animation;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Texture;

public class Button extends GameItem {

  private static Mesh[] meshList;
  private static Texture[] textureList;
  public static Map<String, Integer> characters = new HashMap<>();
  private static float[][] buttonPosition;

  static {
    characters.put("Start", 0);
  }
  
  static {
    try {
      buttonPosition = new float[][] {
        new float[] {
          // V0
          -4.0f, 2.0f, 0.0f,
          // V1
          4.0f, 2.0f, 0.0f,
          // V2
          4.0f, -2.0f, 0.0f,
          // V3
          -4.0f, -2.0f, 0.0f}
      };

      textCoords = new float[]{
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f};

      textureList = new Texture[] {
          new Texture("resources/textures/start0.png"),
          new Texture("resources/textures/start1.png")
      };

      meshList = new Mesh[2];
      for (int i = 0; i < meshList.length; ++i) {
          meshList[i] = new Mesh(buttonPosition[0], textCoords, indices, textureList[i]);
      };

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String charater;
  private int status;
  public boolean isClick;

  public Button(Animation animation) {
    super(animation);
  }

  public Button() throws Exception {
    this(meshList[0]);
    //this(animation);
  }

  public Button(String charater) throws Exception {
    this.charater = charater;
    status = 0;
  }

  public Button(Mesh mesh) throws Exception {
    super(mesh);
  }


  @Override
  public void render(Renderer renderer) {
    meshItem = meshList[status + characters.get(charater)];
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
    double x = input.getMouseX() / window.width * 32 - 16;
    double y = -(input.getMouseY() / window.height * 18 - 9);
    if (getPosition().x - 4 <= x && x <= getPosition().x + 4
    &&  getPosition().y - 2 <= y && y <= getPosition().y + 2) {
        status = 1;
        if (input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
          isClick = true;
        }
    } else {
        status = 0;
    }
  }

}
