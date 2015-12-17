package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Point;

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
        return searchButtonPlayGame(true);
    }

    @Override
    public boolean isDisplayed() {
        return isDisplayedByImageSearch(this::searchButtonPlayGame);
    }

    private Point searchButtonPlayGame(boolean debug) {
        return searchImage(getClass().getResource("coc.png"), null, debug);
    }
}
