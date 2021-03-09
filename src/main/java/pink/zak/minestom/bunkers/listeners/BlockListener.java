package pink.zak.minestom.bunkers.listeners;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.CancellableEvent;
import net.minestom.server.event.Event;
import net.minestom.server.event.PlayerEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.game.RunningGame;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.models.Faction;

import java.util.Optional;

public class BlockListener {
    private final BunkersExtension extension;

    public BlockListener(BunkersExtension extension) {
        this.extension = extension;
    }

    public void init() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockPlaceEvent.class, event -> {
            this.runBlockPlaceBreakChecks(event.getPlayer(), event.getBlockPosition(), event.getBlockStateId(), event);
        });
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockBreakEvent.class, event -> {
            this.runBlockPlaceBreakChecks(event.getPlayer(), event.getBlockPosition(), event.getBlockStateId(), event);
        });
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockInteractEvent.class, event -> {
            if (!this.isGameRunning()) {
                event.setCancelled(true);
                return;
            }
            Player player = event.getPlayer();
            if (!this.isLocationInPlayerBounds(player, event.getBlockPosition())) {
                event.setCancelled(true);
            }
        });
    }

    private boolean isLocationInPlayerBounds(Player player, BlockPosition position) {
        RunningGame runningGame = this.extension.getRunningGame();
        if (!runningGame.getParticipants().contains(player.getUuid()))
            return false;
        Optional<Faction> faction = runningGame.getFactions().stream().filter(loopedFaction -> loopedFaction.getPlayers().contains(player.getUuid())).findFirst();
        return faction.isPresent() && faction.get().getRegion().isPositionWithin(position.toPosition());
    }

    private boolean isGameRunning() {
        return this.extension.getRunningGame() != null && this.extension.getRunningGame().isInProgress();
    }

    private void runBlockPlaceBreakChecks(Player player, BlockPosition blockPosition, short blockStateId, CancellableEvent event) {
        if (!this.isGameRunning()) {
            event.setCancelled(true);
            return;
        }
        Block block = Block.fromStateId(blockStateId);
        if (!block.getName().endsWith("_ore")) {
            if (!this.isLocationInPlayerBounds(player, blockPosition)) {
                player.sendMessage("You are not within your faction's bounds");
                event.setCancelled(true);
            }
        }
    }
}
