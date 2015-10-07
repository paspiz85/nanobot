package it.paspiz85.nanobot.platform.win;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.platform.AbstractPlatform;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.platform.win.KeyboardMapping.Key;
import it.paspiz85.nanobot.util.OS;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Size;
import it.paspiz85.nanobot.util.Utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.jnativehook.GlobalScreen;
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

/**
 * Platform implementation of BlueStacks for Windows.
 *
 * @author paspiz85
 *
 */
public class BlueStacksWinPlatform extends AbstractPlatform {

    public static final Size BS_SIZE = new Size(Platform.GAME_SIZE.x(), Platform.GAME_SIZE.y() + 47);

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    private static final int SWP_NOMOVE = 0x0002;

    private static final int SWP_NOSIZE = 0x0001;

    private static final int TOPMOST_FLAGS = SWP_NOMOVE | SWP_NOSIZE;

    private static final int WM_KEYDOWN = 0x100;

    private static final int WM_KEYUP = 0x101;

    private static final int WM_LBUTTONDOWN = 0x201;

    private static final int WM_LBUTTONUP = 0x202;

    public static BlueStacksWinPlatform instance() {
        return Utils.singleton(BlueStacksWinPlatform.class, () -> new BlueStacksWinPlatform());
    }

    public static boolean isSupported() {
        final OS os = OS.getCurrent();
        return os.getFamily() == OS.Family.WINDOWS && os.getVersion().getMajor() < 10;
    }

    private HWND handler;

    private final Robot robot;

