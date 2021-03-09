package pink.zak.minestom.bunkers.commands.factionadmin.subs;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.models.Faction;

import java.util.Map;
import java.util.StringJoiner;

public class ListFactionsSub extends Command {
    private final FactionLoader factionLoader;

    public ListFactionsSub(BunkersExtension extension) {
        super("list");
        this.factionLoader = extension.getFactionLoader();

        this.addSyntax(this::onExecute, ArgumentType.Literal("factions"));
    }

    private void onExecute(CommandSender executor, Arguments arguments) {
        Map<String, Faction> factionMap = this.factionLoader.getFactionMap();
        if (factionMap.isEmpty()) {
            executor.sendMessage("There are no created factions.");
        } else {
            StringJoiner stringJoiner = new StringJoiner("\n");
            int i = 0;
            for (Faction faction : factionMap.values()) {
                i++;
                stringJoiner.add(i + ") " + faction.getFormattedName());
            }
            executor.sendMessage(stringJoiner.toString());
        }
    }
}
