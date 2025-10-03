package dev.ahnaf30eidiot.tok.component;

import java.util.List;

import com.mojang.serialization.Codec;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TOKComponents {
    public static final ComponentType<StoredInventory> STORED_INVENTORY = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(IdiotsSaccharineTotems.MOD_ID, "stored_inventory"),
            ComponentType.<StoredInventory>builder()
                    .codec(StoredInventory.CODEC)
                    .packetCodec(StoredInventory.PACKET_CODEC)
                    .build()
    );

    public static void registerComponentTypes() {
        // force class loading
    }

    public record StoredInventory(List<ItemStack> stacks) {
        public static final Codec<StoredInventory> CODEC =
                ItemStack.CODEC.listOf().xmap(StoredInventory::new, StoredInventory::stacks);

        public static final PacketCodec<net.minecraft.network.RegistryByteBuf, StoredInventory> PACKET_CODEC =
                ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()).xmap(StoredInventory::new, StoredInventory::stacks);
    }
}
