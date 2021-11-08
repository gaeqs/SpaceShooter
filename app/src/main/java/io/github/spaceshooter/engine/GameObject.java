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

public class GameObject {

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

    public GameObject(Scene scene, String name) {
        Validate.notNull(scene, "Scene cannot be null!");
        Validate.notNull(name, "Name cannot be null!");
        this.scene = scene;
        this.transform = new Transform();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Scene getScene() {
        if (drawing) throw new IllegalStateException("Cannot access this method while drawing!");
        return scene;
    }

    public Transform getTransform() {
        return transform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validate.notNull(name, "Name cannot be null!");
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.stream().filter(type::isInstance).findFirst().orElse(null);
    }

    public <T extends Component> Collection<T> getAllComponents(Class<T> type) {
        return components.stream().filter(type::isInstance)
                .map(it -> (T) it).collect(Collectors.toSet());
    }

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

    public boolean destroyComponent(Component component) {
        if (drawing) throw new IllegalStateException("Cannot access this method while drawing!");
        if (!components.remove(component)) return false;
        if (component instanceof CollisionListenerComponent) {
            collisionListeners.remove(component);
        }
        scene.notifyComponentRemoval(component);
        try {
            component.onDestroy();
        } catch (Throwable ex) {
            System.err.println("ERROR DESTROYING COMPONENT " + component + "!");
            ex.printStackTrace();
        }
        return true;
    }

    public <T extends Component> T findComponent(Class<T> type) {
        return getScene().findComponent(type);
    }

    // region synchronization

    void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    ReentrantLock getLock() {
        return lock;
    }

    // endregion

    void notifyCollision(Collision collision) {
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
}
