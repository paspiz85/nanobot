package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;

/**
 * Parser for main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreenParser extends Parser {

    private static final Area AREA_BUTTON_TROOPS = getArea("area.button.troops");

    private static final Area AREA_BUTTON_TRAIN_CLOSE = getArea("area.button.train.close");

    private static final Area AREA_BUTTON_ATTACK = getArea("area.button.attack");

    private static final Area AREA_BUTTONS_BARRACK = getArea("area.buttons.barrack");

    private static final Point BUTTON_TRAIN_CLOSE = getPoint("point.button.train.close");

    private static final Point BUTTON_TRAIN_NEXT = getPoint("point.button.train.next");

    private static final Area AREA_CAMPS_FULL = getArea("area.camps.full");

    private static final String[] COLLECT_DARK_ELIXIR = { "collect/dark_elixir_1.png" };

    private static final String[] COLLECT_ELIXIR = { "collect/elixir_1.png", "collect/elixir_2.png",
        "collect/elixir_3.png", "collect/elixir_4.png" };

    private static final String[] COLLECT_GOLD = { "collect/gold_1.png", "collect/gold_2.png", "collect/gold_3.png",
        "collect/gold_4.png", "collect/gold_5.png" };

    private Point buttonTroops;

    private Point buttonTrainClose;

    private Point buttonAttack;

    MainScreenParser() {
    }

    public Boolean areCampsFull() {
        final BufferedImage image = os.screenshot(AREA_CAMPS_FULL);
        return searchImage(image, "camps_full.png") != null;
    }

    public Point getButtonAttack() {
        if (buttonAttack == null) {
            buttonAttack = searchButtonAttack();
        }
        return buttonAttack;
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

    public Point getButtonTroops() {
        if (buttonTroops == null) {
            buttonTroops = searchButtonTroops();
        }
        return buttonTroops;
    }

    public TroopsInfo parseTroopsInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Point searchButtonAttack() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_ATTACK);
        return relativePoint(searchImageCenter(image, "button_attack.png"), AREA_BUTTON_ATTACK.getP1());
    }

    public Point searchButtonTrainClose() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_TRAIN_CLOSE);
        return relativePoint(searchImageCenter(image, "button_train_close.png"), AREA_BUTTON_TRAIN_CLOSE.getP1());
    }

    public Point searchButtonTroops() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_TROOPS);
        return relativePoint(searchImageCenter(image, "button_troops.png"), AREA_BUTTON_TROOPS.getP1());
    }

    public Point searchFullDarkElixirDrill() {
        final BufferedImage image = os.screenshot();
        Point point = null;
        for (final String resource : COLLECT_DARK_ELIXIR) {
            point = searchImageCenter(image, resource);
            if (point != null) {
                break;
            }
        }
        return point;
    }

    public Point searchFullElixirCollector() {
        final BufferedImage image = os.screenshot();
        Point point = null;
        for (final String resource : COLLECT_ELIXIR) {
            point = searchImageCenter(image, resource);
            if (point != null) {
                break;
            }
        }
        return point;
    }

    public Point searchFullGoldMine() {
        final BufferedImage image = os.screenshot();
        Point point = null;
        for (final String resource : COLLECT_GOLD) {
            point = searchImageCenter(image, resource);
            if (point != null) {
                break;
            }
        }
        return point;
    }

    public Point searchTrainButton() {
        final BufferedImage image = os.screenshot(AREA_BUTTONS_BARRACK);
        return relativePoint(searchImage(image, "train.png"), AREA_BUTTONS_BARRACK.getP1());
    }
}
