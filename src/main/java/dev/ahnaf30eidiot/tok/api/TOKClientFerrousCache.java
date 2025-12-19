package dev.ahnaf30eidiot.tok.api;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.item.ItemStack;

public class TOKClientFerrousCache {

    private static final HashSet<Integer> HAS_FERROUS = new HashSet<>(); 

    public static void clear() {
        HAS_FERROUS.clear();
    }

    public static void setFerrous(int entityId) {
        HAS_FERROUS.add(entityId);
    }

    public static void removeFerrous(int entityId) {
        HAS_FERROUS.remove(entityId);
    }

    public static boolean hasFerrous(int entityId) {
        return HAS_FERROUS.contains(entityId);
    }

}
