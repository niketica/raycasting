package nl.aniketic.raycasting.engine;

import nl.aniketic.raycasting.display.DisplayManager;
import nl.aniketic.raycasting.input.KeyInput;

public class GameEngine {

    private DisplayManager displayManager;
    private GameComponent mainGameComponent;

    public GameEngine(GameComponent mainGameComponent) {
        this.mainGameComponent = mainGameComponent;

        displayManager = new DisplayManager("Raycasting Engine", mainGameComponent);
        displayManager.addKeyListener(new KeyInput());
        displayManager.createWindow();
    }

    public void input() {
        mainGameComponent.input();
    }

    public void update() {
        mainGameComponent.update();
    }

    public void render() {
        displayManager.render();
    }
}
