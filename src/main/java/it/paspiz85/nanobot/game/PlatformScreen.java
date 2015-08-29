package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;

/**
 * Platform mode screen.
 *
 * @author paspiz85
 *
 */
public class PlatformScreen extends Screen {

    PlatformScreen() {
    }

    public Point getButtonPlayGame() {
        return searchButtonPlayGame();
    }

    @Override
    public boolean isDisplayed() {
        return searchButtonPlayGame() != null;
    }

    public Point searchButtonPlayGame() {
        final BufferedImage image = platform.screenshot();
        return searchImageCenter(image, getClass().getResource("coc.png"));
    }
}
