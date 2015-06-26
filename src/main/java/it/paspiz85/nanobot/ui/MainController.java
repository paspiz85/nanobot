package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.logic.Setup;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Settings;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Main GUI controller.
 *
 * @author paspiz85
 *
 */
public class MainController implements ApplicationAwareController, Constants {

    Application application;

    @FXML
    ComboBox<Attack> autoAttackComboBox;

    @FXML
    Button cancelButton;

    @FXML
    GridPane configGridPane;

    @FXML
    CheckBox saveEnemyCheckBox;

    @FXML
    AnchorPane controlPane;

    @FXML
    TextField deField;

    @FXML
    CheckBox detectEmptyCollectorsCheckBox;

    @FXML
    Label donateLabel;

    @FXML
    Hyperlink donateLink;

    @FXML
    TextField elixirField;

    @FXML
    Hyperlink githubLink;

    @FXML
    TextField goldField;

    @FXML
    ImageView heartImage;

    @FXML
    CheckBox isMatchAllConditionsCheckBox;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    TextField maxThField;

    @FXML
    CheckBox playSoundCheckBox;

    @FXML
    ComboBox<Level> logLevelComboBox;

    @FXML
    ComboBox<Clickable> rax1ComboBox;

    @FXML
    ComboBox<Clickable> rax2ComboBox;

    @FXML
    ComboBox<Clickable> rax3ComboBox;

    @FXML
    ComboBox<Clickable> rax4ComboBox;

    @FXML
    Button settingsButton;

    @FXML
    AnchorPane setupPane;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    TextArea textArea;

    @FXML
    Label updateLabel;

    @FXML
    Label versionLabel;

    private boolean setupDone;

    private Service<Void> runnerService;

