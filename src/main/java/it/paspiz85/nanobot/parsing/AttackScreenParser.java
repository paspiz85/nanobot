package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.sikuli.core.search.RegionMatch;
import org.sikuli.core.search.algorithm.TemplateMatcher;

/**
 * Parser for attack mode screen.
 *
 * @author paspiz85
 *
 */
public class AttackScreenParser extends Parser {

    private static final Area AREA_NEXT_BUTTON = Area.byEdges(692, 488, 739, 547);

    private static final Point BUTTON_END_BATTLE = getPoint("point.button.end_battle");

    private static final Point BUTTON_END_BATTLE_QUESTION_OK = getPoint("point.button.end_battle.question_ok");

    private static final Point BUTTON_END_BATTLE_RETURN_HOME = getPoint("point.button.end_battle.return_home");

    private static final Pixel BUTTON_FIND_MATCH = new Pixel(148, 529, new Color(0xD84B00));

    private static final Point BUTTON_SHIELD_DISABLE = getPoint("point.button.shield_disable");

    private static final Color DARKCHECK_COLOR_NO = getColor("darkcheck.color.no");

    private static final Color DARKCHECK_COLOR_YES = getColor("darkcheck.color.yes");

    private static final Point DARKCHECK_POINT = getPoint("darkcheck.point");

    private static final Area ENEMY_BASE = Area.byEdges(31, 0, 831, 510);

    private static final Point ENEMY_BASE_BOTTOM = new Point(400, 597);

    private static final Point ENEMY_BASE_LEFT = new Point(13, 313);

    private static final Polygon ENEMY_BASE_POLY = new Polygon();

    private static final Point ENEMY_BASE_RIGHT = new Point(779, 312);

    private static final Point ENEMY_BASE_TOP = new Point(401, 16);

    protected static final Area ENEMY_LOOT = Area.byEdges(17, 68, 138, 240);

    private static final Point POINT_DARK_ELIXIR = new Point(33, 57 + 2);

    private static final Point POINT_ELIXIR_HAS_DARK = new Point(33, 29 + 2);

    private static final Point POINT_ELIXIR_HASNT_DARK = new Point(33, 30);

    private static final Point POINT_GOLD_HAS_DARK = getPoint("point.gold.hasdark");

    private static final Point POINT_GOLD_HASNT_DARK = new Point(33, 1);

    private static final Point POINT_TROPHY_WIN_HAS_DARK = new Point(33, 90);

    private static final Point POINT_TROPHY_WIN_HASNT_DARK = new Point(33, 62);

    private Point buttonNext;

