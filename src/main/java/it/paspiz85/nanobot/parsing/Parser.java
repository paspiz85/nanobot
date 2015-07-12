package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.util.Area;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
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

    private static Properties config;

    private static final OS DEFAULT_OS = OS.instance();

    private static final Map<String, Parser> INSTANCES = new HashMap<>();

    private static final int THRESHOLD_DEFAULT = 5;

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

    private static Properties getConfig() {
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

    private static int[][] getOffset(final String name) {
        int[][] result = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            final String[] points = value.split(";");
            result = new int[points.length][];
            for (int i = 0; i < points.length; i++) {
                final String[] point = points[i].split(",");
                result[i] = new int[point.length];
                for (int j = 0; j < point.length; j++) {
                    result[i][j] = Integer.parseInt(point[j].trim());
                }
            }
        }
        return result;
    }

    private static int[][] getOffset(final String name, final String fallback) {
        int[][] result = getOffset(name);
        if (result == null) {
            result = getOffset(fallback);
        }
        return result;
    }

    private static int[][][] getOffsets(final String name) {
        return new int[][][] { getOffset(name + ".a", name), getOffset(name + ".b", name),
                getOffset(name + ".c", name), getOffset(name + ".d", name) };
    }

    protected static final Point getPoint(final String name) {
        Point point = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            final String[] split = value.split(",");
            point = new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
        }
        return point;
    }

    private static Integer getRGB(final String name) {
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
        return new int[][] { getRGBs(name + ".a"), getRGBs(name + ".b"), getRGBs(name + ".c"), getRGBs(name + ".d") };
    }

    private static int getThreshold(final String name) {
        int result = THRESHOLD_DEFAULT;
        final String value = getConfig().getProperty(name + ".threshold");
        if (value != null) {
            result = Integer.parseInt(value.trim());
        }
        return result;
    }

    private static int[] getWidth(final String name) {
        int[] result = null;
        final String value = getConfig().getProperty(name);
        if (value != null) {
            final String[] split = value.split(",");
            result = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Integer.parseInt(split[i].trim());
            }
        }
        return result;
    }

    private static int[] getWidth(final String name, final String fallback) {
        int[] result = getWidth(name);
        if (result == null) {
            result = getWidth(fallback);
        }
        return result;
    }

    private static int[][] getWidths(final String name) {
        return new int[][] { getWidth(name + ".a", name), getWidth(name + ".b", name), getWidth(name + ".c", name),
                getWidth(name + ".d", name) };
    }

    private final int[][][] colors;

    /*
     * Set this var for debugging image screen parsing
     */
    private final Integer learnMode = null;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final int[][][][] offsets;

    protected final OS os = DEFAULT_OS;

    private final int[] thresholds;

    private final int[][] widths;

    Parser() {
        offsets = new int[10][][][];
        colors = new int[10][][];
        thresholds = new int[10];
        for (int i = 0; i < 10; i++) {
            offsets[i] = getOffsets("digit." + i + ".offset");
            colors[i] = getRGBseries("digit." + i + ".color");
            thresholds[i] = getThreshold("digit." + i);
        }
        widths = getWidths("digit.widths");
    }

    protected final void doWithPath(final URI uri, final Consumer<Path> pathConsumer) {
        try {
            if ("jar".equals(uri.getScheme())) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    final String schemeSpecificPart = uri.getSchemeSpecificPart();
                    final Path path = fileSystem.getPath(schemeSpecificPart.substring(
                            schemeSpecificPart.indexOf("!") + 1, schemeSpecificPart.length()));
                    pathConsumer.accept(path);
                }
            } else {
                final Path path = Paths.get(uri);
                pathConsumer.accept(path);
            }
        } catch (final IOException e) {
            logger.log(Level.SEVERE, String.format("Unable to open uri %s: %s", uri, e.getMessage()), e);
        }
    }

    protected final Rectangle findArea(final BufferedImage input, final InputStream in) {
        Rectangle result = null;
        try {
            final BufferedImage tar = ImageIO.read(in);
            final List<RegionMatch> doFindAll = TemplateMatcher.findMatchesByGrayscaleAtOriginalResolution(input, tar,
                    1, 0.9);
            result = doFindAll.isEmpty() ? null : doFindAll.get(0).getBounds();
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Unable to read input", e);
        }
        return result;
    }

    protected final Rectangle findArea(final BufferedImage input, final URL url) {
        Rectangle result = null;
        try {
            result = findArea(input, url.openStream());
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Unable to read url " + url, e);
        }
        return result;
    }

    private Integer parseDigit(final BufferedImage image, final Point start, final int type) {
        Integer result = null;
        for (int i = 0; i < 10; i++) {
            final int[] expected = colors[i][type];
            final int[] actual = new int[offsets[i][type].length];
            for (int j = 0; j < actual.length; j++) {
                actual[j] = image.getRGB(start.x() + offsets[i][type][j][0], start.y() + offsets[i][type][j][1]);
            }
            boolean found = true;
            int count = 0;
            for (int j = 0; j < actual.length; j++) {
                final boolean compare = os.compareColor(new Color(actual[j]), new Color(expected[j]), thresholds[i]);
                if (learnMode != null) {
                    image.setRGB(start.x() + offsets[i][type][j][0], start.y() + offsets[i][type][j][1], compare ? 0xFF
                            : 0xFF0000);
                }
                if (!compare) {
                    found = false;
                    if (learnMode == null) {
                        break;
                    }
                } else {
                    count++;
                }
            }
            if (learnMode != null && learnMode == i) {
                String s = "";
                for (final int element : actual) {
                    s += "#" + Integer.toHexString(element & 0xFFFFFF).toUpperCase() + ",";
                }
                System.out.println(s);
                if (found) {
                    final File f = os.saveImage(image, "test_" + System.currentTimeMillis() + "_found");
                    System.out.println(f.getAbsolutePath());
                    System.out.println();
                } else {
                    final File f = os.saveImage(image, "test_" + System.currentTimeMillis() + "_notfound_" + count);
                    System.out.println(f.getAbsolutePath());
                    System.out.println();
                }
            }
            if (learnMode != null) {
                for (int j = 0; j < actual.length; j++) {
                    image.setRGB(start.x() + offsets[i][type][j][0], start.y() + offsets[i][type][j][1], actual[j]);
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
                curr += widths[type][i] - 1;
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

    protected final Point searchImage(final BufferedImage image, final URL resource) {
        Point result = null;
        final Rectangle rectangle = findArea(image, resource);
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

    protected final Point searchImageCenter(final BufferedImage image, final URL resource) {
        Point result = null;
        final Rectangle rectangle = findArea(image, resource);
        if (rectangle != null) {
            final int x = rectangle.getLocation().x + (int) (rectangle.getWidth() / 2);
            final int y = rectangle.getLocation().y + (int) (rectangle.getHeight() / 2);
            result = new Point(x, y);
        }
        return result;
    }
}
