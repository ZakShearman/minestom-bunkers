package run;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.vanilla.WallPlacementRule;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.network.ConnectionManager;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.storage.systems.FileStorageSystem;
import net.minestom.server.utils.Position;
import net.minestom.server.world.biomes.Biome;

import java.util.Arrays;
import java.util.List;

public class LaunchServerStartup {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        OptifineSupport.enable();


        MinecraftServer.getSchedulerManager().buildShutdownTask(LaunchServerStartup::shutdown);
        MinecraftServer.getStorageManager().defineDefaultStorageSystem(FileStorageSystem::new);

        StorageLocation storageLocation = MinecraftServer.getStorageManager().getLocation("chunks");
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(storageLocation);
        MinecraftServer.setChunkViewDistance(16);

        for (Block block : Block.values()) {
            String blockNameLower = block.toString().toLowerCase();
            if (blockNameLower.endsWith("_wall") || blockNameLower.endsWith("_fence")) {
                MinecraftServer.getBlockManager().registerBlockPlacementRule(new WallPlacementRule(block));
                System.out.println("Registering wall placement rule for " + blockNameLower);
            }
        }
        MinecraftServer.getConnectionManager().addPlayerInitialization(player -> {
            player.setRespawnPoint(new Position(0, 80, 0));
            player.addEventCallback(PlayerLoginEvent.class, event -> {
                event.setSpawningInstance(instanceContainer);
                MinecraftServer.getConnectionManager().broadcastMessage(ColoredText.of(player.getUsername() + " logged in. There are " + MinecraftServer.getConnectionManager().getOnlinePlayers().size() + " online"));
            });
        });

        minecraftServer.start("10.0.0.9", 25565);
    }

    private static void shutdown() {
        ConnectionManager connectionManager = MinecraftServer.getConnectionManager();
        connectionManager.getOnlinePlayers().forEach(player -> {
            player.kick("Server is closing.");
            connectionManager.removePlayer(player.getPlayerConnection());
        });
    }
}
