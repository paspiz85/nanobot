package it.paspiz85.nanobot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility to manage configuration.
 *
 * @author paspiz85
 *
 */
public final class Config {

    private static Config instance;

    public static Config instance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private Properties properties;

    private Config() {
    }

    public Properties getProperties() {
        if (properties == null) {
            final Properties props = new Properties();
            try (InputStream in = Utils.getParentResource(Config.class, "config.properties").openStream()) {
                props.load(in);
            } catch (final IOException e) {
                throw new ExceptionInInitializerError(e);
            }
            properties = props;
        }
        return properties;
    }

    public String getProperty(final String name) {
        return getProperties().getProperty(name);
    }
}
