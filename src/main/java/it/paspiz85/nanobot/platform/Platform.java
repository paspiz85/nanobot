package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.platform.win32.Win32Platform;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.ColoredPoint;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * This wraps Operating System functionalities.
 *
 * @author paspiz85
 *
 */
public interface Platform {

    int WIDTH = 860;

    int HEIGHT = 720;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Supplier<Class<?>> CLASS_FINDER = new Supplier() {

        private Class<?> cache;

        @Override
        public Class<?> get() {
            if (cache == null) {
                try {
                    final String className = System.getProperty(CLASS_PROPERTY, Win32Platform.class.getName());
                    cache = Class.forName(className);
                } catch (final ClassNotFoundException e) {
                    throw new IllegalStateException("unable to initialize OS class", e);
                }
            }
            return cache;
        }
    };

    String CLASS_PROPERTY = Platform.class.getName();

    static Platform instance() {
        try {
            return (Platform) CLASS_FINDER.get().getMethod("instance").invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IllegalStateException("unable to initialize OS", e);
        }
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

    void zoomUp() throws InterruptedException;
}