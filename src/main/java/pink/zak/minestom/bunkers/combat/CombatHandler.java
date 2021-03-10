package pink.zak.minestom.bunkers.combat;

import com.google.common.collect.Maps;
import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.time.TimeUnit;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.combat.meta.types.BlockMeta;
import pink.zak.minestom.bunkers.combat.model.CombatPlayer;

import java.util.Map;
import java.util.UUID;

public class CombatHandler {
    private final Map<UUID, CombatPlayer> combatPlayerMap = Maps.newConcurrentMap();

    public void init() {
        this.initPlayerMap();
        MinecraftServer.getGlobalEventHandler().addEventCallback(EntityAttackEvent.class, event -> {
            if (!(event.getTarget() instanceof LivingEntity))
                return;
            LivingEntity target = (LivingEntity) event.getTarget();
            LivingEntity attacker = (LivingEntity) event.getEntity();
            ItemStack holdingItem = attacker.getItemInMainHand();
            if (target.isDead())
                return;

            float damage = 2.0f; // In vanilla this is Attributes.ATTACK_DAMAGE
            float enchantBonus = BunkersExtension.getAddonMetaManager().getEnchantDamageBonus(holdingItem, target);

            boolean critical = attacker instanceof Player && this.shouldBeCritical((Player) attacker);

            if (critical)
                damage *= 1.5;
            damage += enchantBonus;
            MinecraftServer.getConnectionManager().broadcastMessage(ColoredText.of("Damaging " + (target instanceof Player ? ((Player) target).getUsername() : target.getEntityType()) + " for " + damage + "damage"));
            target.damage(DamageType.fromEntity(attacker), damage);
        });
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

            this.combatPlayerMap.get(event.getPlayer().getUuid()).walkDist += distance;
        });
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            for (CombatPlayer combatPlayer : this.combatPlayerMap.values()) {
                combatPlayer.previousWalkDist = combatPlayer.walkDist;
                combatPlayer.walkDist = 0.0f;
            }
        }).delay(1, TimeUnit.SECOND);
    }

    private boolean shouldBeCritical(Player player) {
        Block blockAtPosition = player.getInstance().getBlock(player.getPosition().toBlockPosition());
        return !player.isOnGround() // player must not be on ground
                && !player.isSprinting() // player must not be sprinting
                && !blockAtPosition.isLiquid() // player must not be in a liquid
                && player.getVehicle() == null // player must not be riding on an entity
                && !this.isBlockClimbable(blockAtPosition) // player must not be on a climbable block
                && !this.hasPlayerGotEffect(player, PotionEffect.BLINDNESS) // player must not have blindness
                && !this.hasPlayerGotEffect(player, PotionEffect.SLOW_FALLING) // player must not have slow falling
                && this.combatPlayerMap.get(player.getUuid()).previousWalkDist <= 4.317; // player must've walked less than 4.317 blocks in the previous second
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
