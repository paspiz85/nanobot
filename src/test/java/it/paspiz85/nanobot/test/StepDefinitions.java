package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.EnemyInfo;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.platform.PlatformResolver;
import it.paspiz85.nanobot.platform.UnsupportedPlatform;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import junit.framework.AssertionFailedError;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step definition for tests.
 *
 * @author paspiz85
 */
public class StepDefinitions {

    public static class PlatformMock extends UnsupportedPlatform {

        public static PlatformMock instance() {
            return Utils.singleton(PlatformMock.class, () -> new PlatformMock());
        }

        private BufferedImage screenshot;

        @Override
        protected Color getColor(final Point point) {
            return new Color(screenshot.getRGB(point.x(), point.y()));
        }

        @Override
        protected BufferedImage screenshot(final Point p1, final Point p2) {
            return getSubimage(screenshot, p1, p2);
        }

        public void setScreenshot(final BufferedImage screenshot) {
            this.screenshot = screenshot;
        }
    }

    static {
        PlatformResolver.instance().setPreferredPlatform(PlatformMock.class);
    }

    private Boolean check;

    private EnemyInfo enemyInfo;

    private Boolean isCollectorsFull;

    protected final Platform platform = Platform.instance();

    private Point point;

    private TreeSet<Point> pointset;

    private BufferedImage screenshot;

    private int[] troopsCount;

    @Given("^screenshot saved as (.*)$")
    public void givenScreenshot(final String imagefile) throws IOException {
        final URI uri = URI.create(imagefile);
        switch (uri.getScheme()) {
        case "classpath":
            try (InputStream inStream = getClass().getResourceAsStream(uri.getPath())) {
                screenshot = ImageIO.read(inStream);
            }
            break;
        default:
            try (InputStream inStream = new FileInputStream(uri.getPath())) {
                screenshot = ImageIO.read(inStream);
            }
            break;
        }
    }

    private void markPointFound(final int x, final int y, final int rgb) {
        screenshot.setRGB(x, y, rgb);
        screenshot.setRGB(x - 1, y - 1, rgb);
        screenshot.setRGB(x - 2, y - 2, rgb);
        screenshot.setRGB(x - 1, y + 1, rgb);
        screenshot.setRGB(x - 2, y + 2, rgb);
        screenshot.setRGB(x + 1, y - 1, rgb);
        screenshot.setRGB(x + 2, y - 2, rgb);
        screenshot.setRGB(x + 1, y + 1, rgb);
        screenshot.setRGB(x + 2, y + 2, rgb);
    }

    private Point parsePoint(final String coords) {
        Point point = null;
        if (!"null".equals(coords)) {
            final String[] split = coords.substring(1, coords.length() - 1).split(",");
            Assert.assertEquals(2, split.length);
            point = new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
        }
        return point;
    }

    @Then("^check is (.*)$")
    public void thenCheckIs(final Boolean check) {
        Assert.assertEquals(check, this.check);
    }

    @Then("^collectors is (.*)$")
    public void thenCollectorIs(final Boolean full) {
        Assert.assertEquals(full, isCollectorsFull);
    }

    @Then("^enemy info found is (.*), (.*), (.*), (.*), (.*)$")
    public void thenEnemyInfoFound(final String gold, final String elixir, final String darkelixir,
            final String trophyWin, final String thophyDefeat) throws BotBadBaseException {
        final EnemyInfo expected = new EnemyInfo();
        if (!"null".equals(gold)) {
            expected.setGold(new Integer(gold));
        }
        if (!"null".equals(elixir)) {
            expected.setElixir(new Integer(elixir));
        }
        if (!"null".equals(darkelixir)) {
            expected.setDarkElixir(new Integer(darkelixir));
        }
        if (!"null".equals(trophyWin)) {
            expected.setTrophyWin(new Integer(trophyWin));
        }
        if (!"null".equals(thophyDefeat)) {
            expected.setTrophyDefeat(new Integer(thophyDefeat));
        }
        Assert.assertEquals(expected, enemyInfo);
        // Parser.getInstance(AttackScreenParserLearner.class).parseAndCheckEnemyInfo(expected);
    }

