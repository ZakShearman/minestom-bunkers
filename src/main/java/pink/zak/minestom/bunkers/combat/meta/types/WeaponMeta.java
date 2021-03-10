package pink.zak.minestom.bunkers.combat.meta.types;

import pink.zak.minestom.bunkers.combat.meta.AddonMeta;

public class WeaponMeta implements AddonMeta {
    private final double damage;
    private final double attackSpeed;

    public WeaponMeta(double damage, double attackSpeed) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    public double getDamage() {
        return this.damage;
    }

    public double getAttackSpeed() {
        return this.attackSpeed;
    }
}
