package pink.zak.minestom.bunkers.utils.config;

import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import pink.zak.minestom.bunkers.BunkersExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.function.BiFunction;

public class ConfigManager {
    private final Map<String, Config> configMap = Maps.newHashMap();
    private final Path basePath;

    public ConfigManager(Path basePath) {
        this.basePath = basePath;
    }

    public ConfigManager config(String name, BiFunction<Path, String, Path> pathFunc) {
        Path resolvedPath = pathFunc.apply(this.basePath, name + ".conf");
        this.saveFileIfNotExists(resolvedPath);
        this.configMap.put(name, ConfigFactory.parseFile(resolvedPath.toFile()));
        return this;
    }

    public Config getConfig(String id) {
        return this.configMap.get(id);
    }

    private void saveFileIfNotExists(Path path) {
        if (!this.basePath.toFile().exists())
            this.basePath.toFile().mkdir();

        if (!path.toFile().exists()) {
            String resourcePath = path.toString().replace(this.basePath.toString() + "\\", "");
            try {
                URL resourceUrl = this.getClass().getClassLoader().getResource(resourcePath);
                if (resourceUrl == null) {
                    BunkersExtension.logger.error("Could not locate resource " + resourcePath);
                } else {
                    Files.write(path, this.getClass().getClassLoader().getResourceAsStream(resourcePath).readAllBytes(), StandardOpenOption.CREATE);
                }
            } catch (FileNotFoundException e) {
                BunkersExtension.logger.error("Could not locate resource " + resourcePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
