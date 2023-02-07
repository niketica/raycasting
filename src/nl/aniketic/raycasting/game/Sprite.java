package nl.aniketic.raycasting.game;

import java.awt.image.BufferedImage;

public class Sprite {

    private final int width;
    private final int height;
    private final int[] pixels;
    private final int alphaColor;

    public Sprite(String filePath, int alphaColor) {
        BufferedImage img = ImageUtil.loadImage(filePath);
        this.pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.alphaColor = alphaColor;
    }

    public Sprite(String filePath, int alphaColor, float scale) {
        BufferedImage img = ImageUtil.loadImage(filePath);
        img = ImageUtil.scaleImage(img, scale);
        this.pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.alphaColor = alphaColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[y * width + x];
    }

    public int getPixel(int index) {
        return pixels[index];
    }

    public int getPixel(float col, float row) {
        int x = (int) Math.floor(width * col);
        int y = (int) Math.floor(height * row);
        return pixels[y * width + x];
    }

    public int getAlphaColor() {
        return alphaColor;
    }
}
