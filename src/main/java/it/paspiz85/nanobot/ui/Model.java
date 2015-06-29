package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Bot model.
 *
 * @author paspiz85
 *
 */
public final class Model implements Constants {

    private static Model instance;

    public static Model instance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    private final Looper looper = Looper.instance();

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private Service<Void> runnerService;

    private Model() {
    }

    /**
     * GitHub dependency is only used here and unused parts are excluded. Make
     * sure it works fine if it is used somewhere else.
     */
    public boolean checkForUpdate() {
        boolean result = false;
        try {
            final String current = getClass().getPackage().getImplementationVersion();
            if (current != null) {
                final DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(current);
                final GitHub github = GitHub.connectAnonymously();
                final GHRepository repository = github.getRepository(REPOSITORY_NAME);
                for (final GHRelease r : repository.listReleases()) {
                    final String release = r.getName().substring(1);
                    final DefaultArtifactVersion releaseVersion = new DefaultArtifactVersion(release);
                    if (currentVersion.compareTo(releaseVersion) < 0) {
                        result = true;
                    }
                }
            }
        } catch (final Exception e) {
            logger.log(Level.WARNING, "Unable to get latest version", e);
        }
        return result;
    }

    public Clickable[] getAvailableTroops() {
        return Settings.instance().getAvailableTroops();
    }

    public void initialize(final BooleanSupplier setupResolution, final Supplier<Point> setupBarracks,
            final Runnable updateUI) {
        // set system locale to ROOT, Turkish clients will break because
        // jnativehook dependency has Turkish I bug
        Locale.setDefault(Locale.ROOT);
        // setup configUtils
        Settings.initialize();
        logger.info("Settings loaded...");
        logger.info("Make sure in-game language is English.");
        runnerService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        updateUI.run();
                        looper.start(setupResolution, setupBarracks,updateUI);
                        return null;
                    }
                };
            }
        };
        runnerService.setOnCancelled(event -> {
            runnerService.reset();
            logger.warning("runner is cancelled.");
            updateUI.run();
        });
        runnerService.setOnFailed(event -> {
            runnerService.reset();
            logger.log(Level.SEVERE, "runner is failed: " + runnerService.getException().getMessage(),
                    runnerService.getException());
            updateUI.run();
        });
    }

    public boolean isRunning() {
        return looper.isRunning();
    }

    public Settings loadSettings() {
        return Settings.instance();
    }

    public void resetBarracks() {
        Settings.instance().setFirstBarrackPosition(null);
    }

    public void saveSettings(final Consumer<Settings> consumer) {
        final Settings settings = Settings.instance();
        consumer.accept(settings);
        settings.save();
    }

    public void start() {
        if (runnerService.getState() == State.READY) {
            runnerService.start();
        }
    }

    public void stop() {
        if (runnerService.isRunning()) {
            runnerService.cancel();
            runnerService.reset();
        }
    }
}
