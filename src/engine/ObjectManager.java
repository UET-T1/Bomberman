package engine;

import engine.sound.SoundBuffer;
import engine.sound.SoundListener;
import engine.sound.SoundManager;
import engine.sound.SoundSource;
import entities.Pencil;
import entities.Bomb;
import entities.Brick;
import entities.Flame;
import entities.Gate;
import entities.Grass;
import entities.Ink;
import entities.Item;
import entities.PenA;
import entities.Player;
import entities.Wall;
import entities.Witch;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.openal.AL11;

public class ObjectManager {

  public final static int BOMB = 1;
  public final static int SPEED = 2;
  public final static int POWER = 3;
  public static GameItem[][] tileMap;
  public static Brick[][] tileBrick;
  public static Bomb[][] tileBomb;
  public static Flame[][] tileFlame;
  public static Item[][] tileItem;
  public static Player player1;
  public static Pencil[] pencil;
  public static PenA[] penA;
  public static Gate gate;
  public static Ink[] ink;
  public static Witch[] witch;
  public static Player[] humanEnemy;
  public static int width;
  public static int height;
  public static float durationTimeBomb;
  public static float durationTimeFlame;
  public static float avgTimePerFrame = 1.0f / 60;
  public static float totalTime = 1.0f / 60;
  public static int totalDem = 1;
  public static SoundManager soundMgr;

  public static GameItem[][] getTileMap() {
    return tileMap;
  }

  public static Brick[][] getTileBrick() {
    return tileBrick;
  }

  public static Bomb[][] getTileBomb() {
    return tileBomb;
  }

  public static Flame[][] getTileFlame() {
    return tileFlame;
  }

  public static Item[][] getTileItem() {
    return tileItem;
  }
  public static Player getPlayer1() {
    return player1;
  }

  public static Pencil[] getPencil() {
    return pencil;
  }

  public static PenA[] getOneal() {
    return penA;
  }

  public static Gate getGate() {
    return gate;
  }

  public static Player[] getHumanEnemy() {
    return humanEnemy;
  }

  public static Ink[] getInk() {
    return ink;
  }

  public static Witch[] getWitch() {
    return witch;
  }
  
