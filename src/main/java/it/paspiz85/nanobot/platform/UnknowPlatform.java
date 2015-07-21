package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

/**
 * Default platform if recognition fails.
 *
 * @author paspiz85
 *
 */
public class UnknowPlatform extends AbstractPlatform {

    private static final String MESSAGE = "Platform not supported";

    public static UnknowPlatform instance() {
        return Utils.singleton(UnknowPlatform.class, () -> new UnknowPlatform());
    }

    protected UnknowPlatform() {
    }

    @Override
    protected Color getColor(final Point point) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public void leftClick(final Point point, final boolean randomize) throws InterruptedException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected BufferedImage screenshot(final Point p1, final Point p2) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public void setup() throws BotConfigurationException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public void setupResolution(final BooleanSupplier setupResolution) throws BotConfigurationException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public Point waitForClick() {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public void zoomUp() throws InterruptedException {
        throw new UnsupportedOperationException(MESSAGE);
    }
}
