package pink.zak.minestom.bunkers.combat.meta.types;

import pink.zak.minestom.bunkers.combat.meta.AddonMeta;

public class WeaponMeta implements AddonMeta {
    private final float damage;
    private final float attackSpeed;

    public WeaponMeta(float damage, float attackSpeed) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    public float getDamage() {
        return this.damage;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }
}
