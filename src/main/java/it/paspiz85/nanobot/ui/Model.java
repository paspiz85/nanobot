package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.exception.BotConfigurationException;
import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.scripting.ScriptManager;
import it.paspiz85.nanobot.util.BuildInfo;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.util.Utils;

import java.util.Locale;
import java.util.Set;
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
public final class Model {

    public static Model instance() {
        return Utils.singleton(Model.class, () -> new Model());
    }

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private final Looper looper = Looper.instance();

    private String lastRunnedScript;

    private String runningScript;

    private Service<Void> runningService;

    private Service<Void> scriptService;

    private Model() {
    }

    public void checkForUpdate(final Runnable updateUI) {
        final Thread checkForUpdateThread = new Thread(() -> {
            while (true) {
                try {
                    if (BuildInfo.instance().checkForUpdate() != null) {
                        updateUI.run();
                    }
                    Thread.sleep(6 * 60 * 60000);
                } catch (final Exception e) {
                    logger.log(Level.FINE, "checkForUpdate failed", e);
                }
            }
        }, "checkForUpdateThread");
        checkForUpdateThread.setDaemon(true);
        checkForUpdateThread.start();
    }

    public TroopButton[] getAvailableTroops() {
        return Settings.instance().getAvailableTroops();
    }

    public String getLastRunnedScript() {
        return lastRunnedScript;
    }

    public String getRunningScript() {
        return runningScript;
    }

    public Set<String> getScripts() {
        return ScriptManager.instance().getScripts();
    }

    public void initialize(final BooleanSupplier autoAdjustResolution, final Runnable updateUI) {
        if (autoAdjustResolution == null) {
            throw new NullPointerException("autoAdjustResolution not provided");
        }
        if (updateUI == null) {
            throw new NullPointerException("updateUI not provided");
        }
        // set system locale to ROOT, Turkish clients will break because
        // jnativehook dependency has Turkish I bug
        Locale.setDefault(Locale.ROOT);
        // setup configUtils
        Settings.initialize();
        logger.log(Level.INFO, "Settings loaded");
        initRunningService(autoAdjustResolution, updateUI);
        initScriptService();
    }

    private void initRunningService(final BooleanSupplier autoAdjustResolution, final Runnable updateUI) {
        runningService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        looper.start(autoAdjustResolution, updateUI);
                        return null;
                    }
                };
            }
        };
        runningService.setOnCancelled(event -> {
            runningService.reset();
            logger.warning("Running is cancelled");
        });
        runningService.setOnFailed(event -> {
            runningService.reset();
            logger.warning("Running is failed");
        });
    }

    private void initScriptService() {
        scriptService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        try {
                            Platform.instance().init();
                        } catch (final BotConfigurationException e) {
                            logger.log(Level.WARNING, e.getMessage(), e);
                        }
                        ScriptManager.instance().run(runningScript);
                        return null;
                    }
                };
            }
        };
        scriptService.setOnCancelled(event -> {
            scriptService.reset();
            runningScript = null;
            logger.warning("Script is cancelled");
        });
        scriptService.setOnFailed(event -> {
            scriptService.reset();
            runningScript = null;
            logger.warning("Script is failed");
        });
        scriptService.setOnSucceeded(event -> {
            scriptService.reset();
            runningScript = null;
            logger.log(Level.INFO, "Script is succeeded");
        });
    }

    public boolean isRunning() {
        return looper.isRunning();
    }

    public Settings loadSettings() {
        return Settings.instance();
    }

    public void runScript(final String script) throws IllegalAccessException {
        if (this.runningScript != null) {
            throw new IllegalAccessException("Wait the previous script");
        }
        this.runningScript = script;
        if (scriptService.getState() == State.READY) {
            scriptService.start();
        }
        this.lastRunnedScript = script;
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
