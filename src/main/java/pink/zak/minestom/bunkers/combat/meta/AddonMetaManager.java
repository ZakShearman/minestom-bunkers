package pink.zak.minestom.bunkers.combat.meta;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import pink.zak.minestom.bunkers.combat.meta.types.ArmourMeta;
import pink.zak.minestom.bunkers.combat.meta.types.BlockMeta;
import pink.zak.minestom.bunkers.combat.meta.types.EntityMeta;
import pink.zak.minestom.bunkers.combat.meta.types.enchantment.EnchantmentMeta;
import pink.zak.minestom.bunkers.combat.meta.types.WeaponMeta;
import pink.zak.minestom.bunkers.combat.meta.types.enchantment.types.SharpnessEnchantmentMeta;
import pink.zak.minestom.bunkers.combat.meta.types.enchantment.types.SmiteEnchantmentMeta;

import java.util.Map;

public class AddonMetaManager {
    private final Map<Material, ImmutableSet<AddonMeta>> materialMetaMap = Maps.newEnumMap(Material.class);
    private final Map<Enchantment, EnchantmentMeta> enchantmentMetaMap = Maps.newEnumMap(Enchantment.class);
    private final Map<EntityType, EntityMeta> entityMetaMap = Maps.newEnumMap(EntityType.class);
    private final Map<Block, BlockMeta> blockMetaMap = Maps.newEnumMap(Block.class);

    public void init() {
        this.initItems();
        this.initArmour();
        this.initEnchantments();
        this.initEntities();
        this.initBlocks();
    }

    private void initItems() {
        // Air is used as the default
        this.configureMaterial(Material.AIR, new WeaponMeta(1, 4));
        // tools / weapons
        // trident
        this.configureMaterial(Material.TRIDENT, new WeaponMeta(9, 1.1));
        // swords
        this.configureMaterial(Material.NETHERITE_SWORD, new WeaponMeta(8, 1.6));
        this.configureMaterial(Material.DIAMOND_SWORD, new WeaponMeta(7, 1.6));
        this.configureMaterial(Material.IRON_SWORD, new WeaponMeta(6, 1.6));
        this.configureMaterial(Material.STONE_SWORD, new WeaponMeta(5, 1.6));
        this.configureMaterial(Material.GOLDEN_SWORD, new WeaponMeta(4, 1.6));
        this.configureMaterial(Material.WOODEN_SWORD, new WeaponMeta(4, 1.6));
        // shovels
        this.configureMaterial(Material.NETHERITE_SHOVEL, new WeaponMeta(6.5, 1));
        this.configureMaterial(Material.DIAMOND_SHOVEL, new WeaponMeta(5.5, 1));
        this.configureMaterial(Material.IRON_SHOVEL, new WeaponMeta(4.5, 1));
        this.configureMaterial(Material.STONE_SHOVEL, new WeaponMeta(3.5, 1));
        this.configureMaterial(Material.GOLDEN_SHOVEL, new WeaponMeta(2.5, 1));
        this.configureMaterial(Material.WOODEN_SHOVEL, new WeaponMeta(2.5, 1));
        // pickaxes
        this.configureMaterial(Material.NETHERITE_PICKAXE, new WeaponMeta(6, 1.2));
        this.configureMaterial(Material.DIAMOND_PICKAXE, new WeaponMeta(5, 1.2));
        this.configureMaterial(Material.IRON_PICKAXE, new WeaponMeta(4, 1.2));
        this.configureMaterial(Material.STONE_PICKAXE, new WeaponMeta(3, 1.2));
        this.configureMaterial(Material.GOLDEN_PICKAXE, new WeaponMeta(2, 1.2));
        this.configureMaterial(Material.WOODEN_PICKAXE, new WeaponMeta(2, 1.2));
        // axes
        this.configureMaterial(Material.NETHERITE_AXE, new WeaponMeta(10, 1));
        this.configureMaterial(Material.DIAMOND_AXE, new WeaponMeta(9, 1));
        this.configureMaterial(Material.IRON_AXE, new WeaponMeta(9, 0.9));
        this.configureMaterial(Material.STONE_AXE, new WeaponMeta(9, 0.8));
        this.configureMaterial(Material.GOLDEN_AXE, new WeaponMeta(7, 1));
        this.configureMaterial(Material.WOODEN_AXE, new WeaponMeta(7, 0.8));
        // hoes
        this.configureMaterial(Material.NETHERITE_HOE, new WeaponMeta(1, 4));
        this.configureMaterial(Material.DIAMOND_HOE, new WeaponMeta(1, 4));
        this.configureMaterial(Material.IRON_HOE, new WeaponMeta(1, 3));
        this.configureMaterial(Material.STONE_HOE, new WeaponMeta(1, 2));
        this.configureMaterial(Material.GOLDEN_HOE, new WeaponMeta(1, 1));
        this.configureMaterial(Material.WOODEN_HOE, new WeaponMeta(1, 1));
    }

