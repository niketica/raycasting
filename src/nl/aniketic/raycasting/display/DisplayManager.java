package nl.aniketic.raycasting.display;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DisplayManager extends JPanel {

    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 900;

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
}
