package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;

/**
 * Main mode screen.
 *
 * @author paspiz85
 *
 */
public final class ManageTroopsScreen extends Screen {

    private static final Area AREA_EROES = getArea("area.eroes");

    private static final Area AREA_TROOPS = getArea("area.troops");

    private static final Point BUTTON_TRAIN_NEXT = getPoint("point.button.train.next");

    private static final Area AREA_BUTTON_TRAIN_CLOSE = getArea("area.button.train.close");

    private static final Area AREA_ICON_CAMP_FULL = getArea("area.icon.camp.full");

    private Point buttonTrainClose;

    ManageTroopsScreen() {
    }

    public Boolean areCampsFull() {
        return isDisplayedByImageSearch(this::searchIconCampFull);
    }

    public Point getButtonTrainClose() {
        if (buttonTrainClose == null) {
            searchButtonTrainClose();
        }
        return buttonTrainClose;
    }

    public Point getButtonTrainNext() {
        return BUTTON_TRAIN_NEXT;
    }

    @Override
    public boolean isDisplayed() {
        return isDisplayedByImageSearch(this::searchButtonTrainClose);
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
        if (searchImageCenter(imageEroes, getClass().getResource("king.png")) != null) {
            troopsInfo.add(Troop.BARBARIAN_KING, 1);
        }
        if (searchImageCenter(imageEroes, getClass().getResource("queen.png")) != null) {
            troopsInfo.add(Troop.ARCHER_QUEEN, 1);
        }
        return troopsInfo;
    }

    private void searchButtonTrainClose() {
        buttonTrainClose = searchImage(getClass().getResource("button_train_close.png"), AREA_BUTTON_TRAIN_CLOSE);
    }

    private void searchIconCampFull() {
        searchImage(getClass().getResource("icon_camp_full.png"), AREA_ICON_CAMP_FULL);
    }
}
