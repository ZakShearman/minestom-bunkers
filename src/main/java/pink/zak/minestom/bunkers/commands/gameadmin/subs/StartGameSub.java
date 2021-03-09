package pink.zak.minestom.bunkers.commands.gameadmin.subs;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.game.RunningGame;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class StartGameSub extends Command {
    private final BunkersExtension extension;
    private final FactionLoader factionLoader;
    private final ReentrantLock lock = new ReentrantLock(false);

    public StartGameSub(BunkersExtension extension) {
        super("start");
        this.extension = extension;
        this.factionLoader = extension.getFactionLoader();

        this.setDefaultExecutor(this::onExecute);
    }

    private void onExecute(CommandSender executor, Arguments arguments) {
        try {
            if (!this.lock.tryLock(2, TimeUnit.SECONDS)) {
                executor.sendMessage("There was an issue executing your command (thread lock could not be acquired)");
            } else {
                if (this.areConditionsValid(executor)) {
                    RunningGame runningGame = new RunningGame(this.extension);
                    this.extension.setRunningGame(runningGame);

                    runningGame.startGame(executor.asPlayer().getInstance());
                    executor.sendMessage("Starting the game WOO HOOOOO");
                }
            }
        } catch (InterruptedException ex) {
            executor.sendMessage("There was an issue executing your command (thread lock was interrupted)");

        } finally {
            if (this.lock.isHeldByCurrentThread())
                this.lock.unlock();
        }
    }

    private boolean areConditionsValid(CommandSender executor) {
        if (this.extension.getRunningGame() != null && this.extension.getRunningGame().isInProgress()) {
            executor.sendMessage("There is already a game in progress");
            return false;
        }
        if (this.factionLoader.getFactionMap().size() <= 1) {
            executor.sendMessage("There must be 2 or more factions configured");
            return false;
        }
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() <= 1) {
            executor.sendMessage("There must be 2 or more online players");
            return false;
        }
        return true;
    }
}
