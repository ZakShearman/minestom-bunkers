package pink.zak.minestom.bunkers.commands.factionadmin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.builder.Command;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.commands.factionadmin.subs.CreateFactionSub;
import pink.zak.minestom.bunkers.commands.factionadmin.subs.ListFactionsSub;
import pink.zak.minestom.bunkers.commands.factionadmin.subs.SetSubCommands;
import pink.zak.minestom.bunkers.commands.factionadmin.subs.ToggleClaimModeSub;

public class FactionAdminCommand extends Command {

    public FactionAdminCommand(BunkersExtension extension) {
        super("factionadmin", "factionsadmin", "fadmin");

        this.addSubcommand(new CreateFactionSub(extension));
        this.addSubcommand(new ListFactionsSub(extension));
        this.addSubcommand(new SetSubCommands(extension));
        this.addSubcommand(new ToggleClaimModeSub(extension));

        this.setDefaultExecutor((sender, args) -> {
            sender.sendMessage(ColoredText.of(ChatColor.YELLOW, "/fadmin list factions" +
                    "\n/fadmin create faction <faction-name>" +
                    "\n/fadmin set spawn <faction-name>" +
                    "\n/fadmin set corner <faction-name>" +
                    "\n/fadmin set formatter <faction-name> <formatter>"));
        });
    }
}
