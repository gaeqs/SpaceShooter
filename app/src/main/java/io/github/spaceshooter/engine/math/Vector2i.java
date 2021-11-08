package io.github.spaceshooter.engine.math;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;

import java.util.Objects;

public class Vector2i {

    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i ONE = new Vector2i(1, 1);
    public static final Vector2i NEG_ONE = new Vector2i(-1, -1);

    private final int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Vector2i withX(int x) {
        return new Vector2i(x, y);
    }

    public Vector2i withY(int y) {
        return new Vector2i(x, y);
    }

    public Vector2i add(Vector2i o) {
        return new Vector2i(x + o.x, y + o.y);
    }

    public Vector2i add(int x, int y) {
        return new Vector2i(this.x + x, this.y + y);
    }

    public Vector2i sub(Vector2i o) {
        return new Vector2i(x - o.x, y - o.y);
    }

    public Vector2i sub(int x, int y) {
        return new Vector2i(this.x - x, this.y - y);
    }

    public Vector2i mul(Vector2i o) {
        return new Vector2i(x * o.x, y * o.y);
    }

    public Vector2i mul(int x, int y) {
        return new Vector2i(this.x * x, this.y * y);
    }

    public Vector2i mul(int v) {
        return new Vector2i(this.x * v, this.y * v);
    }

    public Vector2i div(Vector2i o) {
        return new Vector2i(x / o.x, y / o.y);
    }

    public Vector2i div(int x, int y) {
        return new Vector2i(this.x / x, this.y / y);
    }

    public Vector2i div(int v) {
        return new Vector2i(this.x / v, this.y / v);
    }

    public int dot(Vector2i o) {
        return x * o.x + y * o.y;
    }

    public int dot(int x, int y) {
        return this.x * x + this.y * y;
    }

    public float magnitude() {
        return (int) Math.hypot(x, y);
    }

    public int magnitudeSquared() {
        return x * x + y * y;
    }

    public float distance(Vector2i o) {
        return (int) Math.hypot(x - o.x, y - o.y);
    }

    public float distanceSquared(Vector2i o) {
        int dx = x - o.x;
        int dy = y - o.y;
        return dx * dx + dy * dy;
    }

    public Vector2i max(Vector2i max) {
        return new Vector2i(Math.max(x, max.x), Math.max(y, max.y));
    }

    public Vector2i min(Vector2i min) {
        return new Vector2i(Math.min(x, min.x), Math.min(y, min.y));
    }

    public Vector2i clamp(Vector2i min, Vector2i max) {
        return new Vector2i(
                MathUtils.clamp(x, min.x, max.x),
                MathUtils.clamp(y, min.y, max.y)
        );
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2f = (Vector2i) o;
        return vector2f.x == x && vector2f.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