    @Then("^point found at (.*)$")
    public void thenPointFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, this.point);
    }

    @Then("^points found are (.*)$")
    public void thenPointsFoundAre(final String pointset) {
        // os.saveImage(screenshot, "test");
        final String[] coords = pointset.split(";");
        final TreeSet<Point> expected = new TreeSet<Point>();
        for (final String c : coords) {
            final Point point = parsePoint(c.trim());
            if (point != null) {
                expected.add(point);
            }
        }
        Assert.assertEquals(expected, this.pointset);
    }

    @Then("^troops count is (.*)$")
    public void thenTroopsCountIs(final String troopsCount) {
        final String arr = troopsCount.substring(1, troopsCount.length() - 1).trim();
        final int[] expected;
        if (arr.length() == 0) {
            expected = new int[0];
        } else {
            final String[] split = arr.split(",");
            expected = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                expected[i] = Integer.parseInt(split[i].trim());
            }
        }
        try {
            Assert.assertArrayEquals(expected, this.troopsCount);
        } catch (final AssertionError e) {
            throw new AssertionFailedError(String.format("expected <%s> but was <%s>", Arrays.toString(expected),
                    Arrays.toString(this.troopsCount)));
        }
    }

    @When("^checking collectors$")
    public void whenCheckingCollectors() throws BotException {
        PlatformMock.instance().setScreenshot(screenshot);
        isCollectorsFull = Parser.getInstance(AttackScreenParser.class).isCollectorFullBase();
    }

    @When("^checking if camps are full$")
    public void whenCheckingIfCampsAreFull() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        check = Parser.getInstance(MainScreenParser.class).areCampsFull();
    }

    @When("^counting troops$")
    public void whenCountingTroops() throws BotException {
        PlatformMock.instance().setScreenshot(screenshot);
        troopsCount = Parser.getInstance(MainScreenParser.class).parseTroopsInfo().getTroopsCount();
    }

    @When("^parsing enemy info$")
    public void whenParsingEnemyInfo() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        enemyInfo = Parser.getInstance(AttackScreenParser.class).parseEnemyInfo();
    }

    @When("^searching attack button point$")
    public void whenSearchingAttackButtonPoint() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonAttack();
    }

    @When("^searching close troops button point$")
    public void whenSearchingCloseTroopsButtonPoint() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonTrainClose();
    }

    @When("^searching full dark elixir drill points$")
    public void whenSearchingFullDarkElixirDrillPoints() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        pointset = new TreeSet<>();
        while (true) {
            final Point point = Parser.getInstance(MainScreenParser.class).searchFullDarkElixirDrill();
            if (point == null) {
                break;
            }
            pointset.add(point);
            markPointFound(point.x(), point.y(), 0xFFFFFF);
        }
    }

    @When("^searching full elixir collector points$")
    public void whenSearchingFullElixirCollectorPoints() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        pointset = new TreeSet<>();
        while (true) {
            final Point point = Parser.getInstance(MainScreenParser.class).searchFullElixirCollector();
            if (point == null) {
                break;
            }
            pointset.add(point);
            markPointFound(point.x(), point.y(), 0xFFFF00);
        }
    }

    @When("^searching full gold mine points$")
    public void whenSearchingFullGoldMinePoints() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        pointset = new TreeSet<>();
        while (true) {
            final Point point = Parser.getInstance(MainScreenParser.class).searchFullGoldMine();
            if (point == null) {
                break;
            }
            pointset.add(point);
            markPointFound(point.x(), point.y(), 0xFF);
        }
    }

    @When("^searching next button point$")
    public void whenSearchingNexButtonPoint() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        point = Parser.getInstance(AttackScreenParser.class).searchButtonNext();
    }

    @When("^searching end battle return home button point$")
    public void whenSearchingEndButtonReturnHomeButtonPoint() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        point = Parser.getInstance(AttackScreenParser.class).searchButtonEndBattleReturnHome();
    }

    @When("^searching troops button point$")
    public void whenSearchingTroopsButtonPoint() throws BotBadBaseException {
        PlatformMock.instance().setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonTroops();
    }
}
