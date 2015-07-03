package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.sikuli.core.search.RegionMatch;
import org.sikuli.core.search.algorithm.TemplateMatcher;

/**
 * Abstract screen parse.
 *
 * @author paspiz85
 *
 */
public abstract class Parser {

    private static final int[][] COLOR_EIGHT = getRGBseries("colors.eight");

    private static final int[][] COLOR_FIVE = getRGBseries("colors.five");

    private static final int[][] COLOR_FOUR = getRGBseries("colors.four");

    private static final int[][] COLOR_NINE = getRGBseries("colors.nine");

    private static final int[][] COLOR_ONE = getRGBseries("colors.one");

    private static final int[][] COLOR_SEVEN = getRGBseries("colors.seven");

    private static final int[][] COLOR_SIX = getRGBseries("colors.six");

    private static final int[][] COLOR_THREE = getRGBseries("colors.three");

    private static final int[][] COLOR_TWO = getRGBseries("colors.two");

    private static final int[][] COLOR_ZERO = getRGBseries("colors.zero");

    private static Properties config;

    private static final OS DEFAULT_OS = OS.instance();

    private static final Map<String, Parser> INSTANCES = new HashMap<>();

    private static final int[][] OFFSET_EIGHT = new int[][] { { 5, 3 }, { 5, 10 }, { 1, 6 } };

    private static final int[][] OFFSET_FIVE = new int[][] { { 5, 4 }, { 4, 9 }, { 6, 12 } };

    private static final int[][] OFFSET_FOUR = new int[][] { { 2, 3 }, { 3, 1 }, { 1, 5 } };

    private static final int[][] OFFSET_NINE = new int[][] { { 5, 5 }, { 5, 9 }, { 8, 12 } };

    private static final int[][] OFFSET_ONE = new int[][] { { 1, 1 }, { 1, 12 }, { 4, 12 } };

    private static final int[][] OFFSET_SEVEN = new int[][] { { 5, 11 }, { 4, 3 }, { 7, 7 } };

    private static final int[][] OFFSET_SIX = new int[][] { { 5, 4 }, { 5, 9 }, { 8, 5 } };

    private static final int[][] OFFSET_THREE = new int[][] { { 2, 3 }, { 4, 8 }, { 5, 13 } };

    private static final int[][] OFFSET_TWO = new int[][] { { 1, 7 }, { 3, 6 }, { 7, 7 } };

    private static final int[][] OFFSET_ZERO = new int[][] { { 6, 4 }, { 7, 7 }, { 10, 13 } };

    protected static final int VAR = 5;

