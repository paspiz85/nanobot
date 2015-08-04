package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;

/**
 * Begin battle mode screen.
 *
 * @author paspiz85
 *
 */
public class BattleBeginScreen extends Screen {

    private static final Point BUTTON_SHIELD_DISABLE = getPoint("point.button.shield_disable");

    private static final Pixel BUTTON_FIND_MATCH = new Pixel(148, 529, new Color(0xD84B00));

    BattleBeginScreen() {
    }

    public Pixel getButtonFindMatch() {
        return BUTTON_FIND_MATCH;
    }

    public Point getButtonShieldDisable() {
        return BUTTON_SHIELD_DISABLE;
    }

    @Override
    public boolean isDisplayed() {
        return platform.matchColoredPoint(getButtonFindMatch());
    }
}