    protected BlueStacksWinPlatform() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new IllegalStateException("Unable to init robot", e);
        }
    }

    private Point clientToScreen(final Point clientPoint) {
        final POINT point = new POINT(clientPoint.x(), clientPoint.y());
        User32.INSTANCE.ClientToScreen(handler, point);
        return new Point(point.x, point.y);
    }

    @Override
    protected void doApplySize(final Size size) throws BotConfigurationException {
        final int width = size.x();
        final int height = size.y();
        final HKEYByReference key = Advapi32Util.registryGetKey(WinReg.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\BlueStacks\\Guests\\Android\\FrameBuffer\\0", WinNT.KEY_READ | WinNT.KEY_WRITE);
        final int w1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowWidth");
        final int h1 = Advapi32Util.registryGetIntValue(key.getValue(), "WindowHeight");
        final int w2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestWidth");
        final int h2 = Advapi32Util.registryGetIntValue(key.getValue(), "GuestHeight");
        if (w1 != width || h1 != height || w2 != width || h2 != height) {
            Advapi32Util.registrySetIntValue(key.getValue(), "WindowWidth", width);
            Advapi32Util.registrySetIntValue(key.getValue(), "WindowHeight", height);
            Advapi32Util.registrySetIntValue(key.getValue(), "GuestWidth", width);
            Advapi32Util.registrySetIntValue(key.getValue(), "GuestHeight", height);
            Advapi32Util.registrySetIntValue(key.getValue(), "FullScreen", 0);
        }
        throw new BotConfigurationException(String.format("Please restart %s to fix resolution", BS_WINDOW_NAME));
    }

    protected void doKeyPress(final int keyCode, final boolean shifted) throws InterruptedException {
        logger.log(Level.FINER, "doKeyPress " + keyCode + " " + shifted);
        while (isCtrlKeyDown()) {
            Thread.sleep(100);
        }
        final int lParam = 0x00000001 | 0x50 /* scancode */<< 16 | 0x01000000 /* extended */;
        final WPARAM wparam = new WinDef.WPARAM(keyCode);
        final WPARAM wparamShift = new WinDef.WPARAM(KeyEvent.VK_SHIFT);
        final LPARAM lparamDown = new WinDef.LPARAM(lParam);
        final LPARAM lparamUp = new WinDef.LPARAM(lParam | 1 << 30 | 1 << 31);
        if (shifted) {
            User32.INSTANCE.PostMessage(handler, WM_KEYDOWN, wparamShift, lparamDown);
        }
        User32.INSTANCE.PostMessage(handler, WM_KEYDOWN, wparam, lparamDown);
        User32.INSTANCE.PostMessage(handler, WM_KEYUP, wparam, lparamUp);
        if (shifted) {
            User32.INSTANCE.PostMessage(handler, WM_KEYUP, wparamShift, lparamUp);
        }
    }

    @Override
    protected void doLeftClick(final Point point) throws InterruptedException {
        final int x = point.x();
        final int y = point.y();
        final int lParam = y << 16 | x << 16 >>> 16;
        while (isCtrlKeyDown()) {
            Thread.sleep(100);
        }
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONDOWN, 0x00000001, lParam);
        User32.INSTANCE.SendMessage(handler, WM_LBUTTONUP, 0x00000000, lParam);
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
        doKeyPress(KeyEvent.VK_DOWN, false);
    }

    @Override
    protected void doWrite(final String s) throws InterruptedException {
        for (final char ch : s.toCharArray()) {
            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    doKeyPress(ch, true);
                } else {
                    doKeyPress(Character.toUpperCase(ch), false);
                }
            } else if (Character.isDigit(ch)) {
                doKeyPress(ch, false);
            } else {
                Key key = null;
                KeyboardMapping mapping = KeyboardMapping.get(InputContext.getInstance().getLocale());
                if (mapping != null) {
                    key = mapping.getKey(ch);
                }
                if (key != null) {
                    doKeyPress(key.getCode(), key.isShifted());
                } else {
                    logger.log(Level.WARNING, "Unable to write character '" + ch + "'");
                }
            }
            sleepRandom(200);
        }
    }

    @Override
    protected Size getActualSize() {
        final HWND control = User32.INSTANCE.GetDlgItem(handler, 0);
        final int[] rect = new int[4];
        User32.INSTANCE.GetWindowRect(control, rect);
        final Size bsSize = new Size(rect[2] - rect[0], rect[3] - rect[1]);
        return bsSize;
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

    private boolean isCtrlKeyDown() {
        return User32.INSTANCE.GetKeyState(KeyEvent.VK_CONTROL) < 0;
    }

    @Override
    protected boolean registerForClick(final Consumer<Point> clickConsumer) {
        boolean result;
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.getInstance().addNativeMouseListener(new NativeMouseListener() {

                @Override
                public void nativeMouseClicked(final NativeMouseEvent e) {
                    final int x = e.getX();
                    final int y = e.getY();
                    logger.finest(String.format("clicked %d %d", e.getX(), e.getY()));
                    final POINT screenPoint = new POINT(x, y);
                    User32.INSTANCE.ScreenToClient(handler, screenPoint);
                    clickConsumer.accept(new Point(screenPoint.x, screenPoint.y));
                }

                @Override
                public void nativeMousePressed(final NativeMouseEvent e) {
                }

                @Override
                public void nativeMouseReleased(final NativeMouseEvent e) {
                }
            });
            result = true;
        } catch (final Exception e) {
            logger.log(Level.WARNING, "Unable to capture mouse movement", e);
            result = false;
        }
        return result;
    }

    @Override
    protected void setup() throws BotConfigurationException {
        handler = User32.INSTANCE.FindWindow(null, BS_WINDOW_NAME);
        if (handler == null) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found");
        }
        final int[] rect = { 0, 0, 0, 0 };
        final int result = User32.INSTANCE.GetWindowRect(handler, rect);
        if (result == 0) {
            throw new BotConfigurationException(BS_WINDOW_NAME + " is not found");
        }
        logger.log(
                Level.FINE,
                String.format("The corner locations for the window \"%s\" are %s", BS_WINDOW_NAME,
                        Arrays.toString(rect)));
        // set bs always on top
        User32.INSTANCE.SetWindowPos(handler, -1, 0, 0, 0, 0, TOPMOST_FLAGS);
    }
}
