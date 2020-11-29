package fr.mrcubee.util;

import java.util.UUID;

/**
 * @author MrCubee
 */
public class UUIDFormat {

    private static UUID fromTrimmed(String trimmedUUID) {
        StringBuilder builder;

        if(trimmedUUID == null)
            return null;
        builder = new StringBuilder(trimmedUUID.trim());

        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException ignored){
            return null;
        }
        return UUID.fromString(builder.toString());
    }

    public static UUID formatFromInput(String uuid) {
        if (uuid == null)
            return null;
        uuid = uuid.trim();
        return uuid.length() == 32 ? fromTrimmed(uuid.replaceAll("-", "")) : UUID.fromString(uuid);
    }
}
