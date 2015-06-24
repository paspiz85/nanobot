package it.paspiz85.nanobot.logic;

import it.paspiz85.nanobot.exception.BotException;
import it.paspiz85.nanobot.state.Context;
import it.paspiz85.nanobot.state.StateIdle;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Looper {

	private static final Looper instance = new Looper();

	public static Looper instance() {
		return instance;
	}

	protected final Logger logger = Logger.getLogger(getClass().getName());

	private boolean waitingForDcChecker = false;

	private Looper() {

	}

	public boolean isWaitingForDcChecker() {
		return waitingForDcChecker;
	}

	private void loop(Context context) throws InterruptedException,
			BotException {
		Exception botException; // throw in case of timeout
		try {
			while (true) {
				if (Thread.interrupted()) {
					throw new InterruptedException(
							"BotLauncher is interrupted.");
				}
				context.handle();
			}
		} catch (InterruptedException e) {
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
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			botException = e;
		}

		final long timeout = 10 * 60 * 1000;
		// wait for dc checker to wake me up
		synchronized (context) {
			while (!context.isWaitDone()) {
				long tBefore = System.currentTimeMillis();

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

	public void start() throws InterruptedException, BotException {
		// state pattern
		Context context = new Context();

		// start daemon thread that checks if you are DC'ed etc
		logger.info("Starting disconnect detector...");
		Thread dcThread = new Thread(new DisconnectChecker(context,
				Thread.currentThread()), "DisconnectCheckerThread");
		dcThread.setDaemon(true);
		dcThread.start();

		try {
			while (true) {
				context.setState(StateIdle.instance());
				loop(context);
			}
		} finally {
			dcThread.interrupt();
			this.waitingForDcChecker = false;
			context.setWaitDone(false);
		}
	}
}
