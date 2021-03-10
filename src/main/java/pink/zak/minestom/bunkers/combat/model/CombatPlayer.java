package pink.zak.minestom.bunkers.combat.model;

import net.minestom.server.entity.Player;

import java.util.UUID;

public class CombatPlayer {
    public final Player player;
    public float walkDist;
    public float previousWalkDist;

    public CombatPlayer(Player player) {
        this.player = player;
    }
}
