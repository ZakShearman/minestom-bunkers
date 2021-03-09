package pink.zak.minestom.bunkers;

import com.typesafe.config.Config;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import org.slf4j.Logger;
import pink.zak.minestom.bunkers.cache.UserCache;
import pink.zak.minestom.bunkers.commands.GamemodeCommand;
import pink.zak.minestom.bunkers.commands.SaveCommand;
import pink.zak.minestom.bunkers.commands.StopCommand;
import pink.zak.minestom.bunkers.commands.faction.FactionCommand;
import pink.zak.minestom.bunkers.commands.factionadmin.FactionAdminCommand;
import pink.zak.minestom.bunkers.commands.gameadmin.GameAdminCommand;
import pink.zak.minestom.bunkers.commands.kothadmin.KothAdminCommand;
import pink.zak.minestom.bunkers.game.RunningGame;
import pink.zak.minestom.bunkers.listeners.BlockListener;
import pink.zak.minestom.bunkers.listeners.RegionModeListener;
import pink.zak.minestom.bunkers.loaders.FactionLoader;
import pink.zak.minestom.bunkers.loaders.KothLoader;
import pink.zak.minestom.bunkers.scoreboard.ScoreboardManager;
import pink.zak.minestom.bunkers.utils.command.CommandRegistrar;
import pink.zak.minestom.bunkers.utils.config.ConfigManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BunkersExtension extends Extension {
    private Path basePath;
    private ConfigManager configManager;

    private UserCache userCache;
    private FactionLoader factionLoader;
    private KothLoader kothLoader;
    private RunningGame runningGame;
    private ScoreboardManager scoreboardManager;

    public static Logger logger;

    @SneakyThrows
    @Override
    public void initialize() {
        logger = super.getLogger();
        this.basePath = Paths.get("").resolve("extensions").resolve("bunkers");
        this.configManager = new ConfigManager(this.basePath);

        this.setupConfigs();

        this.userCache = new UserCache();

        this.userCache.init();

        this.factionLoader = new FactionLoader(this);
        this.kothLoader = new KothLoader(this);

        this.scoreboardManager = new ScoreboardManager(this);

        this.scoreboardManager.init();

        this.setupListeners();
        this.registerCommands();

        MinecraftServer.getGlobalEventHandler().addEventCallback(PlayerLoginEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().setAllowFlying(true);
            event.getPlayer().setFlying(true);
            event.getPlayer().setFlyingSpeed(0.15f);
            event.getPlayer().setSkin(PlayerSkin.fromUsername(event.getPlayer().getUsername()));
        });
    }

    @SneakyThrows
    @Override
    public void terminate() {
        this.factionLoader.save();
        this.kothLoader.save();
        // TODO: Save cached data
        // TODO: Add shutdown scheduling? idk? integration with bungee / redis?

    }

    private void setupConfigs() {
        File dataFolder = this.basePath.resolve("data").toFile();
        if (!dataFolder.exists())
            dataFolder.mkdirs();
        this.configManager.config("settings", Path::resolve);
    }

    private void setupListeners() {
        Config settings = this.configManager.getConfig("settings");
        new BlockListener(this).init();
        new RegionModeListener(this).init();
    }

    private void registerCommands() {
        CommandRegistrar registrar = new CommandRegistrar(this.configManager.getConfig("settings"));

        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        registrar.register(
                new FactionCommand(),
                new FactionAdminCommand(this),
                new GameAdminCommand(this),
                new KothAdminCommand(this),
                new SaveCommand(),
                new StopCommand()
        );
    }

    public Path getBasePath() {
        return this.basePath;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public FactionLoader getFactionLoader() {
        return this.factionLoader;
    }

    public KothLoader getKothLoader() {
        return this.kothLoader;
    }

    public RunningGame getRunningGame() {
        return this.runningGame;
    }

    public void setRunningGame(RunningGame runningGame) {
        this.runningGame = runningGame;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }
}
