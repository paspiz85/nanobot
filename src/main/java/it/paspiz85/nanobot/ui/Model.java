package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Settings;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

/**
 * Bot model.
 *
 * @author paspiz85
 *
 */
public final class Model implements Constants {

    private static final OS DEFAULT_OS = OS.instance();

    private static Model instance;

    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final Looper looper = Looper.instance();

    private final OS os = DEFAULT_OS;

    private Service<Void> runningService;

    private Model() {
    }

    public TroopButton[] getAvailableTroops() {
        return Settings.instance().getAvailableTroops();
    }

    public void initialize(final BooleanSupplier setupResolution, final Runnable updateUI) {
        // set system locale to ROOT, Turkish clients will break because
        // jnativehook dependency has Turkish I bug
        Locale.setDefault(Locale.ROOT);
        // setup configUtils
        Settings.initialize();
        logger.info("Settings loaded.");
        runningService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        looper.start(setupResolution, updateUI);
                        return null;
                    }
                };
            }
        };
        runningService.setOnCancelled(event -> {
            runningService.reset();
            logger.warning("Running is cancelled.");
        });
        runningService.setOnFailed(event -> {
            runningService.reset();
            logger.log(Level.SEVERE, "Running is failed: " + runningService.getException().getMessage(),
                    runningService.getException());
        });
    }

    public boolean isRunning() {
        return looper.isRunning();
    }

    public boolean isSetupDone() {
        return looper.isSetupDone();
    }

    public Settings loadSettings() {
        return Settings.instance();
    }

    public void saveScreenshot() {
        os.saveScreenshot("screen_" + System.currentTimeMillis());
    }

    public void saveSettings(final Consumer<Settings> consumer) {
        final Settings settings = Settings.instance();
        consumer.accept(settings);
        settings.save();
    }

    public void start() {
        if (runningService.getState() == State.READY) {
            runningService.start();
        }
    }

    public void stop() {
        if (runningService.isRunning()) {
            runningService.cancel();
            runningService.reset();
        }
    }
}
