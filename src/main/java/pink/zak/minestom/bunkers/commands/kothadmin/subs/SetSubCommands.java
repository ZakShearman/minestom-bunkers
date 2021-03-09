package pink.zak.minestom.bunkers.commands.kothadmin.subs;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.utils.Position;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.KothLoader;
import pink.zak.minestom.bunkers.models.Faction;
import pink.zak.minestom.bunkers.models.Region;

public class SetSubCommands extends Command {
    private final KothLoader kothLoader;

    public SetSubCommands(BunkersExtension extension) {
        super("set");
        this.kothLoader = extension.getKothLoader();

        this.addSyntax(this::onSetCapTimeExecute, ArgumentType.Word("cap"), ArgumentType.Word("time"), ArgumentType.Integer("seconds"));
        this.addSyntax(this::onSetTimeToStartExecute, ArgumentType.Word("time"), ArgumentType.Word("to"), ArgumentType.Word("start"), ArgumentType.Integer("seconds"));
        this.addSyntax(this::onSetMaxYVariationExecute, ArgumentType.Word("max"), ArgumentType.Word("y"), ArgumentType.Word("variation"), ArgumentType.Integer("blocks"));
    }

    private void onSetCapTimeExecute(CommandSender executor, Arguments arguments) {
        int requiredCapTime = arguments.get("seconds");
        this.kothLoader.setRequiredCapTime(requiredCapTime);
        executor.sendMessage("Required KoTH cap time set to " + requiredCapTime + " seconds");
    }

    private void onSetTimeToStartExecute(CommandSender executor, Arguments arguments) {
        int timeToStart = arguments.get("seconds");
        this.kothLoader.setActivateAfter(timeToStart);
        executor.sendMessage("Time to KoTH start set to " + timeToStart + " seconds");

    }

    private void onSetMaxYVariationExecute(CommandSender executor, Arguments arguments) {
        int maxYVariation = arguments.get("blocks");
        this.kothLoader.setMaxYVariation(maxYVariation);
        executor.sendMessage("Max Y variation on KoTH set to " + maxYVariation + " blocks");
    }
}
