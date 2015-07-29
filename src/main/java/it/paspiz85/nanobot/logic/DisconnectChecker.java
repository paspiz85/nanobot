package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.util.Pixel;
import it.paspiz85.nanobot.util.Point;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logic to check if game disconnected.
 *
 * @author paspiz85
 *
 */
public class DisconnectChecker implements Runnable {

    private static final Pixel UNIT_BLUESTACKS_DC = new Pixel(699, 343, new Color(0x282828));

    private static final Point UNIT_RECONNECT = new Point(435, 400);

    private final Context context;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Looper looper;

    private final Thread mainThread;

    private final Platform platform = Platform.instance();

    public DisconnectChecker(final Looper looper, final Context context, final Thread mainThread) {
        this.looper = looper;
        this.context = context;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        logger.log(Level.FINE, "Running disconnect detector...");
        try {
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Disconnect detector is interrupted");
                }
                if (platform.matchColoredPoint(UNIT_BLUESTACKS_DC)) {
                    logger.log(Level.INFO, "Detected disconnect");
                    synchronized (context) {
                        // case 1: launcher was running and it will be
                        // interrupted. It will go back to StateIdle start
                        // running immediately.
                        if (!looper.isWaitingForDcChecker()) {
                            context.setDisconnected(true);
                            // to fix a potential race condition.
                            // if bot launcher throws an exception and this
                            // gets the context lock right before bot launcher,
                            // we don't want bot launcher to wait for this
                            context.setWaitDone(true);
                            mainThread.interrupt();
                            // case 2: launcher was already stopped and was
                            // waiting to be woken up by this.
                        } else {
                            context.setWaitDone(true);
                            context.notify();
                        }
                    }
                    // temporarily pause StateIdle because in case current state
                    // is StateIdle
                    // when you click reload, screen would look like it is
                    // loaded for a second, before
                    // loading actually starts and next state would be executed.
                    StateIdle.instance().setReloading(true);
                    platform.leftClick(UNIT_RECONNECT, true);
                    platform.sleepRandom(5000);
                    Thread.sleep(2000);
                    StateIdle.instance().setReloading(false);
                }
                Thread.sleep(30000);
            }
        } catch (final InterruptedException e) {
            logger.log(Level.CONFIG, "DisconnectChecker interrupted");
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "dc checker: " + e.getMessage(), e);
        }
    }
}
