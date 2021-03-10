package pink.zak.minestom.bunkers.combat.meta.types.enchantment;

import net.minestom.server.entity.EntityType;

public interface EnchantmentMeta {

    float getDamageBonus(EntityType attackedEntity, short level);
}
