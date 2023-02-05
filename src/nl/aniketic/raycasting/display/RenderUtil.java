package nl.aniketic.raycasting.display;

import java.awt.Graphics2D;

public class RenderUtil {

    public static void drawCenteredCircle(Graphics2D g2, float x, float y, float r) {
        x = x - r;
        y = y - r;
        g2.fillOval((int) x, (int) y, (int) r * 2, (int) r * 2);
    }

    private RenderUtil() {
        // Hide constructor
    }
}
