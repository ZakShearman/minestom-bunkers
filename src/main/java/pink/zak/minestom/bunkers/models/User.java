package pink.zak.minestom.bunkers.models;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class User {
    @NotNull
    private final UUID uuid;
    @NotNull
    private final ClaimMode claimMode = new ClaimMode();

    public User(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getUuid() {
        return this.uuid;
    }

    @NotNull
    public ClaimMode getClaimMode() {
        return this.claimMode;
    }

    public static class ClaimMode {
        private final AtomicBoolean enabled = new AtomicBoolean(false);
        private Faction faction;

        public AtomicBoolean getEnabled() {
            return this.enabled;
        }

        public Faction getFaction() {
            return this.faction;
        }

        public void setFaction(Faction faction) {
            this.faction = faction;
        }
    }
}
