package io.github.spaceshooter.space.player;

import io.github.spaceshooter.space.general.LivingComponent;

public interface DamageInflicter {

    int getInflictedDamage(LivingComponent livingComponent);

}
