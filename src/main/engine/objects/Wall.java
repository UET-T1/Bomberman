package main.engine.objects;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.Renderer;
import main.engine.Window;
import main.engine.testGUI.Mesh;

public class Wall extends GameObject {

    public Wall(Mesh mesh) throws Exception {
        super(mesh);
    }
    
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
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
    public void handleEvent(Window window, Input input) {
        // TODO Auto-generated method stub
        
    }
    
}
