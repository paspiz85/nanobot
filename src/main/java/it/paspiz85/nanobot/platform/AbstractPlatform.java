package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Size;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Abstract implementation of {@link Platform}.
 *
 * @author paspiz85
 *
 */
public abstract class AbstractPlatform implements Platform {

    private static final Area FULLSCREEN = Area.bySize(new Point(0, 0), GAME_SIZE);

    private static final String IMG_FOLDER = "img";

    private static final Object WAIT_FOR_CLICK_SYNC = new Object();

    protected final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public final boolean compareColor(final Color c1, final Color c2, final int var) {
        return compareColor(c1.getRGB(), c2.getRGB(), var);
    }

    private boolean compareColor(final int c1, final int c2, final int var) {
        final int r1 = c1 >> 16 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b1 = c1 >> 0 & 0xFF;
        final int b2 = c2 >> 0 & 0xFF;
        return !(Math.abs(r1 - r2) > var || Math.abs(g1 - g2) > var || Math.abs(b1 - b2) > var);
    }

    protected void doActivate() {
    }

    protected abstract void doApplySize(Size size) throws BotConfigurationException;

    protected abstract void doKeyPress(final int keyCode, final boolean shifted) throws InterruptedException;

    /**
     * Do a left click in the game.
     *
     * @param point
     *            coordinates of click.
     * @throws InterruptedException
     */
    protected abstract void doLeftClick(final Point point) throws InterruptedException;

    /**
     * Pick a screenshot from game.
     *
     * @param p1
     *            top left corner of screenshot.
     * @param p2
     *            bottom right corner of screenshot.
     * @return image containing the screenshot.
     */
    protected abstract BufferedImage doScreenshot(Point p1, Point p2);

    protected abstract void doSingleZoomUp() throws InterruptedException;

    protected abstract void doWrite(String s) throws InterruptedException;

    protected abstract Size getActualSize();

    /**
     * Given a point return the color of pixel.
     *
     * @param point
     *            coordinates of pixel.
     * @return color of a pixel.
     */
    protected abstract Color getColor(Point point);

    @Override
    public final BufferedImage getSubimage(final BufferedImage image, final Area area) {
        return getSubimage(image, area.getEdge1(), area.getEdge2());
    }

