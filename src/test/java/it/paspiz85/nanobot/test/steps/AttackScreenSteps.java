package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.AttackScreen;
import it.paspiz85.nanobot.game.EnemyInfo;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;

import org.junit.Assert;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AttackScreenSteps {

    @Then("^collectors is (.*)$")
    public void thenCollectorIs(final Boolean full) {
        final Boolean isCollectorsFull = ScenarioContext.get("isCollectorsFull", Boolean.class);
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
        final EnemyInfo enemyInfo = ScenarioContext.get("enemyInfo", EnemyInfo.class);
        Assert.assertEquals(expected, enemyInfo);
        // Parser.getInstance(AttackScreenParserLearner.class).parseAndCheckEnemyInfo(expected);
    }

    @When("^checking collectors$")
    public void whenCheckingCollectors() throws BotException {
        GenericSteps.initScreenshotMock();
        final Boolean isCollectorsFull = Screen.getInstance(AttackScreen.class).isCollectorFullBase();
        ScenarioContext.put("isCollectorsFull", isCollectorsFull);
    }

    @When("^parsing enemy info$")
    public void whenParsingEnemyInfo() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final EnemyInfo enemyInfo = Screen.getInstance(AttackScreen.class).parseEnemyInfo();
        ScenarioContext.put("enemyInfo", enemyInfo);
    }

    @When("^searching next button point$")
    public void whenSearchingNexButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final Point point = Screen.getInstance(AttackScreen.class).searchButtonNext();
        ScenarioContext.put("point", point);
    }
}
