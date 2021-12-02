package main.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Input {

  private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
  private final boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
  private double x, y;
  private GLFWKeyCallback keyboard;
  private final GLFWMouseButtonCallback mouseButtons;
  private final GLFWCursorPosCallback mousePos;

  public Input() {
    keyboard =
        new GLFWKeyCallback() {
          @Override
          public void invoke(long window, int key, int scancode, int action, int mods) {
            // update status for key: press or release
            keys[key] = (action != GLFW.GLFW_RELEASE);
          }
        };

    mouseButtons =
        new GLFWMouseButtonCallback() {
          @Override
          public void invoke(long window, int button, int action, int mods) {
            buttons[button] = (action != GLFW.GLFW_RELEASE);
          }
        };

    mousePos =
        new GLFWCursorPosCallback() {
          @Override
          public void invoke(long window, double xpos, double ypos) {
            x = xpos;
            y = ypos;
          }
        };
  }

  public void destroy() {
    keyboard.free();
    mousePos.free();
    mouseButtons.free();
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public GLFWKeyCallback getKeyboardCallback() {
    return keyboard;
  }

  public GLFWMouseButtonCallback getMouseButtonsCallback() {
    return mouseButtons;
  }

  public GLFWCursorPosCallback getMousePosCallback() {
    return mousePos;
  }

  public boolean isKeyDown(int key) {
    return keys[key];
  }

  public boolean isButtonDown(int button) {
    return buttons[button];
  }
}
