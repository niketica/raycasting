package nl.aniketic.raycasting.game;

import nl.aniketic.raycasting.display.RenderUtil;
import nl.aniketic.raycasting.engine.GameComponent;
import nl.aniketic.raycasting.input.GameKey;
import nl.aniketic.raycasting.math.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;

import static nl.aniketic.raycasting.game.MainGame.CUBE_SIZE;
import static nl.aniketic.raycasting.game.MainGame.PLAYER_SIZE;

public class Player implements GameComponent {

    public Vector2f position;
    public Vector2f deltaPosition;

    public float angle;
    public float speed = 1.4f;
    public float rot_speed = 1.4f;

    public Player(float x, float y) {
        this.position = new Vector2f(x, y);
        this.deltaPosition = new Vector2f(0.0f, 0.0f);
        this.angle = 0.0f;
        deltaPosition.x=(float)Math.cos(Math.toRadians(angle));
        deltaPosition.y=(float)Math.sin(Math.toRadians(angle));
    }

    @Override
    public void input() {
        rotatePlayer();
    }

    private void rotatePlayer() {

        // rotate left
        if (GameKey.ARROW_LEFT.isPressed())
        {
            angle -= rot_speed;
            if (angle<0.0f)
                angle+=360.0f;
        }
        // rotate right
        else if (GameKey.ARROW_RIGHT.isPressed())
        {
            angle += rot_speed;
            if (angle>=360.0f)
                angle-=360.0f;
        }

        deltaPosition.x=(float)Math.cos(Math.toRadians(angle));
        deltaPosition.y=(float)Math.sin(Math.toRadians(angle));

        // move forward
        if (GameKey.UP.isPressed())
        {
            float potentialX = position.x + deltaPosition.x * speed;
            float potentialY = position.y + deltaPosition.y * speed;
            float checkX = position.x + deltaPosition.x * PLAYER_SIZE * speed;
            float checkY = position.y + deltaPosition.y * PLAYER_SIZE * speed;

            if (!isWall((int)checkX, (int)position.y)) {
                position.x = potentialX;
            }
            if (!isWall((int)position.x, (int)checkY)) {
                position.y = potentialY;
            }
        }
        // move backward
        if (GameKey.DOWN.isPressed())
        {
            float potentialX = position.x - deltaPosition.x * speed;
            float potentialY = position.y - deltaPosition.y * speed;
            float checkX = position.x - deltaPosition.x * PLAYER_SIZE * speed;
            float checkY = position.y - deltaPosition.y * PLAYER_SIZE * speed;

            if (!isWall((int)checkX, (int)position.y)) {
                position.x = potentialX;
            }
            if (!isWall((int)position.x, (int)checkY)) {
                position.y = potentialY;
            }
        }

        if (GameKey.LEFT.isPressed())
        {
            float potentialX = position.x + deltaPosition.y * speed;
            float potentialY = position.y - deltaPosition.x * speed;
            float checkX = position.x + deltaPosition.y * PLAYER_SIZE * speed;
            float checkY = position.y - deltaPosition.x * PLAYER_SIZE * speed;

            if (!isWall((int)checkX, (int)position.y)) {
                position.x = potentialX;
            }
            if (!isWall((int)position.x, (int)checkY)) {
                position.y = potentialY;
            }
        }

        if (GameKey.RIGHT.isPressed())
        {
            float potentialX = position.x - deltaPosition.y * speed;
            float potentialY = position.y + deltaPosition.x * speed;
            float checkX = position.x - deltaPosition.y * PLAYER_SIZE * speed;
            float checkY = position.y + deltaPosition.x * PLAYER_SIZE * speed;

            if (!isWall((int)checkX, (int)position.y)) {
                position.x = potentialX;
            }
            if (!isWall((int)position.x, (int)checkY)) {
                position.y = potentialY;
            }
        }
    }

    public boolean isWall(int x, int y) {
        return MainGame.MAP[y/ CUBE_SIZE][x/ CUBE_SIZE] > 0;
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
