package main.engine;

//class for item can move
public interface Movable {

    public void move(Window window, Input input);
    public void setSpeed(float speed);

}
