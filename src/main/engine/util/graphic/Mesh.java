package main.engine.util.graphic;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
  private final Vertex[] vertices;
  // rendering vertices order
  private final int[] indices;
  private int vertexArrayObj;
  private int positionBufferObj;
  private int indicesBufferObj;

  public Mesh(Vertex[] v, int[] i) {
    vertices = v;
    indices = i;
  }

  public Vertex[] getVertices() {
    return vertices;
  }

  public int[] getIndices() {
    return indices;
  }

  public int getVertexArrayObj() {
    return vertexArrayObj;
  }

  public int getPositionBufferObj() {
    return positionBufferObj;
  }

  public int getIndicesBufferObj() {
    return indicesBufferObj;
  }

  public void create() {
    vertexArrayObj = GL33.glGenVertexArrays();
    // Add buffers to vertexArrayObj
    GL33.glBindVertexArray(vertexArrayObj);

    // Instead of MemoryUtil, we can use stack instead
    FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
    float[] pos = new float[vertices.length * 3];
    for (int i = 0; i < vertices.length; i++) {
      pos[i * 3] = vertices[i].getPosition().x;
      pos[i * 3 + 1] = vertices[i].getPosition().y;
      pos[i * 3 + 2] = vertices[i].getPosition().z;
    }
    positionBuffer.put(pos).flip();

    positionBufferObj = GL33.glGenBuffers();
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, positionBufferObj);
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, positionBuffer, GL33.GL_STATIC_DRAW);
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);

    IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
    indicesBuffer.put(indices).flip();

    indicesBufferObj = GL33.glGenBuffers();
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, indicesBufferObj);
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL33.GL_STATIC_DRAW);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);
  }
}
