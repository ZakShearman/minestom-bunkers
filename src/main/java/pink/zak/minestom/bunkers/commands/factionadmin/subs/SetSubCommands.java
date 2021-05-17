package pink.zak.minestom.bunkers.commands.factionadmin.subs;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.utils.Position;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.models.Faction;

public class SetSubCommands extends Command {
    private final FactionLoader factionLoader;

    public SetSubCommands(BunkersExtension extension) {
        super("set");
        this.factionLoader = extension.getFactionLoader();

        this.addSyntax(this::onSetHomeExecute, ArgumentType.Literal("home"), ArgumentType.Word("faction-name").from(this.factionLoader.getFactionNames()));
        this.addSyntax(this::onSetFormatterExecute, ArgumentType.Literal("formatter"), ArgumentType.Word("faction-name").from(this.factionLoader.getFactionNames()), ArgumentType.Word("formatter"));
        this.addSyntax(this::onSetShopExecute, ArgumentType.Literal("shop"), ArgumentType.Word("shop-type").from("combat", "building", "enchantment"), ArgumentType.Word("faction-name").from(this.factionLoader.getFactionNames()));
    }

    private void onSetHomeExecute(CommandSender executor, CommandContext context) {
        String factionName = context.get("faction-name");

        Faction faction = this.factionLoader.getFaction(factionName);
        if (faction == null) {
            executor.sendMessage("Could not find a faction with the name " + factionName);
        } else {
            Position homePosition = executor.asPlayer().getPosition().clone();
            faction.setHomePosition(homePosition);
            executor.sendMessage(ColoredText.of("The home of " + faction.getFormattedName() + " has been set to your location."));
        }
    }

    private void onSetFormatterExecute(CommandSender executor, CommandContext context) {
        String factionName = context.get("faction-name");
        String formatter = context.get("formatter");

        Faction faction = this.factionLoader.getFaction(factionName);
        if (faction == null) {
            executor.sendMessage("Could not find a faction with the name " + factionName);
        } else {
            faction.setFormatter(formatter);
            executor.sendMessage(ColoredText.of("The formatted name of " + factionName + " is now " + faction.getFormattedName()));
        }
    }

    private void onSetShopExecute(CommandSender executor, CommandContext context) {
        String factionName = context.get("faction-name");
        String shopType = context.get("shop-type");

        Faction faction = this.factionLoader.getFaction(factionName);
        if (faction == null) {
            executor.sendMessage("Could not find a faction with the name " + factionName);
        } else {
            Position shopPosition = executor.asPlayer().getPosition().clone();
            switch (shopType) {
                case "combat":
                    faction.setCombatShopPosition(shopPosition);
                    break;
                case "building":
                    faction.setBuildingShopPosition(shopPosition);
                    break;
                case "enchantment":
                    faction.setEnchantmentShopPosition(shopPosition);
                    break;
                default:
                    return;
            }
            executor.sendMessage(ColoredText.of("Set the position of the " + shopType + " for the faction " + factionName));
        }

    }
}
