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
    ESCAPE(KeyEvent.VK_ESCAPE),
    _0(KeyEvent.VK_0),
    _1(KeyEvent.VK_1),
    _2(KeyEvent.VK_2),
    _3(KeyEvent.VK_3),
    _4(KeyEvent.VK_4),
    _5(KeyEvent.VK_5),
    _6(KeyEvent.VK_6),
    _7(KeyEvent.VK_7),
    _8(KeyEvent.VK_8),
    _9(KeyEvent.VK_9),
    CTRL(KeyEvent.VK_CONTROL);

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
