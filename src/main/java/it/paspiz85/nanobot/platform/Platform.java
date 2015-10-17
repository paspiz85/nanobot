package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Size;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.BooleanSupplier;

/**
 * This wraps Platform functionalities.
 *
 * @author paspiz85
 *
 */
public interface Platform {

    Size GAME_SIZE = new Size(860, 720 - 47);

    static Platform instance() {
        return PlatformResolver.instance().get();
    }

    boolean compareColor(Color c1, Color c2, int var);

    Size getExpectedSize();

    BufferedImage getSubimage(BufferedImage image, Area area);

    BufferedImage getSubimage(BufferedImage image, Point p1, Point p2);

    /**
     * Check emulator windows and setup the platform to working with that.
     *
     * @throws BotConfigurationException
     */
    void init() throws BotConfigurationException;

    /**
     * Check emulator windows and setup the platform to working with that.
     *
     * @param autoAdjustResolution
     *            supplier to decide if auto adjust game resolution.
     * @throws BotConfigurationException
     */
    void init(BooleanSupplier autoAdjustResolution) throws BotConfigurationException;

    void keyPress(int keyCode, boolean shifted) throws InterruptedException;

    /**
     * Do a left click on game. Randomize is true.
     *
     * @param point
     *            coordinates of click.
     * @throws InterruptedException
     */
    void leftClick(Point point) throws InterruptedException;

    /**
     * Do a left click on game.
     *
     * @param point
     *            coordinates of click.
     * @param randomize
     * @throws InterruptedException
     */
    void leftClick(Point point, boolean randomize) throws InterruptedException;

    boolean matchColoredPoint(Pixel point);

    File saveImage(BufferedImage img, String... filePathRest);

    File saveScreenshot(Area area, String... filePathRest);

    File saveScreenshot(String... filePathRest);

    BufferedImage screenshot();

    BufferedImage screenshot(Area area);

    /**
     * Sleep random interval between sleepInMs and 2*sleepInMs.
     *
     * @param sleepInMs
     *            minimum sleep time.
     * @throws InterruptedException
     *             thread interrupted.
     */
    void sleepRandom(int sleepInMs) throws InterruptedException;

    Point waitForClick() throws InterruptedException;

    void write(String str) throws InterruptedException;

    /**
     * Change game view to max zoom level.
     *
     * @throws InterruptedException
     */
    void zoomUp() throws InterruptedException;
}
