package main;


import engine.GameEngine;
import engine.IGameLogic;

public class Main {

  public static void main(String[] args) {
    try {
      boolean vSync = true;
      IGameLogic gameLogic = new BombermanGame();
      GameEngine gameEng = new GameEngine("GAME", 1280, 720, vSync, gameLogic);
      gameEng.run();
    } catch (Exception excp) {
      excp.printStackTrace();
      System.exit(-1);
    }
  }
}