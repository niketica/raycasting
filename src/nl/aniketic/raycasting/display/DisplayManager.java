package nl.aniketic.raycasting.display;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class DisplayManager extends JPanel {

    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 1200;

    public static JFrame WINDOW;

    private String title;
    private RenderComponent renderComponent;

    public DisplayManager(String title, RenderComponent renderComponent) {
        this.title = title;
        this.renderComponent = renderComponent;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void createWindow() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle(title);
        window.add(this);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        WINDOW = window;

        hideMouseCursor();
    }

    public void hideMouseCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit()
                .createCustomCursor(cursorImg, new Point(0, 0), "invicible_cursor");
        WINDOW.getContentPane().setCursor(invisibleCursor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderComponent.render((Graphics2D) g);
        g.dispose();
    }

    public void render() {
        repaint();
    }

    public static JFrame getWindow() {
        return WINDOW;
    }
}
