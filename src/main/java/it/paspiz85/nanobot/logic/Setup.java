package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;
import it.paspiz85.nanobot.win32.User32;

import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

/**
 * Steps to setup bot for interacting with game in BlueStacks.
 * 
 * @author v-ppizzuti
 *
 */
public final class Setup implements Constants {

    private static Setup instance;

    private static final String BS_WINDOW_NAME = "BlueStacks App Player";

    public static Setup instance() {
        if (instance == null) {
            instance = new Setup();
        }
        return instance;
    }

    private HWND bsHwnd;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final NativeMouseListener firstBarrackMouseListener = new NativeMouseListener() {

        @Override
        public void nativeMouseClicked(final NativeMouseEvent e) {
            // not relative to window
            final int x = e.getX();
            final int y = e.getY();
            logger.finest(String.format("clicked %d %d", e.getX(), e.getY()));
            final POINT screenPoint = new POINT(x, y);
            User32.INSTANCE.ScreenToClient(bsHwnd, screenPoint);
            Settings.instance().setFirstBarrackPosition(new Point(screenPoint.x, screenPoint.y));
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
    };

    private Setup() {
    }

    public void setup() throws BotConfigurationException, InterruptedException {
        if (!OS.SYSTEM_OS.toLowerCase(Locale.ROOT).contains("windows")) {
            throw new BotConfigurationException("Bot is only available for Windows OS.");
        }
        logger.info(String.format("Setting up %s window...", BS_WINDOW_NAME));
        setupBsRect();
        // setup resolution
        logger.info(String.format("Setting up %s resolution...", BS_WINDOW_NAME));
        setupResolution();
        // setup RobotUtils
        logger.info("Setting up OS handler...");
        OS.instance().setupWin32(bsHwnd);
        // setup barracks
        logger.info("Setting up Barracks...");
        setupBarracks();
    }

    private void setupBarracks() throws BotConfigurationException, InterruptedException {
        if (Settings.instance().getFirstBarrackPosition() == null) {
            OS.instance().zoomUp();
            final boolean confirmed = OS.instance().confirmationBox(
                    "You must configure the location "
                            + "of first Barracks. First Barracks is the leftmost one when you \n"
                            + "scroll through your barracks via orange next arrow on the right. For example, if you \n"
                            + "have 4 barracks, when you select the first one and click 'Train Troops', all \n"
                            + "3 'next' views should also be barracks.\n\n"
                            + "Click Yes to start configuration and click on your first barracks. Do \n"
                            + "NOT click anything else in between. Click Yes and click barracks. \n\n"
                            + "Make sure you are max zoomed out.", "Barracks configuration");
            if (!confirmed) {
                throw new BotConfigurationException("Cannot proceed without barracks");
            }
            // read mouse click
            try {
                GlobalScreen.registerNativeHook();
                GlobalScreen.getInstance().addNativeMouseListener(firstBarrackMouseListener);
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
}
