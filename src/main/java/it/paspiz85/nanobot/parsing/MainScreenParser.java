package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.ColoredPoint;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Parser for main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreenParser extends Parser {

    private static final Area AREA_BUTTON_TROOPS = getArea("area.button.troops");

    private static final Area AREA_BUTTON_TRAIN_CLOSE = getArea("area.button.train.close");

    private static final Area AREA_TROOPS = getArea("area.troops");

    private static final Area AREA_EROES = getArea("area.eroes");

    private static final Area AREA_BUTTON_ATTACK = getArea("area.button.attack");

    private static final Point BUTTON_TRAIN_NEXT = getPoint("point.button.train.next");

    private static final Area AREA_CAMPS_FULL = getArea("area.camps.full");

    private static final ColoredPoint POINT_WAS_ATTACKED_HEADLINE = new ColoredPoint(437, 158, new Color(0x585450));

    private static final ColoredPoint BUTTON_WAS_ATTACKED_OKAY = new ColoredPoint(432, 507, new Color(0x5CAC10));

    private Point buttonTroops;

    private Point buttonTrainClose;

    private Point buttonAttack;

    MainScreenParser() {
    }

    public Boolean areCampsFull() {
        final BufferedImage image = os.screenshot(AREA_CAMPS_FULL);
        return searchImage(image, getClass().getResource("camps_full.png")) != null;
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

    public ColoredPoint getButtonWasAttackedOK() {
        return BUTTON_WAS_ATTACKED_OKAY;
    }

    public ColoredPoint getPointWasAttackedHeadline() {
        return POINT_WAS_ATTACKED_HEADLINE;
    }

    public TroopsInfo parseTroopsInfo() {
        final BufferedImage image = os.screenshot();
        final BufferedImage imageTroops = os.getSubimage(image, AREA_TROOPS);
        Point start = new Point(9, 4);
        // start = new Point(28, 4);
        // start = new Point(28+63, 4);
        // start = new Point(28+63+62, 4);
        final int[] result = new int[9];
        int len = 0;
        while (len < result.length) {
            final Integer n = parseNumber(imageTroops, 3, start, 46);
            if (n == null) {
                break;
            }
            result[len++] = n;
            start = new Point(start.x() + 62, start.y());
        }
        final BufferedImage imageEroes = os.getSubimage(image, AREA_EROES);
        if (searchImage(imageEroes, getClass().getResource("king.png")) != null) {
            result[len++] = 1;
        }
        // TODO implement queen
        // if (searchImage(imageEroes, "queen.png") != null) {
        // result[len++] = 1;
        // }
        return new TroopsInfo(Arrays.copyOf(result, len));
    }

    public Point searchButtonAttack() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_ATTACK);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_attack.png")),
                AREA_BUTTON_ATTACK.getP1());
    }

    public Point searchButtonAttackLabel() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_ATTACK);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_attack_label.png")),
                AREA_BUTTON_ATTACK.getP1());
    }

    public Point searchButtonTrainClose() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_TRAIN_CLOSE);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_train_close.png")),
                AREA_BUTTON_TRAIN_CLOSE.getP1());
    }

    public Point searchButtonTroops() {
        final BufferedImage image = os.screenshot(AREA_BUTTON_TROOPS);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_troops.png")),
                AREA_BUTTON_TROOPS.getP1());
    }

    private Point searchFullCollector(final Supplier<URI> uriSupplier) {
        Point point = null;
        try {
            final BufferedImage image = os.screenshot();
            final URI uri = uriSupplier.get();
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            // needed for jar packaging
            FileSystems.newFileSystem(uri, env);
            final Path root = Paths.get(uri);
            try (Stream<Path> walk = Files.walk(root, 1)) {
                for (final Iterator<Path> it = walk.iterator(); it.hasNext();) {
                    final Path next = it.next();
                    if (Files.isDirectory(next)) {
                        continue;
                    }
                    point = searchImageCenter(image, next.toUri().toURL());
                    if (point != null) {
                        break;
                    }
                }
            }
        } catch (final IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return point;
    }

    public Point searchFullDarkElixirDrill() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/dark_elixir").toURI();
            result = searchFullCollector(() -> resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public Point searchFullElixirCollector() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/elixir").toURI();
            result = searchFullCollector(() -> resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public Point searchFullGoldMine() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/gold").toURI();
            result = searchFullCollector(() -> resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
}
