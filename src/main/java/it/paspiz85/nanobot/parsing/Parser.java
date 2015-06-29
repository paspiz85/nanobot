package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.util.Point;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final OS DEFAULT_OS = OS.instance();

    private static final int[][] COLOR_EIGHT = new int[][] { { 0x27261F, 0x302F26, 0x26261F },
            { 0x282427, 0x302C30, 0x262326 }, { 0x252525, 0x2D2D2D, 0x242424 }, { 0x282828, 0x303030, 0x262626 } };

    private static final int[][] COLOR_FIVE = new int[][] { { 0x060604, 0x040403, 0xB7B492 },
            { 0x060606, 0x040404, 0xB7A7B6 }, { 0x060606, 0x040404, 0xAFAFAF }, { 0x060606, 0x040404, 0xB7B7B7 }, };

    private static final int[][] COLOR_FOUR = new int[][] { { 0x282720, 0x080806, 0x403F33 },
            { 0x282428, 0x080708, 0x403A40 }, { 0x262626, 0x070707, 0x3D3D3D }, { 0x282828, 0x080808, 0x404040 } };

    private static final int[][] COLOR_NINE = new int[][] { { 0x302F26, 0x050504, 0x272720 },
            { 0x302C30, 0x050505, 0x282427 }, { 0x2E2E2E, 0x050505, 0x262626 }, { 0x303030, 0x050505, 0x272727 } };

    private static final int[][] COLOR_ONE = new int[][] { { 0x979478, 0x313127, 0xD7D4AC },
            { 0x968895, 0x312D31, 0xD8C4D6 }, { 0x8F8F8F, 0x2F2F2F, 0xCDCDCD }, { 0x979797, 0x313131, 0xD7D7D7 } };

    private static final int[][] COLOR_SEVEN = new int[][] { { 0x5E5C4B, 0x87856C, 0x5D5C4B },
            { 0x5F565E, 0x877B86, 0x5F565E }, { 0x5A5A5A, 0x818181, 0x5A5A5A }, { 0x5E5E5E, 0x878787, 0x5D5D5D } };

    private static final int[][] COLOR_SIX = new int[][] { { 0x070605, 0x040403, 0x181713 },
            { 0x070707, 0x040404, 0x181618 }, { 0x060606, 0x030303, 0x161616 }, { 0x070707, 0x040404, 0x181818 } };

    private static final int[][] COLOR_THREE = new int[][] { { 0x7F7D65, 0x070706, 0x37362C },
            { 0x7F737E, 0x070607, 0x373236 }, { 0x797979, 0x070707, 0x343434 }, { 0x7F7F7F, 0x070707, 0x373737 } };

    private static final int[][] COLOR_TWO = new int[][] { { 0xA09E80, 0xD8D4AC, 0x979579 },
            { 0xA0919F, 0xD8C4D6, 0x978A96 }, { 0x989898, 0xCDCDCD, 0x909090 }, { 0xA0A0A0, 0xD8D8D8, 0x979797 } };

    private static final int[][] COLOR_ZERO = new int[][] { { 0x989579, 0x39382E, 0x272720 },
            { 0x978A96, 0x393439, 0x272427 }, { 0x909090, 0x363636, 0x262626 }, { 0x979797, 0x393939, 0x262626 } };

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

    private static final int VAR = 5;

    private static final Map<String, Parser> INSTANCES = new HashMap<>();

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

    private final OS os = DEFAULT_OS;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final int[] widths = new int[] { 13, 6, 10, 10, 12, 10, 11, 10, 11, 11 };

    private final int[][][] offsets = new int[10][][];

    private final int[][][] colors = new int[10][][];

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

    protected final boolean compareColor(final int c1, final int c2, final int var) {
        return os.compareColor(c1, c2, var);
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
                if (!os.compareColor(actual, expected, VAR)) {
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

    protected final Integer parseNumber(final BufferedImage image, final int type, final Point start,
            final int maxSearchWidth) {
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

    protected final BufferedImage screenShot(final Area area) {
        return os.screenShot(area);
    }
}
