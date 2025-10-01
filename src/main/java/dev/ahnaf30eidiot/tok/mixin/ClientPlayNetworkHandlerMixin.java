package dev.ahnaf30eidiot.tok.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.api.TOKTrackedEntity;
import dev.ahnaf30eidiot.item.TOKItems;
import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;

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

    // @Inject(method = "onEntityStatus", at = @At("HEAD"), cancellable = true)
    // private void onCustomTotem(EntityStatusS2CPacket packet, CallbackInfo ci) {
    //     if (packet.getStatus() == (byte) 35) {
    //         MinecraftClient client = MinecraftClient.getInstance();
    //         Entity entity = packet.getEntity(client.world);

    //         if (entity instanceof LivingEntity living) {
    //             client.execute(() -> {
    //                 client.execute(() -> { // double execute = delay 1 tick
    //                     ItemStack stack = ((TOKTrackedEntity) living).getTOKTotem();
    //                     IdiotsSaccharineTotems.LOGGER.error("CLIENT GOT: " + stack.getName());

    //                     if (!stack.isEmpty()) {
    //                         client.gameRenderer.showFloatingItem(stack);
    //                         living.playSound(SoundEvents.ITEM_TOTEM_USE, 1.0F, 1.0F);
    //                     }
    //                 });
    //             });

    //             ci.cancel(); // skip vanilla
    //         }

    //     }
    // }
}