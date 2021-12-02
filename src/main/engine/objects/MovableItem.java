package main.engine.objects;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Mesh;


//class for item can move
public abstract class MovableItem extends GameItem {

    public float speed;

    public MovableItem(Mesh mesh) {
        super(mesh);
    }

    public abstract void setSpeed(float speed);

    protected boolean isInBomb;// true if have bomb in leg

    protected Bomb inBomb;//bomb in leg of object

    // check collision between x, y and map
    public boolean checkCollision(float x, float y,
        GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb) {
        x = (float) Math.round(x * 100) / 100;
        y = (float) Math.round(y * 100) / 100;

        for (int i = 0; i < tileMap.length; ++i)
            for (int j = 0; j < tileMap[i].length; ++j) {
                if (isInBomb && tileBomb[i][j].getPosition().equals(inBomb.getPosition())) {
                    continue;
                }
                if (tileBomb[i][j].isStart() || tileBrokenWall[i][j].getVisible()
                        || tileMap[i][j] instanceof StaticItem) {
                    Vector3f pos = tileMap[i][j].getPosition();
                    if (pos.x < x + 1 &&
                        x < pos.x + 1 &&
                        pos.y < y + 1 &&
                        y < pos.y + 1) {
                        return false;
                    }
                }
            }
        return true;
    }

    // check collision between x, y and targetx, targetY
    public boolean checkCollision(float x, float y, float targetX, float targetY) {
        x = (float) Math.round(x * 100) / 100;
        y = (float) Math.round(y * 100) / 100;
        targetX = (float) Math.round(targetX * 100) / 100;
        targetY = (float) Math.round(targetY * 100) / 100;
        if (targetX < x + 1 &&
                x < targetX + 1 &&
                targetY < y + 1 &&
                y < targetY + 1) {
            return false;
        }
        return true;
    }

    public abstract void move(Window window, Input input,
                              GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb);
}
