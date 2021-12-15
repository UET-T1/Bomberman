package engine.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import engine.GameItem;
import engine.IHud;
import engine.Utils;
import engine.Window;
import org.joml.Matrix4f;

public class Renderer {

  /**
   * Field of View in Radians
   */
  private static final float FOV = (float) Math.toRadians(60.0f);

  private static final float Z_NEAR = 0.01f;

  private static final float Z_FAR = 1000.f;

  private static final int MAX_POINT_LIGHTS = 5;

  private static final int MAX_SPOT_LIGHTS = 5;

  private final Transformation transformation;

  private ShaderProgram sceneShaderProgram;

  private ShaderProgram hudShaderProgram;

  private Window window;

  private Camera camera;


  public Renderer() {
    transformation = new Transformation();
  }

  public void init(Window window, Camera camera) throws Exception {
    setupSceneShader();
    setupHudShader();
    this.window = window;
    this.camera = camera;
  }

  private void setupSceneShader() throws Exception {
    // Create shader
    sceneShaderProgram = new ShaderProgram();
    sceneShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
    sceneShaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
    sceneShaderProgram.link();

    // Create uniforms for modelView and projection matrices and texture
    sceneShaderProgram.createUniform("projectionMatrix");
    sceneShaderProgram.createUniform("modelViewMatrix");
    sceneShaderProgram.createUniform("texture_sampler");

  }

  private void setupHudShader() throws Exception {
    hudShaderProgram = new ShaderProgram();
    hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud_vertex.vs"));
    hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud_fragment.fs"));
    hudShaderProgram.link();

    // Create uniforms for Ortographic-model projection matrix and base colour
    hudShaderProgram.createUniform("projModelMatrix");
    hudShaderProgram.createUniform("colour");
    hudShaderProgram.createUniform("hasTexture");
  }

  public void clear() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void render(Window window, Camera camera, GameItem[] gameItems, IHud hud) {

    clear();

    if (window.isResized()) {
      glViewport(0, 0, window.getWidth(), window.getHeight());
      window.setResized(false);
    }

    renderScene(window, camera, gameItems);
		if (hud == null) {
			return;
		}
    renderHud(window, hud);
  }

  public void render(GameItem gameItem) {
    if (gameItem.getMesh() != null) {
      // Set model view matrix for this item
      Matrix4f viewMatrix = transformation.getViewMatrix(camera);
      Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
      sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
      // Render the mes for this game item
      gameItem.getMesh().render();
    }
  }

  public void renderScene(Window window, Camera camera, GameItem[] gameItems) {

    sceneShaderProgram.bind();

    // Update projection Matrix
    Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(),
        window.getHeight(), Z_NEAR, Z_FAR);
    sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);

    // Update view Matrix
    Matrix4f viewMatrix = transformation.getViewMatrix(camera);

    sceneShaderProgram.setUniform("texture_sampler", 0);
    // Render each gameItem
		if (gameItems == null) {
			return;
		}
    for (GameItem gameItem : gameItems) {
      Mesh mesh = gameItem.getMesh();
      // Set model view matrix for this item
      Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
      sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
      // Render the mesh for this game item
      sceneShaderProgram.setUniform("material", mesh.getMaterial());
      mesh.render();
    }

    sceneShaderProgram.unbind();
  }


  private void renderHud(Window window, IHud hud) {
    hudShaderProgram.bind();

    Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(),
        window.getHeight(), 0);
    for (GameItem gameItem : hud.getGameItems()) {
      Mesh mesh = gameItem.getMesh();
      // Set ortohtaphic and model matrix for this HUD item
      Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
      hudShaderProgram.setUniform("projModelMatrix", projModelMatrix);
      hudShaderProgram.setUniform("colour", gameItem.getMesh().getMaterial().getAmbientColour());
      hudShaderProgram.setUniform("hasTexture",
          gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);

      // Render the mesh for this HUD item
      mesh.render();
    }

    hudShaderProgram.unbind();
  }


  public void cleanup() {
    if (sceneShaderProgram != null) {
      sceneShaderProgram.cleanup();
    }
    if (hudShaderProgram != null) {
      hudShaderProgram.cleanup();
    }
  }

  public void finishRender() {
    sceneShaderProgram.setUniform("texture_sampler", 0);
    sceneShaderProgram.unbind();
  }
}