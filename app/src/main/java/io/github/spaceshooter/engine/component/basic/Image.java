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
import io.github.spaceshooter.engine.component.GUIComponent;
import io.github.spaceshooter.engine.gui.GUIComponentArea;
import io.github.spaceshooter.engine.math.Area;
import io.github.spaceshooter.engine.math.Transform;
import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.util.Validate;

public class Image extends BasicComponent implements GUIComponent {

    private final Matrix matrix = new Matrix();

    private GUIComponentArea area = new GUIComponentArea(
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.5f, 0.5f),
            true,
            false
    );
    private Bitmap bitmap = null;

    public Image(GameObject gameObject) {
        super(gameObject);
    }

    public GUIComponentArea getArea() {
        return area;
    }

    public void setArea(GUIComponentArea area) {
        Validate.notNull(area, "Area cannot be null!");
        this.area = area;
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

    @Override
    public void draw(Canvas canvas, GameView view) {
        if (bitmap == null) return;
        matrix.reset();

        Area a = area.getArea(view);

        Vector2f nScale = a.getSize();
        Vector2f fScale = nScale.div(bitmap.getWidth(), bitmap.getHeight());
        matrix.postScale(fScale.x(), fScale.y());
        matrix.postTranslate(a.getLeft(), a.getBottom());
        canvas.drawBitmap(bitmap, matrix, null);
    }

    @Override
    public int getDrawPriority() {
        return Integer.MAX_VALUE;
    }
}
