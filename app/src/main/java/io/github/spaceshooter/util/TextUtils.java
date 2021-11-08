package io.github.spaceshooter.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class TextUtils {

    public static void drawKernedText(Canvas canvas, String text, float xOffset, float yOffset, Paint paint) {
        drawKernedText(canvas, text, xOffset, yOffset, paint, 0.01f);
    }

    public static void drawKernedText(Canvas canvas, String text, float xOffset, float yOffset,
                                Paint paint, float kernPercentage) {
        Rect textRect = new Rect();
        for (int i = 0; i < text.length(); i++) {
            if (canvas != null) {
                canvas.drawText(String.valueOf(text.charAt(i)), xOffset, yOffset, paint);
            }
            float charWidth = kernPercentage;
            if (text.charAt(i) != ' ') {
                paint.setTextSize(paint.getTextSize() * 1000);
                paint.getTextBounds(text, i, i + 1, textRect);
                paint.setTextSize(paint.getTextSize() / 1000);
                charWidth = textRect.width() / 1000.0f + kernPercentage;
            }
            xOffset += charWidth;
        }
    }

}
