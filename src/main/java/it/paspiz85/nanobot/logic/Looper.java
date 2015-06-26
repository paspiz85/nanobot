package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.state.Context;
import it.paspiz85.nanobot.state.StateIdle;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bot main logic that loop between training and attacking.
 *
 * @author paspiz85
 *
 */
public final class Looper {

    private static Looper instance;

    public static Looper instance() {
        if (instance == null) {
            instance = new Looper();
        }
        return instance;
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    private boolean waitingForDcChecker;

    private boolean running;

    private boolean setupDone;

    private Looper() {
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isWaitingForDcChecker() {
        return waitingForDcChecker;
    }

    private void loop(final Context context) throws InterruptedException, BotException {
        Exception botException; // throw in case of timeout
        try {
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException(getClass().getName() + " is interrupted.");
                }
                context.handle();
            }
        } catch (final InterruptedException e) {
            // either by dc checker
            if (context.isDisconnected()) {
                logger.info("Interrupted by dc checker.");
                context.setDisconnected(false);
                context.setWaitDone(false);
                return;
                // or by user
            } else {
                logger.info("Interrupted by user.");
                throw e;
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            botException = e;
        }
        final long timeout = 10 * 60 * 1000;
        // wait for dc checker to wake me up
        synchronized (context) {
            while (!context.isWaitDone()) {
                final long tBefore = System.currentTimeMillis();
                logger.info("Waiting for dc checker to wake me up...");
                this.waitingForDcChecker = true;
                // if user interrupts here while it is waiting, make sure
                // waitingForDcChecker is set to false
                context.wait(timeout);
                if (System.currentTimeMillis() - tBefore > timeout) {
                    throw new BotException("Timed Out.", botException);
                }
            }
            context.setWaitDone(false);
        }
        this.waitingForDcChecker = false;
        logger.info("Woken up. Launching again...");
    }

    public void start(final BooleanSupplier setupResolution, final Supplier<Point> setupBarracks)
            throws InterruptedException, BotException {
        if (!setupDone) {
            OS.instance().setup();
            setupDone = true;
        }
        OS.instance().setupResolution(setupResolution);
        if (Settings.instance().getFirstBarrackPosition() == null) {
            logger.info("Setting up Barracks...");
            final Point point = setupBarracks.get();
            if (point == null) {
                throw new BotConfigurationException("Cannot proceed without barracks");
            }
            logger.info(String.format("Set barracks location to <%d, %d>", point.x(), point.y()));
            Settings.instance().setFirstBarrackPosition(point);
            Settings.instance().save();
        }
        logger.info("Setup is successful.");
        final Context context = new Context();
        logger.info("Starting disconnect detector...");
        final Thread dcThread = new Thread(new DisconnectChecker(context, Thread.currentThread()),
                "DisconnectCheckerThread");
        dcThread.setDaemon(true);
        dcThread.start();
        try {
            running = true;
            while (true) {
                context.setState(StateIdle.instance());
                loop(context);
            }
        } finally {
            running = false;
            dcThread.interrupt();
            this.waitingForDcChecker = false;
            context.setWaitDone(false);
        }
    }
}
