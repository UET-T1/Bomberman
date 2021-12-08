package main.engine.objects;

import java.util.PriorityQueue;

import org.joml.Vector3f;

import main.engine.Auto;
import main.engine.GameObject;
import main.engine.Input;
import main.engine.Movable;
import main.engine.ObjectManager;
import main.engine.Pair;
import main.engine.Renderer;
import main.engine.Util;
import main.engine.Window;
import main.engine.testGUI.Mesh;

import static org.lwjgl.glfw.GLFW.*;

public class Deadpool extends GameObject implements Movable, Auto {
    
    private boolean autoMode;// true if autoMode is ON

    private final int PUTABOMB = 1;//SEARCH status for findWay function

    private final int GOTOSAFEPOS = 0;//BREAKWALL status for findWay function

    private final int FIND = 2;

    private int status;// status to break wall or search the way

    private Bomb[] bombsOfMe; // bombs of this itemv  

    private boolean isDead;

    private float timeSquare;

    private boolean isInBomb;// true if have bomb in leg

    private Bomb inBomb;// bomb in leg of object

    protected Vector3f targetPosition;// target position
    
    public Deadpool(Mesh mesh) {
        super(mesh);
        autoMode = true;
        bombsOfMe = new Bomb[1];
        isDead = false;
        status = 2;
        isInBomb = false;
        targetPosition = new Vector3f(8,8,0);
    }
    
    @Override
    public void render(Renderer renderer) {
        renderer.render(this);
    }

    @Override
    public void onCollapse() {
        
    }

    @Override
    public void onCollision() {
        
    }

    @Override
    public void handleCollision() {
        
    }

    @Override
    public void move(Window window, Input input) {
        if (input.isKeyDown(GLFW_KEY_LEFT)) {
            moveLeft();
        } else if (input.isKeyDown(GLFW_KEY_RIGHT)) {
            moveRight();
        } else if (input.isKeyDown(GLFW_KEY_UP)) {
            moveUp();
        } else if (input.isKeyDown(GLFW_KEY_DOWN)) {
            moveDown();
        }
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setTargetPosition(Vector3f targetPosition) {
        Vector3f addPos = new Vector3f(targetPosition);
        addPos = Util.round1(addPos);
        if (this.targetPosition.equals(addPos)) {
            return;
        }
        isTargetMoved = true;
        this.targetPosition = new Vector3f(addPos);
    }

    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }

