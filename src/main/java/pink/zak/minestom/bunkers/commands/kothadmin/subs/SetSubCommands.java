package pink.zak.minestom.bunkers.commands.kothadmin.subs;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.KothLoader;

public class SetSubCommands extends Command {
    private final KothLoader kothLoader;

    public SetSubCommands(BunkersExtension extension) {
        super("set");
        this.kothLoader = extension.getKothLoader();

        this.addSyntax(this::onSetCapTimeExecute, ArgumentType.Word("cap"), ArgumentType.Word("time"), ArgumentType.Integer("seconds"));
        this.addSyntax(this::onSetTimeToStartExecute, ArgumentType.Word("time"), ArgumentType.Word("to"), ArgumentType.Word("start"), ArgumentType.Integer("seconds"));
        this.addSyntax(this::onSetMaxYVariationExecute, ArgumentType.Word("max"), ArgumentType.Word("y"), ArgumentType.Word("variation"), ArgumentType.Integer("blocks"));
    }

    private void onSetCapTimeExecute(CommandSender executor, CommandContext context) {
        int requiredCapTime = context.get("seconds");
        this.kothLoader.setRequiredCapTime(requiredCapTime);
        executor.sendMessage("Required KoTH cap time set to " + requiredCapTime + " seconds");
    }

    private void onSetTimeToStartExecute(CommandSender executor, CommandContext context) {
        int timeToStart = context.get("seconds");
        this.kothLoader.setActivateAfter(timeToStart);
        executor.sendMessage("Time to KoTH start set to " + timeToStart + " seconds");

    }

    private void onSetMaxYVariationExecute(CommandSender executor, CommandContext context) {
        int maxYVariation = context.get("blocks");
        this.kothLoader.setMaxYVariation(maxYVariation);
        executor.sendMessage("Max Y variation on KoTH set to " + maxYVariation + " blocks");
    }
}
