package pink.zak.minestom.bunkers.cache;

import com.google.common.collect.Maps;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Nullable;
import pink.zak.minestom.bunkers.models.User;

import java.util.Map;
import java.util.UUID;

public class UserCache {
    private final Map<UUID, User> userMap = Maps.newConcurrentMap();

    public void init() {
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            this.userMap.put(player.getUuid(), new User(player.getUuid()));
        }
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            this.userMap.put(player.getUuid(), new User(player.getUuid()));
        });
    }

    @Nullable
    public User getUser(UUID uuid) {
        return this.userMap.get(uuid);
    }
}
