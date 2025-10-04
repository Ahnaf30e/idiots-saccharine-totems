package dev.ahnaf30eidiot.tok.recipe.ingredient;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class TOKComponentIngredientSerializer implements CustomIngredientSerializer<TOKComponentIngredient> {
    public static final Identifier ID = Identifier.of("saccharine_totems", "components");

    @Override
    public Identifier getIdentifier() {
        return ID;
    }

    @Override
    public MapCodec<TOKComponentIngredient> getCodec(boolean allowEmpty) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Identifier.CODEC.fieldOf("item").forGetter(ingredient -> Registries.ITEM.getId(ingredient.item)),
                Identifier.CODEC.optionalFieldOf("potion").forGetter(ingredient -> ingredient.requiredPotion != null
                        ? Optional.of(Registries.POTION.getId(ingredient.requiredPotion.value()))
                        : Optional.empty()),
                Codec.BOOL.optionalFieldOf("allow_any_potion", false).forGetter(i -> i.allowAnyPotion))
                .apply(instance, (itemId, potionOpt, allowAnyPotion) -> {
                    Item item = Registries.ITEM.get(itemId);
                    RegistryEntry<Potion> potion = null;
                    boolean any = allowAnyPotion;

                    if (potionOpt.isPresent()) {
                        String raw = potionOpt.get().toString();
                        if (raw.equals("*") || raw.isEmpty()) {
                            any = true;
                        } else {
                            potion = Registries.POTION.getEntry(potionOpt.get()).orElse(null);
                        }
                    }

                    return new TOKComponentIngredient(item, potion, any);
                }));
    }

    @Override
    public PacketCodec<RegistryByteBuf, TOKComponentIngredient> getPacketCodec() {
        return PacketCodec.of(
                (ingredient, buf) -> {
                    Identifier.PACKET_CODEC.encode(buf, Registries.ITEM.getId(ingredient.item));
                    buf.writeBoolean(ingredient.requiredPotion != null);
                    if (ingredient.requiredPotion != null) {
                        Identifier.PACKET_CODEC.encode(buf, Registries.POTION.getId(ingredient.requiredPotion.value()));
                    }
                },
                buf -> {
                    Item item = Registries.ITEM.get(Identifier.PACKET_CODEC.decode(buf));
                    RegistryEntry<Potion> potion = null;
                    if (buf.readBoolean()) {
                        potion = Registries.POTION.getEntry(Identifier.PACKET_CODEC.decode(buf)).orElse(null);
                    }
                    return new TOKComponentIngredient(item, potion);
                });
    }
}