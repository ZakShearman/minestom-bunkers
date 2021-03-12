package pink.zak.minestom.bunkers.combat;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDeathEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.sound.Sound;
import net.minestom.server.sound.SoundCategory;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.Vector;
import net.minestom.server.utils.time.TimeUnit;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.combat.meta.AddonMeta;
import pink.zak.minestom.bunkers.combat.meta.types.BlockMeta;
import pink.zak.minestom.bunkers.combat.meta.types.WeaponMeta;
import pink.zak.minestom.bunkers.combat.model.CombatPlayer;

import java.util.Map;
import java.util.UUID;

public class CombatHandler {
    private final Map<UUID, CombatPlayer> combatPlayerMap = Maps.newConcurrentMap();
    private final Map<Entity, Long> entityDamageTimeMap = Maps.newConcurrentMap();

    private final long ATTACK_COOLDOWN_MILLISECONDS = 500;

    public void init() {
        this.initPlayerMap();
        this.initEntityMap();
        MinecraftServer.getGlobalEventHandler().addEventCallback(EntityAttackEvent.class, event -> {
            if (!(event.getTarget() instanceof LivingEntity))
                return;
            LivingEntity victim = (LivingEntity) event.getTarget();
            LivingEntity attacker = (LivingEntity) event.getEntity();
            ItemStack holdingItem = attacker.getItemInMainHand();

            if (this.entityDamageTimeMap.containsKey(victim) && System.currentTimeMillis() - this.entityDamageTimeMap.get(victim) < this.ATTACK_COOLDOWN_MILLISECONDS)
                return;
            if (victim.isDead() || attacker.isDead())
                return;
            if (attacker instanceof Player && ((Player) attacker).getGameMode() == GameMode.SPECTATOR)
                return;

            float damage = this.getBaseDamage(attacker); // In vanilla this is Attributes.ATTACK_DAMAGE
            float enchantBonus = BunkersExtension.getAddonMetaManager().getEnchantDamageBonus(holdingItem, victim);

            boolean critical = attacker instanceof Player && this.shouldBeCritical((Player) attacker);

            if (critical) {
                damage *= 1.5;
                attacker.setSprinting(false);
            }
            damage += enchantBonus;
            MinecraftServer.getConnectionManager().broadcastMessage(ColoredText.of("Damaging " + (victim instanceof Player ? ((Player) victim).getUsername() : victim.getEntityType()) + " for " + damage + " damage"));
            this.entityDamageTimeMap.put(victim, System.currentTimeMillis());

            if (attacker instanceof Player)
                victim.damage(DamageType.fromPlayer((Player) attacker), damage);
            else
                victim.damage(DamageType.fromEntity(attacker), damage);
            victim.heal();
            this.playSounds(victim, attacker, critical);
            this.handleKnockback(victim, attacker);
        });
    }

