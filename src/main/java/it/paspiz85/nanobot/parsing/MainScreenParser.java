package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Point;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Parser for main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreenParser extends Parser {

    MainScreenParser() {
    }

    public Point findAttackButton() {
        Point result = null;
        final BufferedImage image = screenShot(Area.ATTACK_BUTTON);
        final Rectangle rectangle = findArea(image, getClass().getResource("attack.png"));
        if (rectangle != null) {
            result = new Point(rectangle.getLocation().x, rectangle.getLocation().y);
        }
        return result;
    }

    public Point findTrainButton() {
        Point result = null;
        final BufferedImage image = screenShot(Area.BARRACKS_BUTTONS);
        final Rectangle rectangle = findArea(image, getClass().getResource("train.png"));
        if (rectangle != null) {
            result = new Point(rectangle.getLocation().x + Area.BARRACKS_BUTTONS.getP1().x(), rectangle.getLocation().y
                    + Area.BARRACKS_BUTTONS.getP1().y());
        }
        return result;
    }
}
