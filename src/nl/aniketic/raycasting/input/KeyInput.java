package nl.aniketic.raycasting.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        GameKey key = GameKey.getKey(e.getKeyCode());
        if (key != null) key.setPressed(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameKey key = GameKey.getKey(e.getKeyCode());
        if (key != null) key.setPressed(false);
    }
}
