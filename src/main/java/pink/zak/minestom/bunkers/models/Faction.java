package pink.zak.minestom.bunkers.models;

import com.google.common.collect.Sets;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Faction {
    @NotNull
    private final Set<UUID> players = Sets.newConcurrentHashSet();
    @NotNull
    private final String name;
    @NotNull
    private final AtomicInteger balance = new AtomicInteger(200);
    @NotNull
    private final Set<Entity> liveVillagers = Sets.newConcurrentHashSet();
    @Nullable
    private Region region;
    @Nullable
    private String formatter;
    @NotNull
    private String formattedName;
    @Nullable
    private Position homePosition;
    @Nullable
    private Position combatShopPosition;
    @Nullable
    private Position buildingShopPosition;
    @Nullable
    private Position enchantmentShopPosition;

    public Faction(@NotNull String name) {
        this.name = name;
        this.formattedName = name;
    }

    @NotNull
    public Set<UUID> getPlayers() {
        return this.players;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public AtomicInteger getBalance() {
        return this.balance;
    }

    @Nullable
    public Region getRegion() {
        return this.region;
    }

    public void setRegion(@Nullable Region region) {
        this.region = region;
    }

    @NotNull
    public Set<Entity> getLiveVillagers() {
        return this.liveVillagers;
    }

    @Nullable
    public String getFormatter() {
        return this.formatter;
    }

    public void setFormatter(@Nullable String formatter) {
        this.formatter = formatter;
        if (formatter == null)
            this.formattedName = this.name;
        else
            this.formattedName = formatter.concat(this.name);
    }

    @NotNull
    public String getFormattedName() {
        return this.formattedName;
    }

    @Nullable
    public Position getHomePosition() {
        return this.homePosition;
    }

    public void setHomePosition(@Nullable Position homePosition) {
        this.homePosition = homePosition;
    }

    @Nullable
    public Position getCombatShopPosition() {
        return this.combatShopPosition;
    }

    public void setCombatShopPosition(@Nullable Position combatShopPosition) {
        this.combatShopPosition = combatShopPosition;
    }

    @Nullable
    public Position getBuildingShopPosition() {
        return this.buildingShopPosition;
    }

    public void setBuildingShopPosition(@Nullable Position buildingShopPosition) {
        this.buildingShopPosition = buildingShopPosition;
    }

    @Nullable
    public Position getEnchantmentShopPosition() {
        return this.enchantmentShopPosition;
    }

    public void setEnchantmentShopPosition(@Nullable Position enchantmentShopPosition) {
        this.enchantmentShopPosition = enchantmentShopPosition;
    }
}
