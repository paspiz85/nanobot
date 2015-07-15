package it.paspiz85.nanobot.util;

import java.io.IOException;
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
            try {
                final Properties props = new Properties();
                props.load(Config.class.getResourceAsStream("../config.properties"));
                properties = props;
            } catch (final IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return properties;
    }

    public String getProperty(final String name) {
        return getProperties().getProperty(name);
    }
}
