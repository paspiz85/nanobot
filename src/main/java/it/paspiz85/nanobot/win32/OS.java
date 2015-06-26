package it.paspiz85.nanobot.win32;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;

import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * This wraps Operating System functionalities.
 *
 * @author paspiz85
 *
 */
public interface OS {

    static final Random RANDOM = new Random();

    static OS instance() {
        return Win32OS.instance();
    }

    boolean compareColor(int c1, int c2, int var);

    boolean confirmationBox(String msg, String title);

    boolean isClickableActive(Clickable clickable);

    void leftClick(Clickable clickable, boolean randomize) throws InterruptedException;

    void leftClick(Point point, boolean randomize) throws InterruptedException;

    String name();

    File saveScreenShot(Area area, String filePathFirst, String... filePathRest) throws IOException;

    BufferedImage screenShot(Area area);

    void setup() throws BotConfigurationException;

    /**
     * Sleep random interval between sleepInMs and 2*sleepInMs.
     *
     * @param sleepInMs
     *            minimum sleep time.
     * @throws InterruptedException
     *             thread interrupted.
     */
    void sleepRandom(int sleepInMs) throws InterruptedException;

    void sleepTillClickableIsActive(Clickable clickable) throws InterruptedException;

    void waitForClick(MouseAdapter mouseAdapter) throws InterruptedException, BotConfigurationException;

    void zoomUp() throws InterruptedException;
}
