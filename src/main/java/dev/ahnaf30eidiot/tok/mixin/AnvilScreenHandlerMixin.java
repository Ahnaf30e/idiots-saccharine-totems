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
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    // @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    // private void totemOfKeepingFullRepair(CallbackInfo ci) {
    //     AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;
        
    //     ItemStack left = self.getSlot(0).getStack();
    //     ItemStack right = self.getSlot(1).getStack();

    //     // If left is your custom item and right is an Ender Pearl
    //     if (!left.isEmpty() && left.getItem() instanceof TotemOfKeepingItem && right.isOf(Items.ENDER_EYE) && left.getDamage() > 0) {

    //         ItemStack repaired = left.copy();
    //         repaired.setDamage(0); // FULLY repaired

    //         self.getSlot(2).setStack(repaired);
    //         this.levelCost.set(0);
    //         this.repairItemUsage = 1; // consume 1 pearl

    //         ci.cancel(); // cancel vanilla logic
    //     }
    // }
}
