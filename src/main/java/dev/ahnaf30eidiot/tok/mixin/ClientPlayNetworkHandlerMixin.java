package dev.ahnaf30eidiot.tok.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
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

import dev.ahnaf30eidiot.tok.item.TOKItems;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"), cancellable = true)
    private void playCustomTotemSound(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() == 35) {
            ClientPlayNetworkHandler self = (ClientPlayNetworkHandler) (Object) this;
            Entity entity = packet.getEntity(self.getWorld());
            if (entity instanceof PlayerEntity player) {
                ItemStack stack = getTOKTotem(player);

                if (stack.isOf(TOKItems.TOTEM_OF_FERROUS)) {
                    // player.getWorld().playSound(player.getX(), player.getY(), player.getZ(),
                    // TOKSounds.TOTEM_FERROUS_USE, player.getSoundCategory(), 1.0F, 1.0F, false);
                    // ci.cancel();
                } else if (stack.isOf(TOKItems.TOTEM_OF_KEEPING)) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    player.getWorld().playSound(player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, player.getSoundCategory(), 1.0F, 1.0F, false);

                    if (entity == client.player) {
                        client.gameRenderer.showFloatingItem(getTOKTotem(client.player));
                    }

                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "getActiveTotemOfUndying", at = @At("RETURN"), cancellable = true)
    private static void getActiveTOKTotem(PlayerEntity entity, CallbackInfoReturnable<ItemStack> ci) {
        ItemStack stack = getTOKTotem(entity);
        if (stack.isEmpty())
            return;
        ci.setReturnValue(stack);
    }

    private static ItemStack getTOKTotem(PlayerEntity entity) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)
                    || stack.isOf(TOKItems.TOTEM_OF_TENACITY)
                    || stack.isOf(TOKItems.TOTEM_OF_PERSEVERANCE)
                    || stack.isOf(TOKItems.TOTEM_OF_FERROUS)
                    || stack.isOf(TOKItems.TOTEM_OF_KEEPING)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}