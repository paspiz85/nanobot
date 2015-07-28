package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.platform.mac.BlueStacksMacPlatform;
import it.paspiz85.nanobot.platform.win32.BlueStacksWinPlatform;
import it.paspiz85.nanobot.util.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for {@link Platform}.
 *
 * @author paspiz85
 *
 */
public final class PlatformResolver implements Supplier<Platform> {

    private static final String IS_SUPPORTED_METHOD = "isSupported";

    private static final String INSTANCE_METHOD = "instance";

    public static PlatformResolver instance() {
        return Utils.singleton(PlatformResolver.class, () -> new PlatformResolver());
    }

    private Platform platform;

    private final List<Class<? extends Platform>> platforms = new ArrayList<>();

    private Class<? extends Platform> preferredPlatform;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private PlatformResolver() {
        platforms.add(BlueStacksWinPlatform.class);
        platforms.add(BlueStacksMacPlatform.class);
    }

    @Override
    public Platform get() {
        if (platform == null) {
            Class<? extends Platform> platformClass = preferredPlatform;
            if (platformClass == null) {
                final List<Class<? extends Platform>> supportedPlatforms = getSupportedPlatforms();
                if (!supportedPlatforms.isEmpty()) {
                    platformClass = supportedPlatforms.get(0);
                }
            }
            if (platformClass == null) {
                platformClass = UnsupportedPlatform.class;
            }
            platform = getInstance(platformClass);
        }
        return platform;
    }

    private Platform getInstance(final Class<? extends Platform> platformClass) {
        try {
            return (Platform) platformClass.getMethod(INSTANCE_METHOD).invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IllegalStateException("unable to initialize platform instance", e);
        }
    }

    public List<Class<? extends Platform>> getPlatforms() {
        return platforms;
    }

    public Class<? extends Platform> getPreferredPlatform() {
        return preferredPlatform;
    }

    public List<Class<? extends Platform>> getSupportedPlatforms() {
        final List<Class<? extends Platform>> supportedPlatforms = new ArrayList<>();
        for (final Class<? extends Platform> platform : platforms) {
            try {
                platform.getMethod(INSTANCE_METHOD);
                if ((boolean) platform.getMethod(IS_SUPPORTED_METHOD).invoke(null)) {
                    supportedPlatforms.add(platform);
                }
            } catch (final Exception e) {
                logger.log(Level.SEVERE, "Platform not supperted: " + platform.getName(), e);
            }
        }
        return supportedPlatforms;
    }

    public void setPreferredPlatform(final Class<? extends Platform> preferredPlatform) {
        this.preferredPlatform = preferredPlatform;
        this.platform = null;
    }
}
