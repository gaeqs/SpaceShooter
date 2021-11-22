package io.github.spaceshooter.engine.component.basic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import io.github.spaceshooter.R;
import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.TextUtils;
import io.github.spaceshooter.util.Validate;

public class Text extends BasicComponent implements GUIComponent {

    private Paint paint;
    private String text = null;
    private boolean centered;
    private Vector2f position = Vector2f.ZERO;

    public Text(GameObject gameObject) {
        super(gameObject);
        Typeface font = ResourcesCompat.getFont(getEngine().getActivity(),
                R.font.janitor);

        paint = new Paint();
        paint.setColor(0);
        paint.setAlpha (255);
        paint.setTextSize(0.05f);
        paint.setTypeface(font);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        Validate.notNull(paint, "Paint cannot be null!");
        gameObject.getLock().lock();
        this.paint = paint;
        gameObject.getLock().unlock();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        gameObject.getLock().lock();
        this.text = text;
        gameObject.getLock().unlock();
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        gameObject.getLock().lock();
        this.centered = centered;
        gameObject.getLock().unlock();
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        Validate.notNull(position, "Position cannot be null!");
        gameObject.getLock().lock();
        this.position = position;
        gameObject.getLock().unlock();
    }

    @Override
    public void draw(Canvas canvas, GameView view) {
        if (text == null) return;
        if (centered) {
            TextUtils.drawCenteredKernedText(canvas, text, position.x(), position.y(), paint);
        } else {
            TextUtils.drawKernedText(canvas, text, position.x(), position.y(), paint);
        }
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }
}
