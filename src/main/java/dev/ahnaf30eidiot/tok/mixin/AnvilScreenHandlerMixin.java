package dev.ahnaf30eidiot.tok.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ahnaf30eidiot.tok.item.custom.TotemOfKeepingItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private int repairItemUsage;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void totemOfKeepingFullRepair(CallbackInfo ci) {
        AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;

        ItemStack left = self.getSlot(0).getStack();
        ItemStack right = self.getSlot(1).getStack();

        // [Insert AI slop comment.]
        if (!left.isEmpty() && left.getItem() instanceof TotemOfKeepingItem && left.getDamage() > 0) {

            if (right.isOf(Items.ENDER_EYE)) {
                // ENDER EYE
                ItemStack repaired = left.copy();
                repaired.setDamage(0); // FULLY repaired

                self.getSlot(2).setStack(repaired);
                this.levelCost.set(1);
                this.repairItemUsage = 1;

                ci.cancel();
                return;

            }
            else if (right.isOf(Items.ENDER_PEARL)) {
                // ENDER PEARL
                int repairAmount = Math.min(left.getDamage(), right.getCount());
                ItemStack repaired = left.copy();
                repaired.setDamage(left.getDamage() - repairAmount);

                self.getSlot(2).setStack(repaired);
                this.levelCost.set(repairAmount);
                this.repairItemUsage = repairAmount;

                ci.cancel();
            }
        }
    }
}
