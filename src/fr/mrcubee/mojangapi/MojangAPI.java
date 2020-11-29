package fr.mrcubee.mojangapi;

import com.mojang.authlib.GameProfile;
import fr.mrcubee.util.UUIDFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * @author MrCubee
 */
public class MojangAPI {

    public static final String ID_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    public static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    private static String getJSON(String link) throws IOException {
        URLConnection urlConnection = new URL(link).openConnection();
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String line;

        urlConnection.setRequestProperty("User-Agent", "MojangAPI/1.0");
        urlConnection.connect();
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line);
        bufferedReader.close();
        return stringBuilder.toString();
    }

    private static UUID getUUIDFromName(String name) {
        String jsonString;
        JSONParser jsonParser;
        Object object = null;
        JSONObject jsonObject;

        if (name == null)
            return null;
        try {
            jsonString = getJSON(String.format(ID_URL, name));
            jsonParser = new JSONParser();
            object = jsonParser.parse(jsonString);
        } catch (IOException | ParseException ignored) {}
        if (!(object instanceof JSONObject))
            return null;
        jsonObject = (JSONObject) object;
        if (!jsonObject.containsKey("id"))
            return null;
        object = jsonObject.get("id");
        if (!(object instanceof String))
            return null;
        return UUIDFormat.formatFromInput((String) object);
    }

    public static GameProfile getFromUUID(UUID uuid) {
        String jsonString;
        JSONParser jsonParser;
        Object object = null;

        if (uuid == null)
            return null;
        try {
            jsonString = getJSON(String.format(PROFILE_URL, uuid.toString().replaceAll("-", "")));
            jsonParser = new JSONParser();
            object = jsonParser.parse(jsonString);
        } catch (IOException | ParseException ignored) {}
        if (!(object instanceof JSONObject))
            return null;
        return JsonGameProfile.build((JSONObject) object);
    }

    public static GameProfile getFromInput(String value) {
        if (value == null)
            return null;
        return value.length() >= 32 ? getFromUUID(UUIDFormat.formatFromInput(value)) : getFromUUID(getUUIDFromName(value));
    }
}
