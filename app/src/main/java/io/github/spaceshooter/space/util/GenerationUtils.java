package io.github.spaceshooter.space.util;

import java.util.Random;

import io.github.spaceshooter.engine.math.Vector2f;
import io.github.spaceshooter.space.background.PlayArea;

public class GenerationUtils {

    public static Vector2f generateOrigin(Random random) {
        float x, y;
        if (random.nextBoolean()) {
            x = random.nextBoolean() ? PlayArea.DESPAWN_AREA.left : PlayArea.DESPAWN_AREA.right;
            float p = random.nextFloat();
            y = p * PlayArea.DESPAWN_AREA.top + (1 - p) * PlayArea.DESPAWN_AREA.bottom;
        } else {
            y = random.nextBoolean() ? PlayArea.DESPAWN_AREA.bottom : PlayArea.DESPAWN_AREA.top;
            float p = random.nextFloat();
            x = p * PlayArea.DESPAWN_AREA.left + (1 - p) * PlayArea.DESPAWN_AREA.right;
        }
        return new Vector2f(x, y);
    }
}
