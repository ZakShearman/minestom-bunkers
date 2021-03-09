package pink.zak.minestom.bunkers.loaders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.models.Region;
import pink.zak.minestom.bunkers.utils.json.JsonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class KothLoader {
    @NotNull
    private final File kothFile;
    private Region region;
    private int maxYVariation = 3;
    private int requiredCapTime = 300;
    private int activateAfter = 300;

    public KothLoader(BunkersExtension extension) {
        this.kothFile = extension.getBasePath().resolve("data").resolve("koth.json").toFile();
        try {
            this.load(JsonParser.parseReader(new FileReader(this.kothFile)).getAsJsonObject());
        } catch (FileNotFoundException ignored) {
        }
    }

    private void load(JsonObject jsonObject) {
        if (jsonObject.has("region"))
            JsonUtils.jsonToRegion(jsonObject.get("region").getAsJsonObject());
        if (jsonObject.has("maxYVariation"))
            this.maxYVariation = jsonObject.get("maxYVariation").getAsInt();
        if (jsonObject.has("requiredCapTime"))
            this.requiredCapTime = jsonObject.get("requiredCapTime").getAsInt();
        if (jsonObject.has("activateAfter"))
            this.activateAfter = jsonObject.get("activateAfter").getAsInt();

    }

    public void save() throws IOException {
        JsonObject jsonObject = new JsonObject();
        if (this.region != null)
            jsonObject.add("region", JsonUtils.regionToJson(this.region));
        jsonObject.addProperty("maxYVariation", this.maxYVariation);
        jsonObject.addProperty("requiredCapTime", this.requiredCapTime);
        jsonObject.addProperty("activateAfter", this.activateAfter);

        JsonUtils.saveJsonElement(jsonObject, this.kothFile);
    }

    @NotNull
    public Region getRegion() {
        return this.region;
    }

    public int getMaxYVariation() {
        return this.maxYVariation;
    }

    public void setMaxYVariation(int maxYVariation) {
        this.maxYVariation = maxYVariation;
    }

    public int getRequiredCapTime() {
        return this.requiredCapTime;
    }

    public void setRequiredCapTime(int requiredCapTime) {
        this.requiredCapTime = requiredCapTime;
    }

    public int getActivateAfter() {
        return this.activateAfter;
    }

    public void setActivateAfter(int activateAfter) {
        this.activateAfter = activateAfter;
    }
}
