package main.engine.util.graphic;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {

  private Vector3f position;
  private Vector3f color;
  private Vector2f textureCoord;

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
