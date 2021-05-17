package pink.zak.minestom.bunkers.commands;

import java.util.Optional;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

/**
 * Command that make a player change gamemode
 */
public class GamemodeCommand extends Command {
    public GamemodeCommand() {
        super("gamemode");

        setCondition(this::isAllowed);

        setDefaultExecutor(this::usage);

        Argument<?> player = ArgumentType.Word("player");

        GameMode[] gameModes = GameMode.values();
        String[] names = new String[gameModes.length];
        for (int i = 0; i < gameModes.length; i++) {
            names[i] = gameModes[i].name().toLowerCase();
        }
        Argument<?> mode = ArgumentType.Word("mode").from(names);

        setArgumentCallback(this::gameModeCallback, mode);

        addSyntax(this::executeOnSelf, mode);
        addSyntax(this::executeOnOther, player, mode);
    }

    private void usage(CommandSender player, CommandContext context) {
        player.sendMessage("Usage: /gamemode [player] <gamemode>");
    }

    private void executeOnSelf(CommandSender sender, CommandContext context) {
        final Player player = sender.asPlayer();
        final String gamemodeName = context.get("mode");
        final GameMode mode = GameMode.valueOf(gamemodeName.toUpperCase());
        assert mode != null; // mode is not supposed to be null, because gamemodeName will be valid
        player.setGameMode(mode);
        player.sendMessage(ColoredText.of("{@commands.gamemode.success.self," + gamemodeName + "}"));
    }

    private void executeOnOther(CommandSender sender, CommandContext context) {
        final Player player = sender.asPlayer();
        final String gamemodeName = context.get("mode");
        final String targetName = context.get("player");
        final GameMode mode = GameMode.valueOf(gamemodeName.toUpperCase());
        Optional<Player> target = player.getInstance().getPlayers().stream().filter(p -> p.getUsername().equalsIgnoreCase(targetName)).findFirst();
        if (target.isPresent()) {
            target.get().setGameMode(mode);
            player.sendMessage(ColoredText.of("{@commands.gamemode.success.other," + targetName + "," + gamemodeName + "}"));
        } else {
            player.sendMessage(ColoredText.of("{@argument.player.unknown}"));
        }
    }

    private void gameModeCallback(CommandSender player, ArgumentSyntaxException exception) {
        player.sendMessage("'" + exception.getInput() + "' is not a valid gamemode!");
    }

    private boolean isAllowed(CommandSender player, String commandName) {
        // TODO: make useable via console
        return player.isPlayer(); // TODO: permissions
    }
}

