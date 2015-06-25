package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.win32.OS;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public final class MainScreenParser extends AbstractParser {

    MainScreenParser() {
    }

    public Point findTrainButton() {
        Point result = null;
        final BufferedImage image = OS.instance().screenShot(Area.BARRACKS_BUTTONS);
        final Rectangle rectangle = findArea(image, getClass().getResource("train.png"));
        if (rectangle != null) {
            result = rectangle.getLocation();
            result.x += Area.BARRACKS_BUTTONS.getX1();
            result.y += Area.BARRACKS_BUTTONS.getY1();
        }
        return result;
    }
}
