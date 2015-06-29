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
        return findCenterArea(Area.ATTACK_BUTTON, "attack.png");
    }

    private Point findCenterArea(final Area area, final String resource) {
        final BufferedImage image = screenShot(area);
        Point result = null;
        final Rectangle rectangle = findArea(image, getClass().getResource(resource));
        if (rectangle != null) {
            int x = rectangle.getLocation().x + (int) (rectangle.getWidth() / 2);
            int y = rectangle.getLocation().y + (int) (rectangle.getHeight() / 2);
            result = new Point(x, y);
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
