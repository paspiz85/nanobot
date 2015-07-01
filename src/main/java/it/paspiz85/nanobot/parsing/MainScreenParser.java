package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;

/**
 * Parser for main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreenParser extends Parser {

    private static final String[] COLLECT_GOLD = { "collect/gold_1.png", "collect/gold_2.png", "collect/gold_3.png",
            "collect/gold_4.png", "collect/gold_5.png" };

    private static final String[] COLLECT_ELIXIR = { "collect/elixir_1.png", "collect/elixir_2.png",
            "collect/elixir_3.png", "collect/elixir_4.png" };

    private static final String[] COLLECT_DARK_ELIXIR = { "collect/dark_elixir_1.png" };

    private static final Point BUTTON_RAX_CLOSE = new Point(729, 113);

    private static final Point BUTTON_RAX_NEXT = new Point(767, 361);

    MainScreenParser() {
    }

    public Boolean areCampsFull() {
        final BufferedImage image = screenShot(Area.CAMPS_FULL);
        return searchImage(image, "camps_full.png") != null;
    }

    public Point searchAttackButton() {
        final BufferedImage image = screenShot(Area.ATTACK_BUTTON);
        return relativePoint(searchImageCenter(image, "attack.png"), Area.ATTACK_BUTTON.getP1());
    }

    public Point searchFullDarkElixirDrill() {
        final BufferedImage image = screenShot(Area.FULLSCREEN);
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
        final BufferedImage image = screenShot(Area.FULLSCREEN);
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
        final BufferedImage image = screenShot(Area.FULLSCREEN);
        Point point = null;
        for (final String resource : COLLECT_GOLD) {
            point = searchImageCenter(image, resource);
            if (point != null) {
                break;
            }
        }
        return point;
    }

    public Point searchRaxClose() {
        return BUTTON_RAX_CLOSE;
    }

    public Point searchRaxNext() {
        return BUTTON_RAX_NEXT;
    }

    public Point searchTrainButton() {
        final BufferedImage image = screenShot(Area.BARRACKS_BUTTONS);
        return relativePoint(searchImage(image, "train.png"), Area.BARRACKS_BUTTONS.getP1());
    }
}
