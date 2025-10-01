package dev.ahnaf30eidiot.tok;

import dev.ahnaf30eidiot.component.TOKComponents;
import dev.ahnaf30eidiot.effect.TOKEffects;
import dev.ahnaf30eidiot.render.TOKShaders;
import net.fabricmc.api.ClientModInitializer;

public class IdiotsSaccharineTotemsClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    TOKEffects.registerEffects();
    TOKShaders.registerShaders();
    TOKComponents.registerComponentTypes();
  }

}
