package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.EnemyInfo;
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
        protected BufferedImage screenShot(final Point p1, final Point p2) {
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

    @Given("^enemy screenshot saved as (.*?)$")
    public void givenEnemyScreenshot(final String imagefile) throws IOException {
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

    @When("^enemy info found is (.*?), (.*?), (.*?), (.*?), (.*?)$")
    public void thenEnemyInfoFound(final Integer gold, final Integer elixir, final Integer darkelixir,
            final Integer trophyWin, final Integer thophyDefeat) {
        Assert.assertEquals(gold, enemyInfo.getGold());
        Assert.assertEquals(elixir, enemyInfo.getElixir());
        Assert.assertEquals(darkelixir, enemyInfo.getDarkElixir());
        Assert.assertEquals(trophyWin, enemyInfo.getTrophyWin());
        // TODOAssert.assertEquals(thophyDefeat, enemyInfo.getTrophyDefeat());
    }

    @When("^parsing enemy info$")
    public void whenParsingEnemyInfo() throws BotBadBaseException {
        OSMock.instance.setScreenshot(screenshot);
        enemyInfo = Parser.getInstance(AttackScreenParser.class).parseEnemyInfo();
    }
}
