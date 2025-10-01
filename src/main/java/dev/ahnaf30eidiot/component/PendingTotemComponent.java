package dev.ahnaf30eidiot.component;

import org.ladysnake.cca.api.v3.component.Component;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class PendingTotemComponent implements Component {
    private ItemStack pendingTotem = ItemStack.EMPTY;

    public ItemStack get() {
        return pendingTotem;
    }

    public void set(ItemStack stack) {
        this.pendingTotem = stack;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("TOKPendingTotem")) {
            pendingTotem = ItemStack.fromNbtOrEmpty(registryLookup, tag);
        } else {
            pendingTotem = ItemStack.EMPTY;
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (!pendingTotem.isEmpty()) {
            NbtCompound stackTag = new NbtCompound();
            pendingTotem.encode(registryLookup, stackTag);
            tag.put("TOKPendingTotem", stackTag);
        }
    }

}
