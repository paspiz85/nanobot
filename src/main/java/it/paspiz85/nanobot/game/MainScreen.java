package it.paspiz85.nanobot.game;

import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * Main mode screen.
 *
 * @author paspiz85
 *
 */
public final class MainScreen extends Screen {

    private static final Area AREA_BUTTON_ATTACK = getArea("area.button.attack");

    private static final Area AREA_BUTTON_TROOPS = getArea("area.button.troops");

    private static final Pixel BUTTON_WAS_ATTACKED_OKAY = new Pixel(432, 507, new Color(0x5CAC10));

    private static final Pixel POINT_WAS_ATTACKED_HEADLINE = new Pixel(437, 158, new Color(0x585450));

    private Point buttonAttack;

    private Point buttonTroops;

    MainScreen() {
    }

    public Point getButtonAttack() {
        if (buttonAttack == null) {
            buttonAttack = searchButtonAttack();
        }
        return buttonAttack;
    }

    public Point getButtonTroops() {
        if (buttonTroops == null) {
            buttonTroops = searchButtonTroops();
        }
        return buttonTroops;
    }

    public Pixel getButtonWasAttackedOK() {
        return BUTTON_WAS_ATTACKED_OKAY;
    }

    public Pixel getPointWasAttackedHeadline() {
        return POINT_WAS_ATTACKED_HEADLINE;
    }

    @Override
    public boolean isDisplayed() {
        return searchButtonAttack() != null;
    }

    public Point searchButtonAttack() {
        final BufferedImage image = platform.screenshot(AREA_BUTTON_ATTACK);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_attack.png")),
                AREA_BUTTON_ATTACK.getEdge1());
    }

    public Point searchButtonTroops() {
        final BufferedImage image = platform.screenshot(AREA_BUTTON_TROOPS);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_troops.png")),
                AREA_BUTTON_TROOPS.getEdge1());
    }

    private Point searchFullCollector(final URI uri) {
        final Point[] point = new Point[1];
        final BufferedImage image = platform.screenshot();
        Utils.withClasspathFolder(uri, (path) -> {
            try (Stream<Path> walk = Files.walk(path, 1)) {
                for (final Iterator<Path> it = walk.iterator(); it.hasNext();) {
                    final Path next = it.next();
                    if (Files.isDirectory(next)) {
                        continue;
                    }
                    point[0] = searchImageCenter(image, next.toUri().toURL());
                    if (point[0] != null) {
                        break;
                    }
                }
            } catch (final IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        return point[0];
    }

    public Point searchFullDarkElixirDrill() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/dark_elixir").toURI();
            result = searchFullCollector(resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public Point searchFullElixirCollector() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/elixir").toURI();
            result = searchFullCollector(resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public Point searchFullGoldMine() {
        Point result = null;
        try {
            final URI resource = getClass().getResource("collect/gold").toURI();
            result = searchFullCollector(resource);
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
}
