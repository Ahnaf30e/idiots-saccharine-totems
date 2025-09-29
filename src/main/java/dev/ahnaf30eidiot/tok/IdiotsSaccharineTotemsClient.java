package dev.ahnaf30eidiot.tok;

import dev.ahnaf30eidiot.render.TOKShaders;
import net.fabricmc.api.ClientModInitializer;

public class IdiotsSaccharineTotemsClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        TOKShaders.init();
    }
    
}
