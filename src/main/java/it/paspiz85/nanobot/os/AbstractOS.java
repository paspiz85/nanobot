package it.paspiz85.nanobot.os;

import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
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
 * @author v-ppizzuti
 *
 */
public abstract class AbstractOS implements OS, Constants {

    private final Logger logger = Logger.getLogger(getClass().getName());

    protected final boolean compareColor(final Color c1, final Color c2, final int var) {
        return compareColor(c1.getRGB(), c2.getRGB(), var);
    }

    @Override
    public final boolean compareColor(final int c1, final int c2, final int var) {
        final int r1 = c1 >> 16 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b1 = c1 >> 0 & 0xFF;
        final int b2 = c2 >> 0 & 0xFF;
        return !(Math.abs(r1 - r2) > var || Math.abs(g1 - g2) > var || Math.abs(b1 - b2) > var);
    }

    @Override
    public final void leftClick(final Clickable clickable, final boolean randomize) throws InterruptedException {
        leftClick(clickable.getPoint(), randomize);
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
    public final BufferedImage screenshot(final Area area) {
        return screenshot(area.getP1(), area.getP2());
    }

    protected abstract BufferedImage screenshot(Point p1, Point p2);

    @Override
    public final void sleepRandom(final int sleepInMs) throws InterruptedException {
        final int time = sleepInMs + RANDOM.nextInt(sleepInMs);
        logger.fine("Sleeping for " + time + " ms.");
        Thread.sleep(time);
    }
}
