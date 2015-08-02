package it.paspiz85.nanobot.platform.win.v10;

import it.paspiz85.nanobot.platform.win.BlueStacksWinPlatform;
import it.paspiz85.nanobot.util.OS;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Utils;

/**
 * Platform implementation of BlueStacks for Windows 10.
 *
 * @author paspiz85
 *
 */
public final class BlueStacksWin10Platform extends BlueStacksWinPlatform {

    public static BlueStacksWin10Platform instance() {
        return Utils.singleton(BlueStacksWin10Platform.class, () -> new BlueStacksWin10Platform());
    }

    public static boolean isSupported() {
        final OS os = OS.getCurrent();
        return os.getFamily() == OS.Family.WINDOWS && os.getVersion().getMajor() >= 10;
    }

    @Override
    protected void activate() {
        // TODO Auto-generated method stub
        super.activate();
    }

    @Override
    protected void leftClick(final Point point) throws InterruptedException {
        // TODO Auto-generated method stub
        super.leftClick(point);
    }

    @Override
    protected void singleZoomUp() throws InterruptedException {
        // TODO Auto-generated method stub
        super.singleZoomUp();
    }
}
