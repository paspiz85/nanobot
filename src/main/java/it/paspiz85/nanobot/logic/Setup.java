package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.logging.Logger;

/*

 */
/**
 * Steps to setup bot for interacting with game in BlueStacks.
 *
 * @author paspiz85
 *
 */
public final class Setup implements Constants {

    private static Setup instance;

    public static Setup instance() {
        if (instance == null) {
            instance = new Setup();
        }
        return instance;
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    private Setup() {
    }

    public void setup() throws BotConfigurationException, InterruptedException {
        if (!OS.instance().name().toLowerCase(Locale.ROOT).contains("windows")) {
            throw new BotConfigurationException("Bot is only available for Windows OS.");
        }
        OS.instance().setup();
        // setup barracks
        logger.info("Setting up Barracks...");
        setupBarracks();
    }

    private void setupBarracks() throws BotConfigurationException, InterruptedException {
        if (Settings.instance().getFirstBarrackPosition() == null) {
            OS.instance().zoomUp();
            final boolean confirmed = OS.instance().confirmationBox(
                    "You must configure the location "
                            + "of first Barracks. First Barracks is the leftmost one when you \n"
                            + "scroll through your barracks via orange next arrow on the right. For example, if you \n"
                            + "have 4 barracks, when you select the first one and click 'Train Troops', all \n"
                            + "3 'next' views should also be barracks.\n\n"
                            + "Click Yes to start configuration and click on your first barracks. Do \n"
                            + "NOT click anything else in between. Click Yes and click barracks. \n\n"
                            + "Make sure you are max zoomed out.", "Barracks configuration");
            if (!confirmed) {
                throw new BotConfigurationException("Cannot proceed without barracks");
            }
            OS.instance().waitForClick(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    Settings.instance().setFirstBarrackPosition(new Point(e.getX(), e.getY()));
                }
            });
        }
    }
}
