package pink.zak.minestom.bunkers.scoreboard;

import com.google.common.collect.Maps;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.scoreboard.types.GameScoreboard;
import pink.zak.minestom.bunkers.scoreboard.types.PreGameScoreboard;
import pink.zak.minestom.bunkers.scoreboard.types.SpectatorScoreboard;

import java.util.Map;

public class ScoreboardManager {
    private final Map<BunkersScoreboard.Type, BunkersScoreboard> scoreboardMap = Maps.newEnumMap(BunkersScoreboard.Type.class);

    public ScoreboardManager(BunkersExtension extension) {
        this.scoreboardMap.put(BunkersScoreboard.Type.IN_GAME, new GameScoreboard());
        this.scoreboardMap.put(BunkersScoreboard.Type.PRE_GAME, new PreGameScoreboard(extension));
        this.scoreboardMap.put(BunkersScoreboard.Type.SPECTATOR, new SpectatorScoreboard(extension));
    }

    public void init() {
        this.scoreboardMap.values().forEach(BunkersScoreboard::init);

        // remove player from scoreboards on disconnect
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerDisconnectEvent.class, event -> {
            for (BunkersScoreboard scoreboard : this.scoreboardMap.values()) {
                if (scoreboard.removeViewer(event.getPlayer()))
                    break;
            }
        });
    }
}
