package nl.aniketic.raycasting.game;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage loadImage(String filePath) {
        try {
//            return ImageIO.read(Objects.requireNonNull(ObjectRenderer.class.getResourceAsStream(path)));
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            throw new IllegalStateException("Could not load image: " + filePath);
        }
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    public static BufferedImage scaleImage(BufferedImage original, float scale) {
        int width = Math.round(original.getWidth() * scale);
        int height = Math.round(original.getHeight() * scale);

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    private ImageUtil() {
        // Hide constructor
    }
}
