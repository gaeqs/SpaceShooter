package io.github.spaceshooter.space.player;

import io.github.spaceshooter.engine.GameObject;
import io.github.spaceshooter.engine.component.BasicComponent;
import io.github.spaceshooter.engine.component.TickableComponent;
import io.github.spaceshooter.engine.component.basic.Text;

public class ScoreDisplay extends BasicComponent implements TickableComponent {

    private final Text text;

    private PlayerStats stats;

    public ScoreDisplay(GameObject gameObject) {
        super(gameObject);
        text = gameObject.getComponent(Text.class);
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    @Override
    public void tick(float deltaSeconds) {
        int score = stats == null ? 0 : stats.score;
        text.setText(String.valueOf(score));
    }

}
