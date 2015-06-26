package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.logic.Looper;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;
import it.paspiz85.nanobot.win32.OS;

import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
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

    private Application application;

    @FXML
    private ComboBox<Attack> autoAttackComboBox;

    @FXML
    private GridPane configGridPane;

    @FXML
    private CheckBox saveEnemyCheckBox;

    @FXML
    private AnchorPane controlPane;

    @FXML
    private TextField deField;

    @FXML
    private CheckBox detectEmptyCollectorsCheckBox;

    @FXML
    private Label donateLabel;

    @FXML
    private Hyperlink donateLink;

    @FXML
    private TextField elixirField;

    @FXML
    private Hyperlink githubLink;

    @FXML
    private TextField goldField;

    @FXML
    private ImageView heartImage;

    @FXML
    private CheckBox isMatchAllConditionsCheckBox;

    protected final Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private TextField maxThField;

    @FXML
    private CheckBox playSoundCheckBox;

    @FXML
    private ComboBox<Level> logLevelComboBox;

    @FXML
    private ComboBox<Clickable> rax1ComboBox;

    @FXML
    private ComboBox<Clickable> rax2ComboBox;

    @FXML
    private ComboBox<Clickable> rax3ComboBox;

    @FXML
    private ComboBox<Clickable> rax4ComboBox;

    @FXML
    private Button settingsButton;

    @FXML
    private AnchorPane setupPane;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private TextArea textArea;

    @FXML
    private Label updateLabel;

    @FXML
    private Label versionLabel;

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
    public void handleResetButtonAction() {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Reset Settings");
        alert.setHeaderText("This operation delete previous settings");
        alert.setContentText("Are you sure?");
        final Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Settings.instance().reset();
            updateSettingsPane();
        }
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
    private void initialize() {
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
        updateSettingsPane();
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
                        setupResolution();
                        updateButtons(true);
                        Looper.instance().start(() -> setupResolution(), () -> setupBarracks());
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

    @Override
    public void setApplication(final Application application) {
        this.application = application;
        showSettings(false);
    }

    Point setupBarracks() {
        logger.info("Setting up Barracks...");
        Point point = null;
        try {
            OS.instance().zoomUp();
            final Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Barracks configuration");
            alert.setHeaderText("You must configure the location " + "of first Barracks");
            alert.setContentText("First Barracks is the leftmost one when you \n"
                    + "scroll through your barracks via orange next arrow on the right. For example, if you \n"
                    + "have 4 barracks, when you select the first one and click 'Train Troops', all \n"
                    + "3 'next' views should also be barracks.\n\n"
                    + "Click Yes to start configuration and click on your first barracks. Do \n"
                    + "NOT click anything else in between. Click Yes and click barracks. \n\n"
                    + "Make sure you are max zoomed out.");
            final Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                point = OS.instance().waitForClick();
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to setup barracks", e);
        }
        return point;
    }

    boolean setupResolution() {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Resolution");
        alert.setHeaderText("Confirm changing resolution");
        alert.setContentText(String.format("%s must run in resolution %dx%d.\n"
                + "Click YES to change it automatically, NO to do it later.\n", BS_WINDOW_NAME, BS_RES_X, BS_RES_Y));
        final Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    void showSettings(final boolean value) {
        setupPane.setVisible(value);
        controlPane.setVisible(!value);
    }

    void updateButtons(final boolean value) {
        startButton.setDisable(value);
        stopButton.setDisable(!value);
    }

    void updateSettingsPane() {
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
}
