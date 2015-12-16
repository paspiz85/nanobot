package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.exception.BotBadBaseException;
import it.paspiz85.nanobot.game.MainScreen;
import it.paspiz85.nanobot.game.Screen;
import it.paspiz85.nanobot.util.Point;

import java.awt.image.BufferedImage;
import java.util.TreeSet;
import java.util.function.Supplier;

import cucumber.api.java.en.When;

public class MainScreenSteps {

    private TreeSet<Point> searchPointset(final Supplier<Point> pointSupplier, final int markRGB) {
        final TreeSet<Point> pointset = new TreeSet<>();
        while (true) {
            final Point point = pointSupplier.get();
            if (point == null) {
                break;
            }
            pointset.add(point);
            final BufferedImage screenshot = ScenarioContext.get("screenshot", BufferedImage.class);
            final int x = point.x();
            final int y = point.y();
            screenshot.setRGB(x, y, markRGB);
            screenshot.setRGB(x - 1, y - 1, markRGB);
            screenshot.setRGB(x - 2, y - 2, markRGB);
            screenshot.setRGB(x - 1, y + 1, markRGB);
            screenshot.setRGB(x - 2, y + 2, markRGB);
            screenshot.setRGB(x + 1, y - 1, markRGB);
            screenshot.setRGB(x + 2, y - 2, markRGB);
            screenshot.setRGB(x + 1, y + 1, markRGB);
            screenshot.setRGB(x + 2, y + 2, markRGB);
        }
        return pointset;
    }

    @When("^searching attack button point$")
    public void whenSearchingAttackButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        // TODO
        // final Point point =
        // Screen.getInstance(MainScreen.class).searchButtonAttack();
        // ScenarioContext.put("point", point);
    }

    @When("^searching full dark elixir drill points$")
    public void whenSearchingFullDarkElixirDrillPoints() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final TreeSet<Point> pointset = searchPointset(() -> Screen.getInstance(MainScreen.class)
                .searchFullDarkElixirDrill(), 0xFFFFFF);
        ScenarioContext.put("pointset", pointset);
    }

    @When("^searching full elixir collector points$")
    public void whenSearchingFullElixirCollectorPoints() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final TreeSet<Point> pointset = searchPointset(() -> Screen.getInstance(MainScreen.class)
                .searchFullElixirCollector(), 0xFFFF00);
        ScenarioContext.put("pointset", pointset);
    }

    @When("^searching full gold mine points$")
    public void whenSearchingFullGoldMinePoints() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        final TreeSet<Point> pointset = searchPointset(() -> Screen.getInstance(MainScreen.class).searchFullGoldMine(),
                0xFF);
        ScenarioContext.put("pointset", pointset);
    }

    @When("^searching troops button point$")
    public void whenSearchingTroopsButtonPoint() throws BotBadBaseException {
        GenericSteps.initScreenshotMock();
        // TODO
        // final Point point =
        // Screen.getInstance(MainScreen.class).searchButtonTroops();
        // ScenarioContext.put("point", point);
    }
}
