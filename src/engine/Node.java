package engine;

import org.joml.Vector3f;

//A* Node
public class Node implements Comparable<Node> {

  public float f;
  public float h;
  public float g;
  public Vector3f value;
  public Node father;

  public Node(float f, float g, float h, Vector3f value, Node father) {
    this.f = f;
    this.g = g;
    this.h = h;
    this.value = value;
    this.father = father;
  }

  @Override
  public int compareTo(Node o) {
    if (this.f > o.f) {
      return 1;
    }
    if (this.f < o.f) {
      return -1;
    }
    if (this.h > o.h) {
      return 1;
    }
    if (this.h < o.h) {
      return -1;
    }
    return 0;
  }
}
