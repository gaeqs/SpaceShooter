package io.github.spaceshooter.engine;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.ColliderComponent;
import io.github.spaceshooter.engine.component.Component;
import io.github.spaceshooter.engine.component.DrawableComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.GameStatusListenerComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;

public class Scene {

    private final Queue<Component> componentsToAdd = new LinkedList<>();
    private final Queue<Component> componentsToRemove = new LinkedList<>();

    private final Set<TickableComponent> tickableComponents = new HashSet<>();
    private final Set<ColliderComponent> colliderComponents = new HashSet<>();
    private final Set<InputListenerComponent> inputListenerComponents = new HashSet<>();
    private final Set<GameStatusListenerComponent> gameStatusListenerComponents = new HashSet<>();

    private final Set<DrawableComponent> drawableComponents =
            new ConcurrentSkipListSet<>(DrawableComponent.COMPARATOR);
    private final Set<GUIComponent> guiComponents =
            new ConcurrentSkipListSet<>(GUIComponent.COMPARATOR);

    private final Set<GameObject> gameObjects = new HashSet<>();

    private final Lock viewLock = new ReentrantLock();
    private final Matrix viewMatrix = new Matrix();
    private int viewDirtyCount = -1;
    private final Matrix guiMatrix = new Matrix();

    private final GameEngine engine;
    private final Camera camera;

    public Scene(GameEngine engine) {
        this.engine = engine;
        this.camera = new Camera(engine.getGameView());
    }

    public GameEngine getEngine() {
        return engine;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameObject newGameObject() {
        return newGameObject("New GameObject");
    }

    public GameObject newGameObject(String name) {
        GameObject object = new GameObject(this, name);
        gameObjects.add(object);
        return object;
    }

    public boolean destroyGameObject(GameObject gameObject) {
        if (!gameObjects.remove(gameObject)) return false;
        gameObject.destroyAllComponents();
        return true;
    }

    public GameObject findGameObject(String name) {
        return gameObjects.stream().filter(it -> it.getName().equals(name))
                .findFirst().orElse(null);
    }

    public <T extends Component> T findComponent(Class<T> component) {

        // Fast discovery:

        if (TickableComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }
        if (DrawableComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }
        if (GUIComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }
        if (ColliderComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }
        if (InputListenerComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }
        if (GameStatusListenerComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst().orElse(null);
        }

        // Slow discovery:

        for (GameObject o : gameObjects) {
            T value = o.getComponent(component);
            if (value != null) return value;
        }

        return null;
    }

    void tick(float deltaSeconds) {
        flushComponentQueues();

        tickableComponents.forEach(it -> {
            if (!it.getGameObject().isEnabled()) return;
            Lock lock = it.getGameObject().getLock();
            lock.lock();
            runSafe(() -> it.tick(deltaSeconds));
            lock.unlock();
        });

        flushComponentQueues();

        checkCollisions();

        flushComponentQueues();

        viewLock.lock();
        camera.toViewMatrix(viewMatrix, viewDirtyCount);
        viewDirtyCount = camera.getDirtyCount();
        viewLock.unlock();
    }

    void draw(Canvas canvas) {
        GameView gv = engine.getGameView();

        viewLock.lock();
        canvas.setMatrix(viewMatrix);
        viewLock.unlock();

        drawableComponents.forEach(it -> {
            if (!it.getGameObject().isEnabled()) return;
            Lock lock = it.getGameObject().getLock();
            lock.lock();
            it.getGameObject().setDrawing(true);
            runSafe(() -> it.draw(canvas, gv));
            it.getGameObject().setDrawing(false);
            lock.unlock();
        });

        guiMatrix.reset();
        guiMatrix.postScale(gv.getFinalHeight(), gv.getFinalHeight());
        canvas.setMatrix(guiMatrix);

        guiComponents.forEach(it -> {
            if (!it.getGameObject().isEnabled()) return;
            Lock lock = it.getGameObject().getLock();
            lock.lock();
            it.getGameObject().setDrawing(true);
            runSafe(() -> it.draw(canvas, gv));
            it.getGameObject().setDrawing(false);
            lock.unlock();
        });
    }

    void attach() {
        gameStatusListenerComponents.forEach(GameStatusListenerComponent::onSceneAttach);
    }

    void detach() {
        gameStatusListenerComponents.forEach(GameStatusListenerComponent::onSceneDetach);
    }

    void pause() {
        gameStatusListenerComponents.forEach(GameStatusListenerComponent::onPause);
    }

    void resume() {
        gameStatusListenerComponents.forEach(GameStatusListenerComponent::onResume);
    }

    void inputEvent(InputEvent event) {
        if (event instanceof InputDownEvent) {
            inputListenerComponents.stream().filter(it -> it.getGameObject().isEnabled())
                    .forEach(it -> runSafe(() -> it.onInputDown((InputDownEvent) event)));
        } else if (event instanceof InputMoveEvent) {
            inputListenerComponents.stream().filter(it -> it.getGameObject().isEnabled())
                    .forEach(it -> runSafe(() -> it.onInputMove((InputMoveEvent) event)));
        } else if (event instanceof InputUpEvent) {
            inputListenerComponents.stream().filter(it -> it.getGameObject().isEnabled())
                    .forEach(it -> runSafe(() -> it.onInputUp((InputUpEvent) event)));
        }
    }

    void notifyComponentAdd(Component component) {
        componentsToAdd.add(component);
    }

    void notifyComponentRemoval(Component component) {
        componentsToRemove.add(component);
    }


    private void flushComponentQueues() {
        while (!componentsToRemove.isEmpty()) removeComponent(componentsToRemove.poll());
        while (!componentsToAdd.isEmpty()) addComponent(componentsToAdd.poll());
    }

    private void addComponent(Component component) {
        if (component instanceof TickableComponent)
            tickableComponents.add((TickableComponent) component);
        if (component instanceof DrawableComponent)
            drawableComponents.add((DrawableComponent) component);
        if (component instanceof GUIComponent)
            guiComponents.add((GUIComponent) component);
        if (component instanceof ColliderComponent)
            colliderComponents.add((ColliderComponent) component);
        if (component instanceof InputListenerComponent)
            inputListenerComponents.add((InputListenerComponent) component);
        if (component instanceof GameStatusListenerComponent)
            gameStatusListenerComponents.add((GameStatusListenerComponent) component);
    }

    private void removeComponent(Component component) {
        if (component instanceof TickableComponent) tickableComponents.remove(component);
        if (component instanceof DrawableComponent) drawableComponents.remove(component);
        if (component instanceof GUIComponent) guiComponents.remove(component);
        if (component instanceof ColliderComponent) colliderComponents.remove(component);
        if (component instanceof InputListenerComponent) inputListenerComponents.remove(component);
        if (component instanceof GameStatusListenerComponent)
            gameStatusListenerComponents.remove(component);
    }

    private void checkCollisions() {
        for (ColliderComponent it : colliderComponents) {
            if (it.getGameObject().isEnabled()) {
                colliderComponents.stream().filter(o -> o != it && o.getGameObject().isEnabled()).forEach(o -> {
                    Collision collision = it.testCollision(o);
                    if (collision != null) {
                        it.getGameObject().notifyCollision(collision);
                    }
                });
            }
        }
    }

    private static void runSafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
