package nl.aniketic.raycasting.game;

import nl.aniketic.raycasting.display.DisplayManager;
import nl.aniketic.raycasting.engine.GameComponent;
import nl.aniketic.raycasting.math.MathUtil;
import nl.aniketic.raycasting.math.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class MainGame implements GameComponent {

    public static final int CUBE_SIZE = 64;
    public static final float PLAYER_HEIGHT = 32.0f;
    public static final int PLAYER_SIZE = 8;
    public static final int FOV = 60; // Degrees
    public static final int PROJECTION_PLANE_WIDTH = DisplayManager.SCREEN_WIDTH;
    public static final int PROJECTION_PLANE_HEIGHT = DisplayManager.SCREEN_HEIGHT;
    public static final int DISTANCE_TO_PROJECTION_PLANE = (int) ((PROJECTION_PLANE_WIDTH / 2) / Math.tan(Math.toRadians(FOV) / 2.0f));
    public static final int NUMBER_OF_RAYS = PROJECTION_PLANE_WIDTH;
    public static final float ANGLE_BETWEEN_RAYS = (float) FOV / (float) NUMBER_OF_RAYS;
    public static final int MAX_DEPTH = 20;

    public static final int[][] MAP = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    private Player player;

    private int[] checkerPixels;
    private int[] checkerDebugPixels;
    private int[] wallPixels;
    private int[] stonePixels;
    private BufferedImage screenImage;
    private int[] screenPixels;

    public MainGame() {
        player = new Player(96.0f, 224.0f);
        BufferedImage checkerTile = ImageUtil.loadImage("./res/textures/checker_brown_64x64.png");
        checkerPixels = checkerTile.getRGB(0, 0, checkerTile.getWidth(), checkerTile.getHeight(), null, 0, 64);

        BufferedImage checkerDebugTile = ImageUtil.loadImage("./res/textures/checker_brown_debug_64x64.png");
        checkerDebugPixels = checkerDebugTile.getRGB(0, 0, checkerTile.getWidth(), checkerTile.getHeight(), null, 0, 64);

        BufferedImage wallTile = ImageUtil.loadImage("./res/textures/wall_64x64.png");
        wallPixels = wallTile.getRGB(0, 0, checkerTile.getWidth(), checkerTile.getHeight(), null, 0, 64);

//        BufferedImage stoneTile = ImageUtil.loadImage("./res/textures/stone_64x64.png");
//        stonePixels = stoneTile.getRGB(0, 0, checkerTile.getWidth(), checkerTile.getHeight(), null, 0, 64);

        screenImage = new BufferedImage(DisplayManager.SCREEN_WIDTH, DisplayManager.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        screenImage.setAccelerationPriority(0); // Just to be sure
        screenPixels = ((DataBufferInt) screenImage.getRaster().getDataBuffer()).getData();
    }

    @Override
    public void input() {
        player.input();
    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void render(Graphics2D g2) {
        // Clear screen
        Arrays.fill(screenPixels, 0);

        float castArc = player.angle;
        castArc -= FOV / 2.0f;
        if (castArc < 0.0f) {
            castArc += 360.0f;
        }

        for (int castColumn = 0; castColumn < NUMBER_OF_RAYS; castColumn++) {
            Vector2f horizontalWallCollision = getHorizontalWallCollision(castArc);
            Vector2f verticalWallCollision = getVerticalWallCollision(castArc);

            float distanceV = Vector2f.distance(player.position, verticalWallCollision);
            float distanceH = Vector2f.distance(player.position, horizontalWallCollision);

            Vector2f closestIntersection;
            float closestDistance;

            if (distanceV <= distanceH) {
                closestDistance = distanceV;
                closestIntersection = verticalWallCollision;
            } else {
                closestDistance = distanceH;
                closestIntersection = horizontalWallCollision;
            }

            float deltaAngle = player.angle - castArc;
            if (deltaAngle < 0) deltaAngle += 360.0f;
            closestDistance *= Math.cos(Math.toRadians(deltaAngle)); // Fix fisheye

            // DRAW THE WALL SLICE
            float dist;
            int xOffset;
            int topOfWall;   // used to compute the top and bottom of the sliver that
            int bottomOfWall;   // will be the staring point of floor and ceiling

            dist = closestDistance;
            if (distanceV <= distanceH) {
                xOffset = (int) closestIntersection.y % CUBE_SIZE;
            } else {
                xOffset = (int) closestIntersection.x % CUBE_SIZE;
            }

            float projectedWallHeight = (CUBE_SIZE * DISTANCE_TO_PROJECTION_PLANE / dist);
            bottomOfWall = (int) ((PROJECTION_PLANE_HEIGHT * 0.5f) + (projectedWallHeight * 0.5f));
            topOfWall = (int) ((PROJECTION_PLANE_HEIGHT * 0.5f) - (projectedWallHeight * 0.5f));

            float shade = MathUtil.map(closestDistance, 0, 400, 0.0f, 1.0f);
            drawWallSliceRectangle(castColumn, topOfWall, 1, ((bottomOfWall - topOfWall) + 1), shade, xOffset);
            drawFloor(castArc, castColumn, bottomOfWall);

            // Increase the arc for the next iteration
            castArc += ANGLE_BETWEEN_RAYS;
            if (castArc >= 360.0f) {
                castArc -= 360.0f;
            }
        }

        g2.drawImage(screenImage, 0, 0, DisplayManager.SCREEN_WIDTH, DisplayManager.SCREEN_HEIGHT, null);

        renderMiniMap(g2);
    }

    private void drawFloor(float castArc, int castColumn, int bottomOfWall) {
        float deltaAngle = player.angle - castArc;
        if (deltaAngle < 0) deltaAngle += 360.0f;

        float projectionPlaneCenterY=PROJECTION_PLANE_HEIGHT/2.0f;

        for (int row = bottomOfWall; row < PROJECTION_PLANE_HEIGHT; row++)
        {
            float deltaRow = row - projectionPlaneCenterY;
            float ratio = PLAYER_HEIGHT / deltaRow;
            float diagonalDistance = DISTANCE_TO_PROJECTION_PLANE * ratio;
            diagonalDistance *= 1.0f / Math.cos(Math.toRadians(deltaAngle)); // Fix fisheye
            diagonalDistance = (float) Math.floor(diagonalDistance); // Fix rounding error

            float yEnd = (float) (diagonalDistance * Math.sin(Math.toRadians(castArc)));
            float xEnd = (float) (diagonalDistance * Math.cos(Math.toRadians(castArc)));

            // Translate relative to viewer coordinates:
            xEnd += player.position.x;
            yEnd += player.position.y;

            xEnd = (float) Math.floor(xEnd); // Fix rounding error
            yEnd = (float) Math.floor(yEnd); // Fix rounding error

            // Get the tile intersected by ray:
            float cellX = xEnd / CUBE_SIZE;
            float cellY = xEnd / CUBE_SIZE;

            //Make sure the tile is within our map
            if ((cellX<MAP[0].length) &&
                    (cellY<MAP.length) &&
                    cellX>=0 && cellY>=0)
            {
                // Find offset of tile and column in texture
                int tileRow = (int) Math.floor(yEnd % CUBE_SIZE);
                int tileColumn = (int) Math.floor(xEnd % CUBE_SIZE);
                int textureIndex = tileRow * 64 + tileColumn;
                int rgb = checkerDebugPixels[textureIndex];

                float shade = MathUtil.map(diagonalDistance, 0, 320, 0.0f, 1.0f);
                Color shadedPixelColor = applyShade(new Color(rgb), shade);

                int screenPixelIndex = row * DisplayManager.SCREEN_WIDTH + castColumn;
                screenPixels[screenPixelIndex] = shadedPixelColor.getRGB();
            }
        }
    }

    private void drawWallSliceRectangle(int x, int y, int width, int height, float shade, int xOffset) {
        int sx = xOffset;
        int sy;
        for (int worldY = y; worldY < y + height; worldY++) {
            if (worldY < 0 || worldY >= DisplayManager.SCREEN_HEIGHT) continue;
            sy = (int) MathUtil.map(worldY, y, y + height, 0, 64);

            for (int worldX = x; worldX < x + width; worldX++) {
                if (worldX < 0 || worldX >= DisplayManager.SCREEN_WIDTH) continue;
                int rgb = wallPixels[sy * 64 + sx];
                Color c = applyShade(new Color(rgb), shade);
                screenPixels[worldY * DisplayManager.SCREEN_WIDTH + worldX] = c.getRGB();
            }
        }
    }

    private static Color applyShade(Color c, float shade) {
        float newR = c.getRed() * (1 - shade);
        float newG = c.getGreen() * (1 - shade);
        float newB = c.getBlue() * (1 - shade);
        return new Color(
                (int) Math.floor(newR),
                (int) Math.floor(newG),
                (int) Math.floor(newB)
        );
    }

    private Vector2f getHorizontalWallCollision(float castArc) {
        float castTan = (float) Math.tan(Math.toRadians(castArc + 0.0001f));
        boolean rayPointsUp = castArc > 180.0f;

        float yIntersection = rayPointsUp ?
                (float) (Math.floor(player.position.y / CUBE_SIZE) * CUBE_SIZE) :
                (float) (Math.floor(player.position.y / CUBE_SIZE) * CUBE_SIZE + CUBE_SIZE);
        float xIntersection = player.position.x + (yIntersection - player.position.y) / castTan;

        int xGrid = (int) Math.floor(xIntersection / CUBE_SIZE);
        int yGrid = (int) (rayPointsUp ? Math.floor(yIntersection / CUBE_SIZE) - 1 : Math.floor(yIntersection / CUBE_SIZE));

        if (isWithinBounds(xGrid, yGrid) && MAP[yGrid][xGrid] == 0) {
            float yAdjust = rayPointsUp ? -CUBE_SIZE : CUBE_SIZE;
            float xAdjust = yAdjust / castTan;

            for (int i = 0; i < MAX_DEPTH; i++) {
                yIntersection += yAdjust;
                xIntersection += xAdjust;

                xGrid = (int) Math.floor(xIntersection / CUBE_SIZE);
                yGrid = (int) (rayPointsUp ? Math.floor(yIntersection / CUBE_SIZE) - 1 : Math.floor(yIntersection / CUBE_SIZE));

                if (isWithinBounds(xGrid, yGrid) && MAP[yGrid][xGrid] > 0) {
                    break;
                }
            }
        }
        return new Vector2f(xIntersection, yIntersection);
    }

    private Vector2f getVerticalWallCollision(float castArc) {
        float castTan = (float) Math.tan(Math.toRadians(castArc + 0.0001f));
        boolean rayPointsRight = castArc < 90.0f || castArc >= 270.0f;

        float xIntersection = (float) (rayPointsRight ?
                (Math.floor(player.position.x / CUBE_SIZE) * CUBE_SIZE + CUBE_SIZE) :
                (Math.floor(player.position.x / CUBE_SIZE) * CUBE_SIZE));
        float yIntersection = player.position.y + (xIntersection - player.position.x) * castTan;

        int xGrid = (int) (rayPointsRight ? Math.floor(xIntersection / CUBE_SIZE) : Math.floor(xIntersection / CUBE_SIZE) - 1);
        int yGrid = (int) Math.floor(yIntersection / CUBE_SIZE);

        if (isWithinBounds(xGrid, yGrid) && MAP[yGrid][xGrid] == 0) {
            float xAdjust = rayPointsRight ? CUBE_SIZE : -CUBE_SIZE;
            float yAdjust = xAdjust * castTan;

            for (int i = 0; i < MAX_DEPTH; i++) {
                yIntersection += yAdjust;
                xIntersection += xAdjust;

                xGrid = (int) (rayPointsRight ? Math.floor(xIntersection / CUBE_SIZE) : Math.floor(xIntersection / CUBE_SIZE) - 1);
                yGrid = (int) Math.floor(yIntersection / CUBE_SIZE);

                if (isWithinBounds(xGrid, yGrid) && MAP[yGrid][xGrid] > 0) {
                    break;
                }
            }
        }

        return new Vector2f(xIntersection, yIntersection);
    }

    private static boolean isWithinBounds(int xGrid, int yGrid) {
        return !(xGrid < 0 || xGrid >= MAP[0].length || yGrid < 0 || yGrid >= MAP.length);
    }

    private void renderMiniMap(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        for (int y = 0; y < MAP.length; y++) {
            for (int x = 0; x < MAP[0].length; x++) {
                if (MAP[y][x] > 0) {
                    g2.drawRect(x * CUBE_SIZE, y * CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
                }
            }
        }
        player.render(g2);
    }
}
