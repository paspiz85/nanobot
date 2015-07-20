package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.platform.win32.Win32Platform;
import it.paspiz85.nanobot.util.OS;
import it.paspiz85.nanobot.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory for {@link Platform}.
 *
 * @author paspiz85
 *
 */
public final class PlatformResolver implements Supplier<Platform> {

    public static final String CLASS_PROPERTY = Platform.class.getName();

    public static PlatformResolver instance() {
        return Utils.singleton(PlatformResolver.class, () -> new PlatformResolver());
    }

    private Platform platform;

    private final Map<OS.Category, Class<? extends Platform>> platformClassMap = new HashMap<>();

    private PlatformResolver() {
        platformClassMap.put(OS.Category.WINDOWS, Win32Platform.class);
    }

    @Override
    public Platform get() {
        if (platform == null) {
            Class<? extends Platform> platformClass = getClassByName(System.getProperty(CLASS_PROPERTY));
            if (platformClass == null) {
                platformClass = platformClassMap.get(OS.getCategory());
            }
            if (platformClass == null) {
                platformClass = UnknowPlatform.class;
            }
            platform = getInstance(platformClass);
        }
        return platform;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Platform> getClassByName(final String platformClassName) {
        try {
            Class<? extends Platform> platformClass = null;
            if (platformClassName != null) {
                platformClass = (Class<? extends Platform>) Class.forName(platformClassName);
            }
            return platformClass;
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("unable to initialize platform class", e);
        }
    }

    private Platform getInstance(final Class<? extends Platform> platformClass) {
        try {
            return (Platform) platformClass.getMethod("instance").invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IllegalStateException("unable to initialize platform instance", e);
        }
    }
}
