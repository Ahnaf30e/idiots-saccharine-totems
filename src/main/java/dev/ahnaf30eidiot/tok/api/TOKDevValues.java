package dev.ahnaf30eidiot.tok.api;

import java.util.ArrayList;
import java.util.UUID;

public class TOKDevValues {
    public static ArrayList<UUID> ignoreTOKList = new ArrayList<>();

    public static void ignoreThisTime(UUID uuid) {
        if (!ignoreTOKList.contains(uuid)) ignoreTOKList.add(uuid);
    }
    public static Boolean shouldIgnoreThisTime(UUID uuid) {
        return ignoreTOKList.remove(uuid);
    }
}
