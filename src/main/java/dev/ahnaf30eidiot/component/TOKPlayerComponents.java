package dev.ahnaf30eidiot.component;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import dev.ahnaf30eidiot.tok.IdiotsSaccharineTotems;
import net.minecraft.util.Identifier;

public class TOKPlayerComponents implements EntityComponentInitializer {
    
    public static final ComponentKey<PendingTotemComponent> PENDING_TOTEM = 
            ComponentRegistry.getOrCreate(
                    Identifier.of(IdiotsSaccharineTotems.MOD_ID, "pending_totem"),
                    PendingTotemComponent.class
            );
            
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // ALWAYS_COPY so it exists on the new player after respawn
        registry.registerForPlayers(PENDING_TOTEM, player -> new PendingTotemComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }


}
