package it.paspiz85.nanobot.test;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Loot;
import it.paspiz85.nanobot.parsing.Parsers;

import java.awt.image.BufferedImage;
import java.io.IOException;

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

    private Loot loot;

    @Given("^enemy screenshot saved as (.*?)$")
    public void givenEnemyScreenshot(final String imagefile) throws IOException {
        screenshot = ImageIO.read(getClass().getResourceAsStream("/screenshot/" + imagefile));
    }

    @When("^loot found is (.*?), (.*?), (.*?)$")
    public void thenLootFound(final Integer gold, final Integer elixir, final Integer darkelixir) {
        Assert.assertEquals(gold, loot.getGold());
        Assert.assertEquals(elixir, loot.getElixir());
        Assert.assertEquals(darkelixir, loot.getDarkElixir());
    }

    @When("^parsing loot$")
    public void whenParsingLoot() throws BotBadBaseException {
        final int x1 = Area.ENEMY_LOOT.getP1().x();
        final int y1 = Area.ENEMY_LOOT.getP1().y();
        final int x2 = Area.ENEMY_LOOT.getP2().x();
        final int y2 = Area.ENEMY_LOOT.getP2().y();
        final BufferedImage lootScreenshot = screenshot.getSubimage(x1, y1, x2 - x1, y2 - y1);
        loot = Parsers.getAttackScreen().parseLoot(lootScreenshot);
    }
}
