package io.github.spaceshooter.engine.collision;

import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;

public class SphereToSphereCollision extends Collision {

    private final float otherRadius;
    private final float radiusSum;
    private final float distanceSquared;
    private final Vector2f thisCenter, otherCenter;

    private float distance;
    private boolean distanceCalculated;

    public SphereToSphereCollision(
            SphereCollider thisCollider,
            SphereCollider otherCollider,
            float otherRadius,
            float radiusSum,
            float distanceSquared,
            Vector2f thisCenter,
            Vector2f otherCenter) {
        super(thisCollider, otherCollider);
        this.otherRadius = otherRadius;
        this.radiusSum = radiusSum;
        this.distanceSquared = distanceSquared;
        this.thisCenter = thisCenter;
        this.otherCenter = otherCenter;
    }

    @Override
    public SphereCollider getOtherCollider() {
        return (SphereCollider) super.getOtherCollider();
    }

    @Override
    protected Vector2f calculateCollisionPoint() {
        return otherCenter.add(getOtherNormal().mul(otherRadius));
    }

    @Override
    protected Vector2f calculateOtherNormal() {
        return thisCenter.sub(otherCenter).div(getDistance());
    }

    @Override
    protected float calculatePenetration() {
        // distance = radius1 + radius2 - penetration
        // penetration = radius1 + radius2 - distance
        float distance = getDistance();
        return radiusSum - distance;
    }

    public float getDistance() {
        if (!distanceCalculated) {
            distance = calculateDistance();
            distanceCalculated = true;
        }

        return distance;
    }

    private float calculateDistance() {
        return (float) Math.sqrt(distanceSquared);
    }

}
