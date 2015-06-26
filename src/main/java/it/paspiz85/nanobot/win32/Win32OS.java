package it.paspiz85.nanobot.win32;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.parsing.Area;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

public final class Win32OS implements OS, Constants {

    private static Win32OS instance;

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

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    public static Win32OS instance() {
        if (instance == null) {
            instance = new Win32OS();
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

    private HWND handler;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private java.awt.Robot robot;

    private HWND bsHwnd;

    private Win32OS() {
        try {
            robot = new java.awt.Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }
    }

    private Point clientToScreen(final Point clientPoint) {
        final POINT point = new POINT(clientPoint.x(), clientPoint.y());
        User32.INSTANCE.ClientToScreen(handler, point);
        return new Point(point.x, point.y);
    }

    @Deprecated
    private boolean clientToScreene(final POINT clientPoint) {
        return User32.INSTANCE.ClientToScreen(handler, clientPoint);
    }

    private boolean compareColor(final Color c1, final Color c2, final int var) {
        return compareColor(c1.getRGB(), c2.getRGB(), var);
    }

    @Override
    public boolean compareColor(final int c1, final int c2, final int var) {
        final int r1 = c1 >> 16 & 0xFF;
        final int r2 = c2 >> 16 & 0xFF;
        final int g1 = c1 >> 8 & 0xFF;
        final int g2 = c2 >> 8 & 0xFF;
        final int b1 = c1 >> 0 & 0xFF;
        final int b2 = c2 >> 0 & 0xFF;
        return !(Math.abs(r1 - r2) > var || Math.abs(g1 - g2) > var || Math.abs(b1 - b2) > var);
    }

    @Override
    public boolean confirmationBox(final String msg, final String title) {
        return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public boolean isClickableActive(final Clickable clickable) {
        if (clickable.getColor() == null) {
            throw new IllegalArgumentException(clickable.name());
        }
        Point point = clickable.getPoint();
        point = clientToScreen(point);
        final Color actualColor = robot.getPixelColor(point.x(), point.y());
        return compareColor(clickable.getColor(), actualColor, 5);
    }

    private boolean isCtrlKeyDown() {
        return User32.INSTANCE.GetKeyState(VK_CONTROL) < 0;
    }

    @Override
    public void leftClick(final Clickable clickable, final boolean randomize) throws InterruptedException {
        leftClick(clickable.getPoint(), randomize);
    }

    @Override
    public void leftClick(final Point point, final boolean randomize) throws InterruptedException {
        // randomize coordinates little bit
        int x = point.x();
        int y = point.y();
        if (randomize) {
            x += -1 + RANDOM.nextInt(3);
            y += -1 + RANDOM.nextInt(3);
        }
        logger.fine("clicking " + x + " " + y);
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

    @Override
    public String name() {
        return SYSTEM_OS;
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

    @Override
    public File saveScreenShot(final Area area, final String filePathFirst, final String... filePathRest)
            throws IOException {
        return saveScreenShot(area.getP1(), area.getP2(), filePathFirst, filePathRest);
    }

    private File saveScreenShot(final Point p1, final Point p2, final String filePathFirst,
            final String... filePathRest) throws IOException {
        final BufferedImage img = screenShot(p1, p2);
        return saveImage(img, filePathFirst, filePathRest);
    }

    @Override
    public BufferedImage screenShot(final Area area) {
        return screenShot(area.getP1(), area.getP2());
    }

    public BufferedImage screenShot(final Point p1, final Point p2) {
        final Point anchor = clientToScreen(p1);
        final int width = p2.x() - p1.x();
        final int height = p2.y() - p1.y();
        return robot.createScreenCapture(new Rectangle(anchor.x(), anchor.y(), width, height));
    }

    @Override
    public void setup() throws BotConfigurationException {
        logger.info(String.format("Setting up %s window...", BS_WINDOW_NAME));
        setupBsRect();
        // setup resolution
        logger.info(String.format("Setting up %s resolution...", BS_WINDOW_NAME));
        setupResolution();
        // setup RobotUtils
        logger.info("Setting up OS handler...");
        setupWin32(bsHwnd);
    }

    private void setupBsRect() throws BotConfigurationException {
        bsHwnd = User32.INSTANCE.FindWindow(null, BS_WINDOW_NAME);
        if (bsHwnd == null) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found.");
        }
        final int[] rect = { 0, 0, 0, 0 };
        final int result = User32.INSTANCE.GetWindowRect(bsHwnd, rect);
        if (result == 0) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found.");
        }
        logger.finest(String.format("The corner locations for the window \"%s\" are %s", BS_WINDOW_NAME,
                Arrays.toString(rect)));
        // set bs always on top
        final int SWP_NOSIZE = 0x0001;
        final int SWP_NOMOVE = 0x0002;
        final int TOPMOST_FLAGS = SWP_NOMOVE | SWP_NOSIZE;
        User32.INSTANCE.SetWindowPos(bsHwnd, -1, 0, 0, 0, 0, TOPMOST_FLAGS);
    }

    private void setupResolution() throws BotConfigurationException {
        // update registry
        try {
            final HKEYByReference key = Advapi32Util.registryGetKey(WinReg.HKEY_LOCAL_MACHINE,
                    "SOFTWARE\\BlueStacks\\Guests\\Android\\FrameBuffer\\0", WinNT.KEY_READ | WinNT.KEY_WRITE);
            final int w1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowWidth");
            final int h1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowHeight");
            final int w2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestWidth");
            final int h2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestHeight");
            final HWND control = User32.INSTANCE.GetDlgItem(bsHwnd, 0);
            final int[] rect = new int[4];
            User32.INSTANCE.GetWindowRect(control, rect);
            final int bsX = rect[2] - rect[0];
            final int bsY = rect[3] - rect[1];
            if (bsX != BS_RES_X || bsY != BS_RES_Y) {
                logger.warning(String.format("%s resolution is <%d, %d>", BS_WINDOW_NAME, bsX, bsY));
                if (w1 != BS_RES_X || h1 != BS_RES_Y || w2 != BS_RES_X || h2 != BS_RES_Y) {
                    final String msg = String.format("%s must run in resolution %dx%d.\n"
                            + "Click YES to change it automatically, NO to do it later.\n", BS_WINDOW_NAME, BS_RES_X,
                            BS_RES_Y);
                    final boolean ret = OS.instance().confirmationBox(msg, "Change resolution");
                    if (!ret) {
                        throw new BotConfigurationException("Re-run when resolution is fixed.");
                    }
                    Advapi32Util.registrySetIntValue(key.getValue(), "WindowWidth", BS_RES_X);
                    Advapi32Util.registrySetIntValue(key.getValue(), "WindowHeight", BS_RES_Y);
                    Advapi32Util.registrySetIntValue(key.getValue(), "GuestWidth", BS_RES_X);
                    Advapi32Util.registrySetIntValue(key.getValue(), "GuestHeight", BS_RES_Y);
                    Advapi32Util.registrySetIntValue(key.getValue(), "FullScreen", 0);
                    throw new BotConfigurationException("Please restart " + BS_WINDOW_NAME);
                }
            }
        } catch (final BotConfigurationException e) {
            throw e;
        } catch (final Exception e) {
            throw new BotConfigurationException("Unable to change resolution. Do it manually.", e);
        }
    }

    public void setupWin32(final HWND handler) {
        this.handler = handler;
    }

    @Override
    public void sleepRandom(final int sleepInMs) throws InterruptedException {
        final int time = sleepInMs + RANDOM.nextInt(sleepInMs);
        logger.fine("Sleeping for " + time + " ms");
        Thread.sleep(time);
    }

    @Override
    public void sleepTillClickableIsActive(final Clickable clickable) throws InterruptedException {
        while (true) {
            if (isClickableActive(clickable)) {
                return;
            }
            Thread.sleep(RANDOM.nextInt(250) + 750);
        }
    }

    @Override
    public void waitForClick(final MouseAdapter mouseAdapter) throws InterruptedException, BotConfigurationException {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.getInstance().addNativeMouseListener(new NativeMouseListener() {

                @Override
                public void nativeMouseClicked(final NativeMouseEvent e) {
                    // not relative to window
                    final int x = e.getX();
                    final int y = e.getY();
                    logger.finest(String.format("clicked %d %d", e.getX(), e.getY()));
                    final POINT screenPoint = new POINT(x, y);
                    User32.INSTANCE.ScreenToClient(bsHwnd, screenPoint);
                    mouseAdapter.mouseClicked(new MouseEvent(null, MouseEvent.MOUSE_CLICKED,
                            System.currentTimeMillis(), 0, screenPoint.x, screenPoint.y, x, y, 1, false, 0));
                    synchronized (GlobalScreen.getInstance()) {
                        GlobalScreen.getInstance().notify();
                    }
                }

                @Override
                public void nativeMousePressed(final NativeMouseEvent e) {
                }

                @Override
                public void nativeMouseReleased(final NativeMouseEvent e) {
                }
            });
            logger.info("Waiting for user to click on first barracks.");
            synchronized (GlobalScreen.getInstance()) {
                while (Settings.instance().getFirstBarrackPosition() == null) {
                    GlobalScreen.getInstance().wait();
                }
            }
            logger.info(String.format("Set barracks location to <%d, %d>", Settings.instance()
                    .getFirstBarrackPosition().x(), Settings.instance().getFirstBarrackPosition().y()));
            GlobalScreen.unregisterNativeHook();
        } catch (final NativeHookException e) {
            throw new BotConfigurationException("Unable to capture mouse movement.", e);
        }
    }

    @Override
    public void zoomUp() throws InterruptedException {
        zoomUp(14);
    }

    private void zoomUp(final int notch) throws InterruptedException {
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
