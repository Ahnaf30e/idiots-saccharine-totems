package dev.ahnaf30eidiot.recipe;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TOKRecipes {
    public static void registerSpecialRecipes() {
        Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(IdiotsSaccharineTotems.MOD_ID, "totem_core_imbue"),
            TotemCoreImbueRecipe.SERIALIZER);
    }
}
