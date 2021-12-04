package main.game;

import main.engine.GameEngine;
import main.engine.GameLogic;
import main.engine.objects.BommanGame;

public class Main {
 
    public static void main(String[] args) {
        try {
            // Should be declared as a BommanGame object, not interface
            BommanGame gameLogic = new BommanGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}