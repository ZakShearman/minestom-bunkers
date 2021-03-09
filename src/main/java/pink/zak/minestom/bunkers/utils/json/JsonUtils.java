package pink.zak.minestom.bunkers.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import net.minestom.server.utils.Position;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.bunkers.models.Region;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

@UtilityClass
public class JsonUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void saveJsonElement(JsonElement jsonElement, File file) throws IOException {
        Writer writer = Files.newBufferedWriter(file.toPath());
        gson.toJson(jsonElement, writer);
        writer.close();
    }

    @NotNull
    public Position jsonToPosition(@NotNull JsonObject jsonObject, boolean pitchAndYaw) {
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        Position position = new Position(x, y, z);
        if (pitchAndYaw) {
            float pitch = jsonObject.get("pitch").getAsFloat();
            float yaw = jsonObject.get("yaw").getAsFloat();
            position.setPitch(pitch);
            position.setYaw(yaw);
        }
        return position;
    }

    @NotNull
    public JsonObject positionToJson(@NotNull Position position, boolean pitchAndYaw) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", position.getX());
        jsonObject.addProperty("y", position.getY());
        jsonObject.addProperty("z", position.getZ());
        if (pitchAndYaw) {
            jsonObject.addProperty("pitch", position.getPitch());
            jsonObject.addProperty("yaw", position.getYaw());
        }
        return jsonObject;
    }

    @NotNull
    public JsonObject regionToJson(@NotNull Region region) {
        JsonObject regionObject = new JsonObject();
        regionObject.addProperty("maxX", region.getMaxX());
        regionObject.addProperty("minX", region.getMinX());
        regionObject.addProperty("maxZ", region.getMaxZ());
        regionObject.addProperty("minZ", region.getMinZ());
        return regionObject;
    }

    @NotNull
    public Region jsonToRegion(@NotNull JsonObject jsonObject) {
        int minX = jsonObject.get("minX").getAsInt();
        int maxX = jsonObject.get("maxX").getAsInt();
        int minZ = jsonObject.get("minZ").getAsInt();
        int maxZ = jsonObject.get("maxZ").getAsInt();
        return new Region(minX, maxX, minZ, maxZ);
    }
}
