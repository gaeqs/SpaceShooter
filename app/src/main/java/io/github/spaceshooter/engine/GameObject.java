package io.github.spaceshooter.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.Component;
import io.github.spaceshooter.engine.math.Transform;
import io.github.spaceshooter.util.Validate;

/**
 * Represents an element inside a {@link Scene}.
 * This class cannot be extended!
 */
public final class GameObject {

    private static final AtomicInteger GAME_OBJECT_ID_GENERATOR = new AtomicInteger(0);

    private final int id = GAME_OBJECT_ID_GENERATOR.getAndIncrement();
    private final ReentrantLock lock = new ReentrantLock();

    private final Scene scene;
    private final Transform transform;
    private final Set<Component> components = new HashSet<>();
    private final Set<CollisionListenerComponent> collisionListeners = new HashSet<>();

    private String name;
    private boolean enabled = true;

    private boolean drawing = false;

    GameObject(Scene scene, String name) {
        Validate.notNull(scene, "Scene cannot be null!");
        Validate.notNull(name, "Name cannot be null!");
        this.scene = scene;
        this.transform = new Transform();
        this.name = name;
    }

    /**
     * Returns the unique identifier of this game object.
     *
     * @return the identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the scene this game object is inside.
     * <p>
     * This method cannot be invoked on draw methods.
     *
     * @return the scene.
     */
    public Scene getScene() {
        if (drawing) throw new IllegalStateException("Cannot access this method while drawing!");
        return scene;
    }

    /**
     * Returns the transform of this game object.
     *
     * @return the transform.
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Returns the name of this game object.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this game object.
     * The name cannot be null!
     *
     * @param name the name.
     */
    public void setName(String name) {
        Validate.notNull(name, "Name cannot be null!");
        this.name = name;
    }

    /**
     * Returns whether this game object is enabled.
     *
     * @return whether this game object is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables this game object.
     *
     * @param enabled whether this game object is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns a component inside this game object that matches the given type.
     *
     * @param type the type of the component.
     * @param <T>  the type of the component.
     * @return the component or null.
     */
    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.stream().filter(type::isInstance).findFirst().orElse(null);
    }

    /**
     * Returns all components inside this game object that match the given type.
     *
     * @param type the type of the component.
     * @param <T>  the type of the component.
     * @return an immutable collection with the components.
     */
    public <T extends Component> Collection<T> getAllComponents(Class<T> type) {
        return components.stream().filter(type::isInstance)
                .map(it -> (T) it).collect(Collectors.toSet());
    }

    /**
     * Adds a component of the given type to this game object.
     * <p>
     * This method cannot be invoked on draw methods.
     *
     * @param type the type of the component.
     * @param <T>  the type of the component.
     * @return the created component.
     */
    public <T extends Component> T addComponent(Class<T> type) {
        if (drawing) throw new IllegalStateException("Cannot access this method while drawing!");
        try {
            T component = type.getConstructor(GameObject.class).newInstance(this);
            components.add(component);
            scene.notifyComponentAdd(component);
            if (component instanceof CollisionListenerComponent) {
                collisionListeners.add((CollisionListenerComponent) component);
            }
            return component;
        } catch (IllegalAccessException | InstantiationException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Destroys the given component.
     * <p>
     * This method cannot be invoked on draw methods.
     *
     * @param component the component.
     * @return whether the operation was successful.
     */
    public boolean destroyComponent(Component component) {
        if (drawing) throw new IllegalStateException("Cannot access this method while drawing!");
        if (!components.remove(component)) return false;
        if (component instanceof CollisionListenerComponent) {
            collisionListeners.remove(component);
        }
        scene.notifyComponentRemoval(component);
        try {
            component.markAsDestroyed();
            component.onDestroy();
        } catch (Throwable ex) {
            System.err.println("ERROR DESTROYING COMPONENT " + component + "!");
            ex.printStackTrace();
        }
        return true;
    }

    /**
     * Searches a component that matches the given type in the whole scene.
     * <p>
     * This method cannot be invoked on draw methods.
     *
     * @param type the type of the component.
     * @param <T>  the type of the component.
     * @return the component or null.
     */
    public <T extends Component> T findComponent(Class<T> type) {
        return getScene().findComponent(type);
    }

    /**
     * Returns the lock used to synchronize this game object.
     * Use it with caution!
     *
     * @return the lock.
     */
    public ReentrantLock getLock() {
        return lock;
    }

    // region synchronization

    void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    // endregion

    public void notifyCollision(Collision collision) {
        Validate.notNull(collision, "Collision cannot be null!");
        collisionListeners.forEach(it -> {
            try {
                it.onCollision(collision);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    void destroyAllComponents() {
        components.forEach(it -> {
            scene.notifyComponentRemoval(it);
            try {
                it.markAsDestroyed();
                it.onDestroy();
            } catch (Throwable ex) {
                System.err.println("ERROR DESTROYING COMPONENT " + it + "!");
                ex.printStackTrace();
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", components=" + components +
                '}';
    }
}
