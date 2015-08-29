package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.game.ManageTroopsScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;

import java.util.Arrays;

import junit.framework.AssertionFailedError;

import org.junit.Assert;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ManageTroopsScreenSteps {

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
        final int[] actual = ScenarioContext.get("troopsCount", int[].class);
        try {
            Assert.assertArrayEquals(expected, actual);
        } catch (final AssertionError e) {
            throw new AssertionFailedError(String.format("expected <%s> but was <%s>", Arrays.toString(expected),
                    Arrays.toString(actual)));
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
        final int[] troopsCount = Screen.getInstance(ManageTroopsScreen.class).parseTroopsInfo().getTroopsCount();
        ScenarioContext.put("troopsCount", troopsCount);
    }

    @When("^searching close troops button point$")
    public void whenSearchingCloseTroopsButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final Point point = Screen.getInstance(ManageTroopsScreen.class).searchButtonTrainClose();
        ScenarioContext.put("point", point);
    }
}
