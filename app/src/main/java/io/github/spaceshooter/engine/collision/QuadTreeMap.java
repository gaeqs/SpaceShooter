package io.github.spaceshooter.engine.collision;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Area;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.engine.math.Vector2i;

public class QuadTreeMap {

    private final QuadTreePool pool = new QuadTreePool();
    private final Map<Vector2i, QuadTree> trees = new HashMap<>();
    private final int maxObjectsInTree;
    private final int maxLevels;


    public QuadTreeMap(int maxObjectsInTree, int maxLevels) {
        this.maxObjectsInTree = maxObjectsInTree;
        this.maxLevels = maxLevels;
    }

    public void calculate(Stream<SphereCollider> colliders) {
        reset();
        colliders.forEach(this::addToMap);
    }

    public void checkCollisions() {
        trees.values().forEach(QuadTree::checkCollisions);
    }

    private void reset() {
        trees.values().forEach(it -> it.reset(pool));
        trees.clear();
    }

    private void addToMap(SphereCollider collider) {
        Vector2f center = collider.getCenter();
        int minX = (int) Math.floor(center.x() - collider.getRadius());
        int minY = (int) Math.floor(center.y() - collider.getRadius());
        int maxX = (int) Math.floor(center.x() + collider.getRadius());
        int maxY = (int) Math.floor(center.y() + collider.getRadius());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Vector2i vec = new Vector2i(x, y);
                QuadTree tree = trees.computeIfAbsent(vec, v -> {
                    QuadTree got = pool.get();
                    got.setArea(new Area(v.x(), v.y(), v.x() + 1, v.y() + 1));
                    got.setLevel(0);
                    return got;
                });
                tree.add(collider, pool, maxObjectsInTree, maxLevels);
            }
        }
    }

}
