package main.engine.objects;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Mesh;

import java.util.PriorityQueue;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class CapA extends MovableItem{

    protected Vector3f targetPosition = new Vector3f(8,8,0);

    protected Stack<Vector3f> stackPosition;

    protected boolean[][] checked;

    protected boolean stat;

    protected boolean targetStat;

    protected boolean chaseStat;

    protected String chaseDiection = "";

    protected int dem = 0;

    //Constructor
    public CapA(Mesh mesh) {
        super(mesh);
        stat = false;
        targetStat = false;
        chaseStat = false;
        checked = new boolean[16][16];
        stackPosition = new Stack<>();
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
        targetStat = true;
        this.targetPosition = new Vector3f(addPos);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void move(Window window, Input input,
                     GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb){
        if (window.isKeyDown(GLFW_KEY_A)) {
            stat = true;
        }
        if (stat) {
            if (!chaseStat && targetStat) {
                targetStat = false;
                search(tileMap, tileBrokenWall, tileBomb);
            }
            chase();
        }
    }

    //search way to go to target position
    protected void search(GameItem[][] tileMap,
                        StaticItem[][] tileBrokenWall, Bomb[][] tileBomb) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                checked[i][j] = false;
            }
        }
        PriorityQueue<Node> queue = new PriorityQueue<>();
        float h = distant(this.getPosition(), targetPosition);
        Vector3f currentPos = new Vector3f(this.getPosition());
        currentPos = round1(currentPos);
        Node current = new Node(h, 0, h, currentPos, null);
        queue.add(current);

        while (!current.value.equals(targetPosition) && queue.size() > 0) {
            current = queue.poll();
            int i = (int) current.value.x + 7;
            int j = (int) current.value.y + 7;
            checked[i][j] = true;
            if (checkCollision(current.value.x + 1, current.value.y,
                    tileMap, tileBrokenWall, tileBomb)) {
                if (!checked[i + 1][j]) {
                    Vector3f newPos = new Vector3f(current.value);
                    newPos.x += 1;
                    newPos = round1(newPos);
                    float h1 = distant(newPos, targetPosition);
                    float g1 = current.g + 1;
                    queue.add(new Node(h1 + g1, g1, h1, newPos, current));
                }
            }
            if (checkCollision(current.value.x - 1, current.value.y,
                    tileMap, tileBrokenWall, tileBomb)) {
                if (!checked[i - 1][j]) {
                    Vector3f newPos = new Vector3f(current.value);
                    newPos.x -= 1;
                    newPos = round1(newPos);
                    float h1 = distant(newPos, targetPosition);
                    float g1 = current.g + 1;
                    queue.add(new Node(h1 + g1, g1, h1, newPos, current));
                }
            }
            if (checkCollision(current.value.x, current.value.y + 1,
                    tileMap, tileBrokenWall, tileBomb)) {
                if (!checked[i][j + 1]) {
                    Vector3f newPos = new Vector3f(current.value);
                    newPos.y += 1;
                    newPos = round1(newPos);
                    float h1 = distant(newPos, targetPosition);
                    float g1 = current.g + 1;
                    queue.add(new Node(h1 + g1, g1, h1, newPos, current));
                }
            }
            if (checkCollision(current.value.x, current.value.y - 1,
                    tileMap, tileBrokenWall, tileBomb)) {
                if (!checked[i][j - 1]) {
                    Vector3f newPos = new Vector3f(current.value);
                    newPos.y -= 1;
                    newPos = round1(newPos);
                    float h1 = distant(newPos, targetPosition);
                    float g1 = current.g + 1;
                    queue.add(new Node(h1 + g1, g1, h1, newPos, current));
                }
            }
        }

        stackPosition = new Stack<>();
        while (current != null) {
            stackPosition.add(current.value);
            current = current.father;
        }
        dem = (int)(1/speed);
    }

    // calulate distant between s and t
    protected float distant(Vector3f s, Vector3f t) {
        float x = Math.abs(s.x - t.x);
        float y = Math.abs(s.y - t.y);
        float ans = x + y;
        return (float) Math.round(ans);
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
