package dev.ahnaf30eidiot.api;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

public class TOKPersistentValues {
	public static final HashMap<UUID, ItemStack> TOK_HELD_ON = new HashMap<UUID, ItemStack>();
}
