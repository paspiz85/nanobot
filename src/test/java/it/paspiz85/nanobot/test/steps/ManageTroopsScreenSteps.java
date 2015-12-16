package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.game.TroopsInfo;
import junit.framework.AssertionFailedError;

import org.junit.Assert;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ManageTroopsScreenSteps {

    @Then("^troops count is (.*)$")
    public void thenTroopsCountIs(final String troopsCount) {
        final TroopsInfo expected = TroopsInfo.parse(troopsCount);
        final TroopsInfo actual = ScenarioContext.get("troopsInfo", TroopsInfo.class);
        try {
            Assert.assertEquals(expected, actual);
        } catch (final AssertionError e) {
            throw new AssertionFailedError(String.format("expected <%s> but was <%s>", expected.toString(),
                    actual.toString()));
        }
    }

    @When("^checking if camps are full$")
    public void whenCheckingIfCampsAreFull() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final Boolean check = Screen.getInstance(ManageTroopsScreen.class).areCampsFull();
        ScenarioContext.put("check", check);
    }

    @When("^counting troops$")
    public void whenCountingTroops() throws BotException {
        GenericSteps.initScreenshotMock();
        final TroopsInfo troopsInfo = Screen.getInstance(ManageTroopsScreen.class).parseTroopsInfo();
        ScenarioContext.put("troopsInfo", troopsInfo);
    }

    @When("^searching close troops button point$")
    public void whenSearchingCloseTroopsButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        // TODO
        // final Point point =
        // Screen.getInstance(ManageTroopsScreen.class).searchButtonTrainClose();
        // ScenarioContext.put("point", point);
    }
}
