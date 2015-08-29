package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.game.PlatformScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;
import cucumber.api.java.en.When;

public class PlatformScreenSteps {

    @When("^searching play game button point$")
    public void whenSearchingEndButtonReturnHomeButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final Point point = Screen.getInstance(PlatformScreen.class).getButtonPlayGame();
        ScenarioContext.put("point", point);
    }
}
