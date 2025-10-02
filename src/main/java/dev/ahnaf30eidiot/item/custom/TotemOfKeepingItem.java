package dev.ahnaf30eidiot.item.custom;

import java.util.List;

import dev.ahnaf30eidiot.component.TOKComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TotemOfKeepingItem extends Item {
    public TotemOfKeepingItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack itemStack) {
        return itemStack.contains(TOKComponents.STORED_INVENTORY);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient() && stack.contains(TOKComponents.STORED_INVENTORY)) {
            TOKComponents.StoredInventory stored = stack.get(TOKComponents.STORED_INVENTORY);

            for (ItemStack s : stored.stacks()) {
                if (!s.isEmpty()) {
                    if (!user.getInventory().insertStack(s.copy())) {
                        user.dropItem(s.copy(), false);
                    }
                }
            }

            stack.remove(TOKComponents.STORED_INVENTORY); // clear component
            stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            
			user.getWorld().sendEntityStatus(user, (byte) 35);
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.ENDER_PEARL) || ingredient.isOf(Items.ENDER_EYE);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.contains(TOKComponents.STORED_INVENTORY)) {
            tooltip.add(Text.translatable("item.tok.totem_of_keeping.stored")
                    .formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.tok.totem_of_keeping.no_next_death")
                    .formatted(Formatting.DARK_RED));
        } else {
            tooltip.add(Text.translatable("item.tok.totem_of_keeping.empty")
                    .formatted(Formatting.GRAY));
        }
    }

}
