package it.paspiz85.nanobot.os;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.ColoredPoint;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Abstract implementation of {@link OS}.
 *
 * @author paspiz85
 *
 */
public abstract class AbstractOS implements OS, Constants {

    private static final Area FULLSCREEN = new Area(0, 0, BS_RES_X - 1, BS_RES_Y - 1);

    private final Logger logger = Logger.getLogger(getClass().getName());

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

    protected abstract Color getColor(Point point);

    @Override
    public final BufferedImage getSubimage(final BufferedImage image, final Area area) {
        return getSubimage(image, area.getP1(), area.getP2());
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
    public boolean matchColoredPoint(final ColoredPoint point) {
        final Color actualColor = getColor(point);
        return compareColor(point.getColor(), actualColor, 5);
    }

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
        logger.fine("Saving image at " + file.getAbsolutePath());
        ImageIO.write(img, "png", file);
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
        BufferedImage result;
        if (area == null) {
            result = screenshot(FULLSCREEN.getP1(), FULLSCREEN.getP2());
        } else {
            result = screenshot(area.getP1(), area.getP2());
        }
        return result;
    }

    protected abstract BufferedImage screenshot(Point p1, Point p2);

    @Override
    public final void sleepRandom(final int sleepInMs) throws InterruptedException {
        final int time = sleepInMs + RANDOM.nextInt(sleepInMs);
        logger.fine("Sleeping for " + time + " ms.");
        Thread.sleep(time);
    }
}
