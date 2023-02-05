package nl.aniketic.raycasting.engine;

public class GameLoop implements Runnable {

    public static final int FPS = 60;

    private GameEngine gameEngine;

    private boolean running;

    public GameLoop(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS; // 0.016666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        running = true;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {

                gameEngine.input();
                gameEngine.update();
                gameEngine.render();

                delta--;
                drawCount++;
            }

            if (timer >= 1_000_000_000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}
