package main.engine;

import org.joml.Vector3f;

public interface Auto {
    public boolean search();
    public void findBrick() throws Exception;
    public void findSafePos();
    public void chase();
    public void setTargetPosition(Vector3f targetPosition);
}
