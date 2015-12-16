package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.game.BattleEndScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;
import cucumber.api.java.en.When;

public class BattleEndScreenSteps {

    @When("^searching end battle return home button point$")
    public void whenSearchingEndButtonReturnHomeButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        Point point = null;
        if (Screen.getInstance(BattleEndScreen.class).isDisplayed()) {
            point = Screen.getInstance(BattleEndScreen.class).getButtonReturnHome();
        }
        ScenarioContext.put("point", point);
    }
}
