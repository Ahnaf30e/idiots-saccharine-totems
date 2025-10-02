package dev.ahnaf30eidiot.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TOKPersistentValues extends PersistentState {

    private final Map<UUID, ItemStack> heldOn = new HashMap<>();

    public TOKPersistentValues() {}

    // Load from NBT
    public static TOKPersistentValues fromNbt(NbtCompound nbt , RegistryWrapper.WrapperLookup registryLookup) {
        TOKPersistentValues state = new TOKPersistentValues();

        NbtList list = nbt.getList("held_on", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound entry = list.getCompound(i);
            UUID uuid = UUID.fromString(entry.getString("uuid"));
            ItemStack stack = ItemStack.fromNbt(registryLookup, entry.getCompound("item").get("tok_values")).orElse(ItemStack.EMPTY);
            state.heldOn.put(uuid, stack);
        }

        return state;
    }

    // Save to NBT
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (Map.Entry<UUID, ItemStack> entry : heldOn.entrySet()) {
            NbtCompound data = new NbtCompound();
            data.putString("uuid", entry.getKey().toString());
            data.put("item", entry.getValue().encode(registryLookup, new NbtCompound()));
            list.add(data);
        }
        nbt.put("held_on", list);
        return nbt;
    }

    // Accessor
    public Map<UUID, ItemStack> getHeldOn() {
        return heldOn;
    }

    public static final Type<TOKPersistentValues> TYPE = new Type<>(
        TOKPersistentValues::new,     // constructor
        TOKPersistentValues::fromNbt, // deserializer
        null                          // you can pass a codec if using datafixer
    );

    // Utility: get instance for a given world
    public static TOKPersistentValues get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(TYPE, "tok_values");
    }
}
