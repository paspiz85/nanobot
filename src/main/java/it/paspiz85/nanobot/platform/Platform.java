package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.ColoredPoint;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

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

    int HEIGHT = 720;

    int WIDTH = 860;

    static Platform instance() {
        return Utils.singleton(Platform.class, PlatformResolver.instance());
    }

    boolean compareColor(Color c1, Color c2, int var);

    BufferedImage getSubimage(BufferedImage image, Area area);

    BufferedImage getSubimage(BufferedImage image, Point p1, Point p2);

    void leftClick(Point point, boolean randomize) throws InterruptedException;

    boolean matchColoredPoint(ColoredPoint point);

    File saveImage(BufferedImage img, String... filePathRest);

    File saveScreenshot(Area area, String... filePathRest);

    File saveScreenshot(String... filePathRest);

    BufferedImage screenshot();

    BufferedImage screenshot(Area area);

    void setup() throws BotConfigurationException;

    void setupResolution(BooleanSupplier setupResolution) throws BotConfigurationException;

    /**
     * Sleep random interval between sleepInMs and 2*sleepInMs.
     *
     * @param sleepInMs
     *            minimum sleep time.
     * @throws InterruptedException
     *             thread interrupted.
     */
    void sleepRandom(int sleepInMs) throws InterruptedException;

    Point waitForClick();

    void zoomUp() throws InterruptedException;
}
