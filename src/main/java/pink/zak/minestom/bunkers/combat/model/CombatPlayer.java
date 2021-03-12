package pink.zak.minestom.bunkers.combat.model;

import net.minestom.server.entity.Player;
import net.minestom.server.utils.Position;

public class CombatPlayer {
    public final Player player;
    public float walkDist;
    public float previousWalkDist;
    public Position oldPosition;
    public Position newPosition;

    public CombatPlayer(Player player) {
        this.player = player;
    }

    public boolean isFalling() {
        if (this.oldPosition == null || this.newPosition == null)
            return false;
        return this.oldPosition.getY() > this.newPosition.getY();
    }
}
