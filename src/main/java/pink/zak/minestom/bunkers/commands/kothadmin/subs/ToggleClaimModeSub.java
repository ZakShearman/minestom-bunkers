package pink.zak.minestom.bunkers.commands.kothadmin.subs;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.cache.UserCache;
import pink.zak.minestom.bunkers.models.User;

public class ToggleClaimModeSub extends Command {
    private final UserCache userCache;

    public ToggleClaimModeSub(BunkersExtension extension) {
        super("toggle");
        this.userCache = extension.getUserCache();

        this.addSyntax(this::onExecute, ArgumentType.Literal("claim"), ArgumentType.Literal("mode"));
    }

    private void onExecute(CommandSender executor, CommandContext context) {
        User user = this.userCache.getUser(executor.asPlayer().getUuid());
        if (user.getClaimMode().getEnabled().get()) {
            executor.sendMessage("Disabled claim mode.");
            User.ClaimMode claimMode = user.getClaimMode();
            claimMode.getKoth().set(false);
            claimMode.getEnabled().set(false);
        } else {
            executor.sendMessage("Now in KoTH claim mode. Right click a block to start setting corners");
            User.ClaimMode claimMode = user.getClaimMode();
            claimMode.getEnabled().set(true);
            claimMode.getKoth().set(true);
        }
    }
}
