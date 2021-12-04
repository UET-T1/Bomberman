package main.engine;

public interface Observable {
    void render(Renderer renderer);
    void onCollapse();
}
