package main.engine.objects;

import main.engine.GameLogic;
import org.joml.Vector3f;

/**
 * Represent all game objects, would implement GameLogic interface when created
 */
public abstract class Object {
  private Vector3f position;
  private boolean destroyable;

}
