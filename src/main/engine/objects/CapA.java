package main.engine.objects;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Mesh;

import java.util.PriorityQueue;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class CapA extends MovableItem {

    protected boolean stat; //status of CapA

    //Constructor
    public CapA(Mesh mesh) {
        super(mesh);
        stat = false;
    }

    @Override
    public void move(Window window, Input input,
                     GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb){
        if (window.isKeyDown(GLFW_KEY_A)) {
            stat = true;
        }
        if (stat) {
            if (!chaseStat && isTargetMoved) {
                isTargetMoved = false;
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
}
