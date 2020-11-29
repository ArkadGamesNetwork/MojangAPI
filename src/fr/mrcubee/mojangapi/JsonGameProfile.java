package fr.mrcubee.mojangapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import fr.mrcubee.util.UUIDFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * @author MrCubee
 */
public class JsonGameProfile {

    private static Property getProperty(Object object) {
        JSONObject jsonObject;
        String name;
        String value;
        String signature = null;

        if (!(object instanceof JSONObject))
            return null;
        jsonObject = (JSONObject) object;
        if (!jsonObject.containsKey("name") || !jsonObject.containsKey("value"))
            return null;
        object = jsonObject.get("name");
        if (!(object instanceof String))
            return null;
        name = (String) object;
        object = jsonObject.get("value");
        if (!(object instanceof String))
            return null;
        value = (String) object;
        if (jsonObject.containsKey("signature")) {
            object = jsonObject.get("signature");
            if (object instanceof String)
                signature = (String) object;
        }
        return new Property(name, value, signature);
    }

    private static void addProperties(JSONObject jsonObject, GameProfile gameProfile) {
        PropertyMap propertyMap;
        JSONArray jsonArray;
        Object object;
        Property property;

        if (jsonObject == null || gameProfile == null || !jsonObject.containsKey("properties"))
            return;
        propertyMap = gameProfile.getProperties();
        object = jsonObject.get("properties");
        if (!(object instanceof JSONArray))
            return;
        jsonArray = (JSONArray) object;
        for (Object element : jsonArray) {
            property = getProperty(element);
            if (property != null)
                propertyMap.put(property.getName(), property);
        }
    }

    public static GameProfile build(JSONObject jsonObject) {
        GameProfile gameProfile;
        Object object;
        String name;
        UUID uuid;

        if (jsonObject == null || !jsonObject.containsKey("id") || !jsonObject.containsKey("name"))
            return null;
        object = jsonObject.get("name");
        if (!(object instanceof String))
            return null;
        name = (String) object;
        object = jsonObject.get("id");
        if (!(object instanceof String))
            return null;
        uuid = UUIDFormat.formatFromInput((String) object);
        gameProfile = new GameProfile(uuid, name);
        addProperties(jsonObject, gameProfile);
        return gameProfile;
    }
}
