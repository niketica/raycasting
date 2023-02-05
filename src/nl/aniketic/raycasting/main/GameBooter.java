package nl.aniketic.raycasting.main;

import nl.aniketic.raycasting.engine.GameLoop;
import nl.aniketic.raycasting.engine.GameEngine;
import nl.aniketic.raycasting.game.MainGame;

public class GameBooter {

    public static void bootGame() {
        GameEngine gameEngine = new GameEngine(new MainGame());
        GameLoop gameLoop = new GameLoop(gameEngine);
        Thread thread = new Thread(gameLoop);
        thread.start();
    }

    private GameBooter() {
        // Hide constructor
    }
}
