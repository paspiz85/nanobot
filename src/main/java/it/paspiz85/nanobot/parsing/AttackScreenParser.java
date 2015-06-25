package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.win32.OS;

import java.awt.Color;
import java.awt.Point;
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

public final class AttackScreenParser extends AbstractParser {

    private static final Point ENEMY_BASE_BOTTOM = new Point(400, 597);

    // boundaries of base according to Area.ENEMY_BASE
    private static final Point ENEMY_BASE_LEFT = new Point(13, 313);

    private static final Polygon ENEMY_BASE_POLY = new Polygon();

    private static final Point ENEMY_BASE_RIGHT = new Point(779, 312);

    private static final Point ENEMY_BASE_TOP = new Point(401, 16);

    private static final int ATTACK_GROUP_UNIT_DIFF = 72;

    AttackScreenParser() {
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_LEFT.x, ENEMY_BASE_LEFT.y);
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_TOP.x, ENEMY_BASE_TOP.y);
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_RIGHT.x, ENEMY_BASE_RIGHT.y);
        ENEMY_BASE_POLY.addPoint(ENEMY_BASE_BOTTOM.x, ENEMY_BASE_BOTTOM.y);
    }

    public boolean hasDE(final BufferedImage image) throws BotBadBaseException {
        final int deCheck = image.getRGB(20, 0);
        // 0x80752B
        if (OS.instance().compareColor(deCheck, new Color(128, 117, 43).getRGB(), 7)) {
            return true;
        } else if (OS.instance().compareColor(deCheck, 0xffb1a841, 7)) {
            return false;
        } else {
            throw new BotBadBaseException("de: " + Integer.toHexString(deCheck));
        }
    }

    public boolean isCollectorFullBase() throws BotException {
        return isCollectorFullBase(OS.instance().screenShot(Area.ENEMY_BASE));
    }

    public boolean isCollectorFullBase(final BufferedImage image) throws BotException {
        FileSystem fileSystem = null;
        Stream<Path> walk = null;
        try {
            final URI uri = getClass().getResource("elixirs").toURI();
            Path images;
            if (uri.getScheme().equals("jar")) {
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
            }
            final boolean result = attackableElixirs >= 0;
            if (result == false) {
                logger.info("empty collectors");
            }
            return result;
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

    public Integer parseArcherQueenSlot(final BufferedImage image) {
        final Rectangle rectangle = findArea(image, getClass().getResource("aq.png"));
        if (rectangle == null) {
            return null;
        }
        return rectangle.x / ATTACK_GROUP_UNIT_DIFF;
    }

    public Integer parseBarbKingSlot(final BufferedImage image) {
        final Rectangle rectangle = findArea(image, getClass().getResource("bk.png"));
        if (rectangle == null) {
            return null;
        }
        return rectangle.x / ATTACK_GROUP_UNIT_DIFF;
    }

    public int parseDarkElixir(final BufferedImage image) throws BotBadBaseException {
        if (!hasDE(image)) {
            return 0;
        }
        return parseNumber(image, 2, 33, 57, image.getWidth() - 43);
    }

    public int parseElixir(final BufferedImage image) throws BotBadBaseException {
        return parseNumber(image, 1, 33, 29 + (hasDE(image) ? 0 : 1), image.getWidth() - 43);
    }

    public int parseGold(final BufferedImage image) throws BotBadBaseException {
        return parseNumber(image, 0, 33, 0 + (hasDE(image) ? 0 : 1), image.getWidth() - 43);
    }

    public int[] parseLoot() throws BotBadBaseException {
        final BufferedImage image = OS.instance().screenShot(Area.ENEMY_LOOT);
        return parseLoot(image);
    }

    public int[] parseLoot(final BufferedImage image) throws BotBadBaseException {
        final int gold = parseGold(image);
        final int elixir = parseElixir(image);
        final int de = parseDarkElixir(image);
        logger.info(String.format("[gold: %d, elixir: %d, de: %d]", gold, elixir, de));
        return new int[] { gold, elixir, de };
    }

    public int[] parseTroopCount() {
        final BufferedImage image = OS.instance().screenShot(Area.ATTACK_GROUP);
        final int[] troopCount = parseTroopCount(image);
        logger.info("[Troop count: " + Arrays.toString(troopCount) + "]");
        return troopCount;
    }

    public int[] parseTroopCount(final BufferedImage image) {
        final int[] tmp = new int[11]; // max group size
        int xStart = 20;
        final int yStart = 11;
        int no;
        int curr = 0;
        while (true) {
            no = parseNumber(image, 3, xStart, yStart, ATTACK_GROUP_UNIT_DIFF - 10);
            if (no == 0) {
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

    public int parseTrophy(final BufferedImage image) throws BotBadBaseException {
        if (!hasDE(image)) {
            return parseNumber(image, 3, 33, 62, image.getWidth() - 43);
        } else {
            return parseNumber(image, 3, 33, 90, image.getWidth() - 43);
        }
    }
}
