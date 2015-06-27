package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logic to check if game disconnected.
 *
 * @author paspiz85
 *
 */
public class DisconnectChecker implements Runnable {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Context context;

    private final Thread mainThread;

    private final OS os = OS.instance();

    public DisconnectChecker(final Context context, final Thread mainThread) {
        this.context = context;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        logger.info("Running disconnect detector...");
        try {
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException("Disconnect detector is interrupted.");
                }
                if (os.isClickableActive(Clickable.UNIT_BLUESTACKS_DC)) {
                    logger.info("Detected disconnect.");
                    synchronized (context) {
                        // case 1: launcher was running and it will be
                        // interrupted. It will go back to StateIdle start
                        // running immediately.
                        if (!Looper.instance().isWaitingForDcChecker()) {
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
                    os.leftClick(Clickable.UNIT_RECONNECT.getPoint(), true);
                    os.sleepRandom(5000);
                    Thread.sleep(2000);
                    StateIdle.instance().setReloading(false);
                }
                Thread.sleep(10000);
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "dc checker: " + e.getMessage(), e);
        }
    }
}
