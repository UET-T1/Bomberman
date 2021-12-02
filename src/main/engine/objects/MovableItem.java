package main.engine.objects;

import java.util.Stack;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Mesh;


//class for item can move
public abstract class MovableItem extends GameItem {

    protected boolean isTargetMoved;// true if target moved

    protected float speed;// speed of item

    protected Vector3f targetPosition = new Vector3f(8,8,0);// target position

    protected Stack<Vector3f> stackPosition;// stack of the way to target
    
    protected boolean isInBomb;// true if have bomb in leg

    protected Bomb inBomb;// bomb in leg of object
    
    protected boolean chaseStat;// true if item not go full one square

    protected String chaseDiection = "";// chase direction
    
    protected int dem = 0;// the number of step to go full one square

    protected boolean[][] checked;// if tile[i][j] is checked?

    //construction
    public MovableItem(Mesh mesh) {
        super(mesh);
        isTargetMoved = false;
        chaseStat = false;
        checked = new boolean[16][16];
        stackPosition = new Stack<>();
        isInBomb = false;
    }

    // set speed
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    //A* Node
    protected class Node implements Comparable<Node> {
        protected float f;
        protected float h;
        protected float g;
        protected Vector3f value;
        protected Node father;

        public Node(float f, float g, float h, Vector3f value, Node father) {
            this.f = f;
            this.g = g;
            this.h = h;
            this.value = value;
            this.father = father;
        }

        @Override
        public int compareTo(Node o) {
            if (this.f > o.f) {
                return 1;
            }
            if (this.f < o.f) {
                return -1;
            }
            if (this.h > o.h) {
                return 1;
            }
            if (this.h < o.h) {
                return -1;
            }
            return 0;
        }
    }

    // Set target position to chase
    public void setTargetPosition(Vector3f targetPosition) {
        Vector3f addPos = new Vector3f(targetPosition);
        addPos = round1(addPos);
        if (this.targetPosition.equals(addPos)) {
            return;
        }
        isTargetMoved = true;
        this.targetPosition = new Vector3f(addPos);
    }

    // move funtion
    public abstract void move(Window window, Input input,
                              GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb);

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

    //function to chase target
    protected void chase() {
        if (!stackPosition.isEmpty()) {
            Vector3f currentPosition = this.getPosition();
            if (dem < 1/speed) {
                if (chaseDiection.equals("LEFT")) {
                    currentPosition.x -= speed;
                } else if (chaseDiection.equals("RIGHT")) {
                    currentPosition.x += speed;
                } else if (chaseDiection.equals("DOWN")) {
                    currentPosition.y -= speed;
                } else if (chaseDiection.equals("UP")) {
                    currentPosition.y += speed;
                }
                dem++;
                chaseStat = true;
            } else {
                stackPosition.pop();
                dem = 0;
                chaseStat = false;
                if (!stackPosition.isEmpty()) {
                    float x = currentPosition.x;
                    float x1 = stackPosition.peek().x;
                    float y = currentPosition.y;
                    float y1 = stackPosition.peek().y;
                    if (Math.round(x) > Math.round(x1)) {
                        chaseDiection = "LEFT";
                    } else if (Math.round(x) < Math.round(x1)) {
                        chaseDiection = "RIGHT";
                    } else if (Math.round(y) > Math.round(y1)) {
                        chaseDiection = "DOWN";
                    } else if (Math.round(y) < Math.round(y1)) {
                        chaseDiection = "UP";
                    }
                }
            }
        }
    }
}
