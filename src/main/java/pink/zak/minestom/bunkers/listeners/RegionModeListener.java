package pink.zak.minestom.bunkers.listeners;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.utils.BlockPosition;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.cache.UserCache;
import pink.zak.minestom.bunkers.loaders.KothLoader;
import pink.zak.minestom.bunkers.models.Region;
import pink.zak.minestom.bunkers.models.User;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RegionModeListener {
    private final Map<UUID, RegionOperation> regionOperationMap = Maps.newConcurrentMap();
    private final UserCache userCache;
    private final KothLoader kothLoader;

    public RegionModeListener(BunkersExtension extension) {
        this.userCache = extension.getUserCache();
        this.kothLoader = extension.getKothLoader();
    }

    public void init() {
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerDisconnectEvent.class, event -> this.regionOperationMap.remove(event.getPlayer().getUuid()));
        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerBlockInteractEvent.class, event -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUuid();
            User user = this.userCache.getUser(uuid);
            assert user != null; // bad things must've happened for this
            if (user.getClaimMode().getEnabled().get() && event.getHand() == Player.Hand.MAIN) {
                BlockPosition blockPosition = event.getBlockPosition();
                RegionOperation regionOperation;
                if (this.regionOperationMap.containsKey(uuid)) {
                    regionOperation = this.regionOperationMap.get(uuid);
                } else {
                    regionOperation = new RegionOperation();
                    this.regionOperationMap.put(uuid, regionOperation);
                }
                if (regionOperation.positionOne != null) {
                    regionOperation.positionTwo = blockPosition;
                    Region region = new Region(regionOperation.positionOne, regionOperation.positionTwo);
                    player.sendMessage("Set position two to the clicked location.");
                    if (user.getClaimMode().getKoth().get()) {
                        this.kothLoader.setRegion(region);
                        player.sendMessage("Your claiming has been finished and the KoTH's region has been set to what is currently highlighted");
                        player.sendMessage("If you want to reclaim, simply re-perform the same actions. To disable claim mode, type /kadmin toggle claim mode");
                    } else {
                        user.getClaimMode().getFaction().setRegion(region);
                        player.sendMessage("Your claiming has been finished and the faction's region has been set to what is currently highlighted");
                        player.sendMessage("If you want to reclaim, simply re-perform the same actions. To disable claim mode, type /fadmin toggle claim mode");
                    }

                    this.handleParticles(player, region, regionOperation);

                    regionOperation.positionOne = null;
                    regionOperation.positionTwo = null;
                    return;
                }
                regionOperation.positionOne = blockPosition;
                player.sendMessage("Set position one to the clicked location");
            }
        });
    }

    private void handleParticles(Player player, Region region, RegionOperation regionOperation) {
        Set<ParticlePacket> particles = this.createRegionParticles(region, regionOperation);
        AtomicInteger runCount = new AtomicInteger();
        AtomicReference<ScheduledFuture<?>> future = new AtomicReference<>(null);
        future.set(MinecraftServer.getSchedulerManager().getTimerExecutionService().scheduleAtFixedRate(() -> {
            if (runCount.incrementAndGet() <= 20) {
                for (ParticlePacket packet : particles) {
                    player.getPlayerConnection().sendPacket(packet);
                }
            } else {
                future.get().cancel(true);
            }
        }, 0, 250, TimeUnit.MILLISECONDS));
    }

    private Set<ParticlePacket> createRegionParticles(Region region, RegionOperation regionOperation) {
        Set<ParticlePacket> particles = Sets.newHashSet();
        int maxY = Math.max(regionOperation.positionOne.getY(), regionOperation.positionTwo.getY()) + 2;
        int minY = Math.min(regionOperation.positionOne.getY(), regionOperation.positionTwo.getY()) - 1;
        for (int x = region.getMinX(); x <= region.getMaxX(); x++) {
            for (double y = minY; y <= maxY; y += 0.75) {
                particles.add(ParticleCreator.createParticlePacket(Particle.FLAME, x, y, region.getMaxZ(), 0, 0, 0, 1));
                particles.add(ParticleCreator.createParticlePacket(Particle.FLAME, x, y, region.getMinZ(), 0, 0, 0, 1));
            }
        }
        for (int z = region.getMinZ(); z <= region.getMaxZ(); z++) {
            for (double y = minY; y <= maxY; y += 0.75) {
                particles.add(ParticleCreator.createParticlePacket(Particle.FLAME, region.getMaxX(), y, z, 0, 0, 0, 1));
                particles.add(ParticleCreator.createParticlePacket(Particle.FLAME, region.getMinX(), y, z, 0, 0, 0, 1));
            }
        }
        return particles;
    }

    private static class RegionOperation {
        private BlockPosition positionOne;
        private BlockPosition positionTwo;
    }
}
