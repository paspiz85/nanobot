package it.paspiz85.nanobot.platform;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Size;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * Default platform if recognition fails.
 *
 * @author paspiz85
 *
 */
public class UnsupportedPlatform extends AbstractPlatform {

    private static final String MESSAGE = "Platform not supported";

    public static UnsupportedPlatform instance() {
        return Utils.singleton(UnsupportedPlatform.class, () -> new UnsupportedPlatform());
    }

    protected UnsupportedPlatform() {
    }

    @Override
    protected void doApplySize(final Size resolution) throws BotConfigurationException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected void doLeftClick(final Point point) throws InterruptedException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected BufferedImage doScreenshot(final Point p1, final Point p2) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected Size getActualSize() {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected Color getColor(final Point point) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    public Size getExpectedSize() {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected boolean registerForClick(final Consumer<Point> clickConsumer) {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected void setup() throws BotConfigurationException {
        throw new UnsupportedOperationException(MESSAGE);
    }

    @Override
    protected void singleZoomUp() throws InterruptedException {
        throw new UnsupportedOperationException(MESSAGE);
    }
}
