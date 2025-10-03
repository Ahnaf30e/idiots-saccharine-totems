package dev.ahnaf30eidiot.tok.render;

public class ShaderNamespaceContext { // Need for a goofy work around for ShaderProgram
    private static final ThreadLocal<String> EXTRA_NAMESPACE = new ThreadLocal<>();

    public static void set(String namespace) {
        EXTRA_NAMESPACE.set(namespace);
    }

    public static String getAndClear() {
        String ns = EXTRA_NAMESPACE.get();
        EXTRA_NAMESPACE.remove();
        return ns;
    }
}

