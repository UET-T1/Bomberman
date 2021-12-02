package main.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class Window {
  private int width;
  private int height;
  private final String title;
  private long window;
  private Input input;
  public static long time;
  public static int frames = 0;
  private boolean resized;


  public Window(int w, int h, String tl) {
    width = w;
    height = h;
    title = tl;
    input = new Input();
  }

  public void create() {

    //Set default error stream to System.err
    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
  
    window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

    if (window == 0) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup resize callback
    GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
      this.width = width;
      this.height = height;
      this.setResized(true);
    });

    GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
    GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

    // If change will be made, make it in window
    GLFW.glfwMakeContextCurrent(window);

    GL.createCapabilities();

    //Set default callbacks for Input object
    setCallback();

    // Make opengl context current
    GLFW.glfwShowWindow(window);

    // Enable vsync to avoid screen tearing
    GLFW.glfwSwapInterval(1);

    time = System.currentTimeMillis();

    // Set the clear color
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glEnable(GL_DEPTH_TEST);
  }

  public void setResized(boolean b) {
    resized = b;
  }

  public boolean isResized() {
    return resized;
  }

  public void update() {
    // swap buffer to change frame without rendering it from scratch, instead we load it in buffer then swap
    //GLFW.glfwSwapBuffers(window);
    GLFW.glfwPollEvents();
    GLFW.glfwSwapBuffers(window);

    frames++;
    if (System.currentTimeMillis() > time + 1000) {
      GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
      time = System.currentTimeMillis();
      frames = 0;
    }
  }

  private void setCallback() {
    // Handling input through Input class (input object)
    GLFW.glfwSetCursorPosCallback(window, input.getMousePosCallback());
    GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
    GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
  }

  public void render() {
    GLFW.glfwSwapBuffers(window);
  }

  public boolean isClose() {
      return input.isKeyDown(GLFW.GLFW_KEY_ESCAPE) || GLFW.glfwWindowShouldClose(window);
  }

  public void destroy() {
    input.destroy();
    GLFW.glfwWindowShouldClose(window);
    GLFW.glfwDestroyWindow(window);
    GLFW.glfwTerminate();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isKeyDown(int keyCode) {
    return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
  }

  public Input getInputObj() {
    return input;
  }
}
