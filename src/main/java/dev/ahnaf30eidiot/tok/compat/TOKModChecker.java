package dev.ahnaf30eidiot.tok.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;

public class TOKModChecker {

    private static Boolean irisLoaded = null;
    private static Boolean trinketsLoaded = null;

    public static boolean isIrisLoaded() {
        if (irisLoaded != null)
            return irisLoaded;
        irisLoaded = FabricLoader.getInstance().isModLoaded("iris");
        return irisLoaded;
    }

    public static boolean isIrisShaderLoaded() {
        if (!isIrisLoaded())
            return false;
        try {
            // return true;
            return IrisApi.getInstance().isShaderPackInUse();
        } catch (NoClassDefFoundError e) {
            irisLoaded = true;
            return false;
        }
    }

	public static boolean isTrinketsLoaded() {
        if (trinketsLoaded != null)
            return trinketsLoaded;
        trinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        return trinketsLoaded;
    }
}
