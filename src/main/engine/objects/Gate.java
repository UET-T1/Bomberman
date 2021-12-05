package main.engine.objects;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.Renderer;
import main.engine.Window;
import main.engine.testGUI.Mesh;

public class Gate extends GameObject {

    public Gate(Mesh mesh) {
        super(mesh);
        //TODO Auto-generated constructor stub
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
    public void handleEvent(Window window, Input input) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
}
