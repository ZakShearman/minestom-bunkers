package pink.zak.minestom.bunkers.scoreboard.types;

import net.minestom.server.entity.Player;
import pink.zak.minestom.bunkers.scoreboard.BunkersScoreboard;

public class GameScoreboard implements BunkersScoreboard {
    @Override
    public void init() {

    }

    @Override
    public boolean removeViewer(Player player) {
        return false;
    }

    @Override
    public void addViewer(Player player) {

    }
}
