package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.logic.OS;
import it.paspiz85.nanobot.util.Point;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Parser for main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreenParser extends AbstractParser {

    MainScreenParser() {
    }

    public Point findTrainButton() {
        Point result = null;
        final BufferedImage image = OS.instance().screenShot(Area.BARRACKS_BUTTONS);
        final Rectangle rectangle = findArea(image, getClass().getResource("train.png"));
        if (rectangle != null) {
            result = new Point(rectangle.getLocation().x + Area.BARRACKS_BUTTONS.getP1().x(), rectangle.getLocation().y
                    + Area.BARRACKS_BUTTONS.getP1().y());
        }
        return result;
    }
}
