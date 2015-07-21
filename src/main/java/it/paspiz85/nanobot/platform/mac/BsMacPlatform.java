package it.paspiz85.nanobot.platform.mac;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.platform.AbstractPlatform;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

/**
 * TODO.
 *
 * @author paspiz85
 *
 */
public class BsMacPlatform extends AbstractPlatform {

    private static final String MESSAGE = "Platform not supported";

    public static BsMacPlatform instance() {
        return Utils.singleton(BsMacPlatform.class, () -> new BsMacPlatform());
    }

    protected BsMacPlatform() {
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
