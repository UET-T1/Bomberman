package main.engine.objects;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.Renderer;
import main.engine.Window;
import main.engine.testGUI.Mesh;

public class Item extends GameObject{

    private int function;
    
    private boolean visible;
    
    public Item(Mesh mesh, int function) {
        super(mesh);
        visible = false;
        this.function = function;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFunction() {
        return function;
    }

    @Override
    public void onCollapse() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCollision() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleCollision() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleEvent(Window window, Input input) throws Exception {
        // TODO Auto-generated method stub
    }
    
}
