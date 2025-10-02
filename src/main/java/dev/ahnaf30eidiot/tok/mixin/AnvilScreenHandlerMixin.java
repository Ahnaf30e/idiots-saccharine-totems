package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.item.custom.TotemOfKeepingItem;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Shadow @Final private Property levelCost;

    @Shadow @Final protected Inventory input;

    @Shadow @Final private CraftingResultInventory output;

    @Shadow private int repairItemUsage;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void totemOfKeepingFullRepair(CallbackInfo ci) {
        
        ItemStack left = this.input.getStack(0);
        ItemStack right = this.input.getStack(1);

        // If left is your custom item and right is an Ender Pearl
        if (!left.isEmpty() && left.getItem() instanceof TotemOfKeepingItem && right.isOf(Items.ENDER_PEARL)) {
            ItemStack repaired = left.copy();
            repaired.setDamage(0); // FULLY repaired

            this.output.setStack(0, repaired);
            this.levelCost.set(left.getDamage() + 1);    // 1 xp level cost
            this.repairItemUsage = 1; // consume 1 pearl

            ci.cancel(); // cancel vanilla logic
        }
    }
}
