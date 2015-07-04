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
            return getSubimage(screenshot, p1, p2);
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

    private Boolean check;

    private EnemyInfo enemyInfo;

    private Boolean isCollectorsFull;

    protected final OS os = DEFAULT_OS;

    private Point point;

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
        // TODO collecting delete
        expected.setGold(null);
        expected.setElixir(null);
        Assert.assertEquals(expected, enemyInfo);
        // Parser.getInstance(AttackScreenParserLearner.class).parseAndCheckEnemyInfo(expected);
    }

    @Then("^point found at (.*)$")
    public void thenPointFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, this.point);
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

    @When("^searching attack button point$")
    public void whenSearchingAttackButtonPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonAttack();
    }

    @When("^searching close troops button point$")
    public void whenSearchingCloseTroopsButtonPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonTrainClose();
    }

    @When("^searching full dark elixir drill point$")
    public void whenSearchingFullDarkElixirDrillPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchFullDarkElixirDrill();
    }

    @When("^searching full elixir collector point$")
    public void whenSearchingFullElixirCollectorPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchFullElixirCollector();
    }

    @When("^searching full gold mine point$")
    public void whenSearchingFullGoldMinePoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchFullGoldMine();
    }

    @When("^searching next button point$")
    public void whenSearchingNexButtonPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(AttackScreenParser.class).searchButtonNext();
    }

    @When("^searching troops button point$")
    public void whenSearchingTroopsButtonPoint() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        point = Parser.getInstance(MainScreenParser.class).searchButtonTroops();
    }
}
