package dev.ahnaf30eidiot.tok.component;

import java.util.List;
import java.util.Optional;

import com.jcraft.jogg.Packet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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

    public record StoredInventory(List<ItemStack> stacks, Integer xp) {
        public static final Codec<StoredInventory> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                
                ItemStack.CODEC.listOf().fieldOf("stack").forGetter(StoredInventory::stacks),
                Codec.INT.optionalFieldOf("xp", 0).forGetter(StoredInventory::xp)
            ).apply(i, StoredInventory::new)
        );

        public static final PacketCodec<net.minecraft.network.RegistryByteBuf, StoredInventory> PACKET_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()),
            StoredInventory::stacks,
            PacketCodecs.INTEGER,
            StoredInventory::xp,
            StoredInventory::new
        );
    }
}
