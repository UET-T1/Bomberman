package main.engine.objects;

import java.util.PriorityQueue;
import java.util.Stack;

import org.joml.Vector3f;

import main.engine.GameItem;
import main.engine.Input;
import main.engine.Window;
import main.engine.testGUI.Mesh;

import static org.lwjgl.glfw.GLFW.*;

public class Deadpool extends CapA {
    
    private boolean dk;

    private boolean autoMode;
    
    public Deadpool(Mesh mesh) {
        super(mesh);
        dk = false;
        autoMode = false;
    }

    @Override
    public void move(Window window, Input input,
                     GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb) {
        if (isInBomb) {
            if (!inBomb.isStart()) {
                isInBomb = false;
            }
            if (checkCollision(getPosition().x, getPosition().y, inBomb.getPosition().x, inBomb.getPosition().y)) {
                isInBomb = false;
            }
        }
        if (!autoMode) {
            // Not work??
            if (input.isKeyDown(GLFW_KEY_LEFT)) System.err.println(1);

            Vector3f pos = getPosition();
            pos = round100(pos);
            if (window.isKeyDown(GLFW_KEY_LEFT)) {
                pos.x -= speed;
                if (!checkCollision(pos.x, pos.y,
                        tileMap, tileBrokenWall, tileBomb)) {
                    float res = (float) (pos.y - Math.floor(pos.y));
                    if (res >= 0.6f && checkCollision(pos.x, pos.y - res + 1,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.y = pos.y - res + 1;
                    } else if (res <= 0.4f && checkCollision(pos.x, pos.y - res,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.y = pos.y - res;
                    } else {
                        pos.x += speed;
                    }
                }
            } else if (window.isKeyDown(GLFW_KEY_RIGHT)) {
                pos.x += speed;
                if (!checkCollision(pos.x, pos.y,
                        tileMap, tileBrokenWall, tileBomb)) {
                    float res = (float) (pos.y - Math.floor(pos.y));
                    if (res >= 0.6f && checkCollision(pos.x, pos.y - res + 1,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.y = pos.y - res + 1;
                    } else if (res <= 0.4f && checkCollision(pos.x, pos.y - res,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.y = pos.y - res;
                    } else {
                        pos.x -= speed;
                    }
                }
            } else if (window.isKeyDown(GLFW_KEY_UP)) {
                pos.y += speed;
                if (!checkCollision(pos.x, pos.y,
                        tileMap, tileBrokenWall, tileBomb)) {
                    float res = (float) (pos.x - Math.floor(pos.x));
                    if (res >= 0.6f && checkCollision(pos.x - res + 1, pos.y,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.x = pos.x - res + 1;
                    } else if (res <= 0.4f && checkCollision(pos.x - res, pos.y,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.x = pos.x - res;
                    } else {
                        pos.y -= speed;
                    }
                }
            } else if (window.isKeyDown(GLFW_KEY_DOWN)) {
                pos.y -= speed;
                if (!checkCollision(pos.x, pos.y,
                        tileMap, tileBrokenWall, tileBomb)) {
                    float res = (float) (pos.x - Math.floor(pos.x));
                    if (res >= 0.6f && checkCollision(pos.x - res + 1, pos.y,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.x = pos.x - res + 1;
                    } else if (res <= 0.4f && checkCollision(pos.x - res, pos.y,
                            tileMap, tileBrokenWall, tileBomb)) {
                        pos.x = pos.x - res;
                    } else {
                        pos.y += speed;
                    }
                }
            }

            pos = round100(pos);
            super.setPosition(pos.x, pos.y, pos.z);
        }

        else if (autoMode) {

            boolean contidion = true;
            for (Bomb[] bombs : tileBomb) {
                for (Bomb bomb : bombs) {
                    if (bomb.isStart()) {
                        contidion = false;
                    }
                }
            }
            if (contidion) {
                if (!chaseStat && stackPosition.size() == 0) {
                    round1(getPosition());
                    search(window, input, tileMap, tileBrokenWall, tileBomb);
                }
                chase();
                if(!chaseStat && stackPosition.size() == 0) {
                    putABomb(window, input, tileMap, tileBrokenWall, tileBomb);
                }
            }
        }
    }

    // take bomb in hand when press Space
    public void takeBomb(Window window, Input input,
                         GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb) {
        Vector3f bombPos = new Vector3f(this.getPosition());
        bombPos = GameItem.round1(bombPos);
        int bombX = (int)bombPos.x;
        int bombY = (int)bombPos.y;
        if(window.isKeyDown(GLFW_KEY_SPACE) && !tileBomb[bombY + 7][bombX + 7].isStart()) {
            dk = true;
        }
        if(dk && !window.isKeyDown(GLFW_KEY_SPACE)) {
            putABomb(window, input, tileMap, tileBrokenWall, tileBomb);
        }
    }

    // put a bomb to ground
    public void putABomb(Window window, Input input,
                                      GameItem[][] tileMap, StaticItem[][] tileBrokenWall, Bomb[][] tileBomb) {
        Vector3f bombPos = new Vector3f(this.getPosition());
        bombPos = GameItem.round1(bombPos);
        int bombX = (int)bombPos.x;
        int bombY = (int)bombPos.y;
        tileBomb[bombY + 7][bombX + 7].start();
        isInBomb = true;
        inBomb = tileBomb[bombY + 7][bombX + 7];
        dk = false;
    }

    // search the way to target position
    protected void search(Window window, Input input, GameItem[][] tileMap,
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

        if (!current.value.equals(targetPosition)) {
            breakWall(window, input, tileMap, tileBrokenWall, tileBomb);
        } else {
            stackPosition = new Stack<>();
            while (current != null) {
                stackPosition.add(current.value);
                current = current.father;
            }
            dem = (int)(1/speed);
        }
    }

    // break the broken wall when can not find target
    protected void breakWall(Window window, Input input, GameItem[][] tileMap,
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
            for (StaticItem[] brokenWalls : tileBrokenWall) {
                for (StaticItem brokenWall : brokenWalls) {
                    if (brokenWall.getVisible()) {
                        int x1 = (int) brokenWall.getPosition().x;
                        int x2 = (int) current.value.x;
                        int y1 = (int) brokenWall.getPosition().y;
                        int y2 = (int) current.value.y;
                        if (x1 == x2 && Math.abs(y1 - y2) == 1) {
                            stackPosition = new Stack<>();
                            while (current != null) {
                                stackPosition.add(current.value);
                                current = current.father;
                            }
                            dem = (int)(1f/speed);
                            return;
                        } else if (y1 == y2 && Math.abs(x1 - x2) == 1) {
                            stackPosition = new Stack<>();
                            while (current != null) {
                                stackPosition.add(current.value);
                                current = current.father;
                            }
                            dem = (int)(1f/speed);
                            return;
                        }
                    }
                }
            }

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
    }
}
