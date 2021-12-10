package main.engine.objects;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.ObjectManager;
import main.engine.Renderer;
import main.engine.Util;
import main.engine.Window;
import main.engine.testGUI.Mesh;

// class for gameitem can not move
public class Brick extends GameObject {

    protected boolean visible;//is item visible?

    public Brick(Mesh mesh) throws Exception {
        super(mesh);
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    @Override
    public void onCollapse() {
        setVisible(false);
        Util.round1(getPosition());
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        ObjectManager.createItem(x, y);
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
