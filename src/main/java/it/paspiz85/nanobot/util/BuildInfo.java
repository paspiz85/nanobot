package it.paspiz85.nanobot.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Build information retriever.
 *
 * @author paspiz85
 *
 */
public final class BuildInfo implements Constants {

    private static BuildInfo instance;

    public static BuildInfo instance() {
        if (instance == null) {
            instance = new BuildInfo();
        }
        return instance;
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Manifest manifest;

    private BuildInfo() {
    }

    private Manifest getManifest() {
        if (manifest == null) {
            try {
                final Enumeration<URL> resources = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
                while (resources.hasMoreElements()) {
                    try (InputStream in = resources.nextElement().openStream()) {
                        final Manifest man = new Manifest(in);
                        if (NAME.toLowerCase().equals(man.getMainAttributes().getValue("Build-Name"))) {
                            manifest = man;
                            break;
                        }
                    }
                }
            } catch (final IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return manifest;
    }

    public String getName() {
        String buildName;
        final String version = getManifest().getMainAttributes().getValue("Build-Version");
        if (version == null) {
            buildName = "Development build";
        } else {
            buildName = NAME + " v" + version;
        }
        return buildName;
    }

    public String getTimestamp() {
        return getManifest().getMainAttributes().getValue("Build-Timestamp");
    }
}
