package io.github.spaceshooter.engine.math;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;

import java.util.Objects;

public class Vector2f {

    public static final Vector2f ZERO = new Vector2f(0, 0);
    public static final Vector2f ONE = new Vector2f(1, 1);
    public static final Vector2f NEG_ONE = new Vector2f(-1, -1);

    private final float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public Vector2f withX(float x) {
        return new Vector2f(x, y);
    }

    public Vector2f withY(float y) {
        return new Vector2f(x, y);
    }

    public Vector2f add(Vector2f o) {
        return new Vector2f(x + o.x, y + o.y);
    }

    public Vector2f add(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

    public Vector2f sub(Vector2f o) {
        return new Vector2f(x - o.x, y - o.y);
    }

    public Vector2f sub(float x, float y) {
        return new Vector2f(this.x - x, this.y - y);
    }

    public Vector2f mul(Vector2f o) {
        return new Vector2f(x * o.x, y * o.y);
    }

    public Vector2f mul(float x, float y) {
        return new Vector2f(this.x * x, this.y * y);
    }

    public Vector2f mul(float v) {
        return new Vector2f(this.x * v, this.y * v);
    }

    public Vector2f div(Vector2f o) {
        return new Vector2f(x / o.x, y / o.y);
    }

    public Vector2f div(float x, float y) {
        return new Vector2f(this.x / x, this.y / y);
    }

    public Vector2f div(float v) {
        return new Vector2f(this.x / v, this.y / v);
    }

    public float dot(Vector2f o) {
        return x * o.x + y * o.y;
    }

    public float dot(float x, float y) {
        return this.x * x + this.y * y;
    }

    public float magnitude() {
        return (float) Math.hypot(x, y);
    }

    public float magnitudeSquared() {
        return x * x + y * y;
    }

    public float distance(Vector2f o) {
        return (float) Math.hypot(x - o.x, y - o.y);
    }

    public float distanceSquared(Vector2f o) {
        float dx = x - o.x;
        float dy = y - o.y;
        return dx * dx + dy * dy;
    }

    public Vector2f normalized() {
        return div(magnitude());
    }

    public Vector2f max(Vector2f max) {
        return new Vector2f(Math.max(x, max.x), Math.max(y, max.y));
    }

    public Vector2f min(Vector2f min) {
        return new Vector2f(Math.min(x, min.x), Math.min(y, min.y));
    }

    public Vector2f clamp(Vector2f min, Vector2f max) {
        return new Vector2f(
                MathUtils.clamp(x, min.x, max.x),
                MathUtils.clamp(y, min.y, max.y)
        );
    }


    public Vector2f neg() {
        return new Vector2f(-x, -y);
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
        Vector2f vector2f = (Vector2f) o;
        return Float.compare(vector2f.x, x) == 0 && Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
