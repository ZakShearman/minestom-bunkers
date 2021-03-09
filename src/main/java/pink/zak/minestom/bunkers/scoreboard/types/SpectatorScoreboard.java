package pink.zak.minestom.bunkers.scoreboard.types;

import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.scoreboard.BunkersScoreboard;

public class SpectatorScoreboard implements BunkersScoreboard {
    private final BunkersExtension extension;
    private final Sidebar sidebar = new Sidebar("§");

    public SpectatorScoreboard(BunkersExtension extension) {
        this.extension = extension;
    }

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
