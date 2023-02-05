package nl.aniketic.raycasting.engine;

import nl.aniketic.raycasting.display.RenderComponent;

public interface GameComponent extends RenderComponent {
    void input();
    void update();
}