    private void initArmour() {
        // armour
        this.configureMaterial(Material.TURTLE_HELMET, new ArmourMeta(2, 0));
        this.configureMaterial(Material.NETHERITE_HELMET, new ArmourMeta(3, 3));
        this.configureMaterial(Material.DIAMOND_HELMET, new ArmourMeta(3, 2));
        this.configureMaterial(Material.IRON_HELMET, new ArmourMeta(2, 0));
        this.configureMaterial(Material.CHAINMAIL_HELMET, new ArmourMeta(2, 0));
        this.configureMaterial(Material.GOLDEN_HELMET, new ArmourMeta(2, 0));
        this.configureMaterial(Material.LEATHER_HELMET, new ArmourMeta(1, 0));

        this.configureMaterial(Material.NETHERITE_CHESTPLATE, new ArmourMeta(8, 3));
        this.configureMaterial(Material.DIAMOND_CHESTPLATE, new ArmourMeta(8, 2));
        this.configureMaterial(Material.IRON_CHESTPLATE, new ArmourMeta(6, 0));
        this.configureMaterial(Material.CHAINMAIL_CHESTPLATE, new ArmourMeta(5, 0));
        this.configureMaterial(Material.GOLDEN_CHESTPLATE, new ArmourMeta(5, 0));
        this.configureMaterial(Material.LEATHER_CHESTPLATE, new ArmourMeta(3, 0));

        this.configureMaterial(Material.NETHERITE_LEGGINGS, new ArmourMeta(6, 3));
        this.configureMaterial(Material.DIAMOND_LEGGINGS, new ArmourMeta(6, 2));
        this.configureMaterial(Material.IRON_LEGGINGS, new ArmourMeta(5, 0));
        this.configureMaterial(Material.CHAINMAIL_LEGGINGS, new ArmourMeta(4, 0));
        this.configureMaterial(Material.GOLDEN_LEGGINGS, new ArmourMeta(3, 0));
        this.configureMaterial(Material.LEATHER_LEGGINGS, new ArmourMeta(2, 0));

        this.configureMaterial(Material.NETHERITE_BOOTS, new ArmourMeta(3, 3));
        this.configureMaterial(Material.DIAMOND_BOOTS, new ArmourMeta(3, 2));
        this.configureMaterial(Material.IRON_BOOTS, new ArmourMeta(2, 0));
        this.configureMaterial(Material.CHAINMAIL_BOOTS, new ArmourMeta(1, 0));
        this.configureMaterial(Material.GOLDEN_BOOTS, new ArmourMeta(1, 0));
        this.configureMaterial(Material.LEATHER_BOOTS, new ArmourMeta(1, 0));
    }

    private void initEnchantments() {
        this.enchantmentMetaMap.put(Enchantment.SHARPNESS, new SharpnessEnchantmentMeta());
        this.enchantmentMetaMap.put(Enchantment.SMITE, new SmiteEnchantmentMeta());
        this.enchantmentMetaMap.put(Enchantment.BANE_OF_ARTHROPODS, new SharpnessEnchantmentMeta()); // todo
    }

    private void initEntities() {
        this.entityMetaMap.put(EntityType.SKELETON, new EntityMeta(true, false));
        this.entityMetaMap.put(EntityType.WITHER_SKELETON, new EntityMeta(true, false));
        this.entityMetaMap.put(EntityType.ZOMBIE, new EntityMeta(true, false));
        this.entityMetaMap.put(EntityType.ZOMBIFIED_PIGLIN, new EntityMeta(true, false));
        this.entityMetaMap.put(EntityType.DROWNED, new EntityMeta(true, false));
        this.entityMetaMap.put(EntityType.WITHER, new EntityMeta(true, false));

        this.entityMetaMap.put(EntityType.SPIDER, new EntityMeta(false, true));
        this.entityMetaMap.put(EntityType.CAVE_SPIDER, new EntityMeta(false, true));
        this.entityMetaMap.put(EntityType.SILVERFISH, new EntityMeta(false, true));
    }

    private void initBlocks() {
        this.blockMetaMap.put(Block.LADDER, new BlockMeta(true));
        this.blockMetaMap.put(Block.VINE, new BlockMeta(true));
        this.blockMetaMap.put(Block.TWISTING_VINES, new BlockMeta(true));
        this.blockMetaMap.put(Block.WEEPING_VINES, new BlockMeta(true));
        this.blockMetaMap.put(Block.SCAFFOLDING, new BlockMeta(true));
    }

    public float getEnchantDamageBonus(ItemStack itemInHand, LivingEntity attackedEntity) {
        float damageBonus = 0.0f;
        for (Map.Entry<Enchantment, Short> entry : itemInHand.getEnchantmentMap().entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (this.enchantmentMetaMap.containsKey(enchantment))
                damageBonus += this.enchantmentMetaMap.get(enchantment).getDamageBonus(attackedEntity.getEntityType(), entry.getValue());
        }
        return damageBonus;
    }

    public EntityMeta getEntityMeta(EntityType entityType) {
        return this.entityMetaMap.get(entityType);
    }

    public BlockMeta getBlockMeta(Block block) {
        return this.blockMetaMap.get(block);
    }

    private void configureMaterial(Material material, AddonMeta... addonMetas) {
        this.materialMetaMap.put(material, ImmutableSet.copyOf(addonMetas));
    }
}
