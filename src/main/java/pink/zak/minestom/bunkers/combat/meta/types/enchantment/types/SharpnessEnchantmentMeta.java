package pink.zak.minestom.bunkers.combat.meta.types.enchantment.types;

import net.minestom.server.entity.EntityType;
import pink.zak.minestom.bunkers.combat.meta.types.enchantment.EnchantmentMeta;

public class SharpnessEnchantmentMeta implements EnchantmentMeta {

    @Override
    public float getDamageBonus(EntityType attackedEntity, short level) {
        return 1.0f + Math.max(0, level - 1) * 0.5f;
    }
}
