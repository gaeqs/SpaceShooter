package io.github.spaceshooter.engine;

import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.spaceshooter.engine.camera.Camera;
import io.github.spaceshooter.engine.collision.QuadTreeMap;
import io.github.spaceshooter.engine.component.ColliderComponent;
import io.github.spaceshooter.engine.component.Component;
import io.github.spaceshooter.engine.component.DrawableComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.component.GameStatusListenerComponent;
import io.github.spaceshooter.engine.component.InputListenerComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.input.InputDownEvent;
import io.github.spaceshooter.engine.input.InputEvent;
import io.github.spaceshooter.engine.input.InputMoveEvent;
import io.github.spaceshooter.engine.input.InputUpEvent;
import io.github.spaceshooter.engine.scheduler.Scheduler;

/**
 * Represents a scene inside a game engine.
 */
public class Scene {

    private final Queue<Component> componentsToAdd = new LinkedList<>();
    private final Queue<Component> componentsToRemove = new LinkedList<>();

    private final Set<TickableComponent> tickableComponents = new HashSet<>();
    private final Set<SphereCollider> colliderComponents = new HashSet<>();
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

    private final QuadTreeMap quadTreeMap = new QuadTreeMap(8, 3);
    private final Scheduler scheduler = new Scheduler();
    private final Random random = new Random();

    private final GameEngine engine;
    private final Camera camera;


    public Scene(GameEngine engine) {
        this.engine = engine;
        this.camera = new Camera(engine.getGameView());
    }

    /**
     * Returns the {@link GameEngine} of this scene.
     *
     * @return the {@link GameEngine}.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Returns the {@link Camera} of this scene.
     *
     * @return the {@link Camera}.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Returns the {@link Random} instance used by all components of this scene.
     *
     * @return the {@link Random}.
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Returns the amount of game objects this scene has.
     *
     * @return the amount of game object.
     */
    public int getGameObjectsAmount() {
        return gameObjects.size();
    }

    /**
     * Creates a new game object.
     * <p>
     * The new game object's name will be "New GameObject".
     *
     * @return the new game object.
     */
    public GameObject newGameObject() {
        return newGameObject("New GameObject");
    }

    /**
     * Creates a new game object with the given name.
     *
     * @param name the name of the new game object.
     * @return the new game object.
     */
    public GameObject newGameObject(String name) {
        GameObject object = new GameObject(this, name);
        gameObjects.add(object);
        return object;
    }

    /**
     * Destroys the given game object.
     *
     * @param gameObject the game object.
     * @return whether the operation was successful.
     */
    public boolean destroyGameObject(GameObject gameObject) {
        if (!gameObjects.remove(gameObject)) return false;
        gameObject.destroyAllComponents();
        return true;
    }

    /**
     * Finds a game object that matches the given name.
     *
     * @param name the name.
     * @return the game object or null.
     */
    public GameObject findGameObject(String name) {
        return gameObjects.stream().filter(it -> it.getName().equals(name))
                .findFirst().orElse(null);
    }

