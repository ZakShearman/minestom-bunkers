package pink.zak.minestom.bunkers.combat.meta.types;

import pink.zak.minestom.bunkers.combat.meta.AddonMeta;

public class ArmourMeta implements AddonMeta {
    private final int defensePoints;
    private final int toughness;

    public ArmourMeta(int defensePoints, int toughness) {
        this.defensePoints = defensePoints;
        this.toughness = toughness;
    }

    public int getDefensePoints() {
        return this.defensePoints;
    }

    public int getToughness() {
        return this.toughness;
    }
}
