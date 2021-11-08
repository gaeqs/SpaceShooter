package io.github.spaceshooter.engine.collision;

import java.util.LinkedList;
import java.util.List;

import io.github.spaceshooter.engine.component.ColliderComponent;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Area;
import io.github.spaceshooter.engine.math.Vector2f;

public class QuadTree {

    private final List<SphereCollider> colliders = new LinkedList<>();
    private final QuadTree[] children = new QuadTree[4];
    private boolean parent = false;
    private Area area;
    private int level;

    public void setArea(Area area) {
        this.area = area;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void checkAndAdd(SphereCollider collider, QuadTreePool pool,
                            int maxColliders, int maxLevels) {
        if (intersects(collider, area)) {
            add(collider, pool, maxColliders, maxLevels);
        }
    }

    public void add(SphereCollider collider, QuadTreePool pool,
                    int maxColliders, int maxLevels) {
        if (parent) {
            for (QuadTree child : children) {
                child.checkAndAdd(collider, pool, maxColliders, maxLevels);
            }
        } else if (colliders.size() == maxColliders && level < maxLevels) {
            subdivide(pool, maxColliders, maxLevels);
            for (QuadTree child : children) {
                child.checkAndAdd(collider, pool, maxColliders, maxLevels);
            }
        } else {
            colliders.add(collider);
        }
    }

    public void checkCollisions() {
        if (parent) {
            for (QuadTree child : children) {
                child.checkCollisions();
            }
        } else {
            for (ColliderComponent it : colliders) {
                for (ColliderComponent o : colliders) {
                    if (it == o) continue;
                    it.getGameObject().getLock().lock();
                    o.getGameObject().getLock().lock();

                    Collision collision = it.testCollision(o);
                    if (collision != null) {
                        try {
                            it.getGameObject().notifyCollision(collision);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    it.getGameObject().getLock().unlock();
                    o.getGameObject().getLock().unlock();
                }
            }
        }
    }

    public void reset(QuadTreePool pool) {
        pool.add(this);
        if (parent) {
            for (int i = 0; i < children.length; i++) {
                children[i].reset(pool);
                children[i] = null;
            }
        }
        colliders.clear();
        parent = false;
    }

    private void subdivide(QuadTreePool pool, int maxColliders, int maxLevels) {
        for (int i = 0; i < children.length; i++) {
            children[i] = pool.get();
            children[i].setLevel(level + 1);
        }

        children[0].setArea(area.subdivide(false, false));
        children[1].setArea(area.subdivide(true, false));
        children[2].setArea(area.subdivide(false, true));
        children[3].setArea(area.subdivide(true, true));

        for (QuadTree child : children) {
            colliders.forEach(it -> child.checkAndAdd(it, pool, maxColliders, maxLevels));
        }
        parent = true;
        colliders.clear();
    }

    private static boolean intersects(SphereCollider collider, Area box) {
        Vector2f center = collider.getCenter();
        float dx = Math.max(box.getLeft(), Math.min(center.x(), box.getRight())) - center.x();
        float dy = Math.max(box.getBottom(), Math.min(center.y(), box.getTop())) - center.y();
        return dx * dx + dy * dy <= collider.getRadiusSquared();
    }

}
