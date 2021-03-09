package pink.zak.minestom.bunkers.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.instance.Instance;

public class SaveCommand extends Command {

    public SaveCommand() {
        super("save");

        this.setDefaultExecutor((sender, args) -> {
            MinecraftServer.getInstanceManager().getInstances().forEach(Instance::saveChunksToStorage);
            sender.sendMessage("Boop saving bby");
        });
    }
}
