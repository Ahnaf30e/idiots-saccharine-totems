package dev.ahnaf30eidiot.tok;

import dev.ahnaf30eidiot.tok.component.TOKComponents;
import dev.ahnaf30eidiot.tok.effect.TOKEffects;
import dev.ahnaf30eidiot.tok.render.TOKShaders;
import net.fabricmc.api.ClientModInitializer;

public class IdiotsSaccharineTotemsClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    TOKEffects.registerEffects();
    TOKShaders.registerShaders();
    TOKComponents.registerComponentTypes();
  }

}
