package it.paspiz85.nanobot.os.win32;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.os.AbstractOS;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

/**
 * Implementation of {@link OS} for Windows.
 *
 * @author paspiz85
 *
 */
public final class Win32OS extends AbstractOS implements OS, Constants {

    private static final int BS_RES_X = 860;

    private static final int BS_RES_Y = 720;

    private static Win32OS instance;

    private static final int SWP_NOMOVE = 0x0002;

    private static final int SWP_NOSIZE = 0x0001;

    private static final String SYSTEM_OS = System.getProperty("os.name");

    private static final int TOPMOST_FLAGS = SWP_NOMOVE | SWP_NOSIZE;

    private static final int VK_CONTROL = 0x11;

    private static final int VK_DOWN = 0x28;

    private static final int WM_KEYDOWN = 0x100;

    private static final int WM_KEYUP = 0x101;

    private static final int WM_LBUTTONDOWN = 0x201;

    private static final int WM_LBUTTONUP = 0x202;

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    public static Win32OS instance() {
        if (instance == null) {
            instance = new Win32OS();
        }
        return instance;
    }

    private HWND handler;

    private Robot robot;

    private Win32OS() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            logger.log(Level.SEVERE, "Unable to init robot", e);
        }
    }

    private Point clientToScreen(final Point clientPoint) {
        final POINT point = new POINT(clientPoint.x(), clientPoint.y());
        User32.INSTANCE.ClientToScreen(handler, point);
        return new Point(point.x, point.y);
    }

    @Override
    protected Color getColor(final Point point) {
        final Point screenPoint = clientToScreen(point);
        return robot.getPixelColor(screenPoint.x(), screenPoint.y());
    }

    @Override
    public int getGameHeight() {
        return BS_RES_Y;
    }

    @Override
    public int getGameWidth() {
        return BS_RES_X;
    }

    private boolean isCtrlKeyDown() {
        return User32.INSTANCE.GetKeyState(VK_CONTROL) < 0;
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
        logger.fine("Clicking [" + x + " " + y + "].");
        final int lParam = y << 16 | x << 16 >>> 16;
        while (isCtrlKeyDown()) {
            Thread.sleep(100);
        }
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONDOWN, 0x00000001, lParam);
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONUP, 0x00000000, lParam);
    }

    @Override
    protected BufferedImage screenshot(final Point p1, final Point p2) {
        final Point anchor = clientToScreen(p1);
        final int width = p2.x() - p1.x();
        final int height = p2.y() - p1.y();
        return robot.createScreenCapture(new Rectangle(anchor.x(), anchor.y(), width, height));
    }

    @Override
    public void setup() throws BotConfigurationException {
        if (!SYSTEM_OS.toLowerCase(Locale.ROOT).contains("windows")) {
            throw new BotConfigurationException("Bot is only available for Windows OS.");
        }
        logger.info(String.format("Setting up %s window...", BS_WINDOW_NAME));
        handler = User32.INSTANCE.FindWindow(null, BS_WINDOW_NAME);
        if (handler == null) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found.");
        }
        final int[] rect = { 0, 0, 0, 0 };
        final int result = User32.INSTANCE.GetWindowRect(handler, rect);
        if (result == 0) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found.");
        }
        logger.finest(String.format("The corner locations for the window \"%s\" are %s", BS_WINDOW_NAME,
                Arrays.toString(rect)));
        // set bs always on top
        User32.INSTANCE.SetWindowPos(handler, -1, 0, 0, 0, 0, TOPMOST_FLAGS);
    }

    @Override
    public void setupResolution(final BooleanSupplier setupResolution) throws BotConfigurationException {
        logger.info(String.format("Checking %s resolution...", BS_WINDOW_NAME));
        try {
            final HKEYByReference key = Advapi32Util.registryGetKey(WinReg.HKEY_LOCAL_MACHINE,
                    "SOFTWARE\\BlueStacks\\Guests\\Android\\FrameBuffer\\0", WinNT.KEY_READ | WinNT.KEY_WRITE);
            final int w1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowWidth");
            final int h1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowHeight");
            final int w2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestWidth");
            final int h2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestHeight");
            final HWND control = User32.INSTANCE.GetDlgItem(handler, 0);
            final int[] rect = new int[4];
            User32.INSTANCE.GetWindowRect(control, rect);
            final int bsX = rect[2] - rect[0];
            final int bsY = rect[3] - rect[1];
            if (bsX != BS_RES_X || bsY != BS_RES_Y) {
                logger.warning(String.format("%s resolution is <%d, %d>", BS_WINDOW_NAME, bsX, bsY));
                if (w1 != BS_RES_X || h1 != BS_RES_Y || w2 != BS_RES_X || h2 != BS_RES_Y) {
                    if (!setupResolution.getAsBoolean()) {
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
