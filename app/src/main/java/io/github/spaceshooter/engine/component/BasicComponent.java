package io.github.spaceshooter.engine.component;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.util.Validate;

/**
 * Basic implementation of a {@link Component}.
 * User-defined components should extend this class.
 */
public class BasicComponent implements Component {

    private static final AtomicInteger COMPONENT_ID_GENERATOR = new AtomicInteger(0);

    private final int id = COMPONENT_ID_GENERATOR.getAndIncrement();

    public final GameObject gameObject;

    private boolean destroyed;

    public BasicComponent(GameObject gameObject) {
        Validate.notNull(gameObject, "Game object cannot be null!");
        this.gameObject = gameObject;
    }

    @Override
    public final int getId() {
        return id;
    }

    @Override
    public final GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public final boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public final void markAsDestroyed() {
        destroyed = true;
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicComponent that = (BasicComponent) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
