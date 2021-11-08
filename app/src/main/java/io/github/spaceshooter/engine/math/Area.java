package io.github.spaceshooter.engine.math;

public class Area {

    private final Vector2f min, max;

    public Area(Vector2f a, Vector2f b) {
        this.min = a.min(b);
        this.max = a.max(b);
    }

    public Vector2f getMin() {
        return min;
    }

    public Vector2f getMax() {
        return max;
    }

    public boolean isInside(Vector2f p) {
        return p.x() >= min.x() && p.y() >= min.y() && p.x() <= max.x() && p.y() <= max.y();
    }

    public float getLeft () {
        return min.x();
    }

    public float getRight () {
        return max.x();
    }

    public float getBottom() {
        return min.y();
    }

    public float getTop () {
        return max.y();
    }
}
