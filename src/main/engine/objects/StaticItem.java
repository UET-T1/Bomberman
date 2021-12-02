package main.engine.objects;

import main.engine.GameItem;
import main.engine.testGUI.Mesh;

// class for gameitem can not move
public class StaticItem extends GameItem {

    private boolean visible;

    public StaticItem(Mesh mesh) {
        super(mesh);
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }
}
