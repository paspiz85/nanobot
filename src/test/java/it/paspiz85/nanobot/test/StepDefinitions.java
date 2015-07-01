package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.EnemyInfo;
import it.paspiz85.nanobot.parsing.MainScreenParser;
import it.paspiz85.nanobot.parsing.Parser;
import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.imageio.ImageIO;

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

    public static class OSMock extends AbstractOSMock {

        private static OSMock instance;

        public static OSMock instance() {
            if (instance == null) {
                instance = new OSMock();
            }
            return instance;
        }

        private BufferedImage screenshot;

        @Override
        protected BufferedImage screenshot(final Point p1, final Point p2) {
            final int x1 = p1.x();
            final int y1 = p1.y();
            final int x2 = p2.x();
            final int y2 = p2.y();
            return screenshot.getSubimage(x1, y1, x2 - x1, y2 - y1);
        }

        public void setScreenshot(final BufferedImage screenshot) {
            this.screenshot = screenshot;
        }
    }

    private static final OS DEFAULT_OS;
    static {
        System.setProperty(OS.CLASS_PROPERTY, OSMock.class.getName());
        DEFAULT_OS = OS.instance();
    }

    protected final OS os = DEFAULT_OS;

    private BufferedImage screenshot;

    private EnemyInfo enemyInfo;

    private Boolean isCollectorsFull;

    private int[] troopsCount;

    private Point attackButtonPoint;

    private Point fullDarkElixirDrillPoint;

    private Point fullElixirCollectorPoint;

    private Point fullGoldMinePoint;

    private Boolean check;

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

    private Point parsePoint(final String coords) {
        Point point = null;
        if (!"null".equals(coords)) {
            final String[] split = coords.substring(1, coords.length() - 1).split(",");
            Assert.assertEquals(2, split.length);
            point = new Point(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()));
        }
        return point;
    }

    @Then("^attack button found at (.*)$")
    public void thenAttackButtonIs(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, attackButtonPoint);
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
    public void thenEnemyInfoFound(final Integer gold, final Integer elixir, final Integer darkelixir,
            final Integer trophyWin, final Integer thophyDefeat) {
        Assert.assertEquals(gold, enemyInfo.getGold());
        Assert.assertEquals(elixir, enemyInfo.getElixir());
        Assert.assertEquals(darkelixir, enemyInfo.getDarkElixir());
        Assert.assertEquals(trophyWin, enemyInfo.getTrophyWin());
        // TODO implement
        // Assert.assertEquals(thophyDefeat, enemyInfo.getTrophyDefeat());
    }

    @Then("^full dark elixir drill found at (.*)$")
    public void thenFullDarkElixirDrillFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, fullDarkElixirDrillPoint);
    }

    @Then("^full elixir collector found at (.*)$")
    public void thenFullElixirCollectorFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, fullElixirCollectorPoint);
    }

    @Then("^full gold mine found at (.*)$")
    public void thenFullGoldMineFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, fullGoldMinePoint);
    }

    @Then("^troops count is (.*)$")
    public void thenTroopsCountIs(final String troopsCount) {
        final String[] split = troopsCount.substring(1, troopsCount.length() - 1).split(",");
        final int[] expected = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            expected[i] = Integer.parseInt(split[i].trim());
        }
        Assert.assertArrayEquals(expected, this.troopsCount);
    }

    @When("^checking collectors$")
    public void whenCheckingCollectors() throws BotException {
        OSMock.instance.setScreenshot(screenshot);
        isCollectorsFull = Parser.getInstance(AttackScreenParser.class).isCollectorFullBase();
    }

    @When("^checking if camps are full$")
    public void whenCheckingIfCampsAreFull() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        check = Parser.getInstance(MainScreenParser.class).areCampsFull();
    }

    @When("^parsing enemy info$")
    public void whenParsingEnemyInfo() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        enemyInfo = Parser.getInstance(AttackScreenParser.class).parseEnemyInfo();
    }

    @When("^parsing troops$")
    public void whenParsingTroops() throws BotException {
        OSMock.instance.setScreenshot(screenshot);
        troopsCount = Parser.getInstance(AttackScreenParser.class).parseTroopCount();
    }

    @When("^searching attack button$")
    public void whenSearchingAttackButton() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        attackButtonPoint = Parser.getInstance(MainScreenParser.class).searchAttackButton();
    }

    @When("^searching full dark elixir drill$")
    public void whenSearchingFullDarkElixirDrill() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        fullDarkElixirDrillPoint = Parser.getInstance(MainScreenParser.class).searchFullDarkElixirDrill();
    }

    @When("^searching full elixir collector$")
    public void whenSearchingFullElixirCollector() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        fullElixirCollectorPoint = Parser.getInstance(MainScreenParser.class).searchFullElixirCollector();
    }

    @When("^searching full gold mine$")
    public void whenSearchingFullGoldMine() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        fullGoldMinePoint = Parser.getInstance(MainScreenParser.class).searchFullGoldMine();
    }
}
