package it.paspiz85.nanobot.os;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.os.win32.Win32OS;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * This wraps Operating System functionalities.
 *
 * @author paspiz85
 *
 */
public interface OS {

    String CLASS_PROPERTY = OS.class.getName();

    Random RANDOM = new Random();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    Supplier<Class<?>> CLASS_FINDER = new Supplier() {

        private Class<?> cache;

        @Override
        public Class<?> get() {
            if (cache == null) {
                try {
                    final String className = System.getProperty(CLASS_PROPERTY, Win32OS.class.getName());
                    cache = Class.forName(className);
                } catch (final ClassNotFoundException e) {
                    throw new IllegalStateException("unable to initialize OS class", e);
                }
            }
            return cache;
        }
    };

    static OS instance() {
        try {
            return (OS) CLASS_FINDER.get().getMethod("instance").invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new IllegalStateException("unable to initialize OS", e);
        }
    }

    boolean compareColor(int c1, int c2, int var);

    boolean isClickableActive(Clickable clickable);

    void leftClick(Clickable clickable, boolean randomize) throws InterruptedException;

    void leftClick(Point point, boolean randomize) throws InterruptedException;

    File saveScreenshot(Area area, String... filePathRest);

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

    void sleepTillClickableIsActive(Clickable clickable) throws InterruptedException;

    Point waitForClick() throws InterruptedException, BotConfigurationException;

    void zoomUp() throws InterruptedException;

    File saveImage(BufferedImage img, String... filePathRest);
}
