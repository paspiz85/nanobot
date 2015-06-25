package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.logic.Setup;
import it.paspiz85.nanobot.util.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Shell {

    private static final int EXIT_CODE_1 = 1;

    private static final int EXIT_CODE_2 = 2;

    private static final int EXIT_CODE_3 = 3;

    private static final Logger LOGGER = Logger.getLogger(Shell.class.getName());

    public static void main(final String[] args) {
        Logging.initialize();
        try {
            Setup.instance().setup();
            Looper.instance().start();
        } catch (final InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(EXIT_CODE_1);
        } catch (final BotConfigurationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(EXIT_CODE_2);
        } catch (final BotException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(EXIT_CODE_3);
        } finally {
            Logging.close();
        }
    }

    private Shell() {
    }
}
