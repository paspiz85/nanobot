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

public class MainController implements ApplicationAwareController, Constants {

    Application application;

    @FXML
    ComboBox<String> autoAttackComboBox;

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
    ComboBox<String> rax1ComboBox;

    @FXML
    ComboBox<String> rax2ComboBox;

    @FXML
    ComboBox<String> rax3ComboBox;

    @FXML
    ComboBox<String> rax4ComboBox;

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

    private boolean setupDone = false;

    private Service<Void> runnerService = null;

    /**
     * GitHub dependency is only used here and unused parts are excluded. Make
     * sure it works fine if it is used somewhere else.
     */
    boolean checkForUpdate() {
        try {
            String current = getClass().getPackage().getImplementationVersion();
            if (current == null) {
                // IDE run
                return false;
            }
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(current);
            GitHub github = GitHub.connectAnonymously();
            GHRepository repository = github.getRepository(REPOSITORY_NAME);
            for (GHRelease r : repository.listReleases()) {
                String release = r.getName().substring(1);
                DefaultArtifactVersion releaseVersion = new DefaultArtifactVersion(release);
                if (currentVersion.compareTo(releaseVersion) < 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to get latest version", e);
        }
        return false;
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
        Settings.instance().setLogEnemyBase(saveEnemyCheckBox.isSelected());
        Settings.instance().setAttackStrategy(autoAttackComboBox.getValue());
        Settings.instance().getRaxInfo()[0] = Clickable.fromDescription(rax1ComboBox.getValue());
        Settings.instance().getRaxInfo()[1] = Clickable.fromDescription(rax2ComboBox.getValue());
        Settings.instance().getRaxInfo()[2] = Clickable.fromDescription(rax3ComboBox.getValue());
        Settings.instance().getRaxInfo()[3] = Clickable.fromDescription(rax4ComboBox.getValue());
        Settings.instance().save();
        showSettings(false);
    }

    @FXML
    public void handleSettingsButtonAction() {
        showSettings(true);
    }

    @FXML
    public void handleStartButtonAction() {
        if (setupDone && runnerService.getState() == State.READY) {
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
        LogHandler.initialize(textArea);
        // set system locale to ROOT, Turkish clients will break because
        // jnativehook dependency has Turkish I bug
        Locale.setDefault(Locale.ROOT);
        // setup configUtils
        logger.info("Setting up ConfigUtils...");
        logger.info("Make sure in-game language is English.");
        Settings.initialize();
        initializeLinks();
        initializeLabels();
        initializeTextFields();
        githubLink.setText(REPOSITORY_URL);
        githubLink.setVisible(true);
        initializeComboBox();
        updateConfigGridPane();
        updateButtons();
        initializeRunnerService();
        if (checkForUpdate()) {
            updateLabel.setVisible(true);
        }
    }

    void initializeComboBox() {
        Attack[] availableAttackStrategies = Settings.instance().getAvailableAttackStrategies();
        String[] attackStrategies = new String[availableAttackStrategies.length];
        for (int i = 0; i < availableAttackStrategies.length; i++) {
            Attack a = availableAttackStrategies[i];
            attackStrategies[i] = a.getClass().getSimpleName();
        }
        autoAttackComboBox.getItems().addAll(attackStrategies);
        autoAttackComboBox.setValue(autoAttackComboBox.getItems().get(0));
        Clickable[] availableTroops = Settings.instance().getAvailableTroops();
        String[] troops = new String[availableTroops.length];
        for (int i = 0; i < availableTroops.length; i++) {
            Clickable c = availableTroops[i];
            troops[i] = c.getDescription();
        }
        rax1ComboBox.getItems().addAll(troops);
        rax2ComboBox.getItems().addAll(troops);
        rax3ComboBox.getItems().addAll(troops);
        rax4ComboBox.getItems().addAll(troops);
    }

    void initializeLabels() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version != null) {
            versionLabel.setText(NAME + " v" + version);
        }
    }

    void initializeLinks() {
        githubLink.setOnAction(t -> {
            application.getHostServices().showDocument(githubLink.getText());
            githubLink.setVisited(false);
        });
        Image heartIcon = new Image(getClass().getResourceAsStream("heart.png"));
        donateLink.setGraphic(new ImageView(heartIcon));
        donateLink.setOnAction(event -> {
            application.getHostServices().showDocument(REPOSITORY_URL + "#donate");
            donateLink.setVisited(false);
        });
    }

    void initializeRunnerService() {
        runnerService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        if (!setupDone) {
                            Setup.instance().setup();
                            setupDone = true;
                            logger.info("Setup is successful.");
                        }
                        Looper.instance().start();
                        return null;
                    }
                };
            }
        };
        runnerService.setOnRunning(event -> {
            updateButtons();
        });
        runnerService.setOnCancelled(event -> {
            updateButtons();
            logger.warning("runner is cancelled.");
            runnerService.reset();
        });
        runnerService.setOnFailed(event -> {
            updateButtons();
            logger.log(Level.SEVERE, "runner is failed: " + runnerService.getException().getMessage(),
                    runnerService.getException());
            runnerService.reset();
        });
    }

    void initializeTextFields() {
        ChangeListener<String> intFieldListener = (observable, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    Integer.parseInt(newValue);
                }
            } catch (NumberFormatException e) {
                ((TextField) ((StringProperty) observable).getBean()).setText(oldValue);
            }
        };
        goldField.textProperty().addListener(intFieldListener);
        elixirField.textProperty().addListener(intFieldListener);
        deField.textProperty().addListener(intFieldListener);
        maxThField.textProperty().addListener(intFieldListener);
    }

    @Override
    public void setApplication(Application application) {
        this.application = application;
        showSettings(false);
    }

    void showSettings(boolean value) {
        setupPane.setVisible(value);
        controlPane.setVisible(!value);
    }

    void updateButtons() {
        boolean value = Looper.instance().isRunning();
        settingsButton.setDisable(value);
        startButton.setDisable(value);
        stopButton.setDisable(!value);
    }

    void updateConfigGridPane() {
        goldField.setText(Settings.instance().getGoldThreshold() + "");
        elixirField.setText(Settings.instance().getElixirThreshold() + "");
        deField.setText(Settings.instance().getDarkElixirThreshold() + "");
        maxThField.setText(Settings.instance().getMaxThThreshold() + "");
        detectEmptyCollectorsCheckBox.setSelected(Settings.instance().isDetectEmptyCollectors());
        isMatchAllConditionsCheckBox.setSelected(Settings.instance().isMatchAllConditions());
        playSoundCheckBox.setSelected(Settings.instance().isPlaySound());
        saveEnemyCheckBox.setSelected(Settings.instance().isLogEnemyBase());
        autoAttackComboBox.getSelectionModel().select(
                Settings.instance().getAttackStrategy().getClass().getSimpleName());
        rax1ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[0].getDescription());
        rax2ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[1].getDescription());
        rax3ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[2].getDescription());
        rax4ComboBox.getSelectionModel().select(Settings.instance().getRaxInfo()[3].getDescription());
        configGridPane.setVisible(true);
    }
}
