package pink.zak.minestom.bunkers.combat.meta.types;

public class EntityMeta {
    private final boolean undead;
    private final boolean arthropod;

    public EntityMeta(boolean undead, boolean arthropod) {
        this.undead = undead;
        this.arthropod = arthropod;
    }

    public boolean isUndead() {
        return this.undead;
    }

    public boolean isArthropod() {
        return this.arthropod;
    }
}
