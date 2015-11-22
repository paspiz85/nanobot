package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Main mode screen.
 *
 * @author paspiz85
 *
 */
public final class ManageTroopsScreen extends Screen {

    private static final Pixel POINT_CAMPS_FULL = new Pixel(404, 162, new Color(0xE27F81));

    private static final Area AREA_EROES = getArea("area.eroes");

    private static final Area AREA_TROOPS = getArea("area.troops");

    private static final Point BUTTON_TRAIN_NEXT = getPoint("point.button.train.next");

    private static final Area AREA_BUTTON_TRAIN_CLOSE = getArea("area.button.train.close");

    private Point buttonTrainClose;

    ManageTroopsScreen() {
    }

    public Boolean areCampsFull() {
        return platform.matchColoredPoint(POINT_CAMPS_FULL);
    }

    public Point getButtonTrainClose() {
        if (buttonTrainClose == null) {
            buttonTrainClose = searchButtonTrainClose();
        }
        return buttonTrainClose;
    }

    public Point getButtonTrainNext() {
        return BUTTON_TRAIN_NEXT;
    }

    @Override
    public boolean isDisplayed() {
        return searchButtonTrainClose() != null;
    }

    public TroopsInfo parseTroopsInfo() {
        final BufferedImage image = platform.screenshot();
        final BufferedImage imageTroops = platform.getSubimage(image, AREA_TROOPS);
        Point start = new Point(9, 4);
        // start = new Point(28, 4);
        // start = new Point(28+63, 4);
        // start = new Point(28+63+62, 4);
        final TroopsInfo troopsInfo = new TroopsInfo();
        while (true) {
            final Integer n = parseNumber(imageTroops, 3, start, 46);
            if (n == null) {
                break;
            }
            troopsInfo.add(Troop.BARB, n);
            start = new Point(start.x() + 62, start.y());
        }
        final BufferedImage imageEroes = platform.getSubimage(image, AREA_EROES);
        if (searchImage(imageEroes, getClass().getResource("king.png")) != null) {
            troopsInfo.add(Troop.BARBARIAN_KING, 1);
        }
        if (searchImage(imageEroes, getClass().getResource("queen.png")) != null) {
            troopsInfo.add(Troop.ARCHER_QUEEN, 1);
        }
        return troopsInfo;
    }

    public Point searchButtonTrainClose() {
        final BufferedImage image = platform.screenshot(AREA_BUTTON_TRAIN_CLOSE);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_train_close.png")),
                AREA_BUTTON_TRAIN_CLOSE.getEdge1());
    }
}
