package io.github.spaceshooter.engine.collision;

import java.util.LinkedList;
import java.util.Queue;

public class QuadTreePool {
    private final Queue<QuadTree> pool = new LinkedList<>();

    public QuadTree get() {
        if (pool.isEmpty()) {
            return new QuadTree();
        }
        return pool.poll();
    }

    public void add(QuadTree tree) {
        pool.add(tree);
    }

}