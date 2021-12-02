package main.engine.util.opengl;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
  private Vertex[] vertices;
  // rendering vertices order
  private int[] renderingOrder;
  private int vertexArray;

  public Mesh(Vertex[] v, int[] r) {
    vertices = v;
    renderingOrder = r;
  }

  public void create() {
    vertexArray = GL33.glGenVertexArrays();
    GL33.glBindVertexArray(vertexArray);

  }

}
