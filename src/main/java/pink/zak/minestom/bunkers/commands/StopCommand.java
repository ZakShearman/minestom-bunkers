package pink.zak.minestom.bunkers.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop");

        this.setCondition((source, commandString) -> {
            return source instanceof Player && (((Player) source).getUsername().equals("Expectational") || ((Player) source).getUsername().equals("TS3Moderator"));
        });
        this.setDefaultExecutor(this::onExecute);
    }

    private void onExecute(CommandSender executor, CommandContext context) {
        MinecraftServer.stopCleanly();
    }
}
