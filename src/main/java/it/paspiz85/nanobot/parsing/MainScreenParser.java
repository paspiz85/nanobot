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

    private static final String[] COLLECT_GOLD = { "collect/gold_1.png", "collect/gold_2.png" };

    private static final String[] COLLECT_ELIXIR = { "collect/elixir_1.png" };

    private static final String[] COLLECT_DARK_ELIXIR = { "collect/dark_elixir_1.png" };

    MainScreenParser() {
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

    public Point searchTrainButton() {
        final BufferedImage image = screenShot(Area.BARRACKS_BUTTONS);
        return relativePoint(searchImage(image, "train.png"), Area.BARRACKS_BUTTONS.getP1());
    }
}
