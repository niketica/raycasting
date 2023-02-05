package nl.aniketic.raycasting.input;

import java.awt.event.KeyEvent;

public enum GameKey {

    UP(KeyEvent.VK_W),
    DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A),
    RIGHT(KeyEvent.VK_D),
    SPACE(KeyEvent.VK_SPACE),
    USE(KeyEvent.VK_E),
    ARROW_LEFT(KeyEvent.VK_LEFT),
    ARROW_RIGHT(KeyEvent.VK_RIGHT),
    ESCAPE(KeyEvent.VK_ESCAPE);

    private final int keyCode;
    private boolean pressed;

    GameKey(int keyCode) {
        this.keyCode = keyCode;
    }

   public static GameKey getKey(int keyCode) {
        for (GameKey key : GameKey.values()) {
            if (key.keyCode == keyCode) {
                return key;
            }
        }
        return null;
   }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
