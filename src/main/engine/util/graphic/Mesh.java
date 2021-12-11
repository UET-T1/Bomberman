package main.engine.util.graphic;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
  private Vertex[] vertices;
  private int[] indices;
  private int vao, pbo, ibo, tbo;

  public Texture getTexture() {
    return texture;
  }

  private Texture texture;

  public Mesh(Vertex[] vertices, int[] indices, Texture texture) {
    this.vertices = vertices;
    this.indices = indices;
    this.texture = texture;
  }

  public Mesh(Vertex[] vertices, int[] indices) {
    this.vertices = vertices;
    this.indices = indices;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  public void create() {
    vao = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(vao);

    FloatBuffer positionBuffer = setupPositionBuffer();
    setupPBO(positionBuffer);

    FloatBuffer textureBuffer = setupTextureBuffer();
    setupTBO(textureBuffer);

    IntBuffer indicesBuffer = setupIndicesBuffer();
    setupIBO(indicesBuffer);
  }

  private void setupTBO(FloatBuffer textureBuffer) {
    tbo = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tbo);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  private FloatBuffer setupTextureBuffer() {
    FloatBuffer result = MemoryUtil.memAllocFloat(vertices.length * 2);
    float[] textureData = new float[vertices.length * 2];
    for (int i = 0; i < vertices.length; i++) {
      textureData[i * 2] = vertices[i].getTextureCoord().x;
      textureData[i * 2 + 1] = vertices[i].getTextureCoord().y;
    }
    return result.put(textureData).flip();
  }

  private IntBuffer setupIndicesBuffer() {
    IntBuffer result = MemoryUtil.memAllocInt(indices.length);
    return result.put(indices).flip();
  }

  private void setupIBO(IntBuffer indicesBuffer) {
    ibo = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
  }

  private FloatBuffer setupPositionBuffer() {
    FloatBuffer result = MemoryUtil.memAllocFloat(vertices.length * 3);
    float[] positionData = new float[vertices.length * 3];
    for (int i = 0; i < vertices.length; i++) {
      positionData[i * 3] = vertices[i].getPosition().x;
      positionData[i * 3 + 1] = vertices[i].getPosition().y;
      positionData[i * 3 + 2] = vertices[i].getPosition().z;
    }
    return result.put(positionData).flip();
  }

  private void setupPBO(FloatBuffer positionBuffer) {
    pbo = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pbo);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }


  public Vertex[] getVertices() {
    return vertices;
  }

  public int[] getIndices() {
    return indices;
  }

  public int getVAO() {
    return vao;
  }

  public int getPBO() {
    return pbo;
  }

  public int getIBO() {
    return ibo;
  }

  public int getTBO() { return tbo; }
}