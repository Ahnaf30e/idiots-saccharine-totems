package dev.ahnaf30eidiot.tok.tag;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TOKTags {
    public static final TagKey<DamageType> FERROUS_ALLOWED = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(IdiotsSaccharineTotems.MOD_ID, "ferrous_allowed"));

}
