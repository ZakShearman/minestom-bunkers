package pink.zak.minestom.bunkers.commands.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;

public class FactionCommand extends Command {

    public FactionCommand() {
        super("faction", "factions", "fac", "f");

        this.setDefaultExecutor(this::onExecute);
    }

    private void onExecute(CommandSender executor, Arguments arguments) {

    }
}
