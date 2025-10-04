package dev.ahnaf30eidiot.tok.recipe.ingredient;

import java.util.Optional;

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
                Identifier.CODEC.optionalFieldOf("potion").forGetter(ingredient ->
                        ingredient.requiredPotion != null
                                ? Optional.of(Registries.POTION.getId(ingredient.requiredPotion.value()))
                                : Optional.empty())
        ).apply(instance, (itemId, potionOpt) -> {
            Item item = Registries.ITEM.get(itemId);
            RegistryEntry<Potion> potion = potionOpt
                    .flatMap(id -> Registries.POTION.getEntry(id))
                    .orElse(null);
            return new TOKComponentIngredient(item, potion);
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
                }
        );
    }
}