  public static void createMap(String path) throws Exception {
    String tileMapFile = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    JSONObject obj = new JSONObject(tileMapFile);

    width = obj.getInt("width");
    height = obj.getInt("height");
    tileMap = new GameItem[width + 1][height + 1];
    tileBrick = new Brick[width + 1][height + 1];
    tileFlame = new Flame[width + 1][height + 1];
    tileBomb = new Bomb[width + 1][height + 1];
    tileItem = new Item[width + 1][height + 1];
    penA = new PenA[0];
    pencil = new Pencil[0];
    humanEnemy = new Player[0];
    ink = new Ink[0];
    witch = new Witch[0];

    JSONArray array = obj.getJSONArray("map");
    for (int y = height; y >= 1; --y) {
      String s = array.getJSONObject(height - y).getString("0");
      for (int x = 1; x <= s.length(); ++x) {
        if (s.charAt(x - 1) == 'w' || s.charAt(x - 1) == 'b') {
          tileMap[x][y] = new Wall();
        } else {
          tileMap[x][y] = new Grass();
        }
        tileMap[x][y].setPosition(x, y, 0);
        //tileMap[x][y].setPosition(x, 0, -y);

        tileBomb[x][y] = new Bomb(2.0f);
        tileBomb[x][y].setPosition(x, y, 0);
        //tileBomb[x][y].setPosition(x, 0, -y);
        double func = Math.random() * 3;
        if (func <= 1.0f) {
          tileItem[x][y] = new Item(Item.bombItem);
        }
        if (func <= 2.0f && func > 1.0f) {
          tileItem[x][y] = new Item(Item.speedItem);
        }
        if (func <= 3.0f && func > 2.0f) {
          tileItem[x][y] = new Item(Item.powerItem);
        }
        tileItem[x][y].setPosition(x, y, 0);
        tileItem[x][y].setVisible(false);

        tileBrick[x][y] = new Brick();
        tileBrick[x][y].setVisible(false);
        if (s.charAt(x - 1) == 'c') {
          tileBrick[x][y].setVisible(true);
        }
        if (s.charAt(x - 1) == ' ') {
          if (Math.random() <= (float) (obj.getInt("wallGenPercent")) / 100) {
            tileBrick[x][y].setVisible(true);
          }
        }
        tileBrick[x][y].setPosition(x, y, 0);

        tileFlame[x][y] = new Flame(0.5f);
        tileFlame[x][y].setPosition(x, y, 0);
        tileFlame[x][y].setVisible(false);

        if (s.charAt(x - 1) == 'p') {
          player1 = new Player("Deadpool");
          player1.setPosition(x, y, 0);
          player1.setAutoMode(false);
        }

        if (s.charAt(x - 1) == '0' || s.charAt(x - 1) == '1') {
          Pencil[] newpencil = new Pencil[pencil.length + 1];
          for (int i = 0; i < pencil.length; ++i) {
            newpencil[i] = pencil[i];
          }
          pencil = newpencil;
          pencil[pencil.length - 1] = new Pencil();
          pencil[pencil.length - 1].setPosition(x, y, 0);
        }

        if (s.charAt(x - 1) == '2') {
          PenA[] newOneal = new PenA[penA.length + 1];
          for (int i = 0; i < penA.length; ++i) {
            newOneal[i] = penA[i];
          }
          penA = newOneal;
          penA[penA.length - 1] = new PenA();
          penA[penA.length - 1].setPosition(x, y, 0);
        }

        if (s.charAt(x - 1) == '3') {
          Ink[] newInk = new Ink[ink.length + 1];
          for (int i = 0; i < ink.length; ++i) {
            newInk[i] = ink[i];
          }
          ink = newInk;
          ink[ink.length - 1] = new Ink();
          ink[ink.length - 1].setPosition(x, y, 0);
        }

        if (s.charAt(x - 1) == '4' || s.charAt(x - 1) == '5') {
          Witch[] newWitch = new Witch[witch.length + 1];
          for (int i = 0; i < witch.length; ++i) {
            newWitch[i] = witch[i];
          }
          witch = newWitch;
          witch[witch.length - 1] = new Witch();
          witch[witch.length - 1].setPosition(x, y, 0);
        }

        if (s.charAt(x - 1) == '6') {
          Player[] newHumanEnemy = new Player[humanEnemy.length + 1];
          for (int i = 0; i < humanEnemy.length; ++i) {
            newHumanEnemy[i] = humanEnemy[i];
          }
          humanEnemy = newHumanEnemy;
          humanEnemy[humanEnemy.length - 1] = new Player("Enemy");
          humanEnemy[humanEnemy.length - 1].setPosition(x, y, 0);
        }

        if (s.charAt(x - 1) == 'e') {
          gate = new Gate();
          gate.setPosition(x, y, 0);
        }
      }
    }
    durationTimeBomb = 2.0f;
    durationTimeFlame = 0.5f;

    soundMgr = new SoundManager();
    soundMgr.init();
    soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
    setupSounds();
  }

  public static void setupSounds() throws Exception {

    SoundBuffer bombSound = new SoundBuffer("resources/sounds/bombSound.ogg");
    soundMgr.addSoundBuffer(bombSound);
    SoundSource sourceBeep = new SoundSource(false, true);
    sourceBeep.setBuffer(bombSound.getBufferId());
    soundMgr.addSoundSource("BOOM", sourceBeep);
    soundMgr.setListener(new SoundListener(new Vector3f()));
  }

  // Tile Map function
  public static boolean IsWallOfGameObject(int x, int y) {
    return (tileMap[x][y] instanceof Wall);
  }

  // Flame function
  public static void setTimeToBoomOfFlame(int x, int y, float timeToBoom) {
    tileFlame[x][y].setTimeToBoom(timeToBoom);
  }

  public static void setStartOfFlame(int x, int y) {
    tileFlame[x][y].start();
  }

  // Bomb function
  public static boolean isStartOfBomb(int x, int y) {
    return tileBomb[x][y].isStart();
  }

  public static float getTimeToBoomOfBomb(int x, int y) {
    return tileBomb[x][y].getTimeToBoom();
  }

  public static void startABomb(int x, int y, int power) {
    tileBomb[x][y].start(power);
    setUpBomb();
  }

  public static Bomb getBomb(int x, int y) {
    return tileBomb[x][y];
  }

