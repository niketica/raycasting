package nl.aniketic.raycasting.game;

import nl.aniketic.raycasting.display.DisplayManager;
import nl.aniketic.raycasting.display.RenderUtil;
import nl.aniketic.raycasting.engine.GameComponent;
import nl.aniketic.raycasting.input.GameKey;
import nl.aniketic.raycasting.math.Vector2f;

import javax.swing.JFrame;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import static nl.aniketic.raycasting.game.MainGame.CUBE_SIZE;
import static nl.aniketic.raycasting.game.MainGame.PLAYER_SIZE;

public class Player implements GameComponent {

    public Vector2f position;
    public Vector2f deltaPosition;

    public float angle;
    public float speed = 3.0f;
    public float rot_speed = 2.8f;
    public float verticalLookSpeed = 14.0f;
    public float yPerspectiveOffset;
    public float standHeight = 32.0f;
    public float currentHeight = standHeight;
    public float crouchHeight = 16.0f;
    public float crouchSpeed = 0.4f;

    private int prevMouseX;
    private int prevMouseY;
    private Robot robot;

    public Player(float x, float y) {
        this.position = new Vector2f(x, y);
        this.deltaPosition = new Vector2f(0.0f, 0.0f);
        this.angle = 0.0f;
        this.deltaPosition.x = (float) Math.cos(Math.toRadians(angle));
        this.deltaPosition.y = (float) Math.sin(Math.toRadians(angle));
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void input() {
        rotatePlayer();
        movePlayer();

        if (GameKey.CTRL.isPressed() && currentHeight > crouchHeight) {
            currentHeight -= crouchSpeed;
            if (currentHeight < crouchHeight) currentHeight = crouchHeight;
        }

        if (currentHeight < standHeight && !GameKey.CTRL.isPressed()) {
            currentHeight += crouchSpeed;
            if (currentHeight > standHeight) currentHeight = standHeight;
        }
    }

    private void rotatePlayer() {
        Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
        int currentMouseX = currentMouseLocation.x;
        if (currentMouseX != prevMouseX) {
            if (currentMouseX < prevMouseX) {
                angle -= rot_speed;
                if (angle < 0.0f)
                    angle += 360.0f;
            } else {
                angle += rot_speed;
                if (angle >= 360.0f)
                    angle -= 360.0f;
            }
        }

        int currentMouseY = currentMouseLocation.y;
        if (currentMouseY != prevMouseY) {
            if (currentMouseY < prevMouseY) {
                yPerspectiveOffset += verticalLookSpeed;
            } else {
                yPerspectiveOffset -= verticalLookSpeed;
            }
        }


        // Reset mouse position
        JFrame window = DisplayManager.getWindow();
        if (window == null) return;

        Point locationOnScreen = window.getLocationOnScreen();
        int mouseX = (int) (locationOnScreen.getX() + DisplayManager.SCREEN_WIDTH / 2);
        int mouseY = (int) (locationOnScreen.getY() + DisplayManager.SCREEN_HEIGHT / 2);
        robot.mouseMove(mouseX, mouseY);
        currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
        prevMouseX = (int) currentMouseLocation.getX();
        prevMouseY = (int) currentMouseLocation.getY();
    }

    private void movePlayer() {
        deltaPosition.x = (float) Math.cos(Math.toRadians(angle));
        deltaPosition.y = (float) Math.sin(Math.toRadians(angle));

        Vector2f newPos = new Vector2f(position.x, position.y);

        if (GameKey.UP.isPressed()) {
            float potentialX = newPos.x + deltaPosition.x * speed;
            float potentialY = newPos.y + deltaPosition.y * speed;
            float checkX = newPos.x + deltaPosition.x * PLAYER_SIZE * speed;
            float checkY = newPos.y + deltaPosition.y * PLAYER_SIZE * speed;

            if (!isWall((int) checkX, (int) newPos.y)) {
                newPos.x = potentialX;
            }
            if (!isWall((int) newPos.x, (int) checkY)) {
                newPos.y = potentialY;
            }
        }
        if (GameKey.DOWN.isPressed()) {
            float potentialX = newPos.x - deltaPosition.x * speed;
            float potentialY = newPos.y - deltaPosition.y * speed;
            float checkX = newPos.x - deltaPosition.x * PLAYER_SIZE * speed;
            float checkY = newPos.y - deltaPosition.y * PLAYER_SIZE * speed;

            if (!isWall((int) checkX, (int) newPos.y)) {
                newPos.x = potentialX;
            }
            if (!isWall((int) newPos.x, (int) checkY)) {
                newPos.y = potentialY;
            }
        }

        if (GameKey.LEFT.isPressed()) {
            float potentialX = newPos.x + deltaPosition.y * speed;
            float potentialY = newPos.y - deltaPosition.x * speed;
            float checkX = newPos.x + deltaPosition.y * PLAYER_SIZE * speed;
            float checkY = newPos.y - deltaPosition.x * PLAYER_SIZE * speed;

            if (!isWall((int) checkX, (int) newPos.y)) {
                newPos.x = potentialX;
            }
            if (!isWall((int) newPos.x, (int) checkY)) {
                newPos.y = potentialY;
            }
        }

        if (GameKey.RIGHT.isPressed()) {
            float potentialX = newPos.x - deltaPosition.y * speed;
            float potentialY = newPos.y + deltaPosition.x * speed;
            float checkX = newPos.x - deltaPosition.y * PLAYER_SIZE * speed;
            float checkY = newPos.y + deltaPosition.x * PLAYER_SIZE * speed;

            if (!isWall((int) checkX, (int) newPos.y)) {
                newPos.x = potentialX;
            }
            if (!isWall((int) newPos.x, (int) checkY)) {
                newPos.y = potentialY;
            }
        }

        Vector2f delta = Vector2f.sub(newPos, position);
        if (Vector2f.length(delta) > 0.0f) {
            delta = Vector2f.normalize(delta);
            delta = Vector2f.mul(delta, speed);
            position = Vector2f.add(position, delta);
        }
    }

    public boolean isWall(int x, int y) {
        return MainGame.MAP[y / CUBE_SIZE][x / CUBE_SIZE] > 0;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.ORANGE);

//        int x1 = (int)(position.x);
//        int y1 = (int)(position.y);
//        int x2 = (int)(position.x + CUBE_SIZE * Math.cos(Math.toRadians(angle)));
//        int y2 = (int)(position.y + CUBE_SIZE * Math.sin(Math.toRadians(angle)));
//        g2.drawLine(x1, y1, x2, y2);
        RenderUtil.drawCenteredCircle(g2, position.x, position.y, 15);
    }
}
