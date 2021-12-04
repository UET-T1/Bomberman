package main.engine.util.opengl;

import org.lwjgl.opengl.GL33;

public class Renderer {
  public void renderMesh(Mesh mesh) {
    GL33.glBindVertexArray(mesh.getVertexArrayObj());
    GL33.glEnableVertexAttribArray(0);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, mesh.getIndicesBufferObj());
    GL33.glDrawElements(GL33.GL_TRIANGLES, mesh.getIndices().length, GL33.GL_FLOAT, 0);
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL33.glDisableVertexAttribArray(0);
  }
}
