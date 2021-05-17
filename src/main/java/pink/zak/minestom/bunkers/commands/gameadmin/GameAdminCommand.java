package pink.zak.minestom.bunkers.commands.gameadmin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.command.builder.Command;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.commands.gameadmin.subs.StartGameSub;

public class GameAdminCommand extends Command {

    public GameAdminCommand(BunkersExtension extension) {
        super("gameadmin", "gadmin");

        this.addSubcommand(new StartGameSub(extension));

        this.setDefaultExecutor((sender, args) -> {
            sender.sendMessage(Component.text("/gadmin start" +
                    "\n/gadmin set autostart <true/false>" +
                    "\n/gadmin set required players <amount>", TextColor.color(255, 255, 0)));
        });
    }
}
