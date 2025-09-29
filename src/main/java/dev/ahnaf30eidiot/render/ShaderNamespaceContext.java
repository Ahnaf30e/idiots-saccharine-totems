package dev.ahnaf30eidiot.render;

public class ShaderNamespaceContext {
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

