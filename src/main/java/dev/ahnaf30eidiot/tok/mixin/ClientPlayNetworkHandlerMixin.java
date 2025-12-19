package dev.ahnaf30eidiot.tok.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import dev.ahnaf30eidiot.tok.api.TOKClientFerrousCache;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.item.TOKItems;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    // hasStatusEffect doesn't really work that well for rendering so now i have to
    // do ts.
    @Inject(method = "onEntityStatusEffect", at = @At("TAIL"))
    private void setFerrousCache(EntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        // Must run on client thread already, but safe either way.
        var effect = packet.getEffectId();
        IdiotsSaccharineTotems.LOGGER.info("shit added d={}", effect.value().getName());
        if (effect.equals(TOKEffects.FERROUS)) {
            int entityId = packet.getEntityId();
            TOKClientFerrousCache.setFerrous(entityId);
        }
    }

    @Inject(method = "onRemoveEntityStatusEffect", at = @At("TAIL"))
    private void removeFerrousCache(RemoveEntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        // Must run on client thread already, but safe either way.
        var effect = packet.effect();
        IdiotsSaccharineTotems.LOGGER.info("shit removed d={}", effect.value().getName());
        if (effect.equals(TOKEffects.FERROUS)) {
            int entityId = packet.entityId();
            TOKClientFerrousCache.setFerrous(entityId);
        }
    }

    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onJoinFerrousCache(GameJoinS2CPacket packet, CallbackInfo ci) {
        TOKClientFerrousCache.clear();
    }

    // Client Totem shit...
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