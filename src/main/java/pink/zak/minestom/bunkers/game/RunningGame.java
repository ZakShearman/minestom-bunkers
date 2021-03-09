package pink.zak.minestom.bunkers.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.models.Faction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class RunningGame {
    private final Set<UUID> participants = Sets.newHashSet();
    private final long startTime = System.currentTimeMillis();
    private final BunkersExtension extension;
    private Instance gameInstance;
    private List<Faction> factions;
    private boolean inProgress = false;

    public RunningGame(BunkersExtension extension) {
        this.extension = extension;
    }

    public void startGame(Instance gameInstance) {
        this.inProgress = true;
        this.gameInstance = gameInstance;
        this.factions = Lists.newArrayList(this.extension.getFactionLoader().getFactionMap().values());
        List<Player> players = Lists.newArrayList(MinecraftServer.getConnectionManager().getOnlinePlayers());
        Collections.shuffle(players);
        int factionIndex = 0;
        for (Player player : players) {
            UUID playerUuid = player.getUuid();
            this.factions.get(factionIndex).getPlayers().add(playerUuid);
            this.participants.add(playerUuid);
            factionIndex++;
            if (factionIndex >= this.factions.size())
                factionIndex = 0;
        }

        this.teleportPlayersToFactionHome();
        this.spawnVillagers();
    }

    private void teleportPlayersToFactionHome() {
        for (Faction faction : this.factions) {
            faction.getPlayers().stream().map(uuid -> MinecraftServer.getConnectionManager().getPlayer(uuid))
                    .filter(Objects::nonNull)
                    .forEach(player -> {
                        assert faction.getHomePosition() != null;
                        player.teleport(faction.getHomePosition());
                    });
        }
    }

    private void spawnVillagers() {
        for (Faction faction : this.factions) {
            Entity combatShop =  new Entity(EntityType.VILLAGER);
            Entity buildingShop =  new Entity(EntityType.VILLAGER);
            Entity enchantmentShop =  new Entity(EntityType.VILLAGER);

            faction.getLiveVillagers().add(combatShop);
            faction.getLiveVillagers().add(buildingShop);
            faction.getLiveVillagers().add(enchantmentShop);

            combatShop.setInstance(this.gameInstance, faction.getCombatShopPosition());
            buildingShop.setInstance(this.gameInstance, faction.getBuildingShopPosition());
            enchantmentShop.setInstance(this.gameInstance, faction.getEnchantmentShopPosition());

            combatShop.setCustomName(ColoredText.of("{#pink}Combat Shop"));
            buildingShop.setCustomName(ColoredText.of("{#pink}Building Shop"));
            enchantmentShop.setCustomName(ColoredText.of("{#pink}Enchantment Shop"));

            // todo combatShop.addEventCallback()

            combatShop.spawn();
            buildingShop.spawn();
            enchantmentShop.spawn();
        }
    }

    public void endGame() {
        // todo add more cleaning up
        for (Faction faction : this.extension.getFactionLoader().getFactionMap().values()) {
            faction.getPlayers().clear();
            faction.getLiveVillagers().forEach(Entity::remove);
            faction.getLiveVillagers().clear();
        }
    }

    public Set<UUID> getParticipants() {
        return this.participants;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public List<Faction> getFactions() {
        return this.factions;
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}