    /**
     * Searches a component that matches the given type in the whole scene.
     * <p>
     *
     * @param component the type of the component.
     * @param <T>       the type of the component.
     * @return the component or null.
     */
    public <T extends Component> T findComponent(Class<T> component) {

        // Fast discovery:

        if (TickableComponent.class.isAssignableFrom(component)) {
            return (T) tickableComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (TickableComponent) findComponentInComponentsToAdd(component));
        }
        if (DrawableComponent.class.isAssignableFrom(component)) {
            return (T) drawableComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (DrawableComponent) findComponentInComponentsToAdd(component));
        }
        if (GUIComponent.class.isAssignableFrom(component)) {
            return (T) guiComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (GUIComponent) findComponentInComponentsToAdd(component));
        }
        if (ColliderComponent.class.isAssignableFrom(component)) {
            return (T) colliderComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (SphereCollider) findComponentInComponentsToAdd(component));
        }
        if (InputListenerComponent.class.isAssignableFrom(component)) {
            return (T) inputListenerComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (InputListenerComponent) findComponentInComponentsToAdd(component));
        }
        if (GameStatusListenerComponent.class.isAssignableFrom(component)) {
            return (T) gameStatusListenerComponents.stream().filter(component::isInstance)
                    .findFirst()
                    .orElseGet(() -> (GameStatusListenerComponent) findComponentInComponentsToAdd(component));
        }

        // Slow discovery:

        for (GameObject o : gameObjects) {
            T value = o.getComponent(component);
            if (value != null) return value;
        }

        return null;
    }

    private <T extends Component> T findComponentInComponentsToAdd(Class<T> component) {
        return (T) componentsToAdd.stream()
                .filter(component::isInstance).findFirst().orElse(null);
    }

    /**
     * Runs the given lambda after the given seconds have passed.
     * <p>
     * The given code won't be executed if the given component is destroyed before
     * the given time.
     *
     * @param owner    the owner component.
     * @param seconds  the seconds.
     * @param runnable the code to execute.
     */
    public void runAfter(Component owner, float seconds, Runnable runnable) {
        scheduler.runAfter(owner, seconds, runnable);
    }

    void tick(float deltaSeconds) {
        scheduler.flush();
        scheduler.tick(deltaSeconds);

        flushComponentQueues();

        tickableComponents.forEach(it -> runSafeAndLocking(it, () -> it.tick(deltaSeconds)));

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

        drawableComponents.forEach(it -> runSafeAndLocking(it, () -> {
            it.getGameObject().setDrawing(true);
            it.draw(canvas, gv);
            it.getGameObject().setDrawing(false);
        }));

        guiMatrix.reset();
        guiMatrix.postScale(gv.getFinalHeight(), gv.getFinalHeight());
        canvas.setMatrix(guiMatrix);

        guiComponents.forEach(it -> runSafeAndLocking(it, () -> {
            it.getGameObject().setDrawing(true);
            it.draw(canvas, gv);
            it.getGameObject().setDrawing(false);
        }));
    }

    void attach() {
        gameStatusListenerComponents.forEach(it -> runSafeAndLocking(it, it::onSceneAttach));
    }

    void detach() {
        gameStatusListenerComponents.forEach(it -> runSafeAndLocking(it, it::onSceneDetach));
    }

    void pause() {
        gameStatusListenerComponents.forEach(it -> runSafeAndLocking(it, it::onPause));
    }

    void resume() {
        gameStatusListenerComponents.forEach(it -> runSafeAndLocking(it, it::onResume));
    }

    void inputEvent(InputEvent event) {
        if (event instanceof InputDownEvent) {
            inputListenerComponents.forEach(it -> runSafeAndLocking(it,
                    () -> it.onInputDown((InputDownEvent) event)));
        } else if (event instanceof InputMoveEvent) {
            inputListenerComponents.forEach(it -> runSafeAndLocking(it,
                    () -> it.onInputMove((InputMoveEvent) event)));
        } else if (event instanceof InputUpEvent) {
            inputListenerComponents.forEach(it -> runSafeAndLocking(it,
                    () -> it.onInputUp((InputUpEvent) event)));
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
        if (component instanceof SphereCollider)
            colliderComponents.add((SphereCollider) component);
        if (component instanceof InputListenerComponent)
            inputListenerComponents.add((InputListenerComponent) component);
        if (component instanceof GameStatusListenerComponent)
            gameStatusListenerComponents.add((GameStatusListenerComponent) component);
    }

    private void removeComponent(Component component) {
        if (component instanceof TickableComponent) tickableComponents.remove(component);
        if (component instanceof DrawableComponent) drawableComponents.remove(component);
        if (component instanceof GUIComponent) guiComponents.remove(component);
        if (component instanceof SphereCollider) colliderComponents.remove(component);
        if (component instanceof InputListenerComponent) inputListenerComponents.remove(component);
        if (component instanceof GameStatusListenerComponent)
            gameStatusListenerComponents.remove(component);
    }

    private void checkCollisions() {
        quadTreeMap.calculate(colliderComponents.stream()
                .filter(it -> it.getGameObject().isEnabled()));
        quadTreeMap.checkCollisions();
    }

    private static void runSafeAndLocking(Component c, Runnable runnable) {
        GameObject gameObject = c.getGameObject();
        if (!gameObject.isEnabled()) return;
        gameObject.getLock().lock();
        try {
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        gameObject.getLock().unlock();
    }

}
