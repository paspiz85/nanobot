package it.paspiz85.nanobot.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic utilities.
 *
 * @author paspiz85
 *
 */
public final class Utils {

    public static final Random RANDOM = new Random();

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static URL getParentResource(Class<?> c, String resource) {
        String name = c.getName();
        name = name.substring(0, name.lastIndexOf("."));
        name = name.substring(0, name.lastIndexOf(".") + 1);
        name = name.replace(".", "/");
        return c.getClassLoader().getResource(name + resource);
    }

    public static void withClasspathFolder(final URI uri, final Consumer<Path> pathConsumer) {
        try {
            if ("jar".equals(uri.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    final String schemeSpecificPart = uri.getSchemeSpecificPart();
                    final Path path = fileSystem.getPath(schemeSpecificPart.substring(
                            schemeSpecificPart.indexOf("!") + 1, schemeSpecificPart.length()));
                    pathConsumer.accept(path);
                }
            } else {
                final Path path = Paths.get(uri);
                pathConsumer.accept(path);
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, String.format("Unable to open uri %s: %s", uri, e.getMessage()), e);
        }
    }

    private Utils() {
    }
}
