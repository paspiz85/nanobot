package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private static final Area ATTACK_GROUP = new Area(24, 554, 836, 653);

    private static final int ATTACK_GROUP_UNIT_DIFF = 72;

    private static final Color DARKCHECK_COLOR_YES = getColor("darkcheck.color.yes");

    private static final Color DARKCHECK_COLOR_NO = getColor("darkcheck.color.no");

    private static final Area ENEMY_BASE = new Area(31, 0, 831, 510);

    private static final Point ENEMY_BASE_BOTTOM = new Point(400, 597);

    private static final Point ENEMY_BASE_LEFT = new Point(13, 313);

    private static final Polygon ENEMY_BASE_POLY = new Polygon();

    private static final Point ENEMY_BASE_RIGHT = new Point(779, 312);

    private static final Point ENEMY_BASE_TOP = new Point(401, 16);

    protected static final Area ENEMY_LOOT = new Area(17, 68, 138, 240);

    private static final Area AREA_NEXT_BUTTON = new Area(692, 488, 739, 547);

    private static final Point POINT_DARK_ELIXIR = new Point(33, 57 + 2);

    private static final Point POINT_ELIXIR_HAS_DARK = new Point(33, 29 + 2);

    private static final Point POINT_ELIXIR_HASNT_DARK = new Point(33, 30);

    private static final Point DARKCHECK_POINT = getPoint("darkcheck.point");

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

    public Point getButtonNext() {
        if (buttonNext == null) {
            buttonNext = searchButtonNext();
        }
        return buttonNext;
    }

    private boolean hasDE(final BufferedImage image) throws BotBadBaseException {
        final int rgb = image.getRGB(DARKCHECK_POINT.x(), DARKCHECK_POINT.y());
        final Color deCheck = new Color(rgb);
        boolean result;
        if (os.compareColor(deCheck, DARKCHECK_COLOR_YES, 7)) {
            result = true;
        } else if (os.compareColor(deCheck, DARKCHECK_COLOR_NO, 7)) {
            result = false;
        } else {
            throw new BotBadBaseException("de: " + Integer.toHexString(deCheck.getRGB()));
        }
        return result;
    }

    public Boolean isCollectorFullBase() throws BotException {
        final BufferedImage image = os.screenshot(ENEMY_BASE);
        FileSystem fileSystem = null;
        Stream<Path> walk = null;
        try {
            final URI uri = getClass().getResource("elixirs").toURI();
            Path images;
            if ("jar".equals(uri.getScheme())) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                images = fileSystem.getPath("/elixir_images");
            } else {
                images = Paths.get(uri);
            }
            walk = Files.walk(images, 1);
            final List<Rectangle> matchedElixirs = new ArrayList<>();
            int attackableElixirs = 0;
            for (final Iterator<Path> it = walk.iterator(); it.hasNext();) {
                final Path next = it.next();
                if (Files.isDirectory(next)) {
                    continue;
                }
                final BufferedImage tar = ImageIO.read(Files.newInputStream(next, StandardOpenOption.READ));
                final List<RegionMatch> doFindAll = TemplateMatcher.findMatchesByGrayscaleAtOriginalResolution(image,
                        tar, 7, 0.8);
                attackableElixirs += countAttackableElixirs(doFindAll, matchedElixirs, next);
            }
            return attackableElixirs >= 0;
        } catch (final Exception e) {
            throw new BotException(e.getMessage(), e);
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (final IOException e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                }
            }
            if (walk != null) {
                walk.close();
            }
        }
    }

    private Integer parseArcherQueenSlot(final BufferedImage image) {
        Integer result = null;
        final Rectangle rectangle = findArea(image, getClass().getResource("aq.png"));
        if (rectangle != null) {
            result = rectangle.x / ATTACK_GROUP_UNIT_DIFF;
        }
        return result;
    }

    private Integer parseBarbKingSlot(final BufferedImage image) {
        Integer result = null;
        final Rectangle rectangle = findArea(image, getClass().getResource("bk.png"));
        if (rectangle != null) {
            result = rectangle.x / ATTACK_GROUP_UNIT_DIFF;
        }
        return result;
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
        final BufferedImage image = os.screenshot(ENEMY_LOOT);
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

    public int[] parseTroopCount() {
        final BufferedImage image = os.screenshot(ATTACK_GROUP);
        final int[] tmp = new int[11]; // max group size
        int xStart = 20;
        final int yStart = 11;
        Integer no;
        int curr = 0;
        while (true) {
            no = parseNumber(image, 3, new Point(xStart, yStart), ATTACK_GROUP_UNIT_DIFF - 10);
            if (no == null || no == 0) {
                break;
            }
            if (no >= 5) {
                tmp[curr] = no;
            } else {
                // ignore 1,2,3,4 because they are usually
                // cc or spells
                tmp[curr] = 0;
            }
            curr++;
            xStart += ATTACK_GROUP_UNIT_DIFF;
        }
        final Integer barbKingSlot = parseBarbKingSlot(image);
        if (barbKingSlot != null) {
            tmp[barbKingSlot] = 1;
            // if BK was found after a 0 slot, new length should be adjusted
            // according to BK
            // ie [110, 90, 0, BK] -> len = 4
            curr = Math.max(curr + 1, barbKingSlot + 1);
        }
        final Integer archerQueenSlot = parseArcherQueenSlot(image);
        if (archerQueenSlot != null) {
            tmp[archerQueenSlot] = 1;
            // if AQ was found after a 0 slot, new length should be adjusted
            // according to AQ
            // ie [110, 90, 0, AQ] -> len = 4
            curr = Math.max(curr + 1, archerQueenSlot + 1);
        }
        return Arrays.copyOf(tmp, curr);
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
        final BufferedImage image = os.screenshot(AREA_NEXT_BUTTON);
        return relativePoint(searchImageCenter(image, "button_next.png"), AREA_NEXT_BUTTON.getP1());
    }
}
