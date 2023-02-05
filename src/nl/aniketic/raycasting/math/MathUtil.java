package nl.aniketic.raycasting.math;

public class MathUtil {

    public static float map(float value, float min1, float max1, float min2, float max2) {
        float newVal = ((value - min1) / (max1 - min1)) * (max2 - min2) + min2;

        if (min2 < max2) {
            return constrain(newVal, min2, max2);
        } else {
            return constrain(newVal, max2, min2);
        }
    }

    public static float constrain(float n, float low, float high) {
        return Math.max(Math.min(n, high), low);
    }

    private MathUtil() {
        // Hide constructor
    }
}