    protected static final Area getArea(final String name) {
        Area area = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            final String[] split = value.split(",");
            area = new Area(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()),
                    Integer.parseInt(split[2].trim()), Integer.parseInt(split[3].trim()));
        }
        return area;
    }

    protected static final Color getColor(final String name) {
        Color color = null;
        final Integer value = getRGB(name);
        if (value != null) {
            color = new Color(value);
        }
        return color;
    }

    protected static final Properties getConfig() {
        if (config == null) {
            try {
                config = new Properties();
                config.load(Parser.class.getResourceAsStream("config.properties"));
            } catch (final IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return config;
    }

    @SuppressWarnings("unchecked")
    public static <P extends Parser> P getInstance(final Class<P> c) {
        P parser = null;
        if (c != null) {
            parser = (P) INSTANCES.get(c.getName());
            if (parser == null) {
                try {
                    parser = c.newInstance();
                    INSTANCES.put(c.getName(), parser);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return parser;
    }

    protected static final Point getPoint(final String name) {
        Point point = null;
        final String value = config.getProperty(name);
        if (value != null) {
            final String[] split = value.split(",");
            point = new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
        }
        return point;
    }

    protected static final Integer getRGB(final String name) {
        Integer color = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            color = Integer.decode(value.replace("#", "0x").trim());
        }
        return color;
    }

    private static int[] getRGBs(final String name) {
        int[] colors = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            final String[] split = value.split(",");
            colors = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                colors[i] = Integer.decode(split[i].replace("#", "0x").trim());
            }
        }
        return colors;
    }

    private static int[][] getRGBseries(final String name) {
        return new int[][] { getRGBs(name + ".0"), getRGBs(name + ".1"), getRGBs(name + ".2"), getRGBs(name + ".3") };
    }

    protected final int[][][] colors = new int[10][][];

    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected final int[][][] offsets = new int[10][][];

    protected final OS os = DEFAULT_OS;

    protected final int[] widths = new int[] { 13, 6, 10, 10, 12, 10, 11, 10, 11, 11 };

    Parser() {
        offsets[0] = OFFSET_ZERO;
        offsets[1] = OFFSET_ONE;
        offsets[2] = OFFSET_TWO;
        offsets[3] = OFFSET_THREE;
        offsets[4] = OFFSET_FOUR;
        offsets[5] = OFFSET_FIVE;
        offsets[6] = OFFSET_SIX;
        offsets[7] = OFFSET_SEVEN;
        offsets[8] = OFFSET_EIGHT;
        offsets[9] = OFFSET_NINE;
        colors[0] = COLOR_ZERO;
        colors[1] = COLOR_ONE;
        colors[2] = COLOR_TWO;
        colors[3] = COLOR_THREE;
        colors[4] = COLOR_FOUR;
        colors[5] = COLOR_FIVE;
        colors[6] = COLOR_SIX;
        colors[7] = COLOR_SEVEN;
        colors[8] = COLOR_EIGHT;
        colors[9] = COLOR_NINE;
    }

    protected final Rectangle findArea(final BufferedImage input, final URL url) {
        Rectangle result = null;
        try {
            final BufferedImage tar = ImageIO.read(url);
            final List<RegionMatch> doFindAll = TemplateMatcher.findMatchesByGrayscaleAtOriginalResolution(input, tar,
                    1, 0.9);
            result = doFindAll.isEmpty() ? null : doFindAll.get(0).getBounds();
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Unable to read url " + url, e);
        }
        return result;
    }

    private Integer parseDigit(final BufferedImage image, final Point start, final int type) {
        Integer result = null;
        for (int i = 0; i < 10; i++) {
            boolean found = true;
            for (int j = 0; j < offsets[i].length; j++) {
                final int actual = image.getRGB(start.x() + offsets[i][j][0], start.y() + offsets[i][j][1]);
                final int expected = colors[i][type][j];
                if (!os.compareColor(new Color(actual), new Color(expected), VAR)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                result = i;
                break;
            }
        }
        return result;
    }

    protected Integer parseNumber(final BufferedImage image, final int type, final Point start, final int maxSearchWidth) {
        String no = "";
        int curr = start.x();
        while (curr < start.x() + maxSearchWidth) {
            final Integer i = parseDigit(image, new Point(curr, start.y()), type);
            if (i != null) {
                no += i;
                curr += widths[i] - 1;
            } else {
                curr++;
            }
        }
        return no.isEmpty() ? null : Integer.parseInt(no);
    }

    protected final Point relativePoint(final Point point, final Point root) {
        Point result = null;
        if (point != null) {
            result = new Point(point.x() + root.x(), point.y() + root.y());
        }
        return result;
    }

    protected final Point searchImage(final BufferedImage image, final String resource) {
        Point result = null;
        final Rectangle rectangle = findArea(image, getClass().getResource(resource));
        if (rectangle != null) {
            final int x = rectangle.getLocation().x;
            final int y = rectangle.getLocation().y;
            result = new Point(x, y);
        }
        return result;
    }

    protected final Point searchImageCenter(final BufferedImage image, final String resource) {
        Point result = null;
        final Rectangle rectangle = findArea(image, getClass().getResource(resource));
        if (rectangle != null) {
            final int x = rectangle.getLocation().x + (int) (rectangle.getWidth() / 2);
            final int y = rectangle.getLocation().y + (int) (rectangle.getHeight() / 2);
            result = new Point(x, y);
        }
        return result;
    }
}
