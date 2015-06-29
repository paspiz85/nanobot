package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.util.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for running in batch mode.
 *
 * @author paspiz85
 *
 */
public final class Shell {

    private static final Logger LOGGER = Logger.getLogger(Shell.class.getName());

    public static void main(final String[] args) {
        Logging.initialize();
        try {
            final Model model = Model.instance();
            model.initialize(() -> false, () -> null, () -> {
            });
            model.start();
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(1);
        } finally {
            Logging.close();
        }
    }

    private Shell() {
    }
}
