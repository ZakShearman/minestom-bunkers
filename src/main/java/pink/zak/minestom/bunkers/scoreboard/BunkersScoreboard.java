package pink.zak.minestom.bunkers.scoreboard;

import net.minestom.server.entity.Player;

public interface BunkersScoreboard {

    void init();

    boolean removeViewer(Player player);

    void addViewer(Player player);

    enum Type {
        IN_GAME,
        PRE_GAME,
        SPECTATOR
    }
}
