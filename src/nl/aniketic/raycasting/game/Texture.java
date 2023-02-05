package nl.aniketic.raycasting.game;

public class Texture {

    private int width;
    private int height;
    private int[] pixels;

    public Texture(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void setPixel(int x, int y, int value) {
        this.pixels[y * width + x] = value;
    }

    public void setPixel(int index, int value) {
        this.pixels[index] = value;
    }
}
