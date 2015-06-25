package it.paspiz85.nanobot.win32;

import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Point;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;

public final class OS {

    private static OS instance;

    private static Random random = new Random();

    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static final String SYSTEM_OS = System.getProperty("os.name");

    public static final String USER_HOME_DIR = System.getProperty("user.home");

    public static final String USER_NAME = System.getProperty("user.name");

    public static final int VK_CONTROL = 0x11;

    public static final int VK_DOWN = 0x28;

    public static final int WM_COMMAND = 0x111;

    public static final int WM_KEYDOWN = 0x100;

    public static final int WM_KEYUP = 0x101;

    public static final int WM_LBUTTONDBLCLK = 0x203;

    public static final int WM_LBUTTONDOWN = 0x201;

    public static final int WM_LBUTTONUP = 0x202;

    public static final int WM_MOUSEWHEEL = 0x20A;

    // user32
    public static final int WM_NULL = 0x000;

    public static final int WM_RBUTTONDBLCLK = 0x206;

    public static final int WM_RBUTTONDOWN = 0x204;

    public static final int WM_RBUTTONUP = 0x205;

    public static final String WORKING_DIR = System.getProperty("user.dir");

    public static OS instance() {
        if (instance == null) {
            instance = new OS();
        }
        return instance;
    }

    private static int makeParam(final int low, final int high) {
        // to work for negative numbers
        return high << 16 | low << 16 >>> 16;
    }

    private static void msgBox(final String text, final String title) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static Random random() {
        return random;
    }

    private HWND handler;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private java.awt.Robot r;

    private OS() {
        try {
            r = new java.awt.Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean clientToScreen(final POINT clientPoint) {
        return User32.INSTANCE.ClientToScreen(handler, clientPoint);
    }

    public boolean compareColor(final int c1, final int c2, final int var) {
        final int r1 = c1 >> 16 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b1 = c1 >> 0 & 0xFF;
        final int b2 = c2 >> 0 & 0xFF;
        return !(Math.abs(r1 - r2) > var || Math.abs(g1 - g2) > var || Math.abs(b1 - b2) > var);
    }

    public boolean confirmationBox(final String msg, final String title) {
        return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public boolean isClickableActive(final Clickable clickable) {
        if (clickable.getColor() == null) {
            throw new IllegalArgumentException(clickable.name());
        }
        final int tarColor = clickable.getColor().getRGB();
        final int actualColor = pixelGetColor(clickable.getPoint()).getRGB();
        return compareColor(tarColor, actualColor, 5);
    }

    private boolean isCtrlKeyDown() {
        return User32.INSTANCE.GetKeyState(VK_CONTROL) < 0;
    }

    public void leftClick(final Clickable clickable, final int sleepInMs) throws InterruptedException {
        leftClickWin32(clickable.getPoint(), true);
        Thread.sleep(sleepInMs + random.nextInt(sleepInMs));
    }

    public void leftClick(final Point p) throws InterruptedException {
        leftClickWin32(p, false);
    }

    public void leftClick(final Point p, final int sleepInMs) throws InterruptedException {
        leftClickWin32(p, false);
        Thread.sleep(sleepInMs + random.nextInt(sleepInMs));
    }

    private void leftClickWin32(final Point p, final boolean randomize) throws InterruptedException {
        // randomize coordinates little bit
        int x = p.x();
        int y = p.y();
        if (randomize) {
            x += -1 + random.nextInt(3);
            y += -1 + random.nextInt(3);
        }
        logger.finest("clicking " + x + " " + y);
        final int lParam = makeParam(x, y);
        while (isCtrlKeyDown()) {
            Thread.sleep(100);
        }
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONDOWN, 0x00000001, lParam);
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONUP, 0x00000000, lParam);
    }

    public void msgBox(final String text) {
        msgBox(text, "");
    }

    public Color pixelGetColor(final Point p) {
        final POINT point = new POINT(p.x(), p.y());
        clientToScreen(point);
        final Color pixel = r.getPixelColor(point.x, point.y);
        return pixel;
    }

    public File saveImage(final BufferedImage img, final String filePathFirst, final String... filePathRest)
            throws IOException {
        final Path path = Paths.get(filePathFirst, filePathRest).toAbsolutePath();
        String fileName = path.getFileName().toString();
        if (!path.getFileName().toString().toLowerCase().endsWith(".png")) {
            fileName = path.getFileName().toString() + ".png";
        }
        final File file = new File(path.getParent().toString(), fileName);
        if (!file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        ImageIO.write(img, "png", file);
        return file;
    }

    public File saveScreenShot(final Area area, final String filePathFirst, final String... filePathRest)
            throws IOException {
        return saveScreenShot(area.getP1(), area.getP2(), filePathFirst, filePathRest);
    }

    public File saveScreenShot(final Point p1, final Point p2, final String filePathFirst, final String... filePathRest)
            throws IOException {
        final BufferedImage img = screenShot(p1, p2);
        return saveImage(img, filePathFirst, filePathRest);
    }

    public BufferedImage screenShot(final Area area) {
        return screenShot(area.getP1(), area.getP2());
    }

    public BufferedImage screenShot(final Point p1, final Point p2) {
        final POINT point = new POINT(p1.x(), p1.y());
        clientToScreen(point);
        return r.createScreenCapture(new Rectangle(point.x, point.y, p2.x() - p1.x(), p2.y() - p1.y()));
    }

    public void setupWin32(final HWND handler) {
        this.handler = handler;
    }

    public void sleepRandom(final int i) throws InterruptedException {
        Thread.sleep(i + random.nextInt(i));
    }

    public void sleepTillClickableIsActive(final Clickable clickable) throws InterruptedException {
        while (true) {
            if (isClickableActive(clickable)) {
                return;
            }
            Thread.sleep(random.nextInt(250) + 750);
        }
    }

    public void zoomUp() throws InterruptedException {
        zoomUp(14);
    }

    public void zoomUp(final int notch) throws InterruptedException {
        logger.info("Zooming out...");
        final int lParam = 0x00000001 | 0x50 /* scancode */<< 16 | 0x01000000 /* extended */;
        final WPARAM wparam = new WinDef.WPARAM(VK_DOWN);
        final LPARAM lparamDown = new WinDef.LPARAM(lParam);
        final LPARAM lparamUp = new WinDef.LPARAM(lParam | 1 << 30 | 1 << 31);
        for (int i = 0; i < notch; i++) {
            while (isCtrlKeyDown()) {
                Thread.sleep(100);
            }
            User32.INSTANCE.PostMessage(handler, WM_KEYDOWN, wparam, lparamDown);
            User32.INSTANCE.PostMessage(handler, WM_KEYUP, wparam, lparamUp);
            Thread.sleep(1000);
        }
    }
}
