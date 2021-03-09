package pink.zak.minestom.bunkers.loaders;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pink.zak.minestom.bunkers.BunkersExtension;
import pink.zak.minestom.bunkers.models.Faction;
import pink.zak.minestom.bunkers.utils.json.JsonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class FactionLoader {
    @NotNull
    private final Map<String, Faction> factionMap = Maps.newConcurrentMap();
    @NotNull
    private final File factionsFile;

    public FactionLoader(BunkersExtension extension) {
        this.factionsFile = extension.getBasePath().resolve("data").resolve("factions.json").toFile();
        try {
            this.load(JsonParser.parseReader(new FileReader(this.factionsFile)));
        } catch (FileNotFoundException ignored) {
        }
    }

    private void load(JsonElement jsonObject) {
        for (JsonElement factionElement : jsonObject.getAsJsonArray()) {
            JsonObject factionObject = factionElement.getAsJsonObject();
            String factionName = factionObject.get("name").getAsString();
            Faction faction = new Faction(factionName);

            if (factionObject.has("home"))
                faction.setHomePosition(JsonUtils.jsonToPosition(factionObject.get("home").getAsJsonObject(), true));
            if (factionObject.has("combatShop"))
                faction.setCombatShopPosition(JsonUtils.jsonToPosition(factionObject.get("combatShop").getAsJsonObject(), true));
            if (factionObject.has("buildingShop"))
                faction.setBuildingShopPosition(JsonUtils.jsonToPosition(factionObject.get("buildingShop").getAsJsonObject(), true));
            if (factionObject.has("enchantmentShop"))
                faction.setEnchantmentShopPosition(JsonUtils.jsonToPosition(factionObject.get("enchantmentShop").getAsJsonObject(), true));

            faction.setRegion(JsonUtils.jsonToRegion(factionObject.get("region").getAsJsonObject()));

            if (factionObject.has("formatter"))
                faction.setFormatter(factionObject.get("formatter").getAsString());

            this.factionMap.put(factionName, faction);
        }
    }

    public void save() throws IOException {
        JsonArray factionsArray = new JsonArray();
        for (Faction faction : this.factionMap.values()) {
            JsonObject factionObject = new JsonObject();
            factionObject.addProperty("name", faction.getName());

            if (faction.getHomePosition() != null)
                factionObject.add("home", JsonUtils.positionToJson(faction.getHomePosition(), true));
            if (faction.getCombatShopPosition() != null)
                factionObject.add("combatShop", JsonUtils.positionToJson(faction.getCombatShopPosition(), true));
            if (faction.getBuildingShopPosition() != null)
                factionObject.add("buildingShop", JsonUtils.positionToJson(faction.getBuildingShopPosition(), true));
            if (faction.getEnchantmentShopPosition() != null)
                factionObject.add("enchantmentShop", JsonUtils.positionToJson(faction.getEnchantmentShopPosition(), true));

            if (faction.getFormatter() != null)
                factionObject.addProperty("formatter", faction.getFormatter());

            factionObject.add("region", JsonUtils.regionToJson(faction.getRegion()));

            factionsArray.add(factionObject);
        }
        JsonUtils.saveJsonElement(factionsArray, this.factionsFile);
    }

    @Nullable
    public Faction getFaction(String name) {
        return this.factionMap.get(name);
    }

    @NotNull
    public Map<String, Faction> getFactionMap() {
        return this.factionMap;
    }

    @NotNull
    public String[] getFactionNames() {
        return this.factionMap.keySet().toArray(new String[0]);
    }
}
