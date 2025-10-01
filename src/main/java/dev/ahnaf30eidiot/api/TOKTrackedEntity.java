package dev.ahnaf30eidiot.api;

import net.minecraft.item.ItemStack;

public interface TOKTrackedEntity {
    boolean isFerrous();
    ItemStack getTOKTotem();
    void setTOKTotem(ItemStack totem);
}