    public void moveLeft() {
        Vector3f pos = getPosition();
        pos = Util.round100(pos);
        pos.x -= speed;
        if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb)) {
            float res = (float) (pos.y - Math.floor(pos.y));
            if (res >= 0.6f && ObjectManager.checkCollision(pos.x, pos.y - res + 1, isInBomb, inBomb)) {
                pos.y = pos.y - res + 1;
            } else if (res <= 0.4f && ObjectManager.checkCollision(pos.x, pos.y - res, isInBomb, inBomb)) {
                pos.y = pos.y - res;
            } else {
                pos.x += speed;
            }
        }
        pos = Util.round100(pos);
        setPosition(pos.x, pos.y, pos.z);
    }

    public void moveRight() {
        Vector3f pos = getPosition();
        pos = Util.round100(pos);
        pos.x += speed;
        if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb)) {
            float res = (float) (pos.y - Math.floor(pos.y));
            if (res >= 0.6f && ObjectManager.checkCollision(pos.x, pos.y - res + 1, isInBomb, inBomb)) {
                pos.y = pos.y - res + 1;
            } else if (res <= 0.4f && ObjectManager.checkCollision(pos.x, pos.y - res, isInBomb, inBomb)) {
                pos.y = pos.y - res;
            } else {
                pos.x -= speed;
            }
        }
        pos = Util.round100(pos);
        setPosition(pos.x, pos.y, pos.z);
    }

    public void moveUp() {
        Vector3f pos = getPosition();
        pos = Util.round100(pos);
        pos.y += speed;
        if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb)) {
            float res = (float) (pos.x - Math.floor(pos.x));
            if (res >= 0.6f && ObjectManager.checkCollision(pos.x - res + 1, pos.y, isInBomb, inBomb)) {
                pos.x = pos.x - res + 1;
            } else if (res <= 0.4f && ObjectManager.checkCollision(pos.x - res, pos.y, isInBomb, inBomb)) {
                pos.x = pos.x - res;
            } else {
                pos.y -= speed;
            }
        }
        pos = Util.round100(pos);
        setPosition(pos.x, pos.y, pos.z);
    }

    public void moveDown() {
        Vector3f pos = getPosition();
        pos = Util.round100(pos);
        pos.y -= speed;
        if (!ObjectManager.checkCollision(pos.x, pos.y, isInBomb, inBomb)) {
            float res = (float) (pos.x - Math.floor(pos.x));
            if (res >= 0.6f && ObjectManager.checkCollision(pos.x - res + 1, pos.y, isInBomb, inBomb)) {
                pos.x = pos.x - res + 1;
            } else if (res <= 0.4f && ObjectManager.checkCollision(pos.x - res, pos.y, isInBomb, inBomb)) {
                pos.x = pos.x - res;
            } else {
                pos.y += speed;
            }
        }
        pos = Util.round100(pos);
        setPosition(pos.x, pos.y, pos.z);
    }

    public boolean isFullBomb() {
        for (int i = 0; i < bombsOfMe.length; ++i) {
            if (bombsOfMe[i] == null || !bombsOfMe[i].isStart()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleEvent(Window window, Input input) throws Exception {

        if (isInBomb) {//case bomb behind deadpool
            if (!inBomb.isStart()) {
                isInBomb = false;
            }
            if (ObjectManager.checkCollision(getPosition().x, getPosition().y, inBomb.getPosition().x, inBomb.getPosition().y)) {
                isInBomb = false;
            }
        }

        if (!autoMode) {//if auto mode is OFF

            move(window, input);

        }

        else if (autoMode) {//if auto mode is ON

            timeSquare = (1.0f/60) * (1.0f/speed);
            this.chase();
            if (!chaseStat) {
                Util.round1(getPosition());
                nextPosition = null;
                if (search()) {
                    status = FIND;
                } else {
                    if (!isFullBomb()) findBrick();
                    else findSafePos();
                }
            }
        }
    }

    public void dead() {
        float x = (float) Math.round(getPosition().x * 100) / 100;
        float y = (float) Math.round(getPosition().y * 100) / 100;
        if (ObjectManager.checkIfDeadpoolDead(x, y)) {
            isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    // take bomb in hand when press Space
    public void takeBomb(Window window, Input input) {
        Vector3f bombPos = new Vector3f(this.getPosition());
        bombPos = Util.round1(bombPos);
        int bombX = (int)bombPos.x;
        int bombY = (int)bombPos.y;
        if(input.isKeyDown(GLFW_KEY_SPACE) && !ObjectManager.isStartOfBomb(bombX + 7, bombY + 7) && !autoMode) {
            if (!isFullBomb()) putABomb();
        }
    }

    // put a bomb to ground
    public void putABomb() {
        Vector3f bombPos = new Vector3f(this.getPosition());
        bombPos = Util.round1(bombPos);
        int bombX = (int)bombPos.x;
        int bombY = (int)bombPos.y;
        if (!ObjectManager.isStartOfBomb(bombX + 7, bombY + 7)) {
            ObjectManager.startABomb(bombX + 7, bombY + 7);

            ObjectManager.setUpBomb();

            isInBomb = true;
            inBomb = ObjectManager.getBomb(bombX + 7, bombY + 7);
            for (int i = 0; i < bombsOfMe.length; ++i) {
                if (bombsOfMe[i] == null || !bombsOfMe[i].isStart()) {
                    bombsOfMe[i] = inBomb;
                    return;
                }
            }
        }
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

    protected void setNextPosition(Node current) {
        nextPosition = null;
        while (current.father != null) {
            nextPosition = current.value;
            current = current.father;
        }

        if (nextPosition != null) {
            dem = 0;
            chaseStat = false;
            float x = getPosition().x;
            float x1 = nextPosition.x;
            float y = getPosition().y;
            float y1 = nextPosition.y;
            if (Math.round(x) > Math.round(x1)) {
                chaseDiection = "LEFT";
            } else if (Math.round(x) < Math.round(x1)) {
                chaseDiection = "RIGHT";
            } else if (Math.round(y) > Math.round(y1)) {
                chaseDiection = "DOWN";
            } else if (Math.round(y) < Math.round(y1)) {
                chaseDiection = "UP";
            } else {
                chaseDiection = "STAY";
            }
        }
    }

    protected void setNextPosition(Pair current) {
        nextPosition = null;
        while (current.father != null) {
            nextPosition = current.value;
            current = current.father;
        }

        if (nextPosition != null) {
            dem = 0;
            chaseStat = false;
            float x = getPosition().x;
            float x1 = nextPosition.x;
            float y = getPosition().y;
            float y1 = nextPosition.y;
            if (Math.round(x) > Math.round(x1)) {
                chaseDiection = "LEFT";
            } else if (Math.round(x) < Math.round(x1)) {
                chaseDiection = "RIGHT";
            } else if (Math.round(y) > Math.round(y1)) {
                chaseDiection = "DOWN";
            } else if (Math.round(y) < Math.round(y1)) {
                chaseDiection = "UP";
            } else {
                chaseDiection = "STAY";
            }
        }
    }

    // check if find a way to target
    @Override
    public boolean search() {
        boolean[][] checked = new boolean[16][16];
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }
        PriorityQueue<Node> queue = new PriorityQueue<>();
        float h = Util.distant(this.getPosition(), targetPosition);
        Vector3f currentPos = new Vector3f(this.getPosition());
        currentPos = Util.round1(currentPos);
        Node current = new Node(h, 0, h, currentPos, null);
        queue.add(current);

        while (!current.value.equals(targetPosition) && queue.size() > 0) {

            current = queue.poll();

            int numsOfSquare = 0;
            Node point = current;
            while (point.father != null) {
                point = point.father;
                numsOfSquare++;
            }
            float lastTimeToReach = timeSquare * numsOfSquare;

            int i = (int) current.value.x + 7;
            int j = (int) current.value.y + 7;
            checked[j][i] = true;

            if (!ObjectManager.IsWallOfGameObject(i + 1, j)
                && !ObjectManager.isDangerous(i + 1, j, i, j, timeSquare, lastTimeToReach)
                && !checked[j][i + 1]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.x += 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }

            if (!ObjectManager.IsWallOfGameObject(i - 1, j)
                && !ObjectManager.isDangerous(i - 1, j, i, j, timeSquare, lastTimeToReach)
                && !checked[j][i - 1]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.x -= 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }

            if (!ObjectManager.IsWallOfGameObject(i, j + 1)
                && !ObjectManager.isDangerous(i, j + 1, i, j, timeSquare, lastTimeToReach)
                && !checked[j + 1][i]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.y += 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }


            if (!ObjectManager.IsWallOfGameObject(i, j - 1)
                && !ObjectManager.isDangerous(i, j - 1, i, j, timeSquare, lastTimeToReach)
                && !checked[j - 1][i]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.y -= 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }
        }
        if (!current.value.equals(targetPosition)) {
            return false;
        }

        setNextPosition(current);

        return true;
    }

    @Override
    public void findSafePos() {
        status = GOTOSAFEPOS;
        int y = (int)(getPosition().y) + 7;
        int x = (int)(getPosition().x) + 7;
        Pair pair = ObjectManager.findSafePos(x, y, timeSquare);
        setNextPosition(pair);
    }

    //find brick when can not find target
    @Override
    public void findBrick() throws Exception {

        boolean[][] checked = new boolean[16][16];
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }
        PriorityQueue<Node> queue = new PriorityQueue<>();
        float h = Util.distant(this.getPosition(), targetPosition);
        Vector3f currentPos = new Vector3f(this.getPosition());
        currentPos = Util.round1(currentPos);
        Node current = new Node(h, 0, h, currentPos, null);
        queue.add(current);

        while (!current.value.equals(targetPosition) && queue.size() > 0) {

            current = queue.poll();
            
            int numsOfSquare = 0;
            Node point = current;
            while (point.father != null) {
                point = point.father;
                numsOfSquare++;
            }
            float lastTimeToReach = timeSquare * numsOfSquare;

            if (ObjectManager.isHaveBrick((int)current.value.x, (int)current.value.y, timeSquare, lastTimeToReach)) {
                setNextPosition(current);
                status = PUTABOMB;
                return;
            }

            int i = (int) current.value.x + 7;
            int j = (int) current.value.y + 7;
            checked[j][i] = true;

            if (!ObjectManager.IsWallOfGameObject(i + 1, j)
                && !ObjectManager.isDangerous(i + 1, j, i, j, timeSquare, lastTimeToReach)
                && !checked[j][i + 1]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.x += 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }

            if (!ObjectManager.IsWallOfGameObject(i - 1, j)
                && !ObjectManager.isDangerous(i - 1, j, i, j, timeSquare, lastTimeToReach)
                && !checked[j][i - 1]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.x -= 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }

            if (!ObjectManager.IsWallOfGameObject(i, j + 1)
                && !ObjectManager.isDangerous(i, j + 1, i, j, timeSquare, lastTimeToReach)
                && !checked[j + 1][i]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.y += 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }


            if (!ObjectManager.IsWallOfGameObject(i, j - 1)
                && !ObjectManager.isDangerous(i, j - 1, i, j, timeSquare, lastTimeToReach)
                && !checked[j - 1][i]) {

                 Vector3f newPos = new Vector3f(current.value);
                 newPos.y -= 1;
                 newPos = Util.round1(newPos);
                 float h1 = Util.distant(newPos, targetPosition);
                 float g1 = current.g + 1;
                 queue.add(new Node(h1 + g1, g1, h1, newPos, current));
            }
        }

        findSafePos();
    }

    @Override
    public void chase() {
        if (nextPosition != null) {

            Vector3f currentPosition = getPosition();
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
                dem = 0;
                chaseStat = false;
            }
        } else {
            if (status == PUTABOMB) {
                Vector3f currentPosition = getPosition();
                int x = Math.round(currentPosition.x);
                int y = Math.round(currentPosition.y);
                if (!ObjectManager.isStartOfBomb(x + 7, y + 7))
                { 
                    putABomb();
                    ObjectManager.setUpBomb();
                }
                status = GOTOSAFEPOS;
            } else {
                status = PUTABOMB;
            }
        }
    }
}