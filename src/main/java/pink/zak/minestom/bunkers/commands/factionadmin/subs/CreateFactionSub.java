package pink.zak.minestom.bunkers.commands.factionadmin.subs;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.models.Faction;

public class CreateFactionSub extends Command {
    private final FactionLoader factionLoader;

    public CreateFactionSub(BunkersExtension extension) {
        super("create");
        this.factionLoader = extension.getFactionLoader();

        this.addSyntax(this::onExecute, ArgumentType.Literal("faction"), ArgumentType.Word("faction-name"));
    }

    private void onExecute(CommandSender executor, CommandContext context) {
        String factionName = context.get("faction-name");

        if (this.factionLoader.getFactionMap().keySet().stream().anyMatch(string -> string.equalsIgnoreCase(factionName))) {
            executor.sendMessage("A faction with the name " + factionName + " already exists.");
        } else {
            Faction faction = new Faction(factionName);
            this.factionLoader.getFactionMap().put(factionName, faction);
            executor.sendMessage("The faction " + factionName + " has been created.");
        }
    }
}
