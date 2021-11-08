package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.collision.Collision;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.CollisionListenerComponent;
import io.github.spaceshooter.engine.component.DrawableComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.collision.SphereCollider;
import io.github.spaceshooter.engine.math.Vector2f;

public class CollisionDebugger extends BasicComponent
        implements DrawableComponent, TickableComponent, CollisionListenerComponent {

    private final SphereCollider collider;
    private final Paint paint = new Paint();

    private int tickDuration;
    private Collision collision;

    public CollisionDebugger(GameObject gameObject) {
        super(gameObject);
        collider = gameObject.getComponent(SphereCollider.class);
    }

    @Override
    public void tick(float deltaSeconds) {
        if (tickDuration > 0) {
            tickDuration--;
        } else {
            collision = null;
        }
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        Vector2f screenPosition = collider.getCenter();
        if (collision != null) {
            paint.setColor(0x550000FF);
        } else {
            paint.setColor(0x55FF0000);
        }
        canvas.drawCircle(screenPosition.x(), screenPosition.y(), collider.getRadius(), paint);
    }

    @Override
    public int getDrawPriority() {
        return 1;
    }

    @Override
    public void onCollision(Collision collision) {
        this.collision = collision;
        tickDuration = 10;
    }
}
