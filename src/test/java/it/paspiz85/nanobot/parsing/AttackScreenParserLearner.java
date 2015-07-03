package it.paspiz85.nanobot.parsing;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class AttackScreenParserLearner extends AttackScreenParser {

    private final ThreadLocal<Integer> parseNumberExpected = new ThreadLocal<Integer>();

    private Integer parseAndCheckDigit(final BufferedImage image, final Point start, final int type,
            final Integer expectedResult) {
        Integer result = null;
        for (int i = 0; i < 10; i++) {
            boolean found = true;
            for (int j = 0; j < offsets[i].length; j++) {
                final int actual = image.getRGB(start.x() + offsets[i][j][0], start.y() + offsets[i][j][1]);
                final int expected = colors[i][type][j];
                if (!os.compareColor(new Color(actual), new Color(expected), VAR)) {
                    if (expectedResult != null && expectedResult == i) {
                        org.junit.Assert.fail(String.format("Wrong colors[%d][%d][%d] : expected = %s , actual = %s",
                                i, type, j, Integer.toHexString(expected).toUpperCase(), Integer.toHexString(actual)
                                        .toUpperCase()));
                    }
                    found = false;
                    break;
                }
                if (expectedResult != null && expectedResult == i) {
                    image.setRGB(start.x() + offsets[i][j][0], start.y() + offsets[i][j][1], 0xFF);
                }
            }
            if (found) {
                result = i;
                break;
            }
        }
        return result;
    }

    public EnemyInfo parseAndCheckEnemyInfo(final EnemyInfo expected) throws BotBadBaseException {
        final BufferedImage image = os.screenshot(ENEMY_LOOT);
        final String id = "parsing_" + System.currentTimeMillis();
        os.saveImage(image, id);
        try {
            final EnemyInfo info = new EnemyInfo();
            parseNumberExpected.set(expected.getGold());
            info.setGold(parseGold(image));
            parseNumberExpected.set(expected.getElixir());
            info.setElixir(parseElixir(image));
            parseNumberExpected.set(expected.getDarkElixir());
            info.setDarkElixir(parseDarkElixir(image));
            parseNumberExpected.set(expected.getTrophyWin());
            info.setTrophyWin(parseTrophyWin(image));
            parseNumberExpected.set(expected.getTrophyDefeat());
            info.setTrophyDefeat(parseTrophyDefeat(image));
            return info;
        } finally {
            os.saveImage(image, id + "_after");
            parseNumberExpected.set(null);
        }
    }

    protected final Integer parseAndCheckNumber(final BufferedImage image, final int type, final Point start,
            final int maxSearchWidth, final Integer expectedResult) {
        String no = "";
        String expectedNo = expectedResult == null ? "" : expectedResult.toString();
        int curr = start.x();
        while (curr < start.x() + maxSearchWidth) {
            final Integer expectedDigit = expectedNo.length() == 0 ? null : new Integer(expectedNo.substring(0, 1));
            if (expectedDigit != null) {
                expectedNo = expectedNo.substring(1);
            }
            final Integer i = parseAndCheckDigit(image, new Point(curr, start.y()), type, expectedDigit);
            if (i != null) {
                no += i;
                curr += widths[i] - 1;
            } else {
                curr++;
            }
        }
        return no.isEmpty() ? null : Integer.parseInt(no);
    }

    @Override
    protected Integer parseNumber(final BufferedImage image, final int type, final Point start, final int maxSearchWidth) {
        Integer result;
        final Integer expected = parseNumberExpected.get();
        if (expected == null) {
            result = super.parseNumber(image, type, start, maxSearchWidth);
        } else {
            result = parseAndCheckNumber(image, type, start, maxSearchWidth, expected);
        }
        return result;
    }
}
