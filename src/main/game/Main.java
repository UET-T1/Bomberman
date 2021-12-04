package main.game;

import main.engine.BommanGame;
import main.engine.GameEngine;
import main.engine.GameLogic;

public class Main {
 
    public static void main(String[] args) {
        try {
            GameLogic gameLogic = new BommanGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}