package pink.zak.minestom.bunkers.scoreboard.types;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.scoreboard.Sidebar;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.scoreboard.BunkersScoreboard;

import java.util.concurrent.TimeUnit;

public class PreGameScoreboard implements BunkersScoreboard {
    private final BunkersExtension extension;
    private final Sidebar sidebar = new Sidebar("\u00A76\u00A7lZak's Bunkers");

    public PreGameScoreboard(BunkersExtension extension) {
        this.extension = extension;
    }

    public void init() {
        this.startScoreboardUpdater();
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerSpawnEvent.class, event -> {
            if (this.extension.getRunningGame() == null || !this.extension.getRunningGame().isInProgress()) {
                this.sidebar.addViewer(event.getPlayer());
            }
        });
    }

    @Override
    public boolean removeViewer(Player player) {
        if (this.sidebar.getViewers().contains(player))
            return this.sidebar.removeViewer(player);
        return false;
    }

    @Override
    public void addViewer(Player player) {
        this.sidebar.addViewer(player);
    }

    private void startScoreboardUpdater() {
        this.sidebar.createLine(new Sidebar.ScoreboardLine("1", Component.text(" "), 1));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("online-players", Component.text("Online Players: " + MinecraftServer.getConnectionManager().getOnlinePlayers().size()), 0));
        MinecraftServer.getSchedulerManager().getTimerExecutionService().scheduleAtFixedRate(() -> {
            if (this.extension.getRunningGame() == null || !this.extension.getRunningGame().isInProgress()) {
                this.sidebar.updateLineContent("online-players", Component.text("Online Players: " + MinecraftServer.getConnectionManager().getOnlinePlayers().size()));
            }
        }, 0, 5, TimeUnit.SECONDS);
    }
}