    private void initEntityMap() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(EntityDeathEvent.class, event -> this.entityDamageTimeMap.remove(event.getEntity()));
    }

    private void initPlayerMap() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            this.combatPlayerMap.put(player.getUuid(), new CombatPlayer(player));
        });
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerDisconnectEvent.class, event -> this.combatPlayerMap.remove(event.getPlayer().getUuid()));
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerMoveEvent.class, event -> {
            Position oldPosition = event.getPlayer().getPosition();
            Position newPosition = event.getNewPosition();

            float distance = (float) oldPosition.getDistance(newPosition);

            CombatPlayer combatPlayer = this.combatPlayerMap.get(event.getPlayer().getUuid());
            combatPlayer.walkDist += distance;
            combatPlayer.oldPosition = combatPlayer.newPosition;
            combatPlayer.newPosition = newPosition;
        });
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (CombatPlayer combatPlayer : this.combatPlayerMap.values()) {
                combatPlayer.previousWalkDist = combatPlayer.walkDist;
                combatPlayer.walkDist = 0.0f;
            }
        }).delay(1, TimeUnit.SECOND);
    }

    private void handleKnockback(LivingEntity victim, LivingEntity attacker) {
        int knockbackMultiplier = 0;
        if (attacker.isSprinting())
            knockbackMultiplier++;
        double xVelocity = Math.sin(attacker.getPosition().getYaw() * 0.017453292f);
        double zVelocity = -Math.sin(attacker.getPosition().getYaw() * 0.017453292f);

        Vector velocityVector = new Vector(xVelocity, 0, zVelocity).normalize();
        velocityVector.multiply(knockbackMultiplier);

        Vector currentVelocity = victim.getVelocity();

        velocityVector.setX(currentVelocity.getX() / 2 - velocityVector.getX());
        velocityVector.setY(victim.isOnGround() ? Math.min(0.4, currentVelocity.getY() / 2.0 + knockbackMultiplier) : velocityVector.getY());
        velocityVector.setZ(currentVelocity.getZ() / 2 - velocityVector.getZ());

        velocityVector.setX(velocityVector.getX() * 3);
        velocityVector.setY(velocityVector.getY() * 3);
        velocityVector.setZ(velocityVector.getX() * 3);

        victim.setVelocity(velocityVector);
        MinecraftServer.getConnectionManager().broadcastMessage(ColoredText.of("\n" +
                "attacker yaw: " + attacker.getPosition().getYaw() +
                "\nx velocity: " + velocityVector.getX() +
                "\ny velocity: " + velocityVector.getY() +
                "\nz velocity: " + velocityVector.getZ()));
    }

    private void playSounds(LivingEntity victim, LivingEntity attacker, boolean critical) {
        Position position = victim.getPosition();
        Chunk victimChunk = victim.getChunk();
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();
        if (victimChunk != null && attacker instanceof Player)
            if (critical)
                for (Player viewer : victimChunk.getViewers())
                    viewer.playSound(Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, x, y, z, 1, 0);
            else
                for (Player viewer : victimChunk.getViewers())
                    viewer.playSound(Sound.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, x, y, z, 1, 0);
    }

    private float getBaseDamage(LivingEntity entity) {
        Material heldMaterial = entity.getItemInMainHand().getMaterial();
        ImmutableSet<AddonMeta> materialMetas = BunkersExtension.getAddonMetaManager().getMaterialMeta(heldMaterial);
        if (materialMetas == null || materialMetas.isEmpty())
            return 2.0f;
        WeaponMeta weaponMeta = null;
        for (AddonMeta addonMeta : materialMetas) {
            if (addonMeta instanceof WeaponMeta) {
                weaponMeta = (WeaponMeta) addonMeta;
                break;
            }
        }
        if (weaponMeta == null)
            return 2.0f;
        return weaponMeta.getDamage();
    }

    private boolean shouldBeCritical(Player player) {
        Block blockAtPosition = player.getInstance().getBlock(player.getPosition().toBlockPosition());
        if (!player.isOnGround() // player must not be on ground
                // && !player.isSprinting() // player must not be sprinting (I don't want this stupid new mechanic)
                && !blockAtPosition.isLiquid() // player must not be in a liquid
                && player.getVehicle() == null // player must not be riding on an entity
                && !this.isBlockClimbable(blockAtPosition) // player must not be on a climbable block
                && !this.hasPlayerGotEffect(player, PotionEffect.BLINDNESS) // player must not have blindness
                && !this.hasPlayerGotEffect(player, PotionEffect.SLOW_FALLING)) { // player must not have slow falling

            CombatPlayer combatPlayer = this.combatPlayerMap.get(player.getUuid());
            return combatPlayer.previousWalkDist <= 4.317
                    && combatPlayer.isFalling();
        }
        return false;
    }

    private boolean isBlockClimbable(Block block) {
        BlockMeta blockMeta = BunkersExtension.getAddonMetaManager().getBlockMeta(block);
        return blockMeta != null && blockMeta.isClimbable();
    }

    private boolean hasPlayerGotEffect(Player player, PotionEffect potionEffect) {
        for (TimedPotion timedPotion : player.getActiveEffects())
            if (timedPotion.getPotion().getEffect() == potionEffect)
                return true;
        return false;
    }
}
