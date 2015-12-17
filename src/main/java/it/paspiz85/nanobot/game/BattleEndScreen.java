package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.util.logging.Level;

/**
 * Attack mode screen.
 *
 * @author paspiz85
 *
 */
public class BattleEndScreen extends Screen {

    private static final Point BUTTON_RETURN_HOME = getPoint("point.button.end_battle.return_home");

    private static final int[] BUTTON_RETURN_HOME_COLORS = getRGBs("point.button.end_battle.return_home.color");

    private static final int[][] BUTTON_RETURN_HOME_OFFSET = getOffset("point.button.end_battle.return_home.offset");

    BattleEndScreen() {
    }

    public Point getButtonReturnHome() {
        return BUTTON_RETURN_HOME;
    }

    @Override
    public boolean isDisplayed() {
        return searchButtonReturnHome() != null;
    }

    private Point searchButtonReturnHome() {
        logger.log(Level.FINE, "search button EndBattle-ReturnHome");
        Point result = BUTTON_RETURN_HOME;
        for (int i = 0; i < BUTTON_RETURN_HOME_OFFSET.length; i++) {
            final int[] coords = BUTTON_RETURN_HOME_OFFSET[i];
            final int rgb = BUTTON_RETURN_HOME_COLORS[i];
            final Pixel point = new Pixel(coords[0], coords[1], new Color(rgb));
            if (!platform.matchColoredPoint(point)) {
                result = null;
                break;
            }
        }
        return result;
    }
}