    AttackScreenParser() {
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_LEFT.x(), ENEMY_BASE_LEFT.y());
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_TOP.x(), ENEMY_BASE_TOP.y());
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_RIGHT.x(), ENEMY_BASE_RIGHT.y());
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_BOTTOM.x(), ENEMY_BASE_BOTTOM.y());
    }

    private int countAttackableElixirs(final List<RegionMatch> doFindAll, final List<Rectangle> matchedElixirs,
            final Path next) {
        int attackableElixirs = 0;
        int c = 0;
        RECT_LOOP: for (final RegionMatch i : doFindAll) {
            // if matched area is out of enemy poly
            if (!ENEMY_BASE_POLY.contains(i.x, i.y)) {
                continue;
            }
            // check if it's an existing match
            for (final Rectangle r : matchedElixirs) {
                if (r.intersects(i.getBounds())) {
                    break RECT_LOOP;
                }
            }
            c++;
            matchedElixirs.add(i.getBounds());
            if (next.getFileName().toString().startsWith("empty")) {
                attackableElixirs--;
            } else if (next.getFileName().toString().startsWith("full")) {
                attackableElixirs++;
            }
            logger.finest("\t" + i.getBounds() + " score: " + i.getScore());
        }
        if (c > 0) {
            logger.finest(String.format("\tfound %d elixirs matching %s\n", c, next.getFileName().toString()));
        }
        return attackableElixirs;
    }

    public Point getButtonAttackUnit(final int x) {
        Point result;
        switch (x) {
        case 1:
            result = new Point(72, 600);
            break;
        case 2:
            result = new Point(145, 600);
            break;
        case 3:
            result = new Point(217, 600);
            break;
        case 4:
            result = new Point(288, 600);
            break;
        case 5:
            result = new Point(361, 600);
            break;
        case 6:
            result = new Point(435, 600);
            break;
        case 7:
            result = new Point(505, 600);
            break;
        default:
            throw new IllegalArgumentException(x + "");
        }
        return result;
    }

    public Point getButtonEndBattle() {
        return BUTTON_END_BATTLE_RETURN_HOME;
    }

    public Point getButtonEndBattleQuestionOK() {
        return BUTTON_END_BATTLE_QUESTION_OK;
    }

    public Point getButtonEndBattleReturnHome() {
        return BUTTON_END_BATTLE;
    }

    public Pixel getButtonFindMatch() {
        return BUTTON_FIND_MATCH;
    }

    public Point getButtonNext() {
        if (buttonNext == null) {
            buttonNext = searchButtonNext();
        }
        return buttonNext;
    }

    public Point getButtonShieldDisable() {
        return BUTTON_SHIELD_DISABLE;
    }

    private boolean hasDE(final BufferedImage image) throws BotBadBaseException {
        final int rgb = image.getRGB(DARKCHECK_POINT.x(), DARKCHECK_POINT.y());
        final Color deCheck = new Color(rgb);
        boolean result;
        if (platform.compareColor(deCheck, DARKCHECK_COLOR_YES, 7)) {
            result = true;
        } else if (platform.compareColor(deCheck, DARKCHECK_COLOR_NO, 7)) {
            result = false;
        } else {
            throw new BotBadBaseException("de: " + Integer.toHexString(deCheck.getRGB()));
        }
        return result;
    }

    public Boolean isCollectorFullBase() throws BotException {
        final int[] attackableElixirs = { 0 };
        final BufferedImage image = platform.screenshot(ENEMY_BASE);
        try {
            final URI uri = getClass().getResource("elixirs").toURI();
            Utils.withClasspathFolder(
                    uri,
                    (path) -> {
                        final List<Rectangle> matchedElixirs = new ArrayList<>();
                        try (Stream<Path> walk = Files.walk(path, 1)) {
                            for (final Iterator<Path> it = walk.iterator(); it.hasNext();) {
                                final Path next = it.next();
                                if (Files.isDirectory(next)) {
                                    continue;
                                }
                                final BufferedImage tar = ImageIO.read(Files.newInputStream(next,
                                        StandardOpenOption.READ));
                                final List<RegionMatch> doFindAll = TemplateMatcher
                                        .findMatchesByGrayscaleAtOriginalResolution(image, tar, 7, 0.8);
                                attackableElixirs[0] += countAttackableElixirs(doFindAll, matchedElixirs, next);
                            }
                        } catch (final IOException e) {
                            logger.log(Level.SEVERE, e.getMessage(), e);
                        }
                    });
        } catch (final URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return attackableElixirs[0] >= 0;
    }

    protected final Integer parseDarkElixir(final BufferedImage image) throws BotBadBaseException {
        Integer result = null;
        if (hasDE(image)) {
            result = parseNumber(image, 2, POINT_DARK_ELIXIR, image.getWidth() - 43);
        }
        return result;
    }

    protected final Integer parseElixir(final BufferedImage image) throws BotBadBaseException {
        return parseNumber(image, 1, hasDE(image) ? POINT_ELIXIR_HAS_DARK : POINT_ELIXIR_HASNT_DARK,
                image.getWidth() - 43);
    }

    public EnemyInfo parseEnemyInfo() throws BotBadBaseException {
        final BufferedImage image = platform.screenshot(ENEMY_LOOT);
        final EnemyInfo info = new EnemyInfo();
        info.setGold(parseGold(image));
        info.setElixir(parseElixir(image));
        info.setDarkElixir(parseDarkElixir(image));
        info.setTrophyWin(parseTrophyWin(image));
        info.setTrophyDefeat(parseTrophyDefeat(image));
        return info;
    }

    protected final Integer parseGold(final BufferedImage image) throws BotBadBaseException {
        return parseNumber(image, 0, hasDE(image) ? POINT_GOLD_HAS_DARK : POINT_GOLD_HASNT_DARK, image.getWidth() - 43);
    }

    protected final Integer parseTrophyDefeat(final BufferedImage image) throws BotBadBaseException {
        // TODO implement trophyDefeat
        return null;
    }

    protected final Integer parseTrophyWin(final BufferedImage image) throws BotBadBaseException {
        // TODO implement trophyWin
        return null;
        /*
         * return parseNumber(image, 3, hasDE(image) ? POINT_TROPHY_WIN_HAS_DARK
         * : POINT_TROPHY_WIN_HASNT_DARK, image.getWidth() - 43);
         */
    }

    public Point searchButtonNext() {
        final BufferedImage image = platform.screenshot(AREA_NEXT_BUTTON);
        return relativePoint(searchImageCenter(image, getClass().getResource("button_next.png")),
                AREA_NEXT_BUTTON.getEdge1());
    }
}
