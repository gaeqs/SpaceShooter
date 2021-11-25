package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Circle extends BasicComponent implements GUIComponent {

    private GUIComponentArea area = new GUIComponentArea(
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.5f, 0.5f),
            true,
            false
    );


    private Paint paint = new Paint();

    public Circle(GameObject gameObject) {
        super(gameObject);
    }

    public GUIComponentArea getArea() {
        return area;
    }

    public void setArea(GUIComponentArea area) {
        Validate.notNull(area, "Area cannot be null!");
        this.area = area;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        Validate.notNull(paint, "Paint cannot be null!");
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        Vector2f o = area.getOffset();
        Vector2f s = area.getSize();
        Vector2f center = o.add(s.div(2));
        canvas.drawCircle(center.x(), center.y(), Math.min(s.x(), s.y()) / 2, paint);
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE - 1;
    }
}
