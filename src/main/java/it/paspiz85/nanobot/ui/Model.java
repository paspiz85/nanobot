package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.scripting.ScriptManager;
import it.paspiz85.nanobot.util.Settings;

import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
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
public final class Model {

    private static Model instance;

    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final Looper looper = Looper.instance();

    private Service<Void> runningService;

    private Service<Void> scriptService;

    private String runningScript;

    private Model() {
    }

    public TroopButton[] getAvailableTroops() {
        return Settings.instance().getAvailableTroops();
    }

    public Set<String> getScripts() {
        return ScriptManager.instance().getScripts();
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
            logger.warning("Running is failed.");
        });
        scriptService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        ScriptManager.instance().run(runningScript);
                        return null;
                    }
                };
            }
        };
        scriptService.setOnCancelled(event -> {
            scriptService.reset();
            runningScript = null;
            logger.warning("Script is cancelled.");
        });
        scriptService.setOnFailed(event -> {
            scriptService.reset();
            runningScript = null;
            logger.warning("Script is failed.");
        });
        scriptService.setOnSucceeded(event -> {
            scriptService.reset();
            runningScript = null;
            logger.info("Script is succeeded.");
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

    public void runScript(final String script) {
        this.runningScript = script;
        if (scriptService.getState() == State.READY) {
            scriptService.start();
        }
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
