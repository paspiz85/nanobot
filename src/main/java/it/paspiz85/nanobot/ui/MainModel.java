/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.logic.Setup;
import it.paspiz85.nanobot.util.Constants;

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
 *
 * @author v-ppizzuti
 */
public class MainModel implements Constants {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	private boolean setupDone = false;

	private Service<Void> setupService = null;

	private Service<Void> runnerService = null;

	private void botLauncherSetup() throws Exception {
		Setup.instance().setup();
	}

	private void botLauncherStart() throws Exception {
		Looper.instance().start();
	}

	private void botLauncherTearDown() {
		Looper.instance().tearDown();
	}

	/**
	 * GitHub dependency is only used here and unused parts are excluded. Make
	 * sure it works fine if it is used somewhere else.
	 */
	public boolean checkForUpdate() {
		try {
			String current = getClass().getPackage().getImplementationVersion();
			if (current == null) {
				// IDE run
				return false;
			}
			DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(
					current);
			GitHub github = GitHub.connectAnonymously();
			GHRepository repository = github.getRepository(REPOSITORY_NAME);
			for (GHRelease r : repository.listReleases()) {
				String release = r.getName().substring(1);
				DefaultArtifactVersion releaseVersion = new DefaultArtifactVersion(
						release);
				if (currentVersion.compareTo(releaseVersion) < 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to get latest version", e);
		}
		return false;
	}

	void initialize() {
		initializeSetupService();
		initializeRunnerService();

		if (setupService.getState() == State.READY) {
			setupService.start();
		}
	}

	private void initializeRunnerService() {
		runnerService = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						botLauncherStart();
						return null;
					}
				};
			}
		};

		runnerService.setOnCancelled(event -> {
			logger.warning("runner is cancelled.");
			runnerService.reset();
		});

		runnerService.setOnFailed(event -> {
			logger.log(Level.SEVERE, "runner is failed: "
					+ runnerService.getException().getMessage(),
					runnerService.getException());
			runnerService.reset();
		});
	}

	private void initializeSetupService() {
		setupService = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						botLauncherTearDown();
						botLauncherSetup();
						return null;
					}
				};
			}
		};
		setupService.setOnSucceeded(event -> {
			setupDone = true;
			logger.info("Setup is successful.");
			logger.info("Click start to run.");
		});

		setupService.setOnFailed(event -> {
			setupDone = false;
			logger.log(Level.SEVERE, "Setup is failed: "
					+ setupService.getException().getMessage(),
					setupService.getException());
			setupService.reset();
		});

		setupService.setOnCancelled(event -> {
			setupDone = false;
			logger.warning("Setup is cancelled.");
			setupService.reset();
		});
	}

	public void start() {
		if (setupDone && runnerService.getState() == State.READY) {
			runnerService.start();
		}
	}

	public void stop() {
		if (setupService.isRunning()) {
			setupService.cancel();
			setupService.reset();
		}
		if (runnerService.isRunning()) {
			runnerService.cancel();
			runnerService.reset();
		}
	}

}