  public static void linkBomb(int cx, int cy) {
    boolean[][] checked = new boolean[width + 1][height + 1];
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        checked[i][j] = false;
      }
    }

    float timeMax = (float) (tileBomb[cx][cy].getTime());

    Queue<Vector3f> queue = new LinkedList<>();
    queue.add(tileBomb[cx][cy].getPosition());

    timeMax = tileBomb[cx][cy].getTime();

    while (!queue.isEmpty()) {
      Vector3f currentBomb = queue.remove();
      Utils.round1(currentBomb);
      int x = (int) (currentBomb.x);
      int y = (int) (currentBomb.y);
      checked[x][y] = true;

      for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
        if (x + i <= width) {
          if (tileMap[x + i][y] instanceof Wall) {
            break;
          }
          if (tileBomb[x + i][y].isStart()
              && tileBomb[x + i][y].getTime() < timeMax) {
              if (!checked[x + i][y]) {
                  queue.add(tileBomb[x + i][y].getPosition());
              }
            break;
          }
        }
      }
      for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
        if (x - i >= 1) {
          if (tileMap[x - i][y] instanceof Wall) {
            break;
          }
          if (tileBomb[x - i][y].isStart()
              && tileBomb[x - i][y].getTime() < timeMax) {
              if (!checked[x - i][y]) {
                  queue.add(tileBomb[x - i][y].getPosition());
              }
            break;
          }
        }
      }
      for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
        if (y + i <= height) {
          if (tileMap[x][y + i] instanceof Wall) {
            break;
          }
          if (tileBomb[x][y + i].isStart()
              && tileBomb[x][y + i].getTime() < timeMax) {
              if (!checked[x][y + i]) {
                  queue.add(tileBomb[x][y + i].getPosition());
              }
            break;
          }
        }
      }
      for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
        if (y - i >= 1) {
          if (tileMap[x][y - i] instanceof Wall) {
            break;
          }
          if (tileBomb[x][y - i].isStart()
              && tileBomb[x][y - i].getTime() < timeMax) {
              if (!checked[x][y - i]) {
                  queue.add(tileBomb[x][y - i].getPosition());
              }
            break;
          }
        }
      }
    }
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        if (checked[i][j]) {
          tileBomb[i][j].setTimeToBoom(timeMax);
        }
      }
    }
  }

  public static void setTimeFlameToBoomFromBomb(int x, int y, float timeToBoom) {

    tileFlame[x][y].setTimeToBoom(timeToBoom);

    for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
      if (x + i <= width) {
        if (tileMap[x + i][y] instanceof Wall) {
          break;
        }
        if (tileBomb[x + i][y].isStart()
            && tileBomb[x + i][y].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
          break;
        }
        tileFlame[x + i][y].setTimeToBoom(timeToBoom);
      }
    }
    for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
      if (x - i >= 1) {
        if (tileMap[x - i][y] instanceof Wall) {
          break;
        }
        if (tileBomb[x - i][y].isStart()
            && tileBomb[x - i][y].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
          break;
        }
        tileFlame[x - i][y].setTimeToBoom(timeToBoom);
      }
    }
    for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
      if (y + i <= height) {
        if (tileMap[x][y + i] instanceof Wall) {
          break;
        }
        if (tileBomb[x][y + i].isStart()
            && tileBomb[x][y + i].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
          break;
        }
        tileFlame[x][y + i].setTimeToBoom(timeToBoom);
      }
    }
    for (int i = 1; i <= tileBomb[x][y].getPower(); ++i) {
      if (y - i >= 1) {
        if (tileMap[x][y - i] instanceof Wall) {
          break;
        }
        if (tileBomb[x][y - i].isStart()
            && tileBomb[x][y - i].getTimeToBoom() <= tileBomb[x][y].getTimeToBoom()) {
          break;
        }
        tileFlame[x][y - i].setTimeToBoom(timeToBoom);
      }
    }
  }

  public static void setUpBomb() {
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        if (tileBomb[i][j].isStart()) {
          linkBomb(i, j);
        }
      }
    }

    for (Flame[] flames : tileFlame) {
      for (Flame flame : flames) {
          if (flame != null) {
              flame.setNewTimeToBoom();
          }
      }
    }
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        if (tileBomb[i][j].isStart()) {
          setTimeFlameToBoomFromBomb(i, j, tileBomb[i][j].getTimeToBoom());
        }
      }
    }
  }

  public static boolean lanFromBomb(int x, int y) {
    if (tileMap[x][y] instanceof Wall) {
      return false;
    }
    if (tileBrick[x][y].getVisible()) {
      tileFlame[x][y].start();
      tileBrick[x][y].onCollapse();
      return false;
    }
    if (tileBomb[x][y].isShow()) {
      tileBomb[x][y].boom();
      return false;
    }
    tileFlame[x][y].start();
    return true;
  }

  public static void createItem(int x, int y) {
    if (Math.random() <= 0.3f) {
      tileItem[x][y].setVisible(true);
    }
  }

  public static void checkIfEatItem(Player player) {
    float x = (float) Math.round(player.getPosition().x * 100) / 100;
    float y = (float) Math.round(player.getPosition().y * 100) / 100;
      for (int i = 1; i <= width; ++i) {
          for (int j = 1; j <= height; ++j) {
              if (tileItem[i][j].isVisible()) {
                  Vector3f pos = tileMap[i][j].getPosition();
                  if (pos.x < x + 1 &&
                      x < pos.x + 1 &&
                      pos.y < y + 1 &&
                      y < pos.y + 1) {
                      if (tileItem[i][j].getFunction() == BOMB) {
                          player.increaseBomb();
                      }
                      if (tileItem[i][j].getFunction() == SPEED) {
                          player.increaseSpeed();
                      }
                      if (tileItem[i][j].getFunction() == POWER) {
                          player.increasePower();
                      }
                      tileItem[i][j].setVisible(false);
                  }
              }
          }
      }
  }

  public static boolean isPotential(int cx, int cy, int x, int y, float timeSquare,
      float lastTimeToReach) {

    float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
    if (tileMap[cx][cy] instanceof Wall) {
      return false;
    }
    if (tileBrick[cx][cy].getVisible()) {
      return false;
    }
    if (tileBomb[cx][cy].isStart()) {
      if (timeToReach <= durationTimeBomb - tileBomb[cx][cy].getTimeToBoom() + durationTimeFlame) {
        return false;
      }
    }

    // for (int i = 0; i < tileFlame[cx][cy].timeToBoom.length; ++i) {
    //     if (timeToReach - timeSquare <= durationTimeBomb - tileFlame[cx][cy].timeToBoom[i] + durationTimeFlame) {
    //         return false;
    //     }
    // }
    if (tileFlame[cx][cy].timeToBoom.length > 0) {
      return false;
    }

    return true;
  }

  public static boolean isDangerous(int cx, int cy, int x, int y, float timeSquare,
      float lastTimeToReach) {
    float timeToReach = (int) (Math.abs(x - cx) + Math.abs(y - cy)) * timeSquare + lastTimeToReach;
    if (tileMap[cx][cy] instanceof Wall) {
      return true;
    }
    if (tileBrick[cx][cy].getVisible()) {
      return true;
    }
    if (tileBomb[cx][cy].isStart()) {
      // System.out.println(tileBomb[y][x].getTimeToBoom());
      if (timeToReach - timeSquare
          <= durationTimeBomb - tileBomb[cx][cy].getTimeToBoom() + durationTimeFlame) {
        return true;
      }
    }
    for (int i = 0; i < tileFlame[cx][cy].timeToBoom.length; ++i) {
      if (timeToReach + timeSquare < durationTimeBomb - tileFlame[cx][cy].timeToBoom[i]
          || timeToReach - timeSquare
          > durationTimeBomb - tileFlame[cx][cy].timeToBoom[i] + durationTimeFlame) {
        return false;
      } else {
        return true;
      }
    }

    return false;
  }

  // check if Puting a bomb is save
  public static boolean testBomb(int x, int y, float timeSquare, float lastTimeToReach, int power)
      throws Exception {

    if (isDangerous(x, y, x, y, timeSquare, lastTimeToReach - timeSquare)) {
      return false;
    }

    Bomb oldBomb = new Bomb(tileBomb[x][y]);
    oldBomb.setPosition(x, y, 0.0f);

    tileBomb[x][y] = new Bomb(oldBomb.getMesh(), oldBomb.getDurationTime());
    tileBomb[x][y].setPosition(x, y, 0.0f);

    // tileBomb[x][y].start(power);

    boolean[][] checked = new boolean[width + 1][height + 1];
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        checked[i][j] = false;
      }
    }

    tileBomb[x][y].start(power);
    setUpBomb();
    Queue<Pair> queue = new LinkedList<>();
    queue.add(new Pair(x, y));

    while (!queue.isEmpty()) {
      Pair current = (Pair) queue.remove();
      checked[current.x][current.y] = true;

      if (!(tileMap[current.x + 1][current.y] instanceof Wall)
          && !isDangerous(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)
          && !checked[current.x + 1][current.y]) {

        if (isPotential(current.x + 1, current.y, x, y, timeSquare, lastTimeToReach)) {
          tileBomb[x][y] = oldBomb;
          setUpBomb();
          return true;
        }
        queue.add(new Pair(current.x + 1, current.y));
      }

      if (!(tileMap[current.x - 1][current.y] instanceof Wall)
          && !isDangerous(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)
          && !checked[current.x - 1][current.y]) {

        if (isPotential(current.x - 1, current.y, x, y, timeSquare, lastTimeToReach)) {
          tileBomb[x][y] = oldBomb;
          setUpBomb();
          return true;
        }
        queue.add(new Pair(current.x - 1, current.y));
      }

      if (!(tileMap[current.x][current.y + 1] instanceof Wall)
          && !isDangerous(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)
          && !checked[current.x][current.y + 1]) {

        if (isPotential(current.x, current.y + 1, x, y, timeSquare, lastTimeToReach)) {
          tileBomb[x][y] = oldBomb;
          setUpBomb();
          return true;
        }
        queue.add(new Pair(current.x, current.y + 1));
      }

      if (!(tileMap[current.x][current.y - 1] instanceof Wall)
          && !isDangerous(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)
          && !checked[current.x][current.y - 1]) {

        if (isPotential(current.x, current.y - 1, x, y, timeSquare, lastTimeToReach)) {
          tileBomb[x][y] = oldBomb;
          setUpBomb();
          return true;
        }
        queue.add(new Pair(current.x, current.y - 1));
      }
    }
    tileBomb[x][y] = oldBomb;
    setUpBomb();
    return false;
  }

  public static Pair findSafePos(int x, int y, float timeSquare) {

    boolean[][] checked = new boolean[width + 1][height + 1];
    for (int i = 1; i <= width; ++i) {
      for (int j = 1; j <= height; ++j) {
        checked[i][j] = false;
      }
    }

    Queue<Pair> queue = new LinkedList<>();

    Pair newPair = new Pair(x, y);
    queue.add(newPair);
    if (isPotential(x, y, x, y, timeSquare, 0.0f)) {
      return newPair;
    }

    while (!queue.isEmpty()) {

      Pair current = (Pair) queue.remove();
      checked[current.x][current.y] = true;

      if (!(tileMap[current.x + 1][current.y] instanceof Wall)
          && !isDangerous(current.x + 1, current.y, x, y, timeSquare, 0.0f)
          && !checked[current.x + 1][current.y]) {

        newPair = new Pair(current.x + 1, current.y, current);
        queue.add(newPair);
        if (isPotential(current.x + 1, current.y, x, y, timeSquare, 0.0f)) {
          return newPair;
        }
      }

      if (!(tileMap[current.x - 1][current.y] instanceof Wall)
          && !isDangerous(current.x - 1, current.y, x, y, timeSquare, 0.0f)
          && !checked[current.x - 1][current.y]) {

        newPair = new Pair(current.x - 1, current.y, current);
        queue.add(newPair);
        if (isPotential(current.x - 1, current.y, x, y, timeSquare, 0.0f)) {
          return newPair;
        }
      }

      if (!(tileMap[current.x][current.y + 1] instanceof Wall)
          && !isDangerous(current.x, current.y + 1, x, y, timeSquare, 0.0f)
          && !checked[current.x][current.y + 1]) {

        newPair = new Pair(current.x, current.y + 1, current);
        queue.add(newPair);
        if (isPotential(current.x, current.y + 1, x, y, timeSquare, 0.0f)) {
          return newPair;
        }
      }

      if (!(tileMap[current.x][current.y - 1] instanceof Wall)
          && !isDangerous(current.x, current.y - 1, x, y, timeSquare, 0.0f)
          && !checked[current.x][current.y - 1]) {

        newPair = new Pair(current.x, current.y - 1, current);
        queue.add(newPair);
        if (isPotential(current.x, current.y - 1, x, y, timeSquare, 0.0f)) {
          return newPair;
        }
      }
    }

    return newPair;
  }

  public static void createBrick(int x, int y) {
    tileBrick[x][y].setVisible(true);
  }

  public static boolean isHaveBrick(int x2, int y2, float timeSquare, float lastTimeToReach,
      int power) throws Exception {
    for (Brick[] bricks : tileBrick) {
      for (Brick brick : bricks) {
        if (brick != null && brick.getVisible()) {

          int x1 = (int) brick.getPosition().x;
          int y1 = (int) brick.getPosition().y;

          if (x1 == x2 && Math.abs(y1 - y2) == 1 && testBomb(x2, y2, timeSquare, lastTimeToReach,
              power)) {
            return true;
          } else if (y1 == y2 && Math.abs(x1 - x2) == 1 && testBomb(x2, y2, timeSquare,
              lastTimeToReach, power)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  // check collision between x, y and map
  public static boolean checkCollision(float x, float y, boolean isInBomb, Bomb inBomb,
      GameItem obj) {
    x = (float) Math.round(x * 100) / 100;
    y = (float) Math.round(y * 100) / 100;

      for (int i = 1; i <= width; ++i) {
          for (int j = 1; j <= height; ++j) {
              if (isInBomb && tileBomb[i][j].getPosition().equals(inBomb.getPosition())) {
                  continue;
              }
              if (tileBomb[i][j].isStart() || tileBrick[i][j].getVisible()
                  || tileMap[i][j] instanceof Wall) {
                  Vector3f pos = tileMap[i][j].getPosition();
                  if (pos.x < x + 1 &&
                      x < pos.x + 1 &&
                      pos.y < y + 1 &&
                      y < pos.y + 1) {
                      return false;
                  }
              }
          }
      }

    if (obj instanceof PenA || obj instanceof Pencil || obj instanceof Ink
        || obj instanceof Witch) {
      Vector3f pos = gate.getPosition();
      if (pos.x < x + 1 &&
          x < pos.x + 1 &&
          pos.y < y + 1 &&
          y < pos.y + 1) {
        return false;
      }
    }
    return true;
  }

  // check collision between x, y and targetx, targetY
  public static boolean checkCollision(float x, float y, float targetX, float targetY) {
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

  public static boolean checkIfPlayerDead(float x, float y) {
    for (Flame[] flames : tileFlame) {
      for (Flame flame : flames) {
        if (flame != null && flame.isStart()) {
          Vector3f pos = flame.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }
    for (GameItem[] tiles : tileMap) {
      for (GameItem tile : tiles) {
        if (tile != null && tile instanceof Wall) {
          Vector3f pos = tile.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }
    for (Brick[] bricks : tileBrick) {
      for (Brick brick : bricks) {
        if (brick != null && brick.getVisible()) {
          Vector3f pos = brick.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }

    for (Pencil pencill : pencil) {
      Vector3f pos = pencill.getPosition();
      if (!pencill.isDead() &&
          pos.x < x + 1 &&
          x < pos.x + 1 &&
          pos.y < y + 1 &&
          y < pos.y + 1) {
        return true;
      }
    }

    for (PenA one : penA) {
      Vector3f pos = one.getPosition();
      if (!one.isDead() &&
          pos.x < x + 1 &&
          x < pos.x + 1 &&
          pos.y < y + 1 &&
          y < pos.y + 1) {
        return true;
      }
    }

    for (Ink inkk : ink) {
      Vector3f pos = inkk.getPosition();
      if (!inkk.isDead() &&
          pos.x < x + 1 &&
          x < pos.x + 1 &&
          pos.y < y + 1 &&
          y < pos.y + 1) {
        return true;
      }
    }

    return false;
  }

  public static boolean checkIfEnemyDead(float x, float y) {
    for (Flame[] flames : tileFlame) {
      for (Flame flame : flames) {
        if (flame != null && flame.isStart()) {
          Vector3f pos = flame.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }
    for (GameItem[] tiles : tileMap) {
      for (GameItem tile : tiles) {
        if (tile != null && tile instanceof Wall) {
          Vector3f pos = tile.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }
    for (Brick[] bricks : tileBrick) {
      for (Brick brick : bricks) {
        if (brick != null && brick.getVisible()) {
          Vector3f pos = brick.getPosition();
          if (pos.x < x + 1 &&
              x < pos.x + 1 &&
              pos.y < y + 1 &&
              y < pos.y + 1) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static void lurePlayer(float x, float y) {
    for (Witch wit : witch) {
      Vector3f pos = wit.getPosition();
      if (!wit.isDead() &&
          pos.x < x + 1 &&
          x < pos.x + 1 &&
          pos.y < y + 1 &&
          y < pos.y + 1) {
        player1.lured();
        wit.onCollapse();
      }
    }
  }
}
