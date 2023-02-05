package nl.aniketic.raycasting.math;

public class Vector2f {

    public float x;
    public float y;

    public Vector2f() {

    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f add(Vector2f v1, Vector2f v2) {
        Vector2f o = new Vector2f();
        o.x = v1.x + v2.x;
        o.y = v1.y + v2.y;
        return o;
    }

    public static Vector2f sub(Vector2f v1, Vector2f v2) {
        Vector2f o = new Vector2f();
        o.x = v1.x - v2.x;
        o.y = v1.y - v2.y;
        return o;
    }

    public static float distance(Vector2f v1, Vector2f v2) {
        return (float) Math.sqrt((v2.x - v1.x)*(v2.x - v1.x) + (v2.y - v1.y)*(v2.y - v1.y));
    }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
