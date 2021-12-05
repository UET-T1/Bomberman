package main.engine.objects;

import main.engine.GameObject;
import main.engine.Input;
import main.engine.ObjectManager;
import main.engine.Renderer;
import main.engine.Timer;
import main.engine.Window;
import main.engine.testGUI.Mesh;

//Bomb Item
public class Bomb extends GameObject{

    private Timer time; // calculate the time to "BOOM"
    private boolean isStart; //true if bomb is start
    private int power; //power of the bomb
    private float durationTime; //duration time of bomb
    private float timeToBoom;// acuracy time to "BOOM"
    private boolean isShow;

    public Bomb(Mesh mesh, float durationTime) throws Exception {
        super(mesh);
        time = new Timer();
        power = 1;
        this.durationTime = durationTime;
        isStart = false;
        isShow = false;
    }

    public Bomb(Bomb bomb) {
        super(bomb.getMesh());
        time = bomb.time;
        power = bomb.power;
        this.durationTime = bomb.durationTime;
        this.isStart = bomb.isStart();
        this.isShow = bomb.isShow();
    }

    public float getTime() {
        return (float) (time.getTime() - time.getLastLoopTime());
    }

    public void setTimeToBoom(float timeToBoom) {
        this.timeToBoom = timeToBoom;
    }

    public float getTimeToBoom() {
        return timeToBoom;
    }

    public int getPower() {
        return power;
    }

    //check if bomb is start?
    public boolean isStart() {
        return isStart;
    }

    public boolean isShow() {
        return isShow;
    }

    public void stop() {
        isStart = false;
    }

    public float getDurationTime() {
        return durationTime;
    }

    public void handleEvent() {
        if (isStart) {
            if (isShow && time.getTime() >= durationTime + time.getLastLoopTime()) {
                boom();
                time.init();
            }
            if (!isShow && time.getTime() >= time.getLastLoopTime() + 0.5f) {
                isStart = false;
            }
        }
    }

    //starting bomb
    public void start(int power) {
        this.power = power;
        time.init();
        isStart = true;
        isShow = true;
    }

    //"BOOM" function to break the broken wall or people
    public void boom() {
        isShow = false;
        boomCenter();
        boomRight();
        boomLeft();
        boomUp();
        boomDown();
    }

    private void boomCenter() {
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        ObjectManager.setStartOfFlame(x, y);
    }

    private void boomRight() {
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        for (int i = 1; i <= power; ++i) {
            if (x + i <= ObjectManager.width) {
                if(!ObjectManager.lanFromBomb(x + i, y)) {
                    return;
                }
            }
        }
    }

    private void boomLeft() {
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        for (int i = 1; i <= power; ++i) {
            if (x - i >= 1) {
                if (!ObjectManager.lanFromBomb(x - i, y)) {
                    return;
                }
            }
        }
    }

    private void boomUp() {
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        for (int i = 1; i <= power; ++i) {
            if (y + i <= ObjectManager.height) {
                if (!ObjectManager.lanFromBomb(x, y + i)) {
                    return;
                }
            }
        }
    }

    private void boomDown() {
        int x = (int) getPosition().x;
        int y = (int) getPosition().y;
        for (int i = 1; i <= power; ++i) {
            if (y - i >= 1) {
                if (!ObjectManager.lanFromBomb(x, y - i)) {
                    return;
                }
            }
        }
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