    /**
     * GitHub dependency is only used here and unused parts are excluded. Make
     * sure it works fine if it is used somewhere else.
     */
    boolean checkForUpdate() {
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

    @FXML
    public void handleCancelButtonAction() {
        showSettings(false);
    }

    @FXML
    public void handleSaveButtonAction() {
        if (!goldField.getText().isEmpty()) {
            Settings.instance().setGoldThreshold(Integer.parseInt(goldField.getText()));
        }
        if (!elixirField.getText().isEmpty()) {
            Settings.instance().setElixirThreshold(Integer.parseInt(elixirField.getText()));
        }
        if (!deField.getText().isEmpty()) {
            Settings.instance().setDarkElixirThreshold(Integer.parseInt(deField.getText()));
        }
        if (!maxThField.getText().isEmpty()) {
            Settings.instance().setMaxThThreshold(Integer.parseInt(maxThField.getText()));
        }
        Settings.instance().setDetectEmptyCollectors(detectEmptyCollectorsCheckBox.isSelected());
        Settings.instance().setMatchAllConditions(isMatchAllConditionsCheckBox.isSelected());
        Settings.instance().setPlaySound(playSoundCheckBox.isSelected());
        Settings.instance().setLogLevel(logLevelComboBox.getValue());
        Settings.instance().setLogEnemyBase(saveEnemyCheckBox.isSelected());
        Settings.instance().setAttackStrategy(autoAttackComboBox.getValue());
        Settings.instance().getRaxInfo()[0] = rax1ComboBox.getValue();
        Settings.instance().getRaxInfo()[1] = rax2ComboBox.getValue();
        Settings.instance().getRaxInfo()[2] = rax3ComboBox.getValue();
        Settings.instance().getRaxInfo()[3] = rax4ComboBox.getValue();
        Settings.instance().save();
        showSettings(false);
    }

    @FXML
    public void handleSettingsButtonAction() {
        showSettings(true);
    }

    @FXML
    public void handleStartButtonAction() {
        if (runnerService.getState() == State.READY) {
            runnerService.start();
        }
    }

    @FXML
    public void handleStopButtonAction() {
        if (runnerService.isRunning()) {
            runnerService.cancel();
            runnerService.reset();
        }
    }

    @FXML
    void initialize() {
        // set system locale to ROOT, Turkish clients will break because
        // jnativehook dependency has Turkish I bug
        Locale.setDefault(Locale.ROOT);
        // setup configUtils
        Settings.initialize();
        LogHandler.initialize(textArea);
        logger.info("Settings loaded...");
        logger.info("Make sure in-game language is English.");
        initLabels();
        initLinks();
        initSettingsPane();
        initializeRunnerService();
        if (checkForUpdate()) {
            updateLabel.setVisible(true);
        }
    }

    void initializeRunnerService() {
        updateButtons(false);
        runnerService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        updateButtons(true);
                        if (!setupDone) {
                            Setup.instance().setup();
                            Settings.instance().save();
                            setupDone = true;
                        }
                        logger.info("Setup is successful.");
                        Looper.instance().start();
                        return null;
                    }
                };
            }
        };
        runnerService.setOnCancelled(event -> {
            updateButtons(false);
            logger.warning("runner is cancelled.");
            runnerService.reset();
        });
        runnerService.setOnFailed(event -> {
            updateButtons(false);
            logger.log(Level.SEVERE, "runner is failed: " + runnerService.getException().getMessage(),
                    runnerService.getException());
            runnerService.reset();
        });
    }

    void initLabels() {
        final String version = getClass().getPackage().getImplementationVersion();
        if (version != null) {
            versionLabel.setText(NAME + " v" + version);
        }
    }

    void initLinks() {
        githubLink.setOnAction(t -> {
            application.getHostServices().showDocument(githubLink.getText());
            githubLink.setVisited(false);
        });
        githubLink.setText(REPOSITORY_URL);
        githubLink.setVisible(true);
        final Image heartIcon = new Image(getClass().getResourceAsStream("heart.png"));
        donateLink.setGraphic(new ImageView(heartIcon));
        donateLink.setOnAction(event -> {
            application.getHostServices().showDocument(REPOSITORY_URL + "#donate");
            donateLink.setVisited(false);
        });
    }

    void initSettingsPane() {
        final ChangeListener<String> intFieldListener = (observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    Integer.parseInt(newValue);
                }
            } catch (final NumberFormatException e) {
                ((TextField) ((StringProperty) observable).getBean()).setText(oldValue);
            }
        };
        goldField.textProperty().addListener(intFieldListener);
        elixirField.textProperty().addListener(intFieldListener);
        deField.textProperty().addListener(intFieldListener);
        maxThField.textProperty().addListener(intFieldListener);
        logLevelComboBox.getItems().addAll(Level.FINE, Level.INFO, Level.WARNING, Level.SEVERE);
        logLevelComboBox.setValue(logLevelComboBox.getItems().get(1));
        autoAttackComboBox.getItems().addAll(Attack.getAvailableStrategies());
        autoAttackComboBox.setValue(autoAttackComboBox.getItems().get(0));
        final Clickable[] availableTroops = Settings.instance().getAvailableTroops();
        rax1ComboBox.getItems().addAll(availableTroops);
        rax2ComboBox.getItems().addAll(availableTroops);
        rax3ComboBox.getItems().addAll(availableTroops);
        rax4ComboBox.getItems().addAll(availableTroops);
        goldField.setText(Settings.instance().getGoldThreshold() + "");
        elixirField.setText(Settings.instance().getElixirThreshold() + "");
        deField.setText(Settings.instance().getDarkElixirThreshold() + "");
        maxThField.setText(Settings.instance().getMaxThThreshold() + "");
        detectEmptyCollectorsCheckBox.setSelected(Settings.instance().isDetectEmptyCollectors());
        isMatchAllConditionsCheckBox.setSelected(Settings.instance().isMatchAllConditions());
        playSoundCheckBox.setSelected(Settings.instance().isPlaySound());
        logLevelComboBox.getSelectionModel().select(Settings.instance().getLogLevel());
        saveEnemyCheckBox.setSelected(Settings.instance().isLogEnemyBase());
        autoAttackComboBox.getSelectionModel().select(Settings.instance().getAttackStrategy());
        rax1ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[0]);
        rax2ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[1]);
        rax3ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[2]);
        rax4ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[3]);
        configGridPane.setVisible(true);
    }

    @Override
    public void setApplication(final Application application) {
        this.application = application;
        showSettings(false);
    }

    void showSettings(final boolean value) {
        setupPane.setVisible(value);
        controlPane.setVisible(!value);
    }

    void updateButtons(final boolean value) {
        startButton.setDisable(value);
        stopButton.setDisable(!value);
    }
}
