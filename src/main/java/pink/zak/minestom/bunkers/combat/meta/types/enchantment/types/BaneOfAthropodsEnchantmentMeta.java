package pink.zak.minestom.bunkers.combat.meta.types.enchantment.types;

import net.minestom.server.entity.EntityType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.combat.meta.types.EntityMeta;
import pink.zak.minestom.bunkers.combat.meta.types.enchantment.EnchantmentMeta;

public class BaneOfAthropodsEnchantmentMeta implements EnchantmentMeta {

    @Override
    public float getDamageBonus(EntityType attackedEntity, short level) {
        EntityMeta entityMeta = BunkersExtension.getAddonMetaManager().getEntityMeta(attackedEntity);
        if (entityMeta != null && entityMeta.isArthropod())
            return level * 2.5f;
        return 0;
    }
}
