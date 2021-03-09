package pink.zak.minestom.bunkers.commands.factionadmin.subs;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.cache.UserCache;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.models.Faction;
import pink.zak.minestom.bunkers.models.User;

public class ToggleClaimModeSub extends Command {
    private final FactionLoader factionLoader;
    private final UserCache userCache;

    public ToggleClaimModeSub(BunkersExtension extension) {
        super("toggle");
        this.factionLoader = extension.getFactionLoader();
        this.userCache = extension.getUserCache();

        this.addSyntax(this::onExecute, ArgumentType.Literal("claim"), ArgumentType.Literal("mode"), ArgumentType.Word("faction-name").from(this.factionLoader.getFactionNames()).setDefaultValue(" "));
    }

    private void onExecute(CommandSender executor, Arguments arguments) {
        String factionName = arguments.get("faction-name");
        User user = this.userCache.getUser(executor.asPlayer().getUuid());
        if (!user.getClaimMode().getEnabled().get() && factionName.equals(" ")) {
            executor.sendMessage("You must specify a faction name to enable claim mode.");
        } else if (user.getClaimMode().getEnabled().get()) {
            executor.sendMessage("Disabled claim mode.");
            User.ClaimMode claimMode = user.getClaimMode();
            claimMode.setFaction(null);
            claimMode.getEnabled().set(false);
        } else {
            Faction faction = this.factionLoader.getFaction(factionName);
            if (faction == null) {
                executor.sendMessage("Could not find a faction with the name " + factionName);
            } else {
                executor.sendMessage("Now in claim mode for " + faction.getFormattedName() + "{#reset}. Right click a block to start setting corners");
                User.ClaimMode claimMode = user.getClaimMode();
                claimMode.getEnabled().set(true);
                claimMode.setFaction(faction);
            }
        }
    }
}
