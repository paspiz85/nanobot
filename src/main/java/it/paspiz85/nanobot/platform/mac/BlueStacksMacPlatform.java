package it.paspiz85.nanobot.platform.mac;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.platform.AbstractPlatform;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.util.OS;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
public final class BlueStacksMacPlatform extends AbstractPlatform {

    public static final Size BS_SIZE = new Size(Platform.GAME_SIZE.x(), Platform.GAME_SIZE.y() + 47);

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    private static final int TITLE_BAR_HEIGHT = 22;

    public static Platform instance() {
        // return Utils.singleton(UnsupportedPlatform.class, () ->
        // UnsupportedPlatform.instance());
        return Utils.singleton(BlueStacksMacPlatform.class, () -> new BlueStacksMacPlatform());
    }

    private static BlueStacksMacPlatform instanceNew() {
        return Utils.singleton(BlueStacksMacPlatform.class, () -> new BlueStacksMacPlatform());
    }

    public static boolean isSupported() {
        return OS.getCurrent().getFamily() == OS.Family.MAC;
    }

    private Point position;

    private final Robot robot;

    private final ScriptEngineManager scriptEngineManager;

    private BlueStacksMacPlatform() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("Unable to init robot", e);
        }
        scriptEngineManager = new ScriptEngineManager();
    }

    private Point clientToScreen(final Point point) {
        final int x = point.x() + position.x();
        final int y = point.y() + position.y() + TITLE_BAR_HEIGHT + 3;
        return new Point(x, y);
    }

    @Override
    protected void doActivate() {
        try {
            final ScriptEngine engine = scriptEngineManager.getEngineByName("AppleScript");
            final String script = "tell application \"BlueStacks\" to activate\n";
            engine.eval(script);
        } catch (final ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    protected void doApplySize(final Size size) throws BotConfigurationException {
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
            if (!current.equals(size)) {
                widthNode.setTextContent(Integer.toString(size.x()));
                heightNode.setTextContent(Integer.toString(size.y()));
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

    @Override
    protected void doKeyPress(final int keyCode, final boolean shifted) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    protected void doLeftClick(final Point point) throws InterruptedException {
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
    protected BufferedImage doScreenshot(final Point p1, final Point p2) {
        final Point anchor = clientToScreen(p1);
        final int width = p2.x() - p1.x();
        final int height = p2.y() - p1.y();
        return robot.createScreenCapture(new Rectangle(anchor.x(), anchor.y(), width, height));
    }

    @Override
    protected void doSingleZoomUp() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_DOWN);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_DOWN);
    }

    @Override
    protected void doWrite(final String s) throws InterruptedException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    protected Size getActualSize() {
        Size size = null;
        try {
            final ScriptEngine engine = scriptEngineManager.getEngineByName("AppleScript");
            final String script = "tell application \"System Events\" to tell application process \"BlueStacks\"\n"
                    + "get size of front window\n" + "end tell\n";
            @SuppressWarnings("unchecked")
            final List<? extends Number> result = (List<? extends Number>) engine.eval(script);
            size = new Size(result.get(0).intValue(), result.get(1).intValue() - TITLE_BAR_HEIGHT);
        } catch (final ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            size = null;
        }
        return size;
    }

    @Override
    protected Color getColor(final Point point) {
        final Point screenPoint = clientToScreen(point);
        return robot.getPixelColor(screenPoint.x(), screenPoint.y());
    }

    @Override
    public Size getExpectedSize() {
        return BS_SIZE;
    }

    private Point getPosition() {
        Point position = null;
        try {
            final ScriptEngine engine = scriptEngineManager.getEngineByName("AppleScript");
            final String script = "tell application \"System Events\" to tell application process \"BlueStacks\"\n"
                    + "get position of front window\n" + "end tell\n";
            @SuppressWarnings("unchecked")
            final List<? extends Number> result = (List<? extends Number>) engine.eval(script);
            position = new Point(result.get(0).intValue(), result.get(1).intValue());
        } catch (final ScriptException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            position = null;
        }
        return position;
    }

    @Override
    protected boolean registerForClick(final Consumer<Point> clickConsumer) {
        // TODO implement but not now (it's not used from main logic)
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    protected void setup() throws BotConfigurationException {
        position = getPosition();
    }
}
