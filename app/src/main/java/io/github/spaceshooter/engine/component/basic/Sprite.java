package io.github.spaceshooter.engine.component.basic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.GameView;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.DrawableComponent;
import io.github.spaceshooter.engine.math.Transform;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Sprite extends BasicComponent implements DrawableComponent {

    public Sprite(GameObject gameObject) {
        super(gameObject);
    }

    private final Matrix matrix = new Matrix();

    private Vector2f spriteScale = new Vector2f(0.1f, 0.1f);
    private Bitmap bitmap = null;

    @Override
    public void draw(Canvas canvas, GameView view) {
        if (bitmap == null) return;
        Transform t = gameObject.getTransform();
        matrix.reset();

        Vector2f nScale = t.getScale().mul(spriteScale);
        Vector2f fScale = nScale.div(bitmap.getWidth(), bitmap.getHeight());
        Vector2f screenPosition = t.getPosition();

        matrix.postScale(fScale.x(), fScale.y());
        matrix.postRotate(t.getRotation(), nScale.x() / 2, nScale.y() / 2);
        matrix.postTranslate(
                screenPosition.x() - nScale.x() / 2,
                screenPosition.y() - nScale.y() / 2
        );

        canvas.drawBitmap(bitmap, matrix, null);
    }

    @Override
    public int getDrawPriority() {
        return 0;
    }

    public Vector2f getSpriteScale() {
        return spriteScale;
    }

    public void setSpriteScale(Vector2f spriteScale) {
        Validate.notNull(spriteScale, "Sprite scale cannot be null!");
        this.spriteScale = spriteScale;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(int resId) {
        Resources r = getEngine().getActivity().getResources();
        Drawable drawable = ResourcesCompat.getDrawable(r, resId, null);
        this.bitmap = drawable instanceof BitmapDrawable
                ? ((BitmapDrawable) drawable).getBitmap()
                : null;
    }
}
