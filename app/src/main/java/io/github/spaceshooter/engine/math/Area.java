package io.github.spaceshooter.engine.math;

public class Area {

    private final Vector2f min, max, size, size2;

    public Area(Vector2f a, Vector2f b) {
        this.min = a.min(b);
        this.max = a.max(b);
        size = max.sub(min);
        size2 = size.div(2);
    }

    public Area(float ax, float ay, float bx, float by) {
        this(new Vector2f(ax, ay), new Vector2f(bx, by));
    }

    public Vector2f getMin() {
        return min;
    }

    public Vector2f getMax() {
        return max;
    }

    public Vector2f getSize() {
        return size;
    }

    public boolean isInside(Vector2f p) {
        return p.x() >= min.x() && p.y() >= min.y() && p.x() <= max.x() && p.y() <= max.y();
    }

    public float getLeft() {
        return min.x();
    }

    public float getRight() {
        return max.x();
    }

    public float getBottom() {
        return min.y();
    }

    public float getTop() {
        return max.y();
    }

    public Area subdivide(boolean offsetX, boolean offsetY) {
        float minX = min.x();
        float minY = min.y();
        float maxX = min.x() + size2.x();
        float maxY = min.y() + size2.y();
        if (offsetX) {
            minX
                    += size2.x();
            maxX += size2.x();
        }
        if (offsetY) {
            minY += size2.y();
            maxY += size2.y();
        }
        return new Area(minX, minY, maxX, maxY);
    }

    @Override
    public String toString() {
        return "Area{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
