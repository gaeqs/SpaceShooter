package io.github.spaceshooter.engine.component.collision;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.collision.SphereToSphereCollision;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.ColliderComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class SphereCollider extends BasicComponent implements ColliderComponent {

    private float radius = 0.1f;
    private float radiusSquared = radius * radius;
    private Vector2f offset = Vector2f.ZERO;

    public SphereCollider(GameObject gameObject) {
        super(gameObject);
    }

    public Vector2f getCenter() {
        return gameObject.getTransform().getPosition().add(offset);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        Validate.notNull(offset, "Offset cannot be null!");
        this.offset = offset;
    }

    @Override
    public boolean testPoint(Vector2f point) {
        return getCenter().distanceSquared(point) < radiusSquared;
    }

    @Override
    public Collision testCollision(ColliderComponent other) {
        if (other instanceof SphereCollider) {
            return sphere((SphereCollider) other);
        }
        return null;
    }

    private Collision sphere(SphereCollider collider) {
        float radiusSum = (radius + collider.radius);
        Vector2f thisCenter = getCenter();
        Vector2f otherCenter = collider.getCenter();

        float distanceSquared = thisCenter.distanceSquared(otherCenter);
        if (distanceSquared < radiusSum * radiusSum) {
            return new SphereToSphereCollision(
                    collider, collider.radius, radiusSum,
                    distanceSquared, thisCenter, otherCenter);
        }

        return null;
    }
}
