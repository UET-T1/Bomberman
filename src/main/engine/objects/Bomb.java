package main.engine.objects;

import main.engine.Timer;
import main.engine.testGUI.Mesh;

//Bomb Item
public class Bomb extends StaticItem{

    private Timer time; // calculate the time to "BOOM"
    private boolean isStart; //true if bomb is start

    //Constructor
    public Bomb(Mesh mesh) {
        super(mesh);
        time = new Timer();
    }

    //"BOOM" function to break the broken wall or people
    public void boom(StaticItem[][] tileBrokenWall) {
        if (isStart) {
            if (time.getTime() >= 2.0f + time.getLastLoopTime()) {
                isStart = false;
                for (StaticItem[] brokenWalls : tileBrokenWall) {
                    for (StaticItem brokenWall : brokenWalls) {
                        if (brokenWall.getVisible()) {
                            int x1 = (int) brokenWall.getPosition().x;
                            int x2 = (int) getPosition().x;
                            int y1 = (int) brokenWall.getPosition().y;
                            int y2 = (int) getPosition().y;
                            if (x1 == x2 && Math.abs(y1 - y2) == 1) {
                                brokenWall.setVisible(false);
                            } else if (y1 == y2 && Math.abs(x1 - x2) == 1) {
                                brokenWall.setVisible(false);
                            }
                        }
                    }
                }
            }
        }
    }

    //starting bomb
    public void start() {
        time.init();
        isStart = true;
    }

    //check if bomb is start?
    public boolean isStart() {
        return isStart;
    }
}
