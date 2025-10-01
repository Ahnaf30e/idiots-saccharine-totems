package dev.ahnaf30eidiot.tok.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.item.TOKItems;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    
    @Inject(method = "getActiveTotemOfUndying", at = @At("RETURN"), cancellable = true)
    private static void getActiveTOKTotem(PlayerEntity entity, CallbackInfoReturnable<ItemStack> ci) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)
                    || stack.isOf(TOKItems.TOTEM_OF_TENACITY)
                    || stack.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)
                    || stack.isOf(TOKItems.TOTEM_OF_FERROUS)
                    || stack.isOf(TOKItems.TOTEM_OF_KEEPING)) {
                ci.setReturnValue(stack);
            }
        }
        ci.setReturnValue(ci.getReturnValue());
    }
}