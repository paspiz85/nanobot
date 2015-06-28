package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.AttackScreenParser;
import it.paspiz85.nanobot.parsing.EnemyInfo;
import it.paspiz85.nanobot.parsing.Parser;

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
        final int x1 = Area.ENEMY_LOOT.getP1().x();
        final int y1 = Area.ENEMY_LOOT.getP1().y();
        final int x2 = Area.ENEMY_LOOT.getP2().x();
        final int y2 = Area.ENEMY_LOOT.getP2().y();
        final BufferedImage lootScreenshot = screenshot.getSubimage(x1, y1, x2 - x1, y2 - y1);
        enemyInfo = Parser.getInstance(AttackScreenParser.class).parseEnemyInfo(lootScreenshot);
    }
}
