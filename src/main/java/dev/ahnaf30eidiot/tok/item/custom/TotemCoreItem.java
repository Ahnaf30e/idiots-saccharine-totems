package dev.ahnaf30eidiot.tok.item.custom;

import dev.ahnaf30eidiot.tok.item.TOKItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TotemCoreItem extends Item {

    public TotemCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient)
            return;

        PotionContentsComponent potion = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potion == null || potion.potion().isEmpty())
            return;

        ItemStack newStack = new ItemStack(TOKItems.IMBUED_CORE);
        newStack.set(DataComponentTypes.POTION_CONTENTS, potion);

        if (entity instanceof PlayerEntity player) {
            player.getInventory().setStack(slot, newStack);
            return;
        }

        if (entity instanceof ItemEntity itemEntity) {
            itemEntity.setStack(newStack);
        }
    }

}
