package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.util.Utils;

import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bot main logic that loop between training and attacking.
 *
 * @author paspiz85
 *
 */
public final class Looper {

    public static Looper instance() {
        return Utils.singleton(Looper.class, () -> new Looper());
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Platform platform = Platform.instance();

    private boolean running;

    private boolean waitingForDcChecker;

    private boolean reloading;

    private Looper() {
    }

    public boolean isReloading() {
        return reloading;
    }

    public boolean isRunning() {
        return running;
    }

    boolean isWaitingForDcChecker() {
        return waitingForDcChecker;
    }

    private void loop(final Context context) throws InterruptedException, BotException {
        Exception botException; // throw in case of timeout
        try {
            while (true) {
                if (Thread.interrupted()) {
                    throw new InterruptedException(getClass().getName() + " is interrupted");
                }
                context.handle();
            }
        } catch (final InterruptedException e) {
            // either by dc checker
            if (context.isDisconnected()) {
                logger.log(Level.INFO, "Interrupted by DisconnectChecker");
                context.setDisconnected(false);
                context.setWaitDone(false);
                return;
                // or by user
            } else {
                logger.log(Level.INFO, "Interrupted by User");
                throw e;
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            botException = e;
            if (e instanceof BotConfigurationException) {
                throw e;
            }
        }
        final long timeout = 10 * 60 * 1000;
        // wait for dc checker to wake me up
        synchronized (context) {
            while (!context.isWaitDone()) {
                final long tBefore = System.currentTimeMillis();
                logger.log(Level.INFO, "Waiting for dc checker to wake me up...");
                this.waitingForDcChecker = true;
                // if user interrupts here while it is waiting, make sure
                // waitingForDcChecker is set to false
                context.wait(timeout);
                if (System.currentTimeMillis() - tBefore > timeout) {
                    throw new BotException("Timed Out", botException);
                }
            }
            context.setWaitDone(false);
        }
        this.waitingForDcChecker = false;
        logger.log(Level.INFO, "Woken up. Launching again...");
    }

    public void setReloading(final boolean reloading) {
        this.reloading = reloading;
    }

    public void start(final BooleanSupplier autoAdjustResolution, final Runnable updateUI) throws Exception {
        try {
            logger.log(Level.INFO, "Starting...");
            platform.init(autoAdjustResolution);
            final Context context = new Context();
            logger.log(Level.FINE, "Starting disconnect detector...");
            final Thread dcThread = new Thread(new DisconnectChecker(this, context, Thread.currentThread()),
                    "DisconnectCheckerThread");
            dcThread.setDaemon(true);
            dcThread.start();
            StateIdle.instance().setLooper(this);
            try {
                running = true;
                logger.log(Level.FINE, "looper running");
                updateUI.run();
                while (running) {
                    context.setState(StateIdle.instance());
                    loop(context);
                }
            } finally {
                running = false;
                logger.log(Level.FINE, "looper stopped");
                updateUI.run();
                dcThread.interrupt();
                this.waitingForDcChecker = false;
                context.setWaitDone(false);
            }
        } catch (final InterruptedException e) {
            logger.log(Level.FINE, e.getMessage());
            throw e;
        } catch (final BotException e) {
            logger.log(Level.WARNING, e.getMessage());
            throw e;
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Got exception: " + e.getMessage(), e);
            throw e;
        }
    }

    public void stop() {
        running = false;
    }
}
