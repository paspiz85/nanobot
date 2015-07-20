package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.platform.win32.Win32Platform;
import it.paspiz85.nanobot.util.OS;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for {@link Platform}.
 *
 * @author paspiz85
 *
 */
public final class PlatformFactory {

    private static PlatformFactory instance;

    public static final String CLASS_PROPERTY = Platform.class.getName();

    public static PlatformFactory instance() {
        if (instance == null) {
            instance = new PlatformFactory();
        }
        return instance;
    }

    private Platform platform;

    private final Map<OS.Category, Class<? extends Platform>> platformClassMap = new HashMap<>();

    private PlatformFactory() {
        platformClassMap.put(OS.Category.WINDOWS, Win32Platform.class);
    }

    public Platform get() {
        if (platform == null) {
            try {
                Class<? extends Platform> platformaClass = platformClassMap.get(OS.getCategory());
                if (platformaClass == null) {
                    platformaClass = UnknowPlatform.class;
                }
                final String className = System.getProperty(CLASS_PROPERTY, platformaClass.getName());
                if (className == null) {
                    throw new IllegalStateException("Bot is not available for your OS.");
                }
                platform = (Platform) Class.forName(className).getMethod("instance").invoke(null);
            } catch (final ClassNotFoundException e) {
                throw new IllegalStateException("unable to initialize OS class", e);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                throw new IllegalStateException("unable to initialize OS instance", e);
            }
        }
        return platform;
    }
}
