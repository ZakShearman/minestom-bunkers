package pink.zak.minestom.bunkers.utils.command;

import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import pink.zak.minestom.bunkers.BunkersExtension;

import java.util.Arrays;
import java.util.Set;

public class CommandRegistrar {
    private final Set<String> enabledCommands;

    public CommandRegistrar(Config settings) {
        this.enabledCommands = Sets.newHashSet(settings.getStringList("enabled-commands"));
    }

    public void register(Command... commands) {
        CommandManager commandManager = MinecraftServer.getCommandManager();
        for (Command command : commands) {
            if (this.enabledCommands.contains(command.getName()) || (command.getAliases() != null && Arrays.stream(command.getAliases()).anyMatch(this.enabledCommands::contains))) {
                BunkersExtension.logger.info("Loading command /" + command.getName());
                commandManager.register(command);
            }
        }
    }
}
