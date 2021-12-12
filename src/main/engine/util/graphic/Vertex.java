package main.engine.util.graphic;

import main.engine.util.math.*;


public class Vertex {
  private final Vector3f position;

  public Vector3f getColor() {
    return color;
  }

  private Vector3f color;
  private Vector2f textureCoord;

  public Vertex(Vector3f position) {
    this.position = position;
  }

  public Vertex(Vector3f position, Vector2f textureCoord) {
    this.position = position;
    this.textureCoord = textureCoord;
  }

  public Vertex(Vector3f position, Vector3f color, Vector2f textureCoord) {
    this.position = position;
    this.color = color;
    this.textureCoord = textureCoord;
  }

  public Vector2f getTextureCoord() {
    return textureCoord;
  }

  public Vector3f getPosition() {
    return position;
  }
}
