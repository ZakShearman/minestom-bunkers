package pink.zak.minestom.bunkers.commands.kothadmin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.builder.Command;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.commands.kothadmin.subs.SetSubCommands;
import pink.zak.minestom.bunkers.commands.kothadmin.subs.ToggleClaimModeSub;

public class KothAdminCommand extends Command {

    public KothAdminCommand(BunkersExtension extension) {
        super("kothadmin", "kadmin");

        this.addSubcommand(new SetSubCommands(extension));
        this.addSubcommand(new ToggleClaimModeSub(extension));

        this.setDefaultExecutor((sender, args) -> {
            sender.sendMessage(ColoredText.of(ChatColor.YELLOW, "/kadmin set cap time <seconds>" +
                    "\n/kadmin set time to start <seconds>" +
                    "\n/kadmin set max y variation <blocks>" +
                    "\n/kadmin set corner"));
        });
    }
}
