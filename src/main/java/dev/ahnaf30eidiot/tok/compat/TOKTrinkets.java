package dev.ahnaf30eidiot.tok.compat;

import java.util.List;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TOKTrinkets {

    private static final String API_CLASS = "dev.emi.trinkets.api.TrinketsApi";

    public static void collectTrinkets(LivingEntity player, List<ItemStack> inv) {
        if (!FabricLoader.getInstance().isModLoaded("trinkets")) return;

        try {
            // Lazy load the TrinketsApi reflectively
            Class<?> apiClass = Class.forName(API_CLASS);
            var method = apiClass.getMethod("getTrinketComponent", LivingEntity.class);
            var optional = (java.util.Optional<?>) method.invoke(null, player);
            Object trinkComp = optional.orElse(null);
            if (trinkComp == null) return;

            var getAllEquipped = trinkComp.getClass().getMethod("getAllEquipped");
            var pairs = (List<?>) getAllEquipped.invoke(trinkComp);

            for (Object pair : pairs) {
                var getRight = pair.getClass().getMethod("getRight");
                ItemStack stack = (ItemStack) getRight.invoke(pair);
                if (!stack.isEmpty()) inv.add(stack);
            }
        } catch (Throwable t) {
            IdiotsSaccharineTotems.LOGGER.warn("Trinkets integration failed: " + t);
        }
    }

    
    public static void emptyTrinkets(LivingEntity player) {
        if (!FabricLoader.getInstance().isModLoaded("trinkets")) return;

        try {
            Class<?> apiClass = Class.forName(API_CLASS);
            var method = apiClass.getMethod("getTrinketComponent", LivingEntity.class);
            var optional = (java.util.Optional<?>) method.invoke(null, player);
            Object trinkComp = optional.orElse(null);
            if (trinkComp == null) return;

            var getInventory = trinkComp.getClass().getMethod("getInventory");
            Object inventory = getInventory.invoke(trinkComp);

            if (inventory instanceof java.util.Map<?, ?> map) {
                for (Object slotMap : map.values()) {
                    if (slotMap instanceof java.util.Map<?, ?> inner) {
                        for (Object trinkInv : inner.values()) {
                            var clear = trinkInv.getClass().getMethod("clear");
                            clear.invoke(trinkInv);
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
    }
}
