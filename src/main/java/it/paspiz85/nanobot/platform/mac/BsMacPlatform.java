package it.paspiz85.nanobot.platform.mac;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.platform.AbstractPlatform;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Size;
import it.paspiz85.nanobot.util.Utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * Platform implementation of BlueStacks for Mac.
 *
 * @author paspiz85
 *
 */
public final class BsMacPlatform extends AbstractPlatform {

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    public static BsMacPlatform instance() {
        return Utils.singleton(BsMacPlatform.class, () -> new BsMacPlatform());
    }

    @Deprecated
    public static void main(final String... args) throws Exception {
        // TODO remove this method
        Thread.sleep(3000);
        // BsMacPlatform.instance().saveScreenshot("tmp");
        // BsMacPlatform.instance().applyResolution(new Size(860, 720));
        // BsMacPlatform.instance().leftClick(new Point(1, 1));
        // BsMacPlatform.instance().zoomUp();
        System.out.println("bye");
    }

    private final Robot robot;

    private BsMacPlatform() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("Unable to init robot", e);
        }
    }

    @Override
    protected void applyResolution(final Size resolution) throws BotConfigurationException {
        try {
            final File plistFile = new File(System.getProperty("user.home")
                    + "/Library/Preferences/com.BlueStacks.AppPlayer.plist");
            final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            final XPathFactory xpathFactory = XPathFactory.newInstance();
            final XPath xpath = xpathFactory.newXPath();
            final TransformerFactory transformerFactor = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactor.newTransformer();
            final Document document = docBuilder.parse(plistFile);
            final Element frameBufferNode = (Element) xpath.evaluate("/plist/dict"
                    + "/key[text()='Guests']/following-sibling::dict[1]"
                    + "/key[text()='Android']/following-sibling::dict[1]"
                    + "/key[text()='FrameBuffer']/following-sibling::dict[1]", document, XPathConstants.NODE);
            final Element widthNode = (Element) xpath.evaluate("key[text()='Width']/following-sibling::integer[1]",
                    frameBufferNode, XPathConstants.NODE);
            final Element heightNode = (Element) xpath.evaluate("key[text()='Height']/following-sibling::integer[1]",
                    frameBufferNode, XPathConstants.NODE);
            final Size current = new Size(Integer.parseInt(widthNode.getTextContent()), Integer.parseInt(heightNode
                    .getTextContent()));
            if (!current.equals(resolution)) {
                widthNode.setTextContent(Integer.toString(resolution.x()));
                heightNode.setTextContent(Integer.toString(resolution.y()));
                final DocumentType documentType = document.getDoctype();
                if (documentType != null) {
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, documentType.getSystemId());
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, documentType.getPublicId());
                }
                transformer.transform(new DOMSource(document), new StreamResult(plistFile));
            }
            throw new BotConfigurationException(String.format("Please restart %s to fix resolution", BS_WINDOW_NAME));
        } catch (BotConfigurationException | RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Point clientToScreen(final Point point) {
        // TODO Auto-generated method stub
        // use saved position of BlueStacks windows (see setup)
        Point result = new Point(point.x(), point.y() + 48);
        return result;
    }

    @Override
    protected Color getColor(final Point point) {
        final Point screenPoint = clientToScreen(point);
        return robot.getPixelColor(screenPoint.x(), screenPoint.y());
    }

    @Override
    protected Size getCurrentResolution() {
        // TODO implement
        return RESOLUTION;
    }

    @Override
    protected String getName() {
        return BS_WINDOW_NAME;
    }

    @Override
    protected void leftClick(final Point point) throws InterruptedException {
        // TODO non funziona
        final PointerInfo a = MouseInfo.getPointerInfo();
        final java.awt.Point b = a.getLocation();
        final int xOrig = (int) b.getX();
        final int yOrig = (int) b.getY();
        try {
            final Point p = clientToScreen(point);
            robot.mouseMove(p.x(), p.y());
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } finally {
            robot.mouseMove(xOrig, yOrig);
        }
    }

    @Override
    @Deprecated
    protected boolean registerForClick(final Consumer<Point> clickConsumer) {
        // TODO implement but not now (it's not used from main logic)
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    protected BufferedImage screenshot(final Point p1, final Point p2) {
        final Point anchor = clientToScreen(p1);
        final int width = p2.x() - p1.x();
        final int height = p2.y() - p1.y();
        return robot.createScreenCapture(new Rectangle(anchor.x(), anchor.y(), width, height));
    }

    @Override
    protected void setup() throws BotConfigurationException {
        // TODO implement
        // save current position of BlueStacks window in a variable
    }

    @Override
    protected void singleZoomUp() throws InterruptedException {
        // TODO set focus on BlueStacks window
        robot.keyPress(KeyEvent.VK_DOWN);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_DOWN);
    }
}
