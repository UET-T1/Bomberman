package main.engine.util.opengl;

import org.joml.Vector3f;

public class Vertex {

  private Vector3f position;

  public Vertex(Vector3f pos) {
    position = pos;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setPosition(Vector3f position) {
    this.position = position;
  }
}
