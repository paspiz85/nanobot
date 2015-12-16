package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

/**
 * Begin battle mode screen.
 *
 * @author paspiz85
 *
 */
public class BattleBeginScreen extends Screen {

    private static final Point BUTTON_SHIELD_DISABLE = getPoint("point.button.shield_disable");

    private static final Area AREA_BUTTON_FIND_MATCH = getArea("area.button.find.match");

    private Point buttonFindMatch;

    BattleBeginScreen() {
    }

    public Point getButtonFindMatch() {
        if (buttonFindMatch == null) {
            searchButtonFindMatch();
        }
        return buttonFindMatch;
    }

    public Point getButtonShieldDisable() {
        return BUTTON_SHIELD_DISABLE;
    }

    @Override
    public boolean isDisplayed() {
        return isDisplayedByImageSearch(this::searchButtonFindMatch);
    }

    private void searchButtonFindMatch() {
        buttonFindMatch = searchImage(getClass().getResource("button_find_match.png"), AREA_BUTTON_FIND_MATCH);
    }
}
