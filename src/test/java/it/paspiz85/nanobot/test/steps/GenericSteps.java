package it.paspiz85.nanobot.test.steps;

import it.paspiz85.nanobot.platform.PlatformResolver;
import it.paspiz85.nanobot.platform.UnsupportedPlatform;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * Step definition for tests.
 *
 * @author paspiz85
 */
public class GenericSteps {

    public static class PlatformMock extends UnsupportedPlatform {

        public static PlatformMock instance() {
            return Utils.singleton(PlatformMock.class, () -> new PlatformMock());
        }

        private BufferedImage screenshot;

        @Override
        protected Color getColor(final Point point) {
            return new Color(screenshot.getRGB(point.x(), point.y()));
        }

        @Override
        protected BufferedImage doScreenshot(final Point p1, final Point p2) {
            return getSubimage(screenshot, p1, p2);
        }

        public void setScreenshot(final BufferedImage screenshot) {
            this.screenshot = screenshot;
        }
    }

    static {
        PlatformResolver.instance().setPreferredPlatform(PlatformMock.class);
    }

    public static void initScreenshotMock() {
        final BufferedImage screenshot = ScenarioContext.get("screenshot", BufferedImage.class);
        PlatformMock.instance().setScreenshot(screenshot);
        ;
    }

    @Given("^screenshot saved as (.*)$")
    public void givenScreenshot(final String imagefile) throws IOException {
        BufferedImage screenshot;
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
        ScenarioContext.put("screenshot", screenshot);
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
        Assert.assertEquals(check, ScenarioContext.get("check", Boolean.class));
    }

    @Then("^point found at (.*)$")
    public void thenPointFoundAt(final String coords) {
        final Point point = parsePoint(coords);
        Assert.assertEquals(point, ScenarioContext.get("point", Point.class));
    }

    @Then("^points found are (.*)$")
    public void thenPointsFoundAre(final String pointset) {
        // os.saveImage(screenshot, "test");
        final String[] coords = pointset.split(";");
        final TreeSet<Point> expected = new TreeSet<Point>();
        for (final String c : coords) {
            final Point point = parsePoint(c.trim());
            if (point != null) {
                expected.add(point);
            }
        }
        final Object actual = ScenarioContext.get("pointset");
        Assert.assertEquals(expected, actual);
    }
}
