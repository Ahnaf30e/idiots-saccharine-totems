package dev.ahnaf30eidiot.event;

import dev.ahnaf30eidiot.command.TOKCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class TOKEvents {

    public static void registerEvents() {
        
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TOKCommands.registerCommands(dispatcher);
        });
    }
}