    @Override
    public final BufferedImage getSubimage(final BufferedImage image, final Point p1, final Point p2) {
        final int x1 = p1.x();
        final int y1 = p1.y();
        final int x2 = p2.x();
        final int y2 = p2.y();
        return image.getSubimage(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public final void init() throws BotConfigurationException {
        init(() -> false);
    }

    @Override
    public final void init(final BooleanSupplier autoAdjustResolution) throws BotConfigurationException {
        doActivate();
        logger.log(Level.CONFIG, "Setting up platform...");
        setup();
        setupResolution(autoAdjustResolution);
        logger.log(Level.CONFIG, "Setup is successful");
    }

    @Override
    public final void keyPress(final int keyCode, final boolean shifted) throws InterruptedException {
        doActivate();
        doKeyPress(keyCode, shifted);
    }

    @Override
    public final void leftClick(final Point point) throws InterruptedException {
        leftClick(point, true);
    }

    @Override
    public final void leftClick(final Point point, final boolean randomize) throws InterruptedException {
        if (point == null) {
            throw new IllegalArgumentException("point not provided");
        }
        doActivate();
        Point p = point;
        logger.log(Level.FINER, "Clicking " + p);
        // randomize coordinates little bit
        if (randomize) {
            p = new Point(p.x() - 1 + Utils.RANDOM.nextInt(3), p.y() - 1 + Utils.RANDOM.nextInt(3));
        }
        doLeftClick(p);
    }

    @Override
    public boolean matchColoredPoint(final Pixel point) {
        if (point == null) {
            throw new IllegalArgumentException("point not provided");
        }
        final Color actualColor = getColor(point);
        final boolean result = compareColor(point.getColor(), actualColor, 5);
        return result;
    }

    /**
     * Register for a click on the game.
     *
     * @param clickConsumer
     *            listener that receive the click.
     * @return true if registration is OK, false otherwise.
     */
    protected abstract boolean registerForClick(Consumer<Point> clickConsumer);

    @Override
    public File saveImage(final BufferedImage img, final String... filePathRest) {
        File result = null;
        try {
            result = saveImageInternal(img, IMG_FOLDER, filePathRest);
        } catch (final IOException e1) {
            logger.log(Level.SEVERE, e1.getMessage(), e1);
        }
        return result;
    }

    private File saveImageInternal(final BufferedImage img, final String filePathFirst, final String... filePathRest)
            throws IOException {
        final Path path = Paths.get(filePathFirst, filePathRest).toAbsolutePath();
        String fileName = path.getFileName().toString();
        if (!path.getFileName().toString().toLowerCase().endsWith(".png")) {
            fileName = path.getFileName().toString() + ".png";
        }
        final File file = new File(path.getParent().toString(), fileName);
        if (!file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        logger.log(Level.FINE, "Saving image at " + file.getAbsolutePath());
        ImageIO.write(img, "png", file);
        logger.log(Level.INFO, "Saved image at " + file.getAbsolutePath());
        return file;
    }

    @Override
    public final File saveScreenshot(final Area area, final String... filePathRest) {
        final BufferedImage img = screenshot(area);
        return saveImage(img, filePathRest);
    }

    @Override
    public final File saveScreenshot(final String... filePathRest) {
        final BufferedImage img = screenshot();
        return saveImage(img, filePathRest);
    }

    @Override
    public final BufferedImage screenshot() {
        return screenshot(null);
    }

    @Override
    public final BufferedImage screenshot(final Area area) {
        doActivate();
        BufferedImage result;
        if (area == null) {
            result = doScreenshot(FULLSCREEN.getEdge1(), FULLSCREEN.getEdge2());
        } else {
            result = doScreenshot(area.getEdge1(), area.getEdge2());
        }
        return result;
    }

    protected abstract void setup() throws BotConfigurationException;

    private void setupResolution(final BooleanSupplier autoAdjustResolution) throws BotConfigurationException {
        logger.log(Level.INFO, "Checking plaftorm resolution...");
        try {
            final Size bsActualSize = getActualSize();
            final Size bsExpectedSize = getExpectedSize();
            if (!bsExpectedSize.equals(bsActualSize)) {
                logger.warning(String.format("Platform resolution is %s", bsActualSize.toString()));
                if (!autoAdjustResolution.getAsBoolean()) {
                    throw new BotConfigurationException("Re-run when resolution is fixed");
                }
                doApplySize(bsExpectedSize);
            }
        } catch (final BotConfigurationException e) {
            throw e;
        } catch (final Exception e) {
            throw new BotConfigurationException("Unable to change resolution. Do it manually", e);
        }
    }

    @Override
    public final void sleepRandom(final int sleepInMs) throws InterruptedException {
        final int time = sleepInMs + Utils.RANDOM.nextInt(sleepInMs);
        logger.log(Level.FINER, "Sleeping for " + time + " ms");
        Thread.sleep(time);
    }

    @Override
    public final Point waitForClick() throws InterruptedException {
        final Point[] result = new Point[1];
        final boolean[] done = new boolean[1];
        final boolean registered = registerForClick((point) -> {
            result[0] = point;
            done[0] = true;
            synchronized (WAIT_FOR_CLICK_SYNC) {
                WAIT_FOR_CLICK_SYNC.notify();
            }
        });
        if (registered) {
            logger.log(Level.INFO, "Waiting for user to click");
            synchronized (WAIT_FOR_CLICK_SYNC) {
                while (!done[0]) {
                    WAIT_FOR_CLICK_SYNC.wait();
                }
            }
        }
        return result[0];
    }

    @Override
    public final void write(final String s) throws InterruptedException {
        doActivate();
        doWrite(s);
    }

    @Override
    public final void zoomUp() throws InterruptedException {
        doActivate();
        logger.log(Level.CONFIG, "Zooming out...");
        final int notch = 14;
        for (int i = 0; i < notch; i++) {
            doSingleZoomUp();
            Thread.sleep(1000);
        }
    }
}
