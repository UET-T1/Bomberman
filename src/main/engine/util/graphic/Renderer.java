package main.engine.util.graphic;

import main.engine.Window;
import main.engine.GameObject;
import main.engine.util.math.*;
import main.engine.util.graphic.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class Renderer {
  private Shader shader;
  private double temp = 0;
  private Window window;
  public Renderer(Window window, Shader shader) {
    this.shader = shader;
    this.window = window;
  }

  // improve with batch rendering
  public void renderMesh(GameObject object, Camera camera) {
    temp += 0.02;
    GL30.glBindVertexArray(object.getMesh().getVAO());
    GL30.glEnableVertexAttribArray(0);
    GL33.glEnableVertexAttribArray(1);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL13.glBindTexture(GL11.GL_TEXTURE_2D, object.getMesh().getTexture().getId());
    // bind before drawing
    shader.bind();
    shader.setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
    shader.setUniform("projection", window.getProjection());
    shader.setUniform("view", Matrix4f.viewMatrix(camera.getPosition(), camera.getRotation()));
    GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
    // unbind after drawing to prepare to next shader
    shader.unbind();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL30.glDisableVertexAttribArray(0);
    GL33.glDisableVertexAttribArray(1);
    GL30.glBindVertexArray(0);
  }
}