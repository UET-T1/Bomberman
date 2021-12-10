package main.engine.objects;

import java.util.concurrent.ThreadLocalRandom;

import org.joml.Vector3f;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.Movable;
import main.engine.ObjectManager;
import main.engine.Renderer;
import main.engine.Util;
import main.engine.Window;
import main.engine.testGUI.Mesh;

public class Balloom extends GameObject implements Movable{

    private boolean isDead;
    private boolean chaseStat;
    private Vector3f nextPosition;
    
    public Balloom(Mesh mesh) {
        super(mesh);
        isDead = false;
        chaseStat = false;
        nextPosition = null;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
        
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public void onCollapse() {
        isDead = true;
    }

    @Override
    public void onCollision() {
        
    }

    @Override
    public void handleCollision() {
        if(ObjectManager.checkIfEnemyDead(getPosition().x, getPosition().y)) {
            onCollapse();
        }
    }

    @Override
    public void move(Window window, Input input) {
        Vector3f pos = getPosition();
        pos = Util.round100(pos);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        if (randomNum == 1 && ObjectManager.checkCollision(pos.x + 1, pos.y, false, null, this)) {
            nextPosition = new Vector3f(pos.x + 1, pos.y, pos.z);
            chaseStat = true;
            return;
        }
        if (randomNum == 2 && ObjectManager.checkCollision(pos.x - 1, pos.y, false, null, this)) {
            nextPosition = new Vector3f(pos.x - 1, pos.y, pos.z);
            chaseStat = true;
            return;
        }
        if (randomNum == 3 && ObjectManager.checkCollision(pos.x, pos.y + 1, false, null, this)) {
            nextPosition = new Vector3f(pos.x, pos.y + 1, pos.z);
            chaseStat = true;
            return;
        }
        if (randomNum == 4 && ObjectManager.checkCollision(pos.x, pos.y - 1, false, null, this)) {
            nextPosition = new Vector3f(pos.x, pos.y - 1, pos.z);
            chaseStat = true;
            return;
        }
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
        
    }

    @Override
    public void handleEvent(Window window, Input input) throws Exception {
        if (!chaseStat) {
            nextPosition = null;
            move(window, input);
        }
        else chase();
    }

    public void chase() {
        if (nextPosition != null) {
            Util.round100(nextPosition);
            Util.round100(getPosition());
            if (nextPosition.x == getPosition().x) {
                if (nextPosition.y > getPosition().y) {
                    getPosition().y += speed;
                } else if (nextPosition.y < getPosition().y) {
                    getPosition().y -= speed;
                } else {
                    chaseStat = false;
                }
            }
            if (nextPosition.y == getPosition().y) {
                if (nextPosition.x > getPosition().x) {
                    getPosition().x += speed;
                } else if (nextPosition.x < getPosition().x) {
                    getPosition().x -= speed;
                } else {
                    chaseStat = false;
                }
            }
            Util.round100(nextPosition);
            Util.round100(getPosition());
        }
    }
    
}
