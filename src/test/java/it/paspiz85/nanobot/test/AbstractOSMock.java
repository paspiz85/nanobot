package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.os.AbstractOS;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

public abstract class AbstractOSMock extends AbstractOS {

    @Override
    protected Color getColor(final Point point) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void leftClick(final Point point, final boolean randomize) throws InterruptedException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    protected BufferedImage screenshot(final Point p1, final Point p2) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setup() throws BotConfigurationException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setupResolution(final BooleanSupplier setupResolution) throws BotConfigurationException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Point waitForClick() throws InterruptedException, BotConfigurationException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void zoomUp() throws InterruptedException {
        throw new IllegalStateException("not implemented");
    }
}
