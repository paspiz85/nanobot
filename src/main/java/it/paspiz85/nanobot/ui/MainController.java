package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.parsing.TroopButton;
import it.paspiz85.nanobot.platform.Platform;
import it.paspiz85.nanobot.scripting.ScriptManager;
import it.paspiz85.nanobot.util.BuildInfo;
import it.paspiz85.nanobot.util.Settings;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;

/**
 * Main GUI controller.
 *
 * @author paspiz85
 *
 */
public class MainController implements ApplicationAwareController {

    private Application application;

    @FXML
    private ComboBox<Attack> autoAttackComboBox;

    @FXML
    private CheckBox collectResourcesCheckBox;

    @FXML
    private GridPane configGridPane;

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
    private ComboBox<Level> logLevelComboBox;

    @FXML
    private TextField maxThField;

    private final Model model = Model.instance();

    private final Platform platform = Platform.instance();

    @FXML
    private ComboBox<TroopButton> rax1ComboBox;

    @FXML
    private ComboBox<TroopButton> rax2ComboBox;

    @FXML
    private ComboBox<TroopButton> rax3ComboBox;

    @FXML
    private ComboBox<TroopButton> rax4ComboBox;

    @FXML
    private ComboBox<TroopButton> rax5ComboBox;

    @FXML
    private ComboBox<TroopButton> rax6ComboBox;

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
    private Slider trainTroopsSlider;

    @FXML
    private Label versionLabel;

    @FXML
    private WebView webView;

    private void alert(final String str) {
        platformRunNow(() -> {
            final Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.initOwner(application.getPrimaryStage());
            dialog.setHeaderText(str);
            final Optional<ButtonType> result = dialog.showAndWait();
        });
    }

    private boolean autoAdjustResolution() {
        final boolean[] ret = new boolean[1];
        try {
            platformRunNow(() -> {
                final Alert dialog = new Alert(AlertType.CONFIRMATION);
                dialog.initOwner(application.getPrimaryStage());
                dialog.setTitle("Resolution");
                dialog.setHeaderText("Confirm changing resolution");
                dialog.setContentText(String.format("Game must run in resolution %s.\n"
                        + "Click YES to change it automatically, NO to do it later.\n", platform.getExpectedSize()
                        .toString()));
                final Optional<ButtonType> result = dialog.showAndWait();
                ret[0] = result.isPresent() && result.get() == ButtonType.OK;
            });
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to setup resolution", e);
        }
        return ret[0];
    }

    private boolean confirm(final String str) {
        final boolean[] toReturn = new boolean[1];
        platformRunNow(() -> {
            final Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.initOwner(application.getPrimaryStage());
            dialog.setHeaderText(str);
            final Optional<ButtonType> result = dialog.showAndWait();
            toReturn[0] = result.isPresent() && result.get() == ButtonType.OK;
        });
        return toReturn[0];
    }

    @FXML
    public void handleCancelButtonAction() {
        showSettings(false);
    }

    @FXML
    public void handleSaveButtonAction() {
        model.saveSettings((settings) -> {
            if (!goldField.getText().isEmpty()) {
                settings.setGoldThreshold(Integer.parseInt(goldField.getText()));
            }
            if (!elixirField.getText().isEmpty()) {
                settings.setElixirThreshold(Integer.parseInt(elixirField.getText()));
            }
            if (!deField.getText().isEmpty()) {
                settings.setDarkElixirThreshold(Integer.parseInt(deField.getText()));
            }
            if (!maxThField.getText().isEmpty()) {
                settings.setMaxThThreshold(Integer.parseInt(maxThField.getText()));
            }
            settings.setDetectEmptyCollectors(detectEmptyCollectorsCheckBox.isSelected());
            settings.setMatchAllConditions(isMatchAllConditionsCheckBox.isSelected());
            settings.setCollectResources(collectResourcesCheckBox.isSelected());
            settings.setTrainMaxTroops((int) Math.round(trainTroopsSlider.getValue()));
            settings.setLogLevel(logLevelComboBox.getValue());
            settings.setAttackStrategy(autoAttackComboBox.getValue());
            settings.getRaxInfo()[0] = rax1ComboBox.getValue();
            settings.getRaxInfo()[1] = rax2ComboBox.getValue();
            settings.getRaxInfo()[2] = rax3ComboBox.getValue();
            settings.getRaxInfo()[3] = rax4ComboBox.getValue();
            settings.getRaxInfo()[4] = rax5ComboBox.getValue();
            settings.getRaxInfo()[5] = rax6ComboBox.getValue();
        });
        showSettings(false);
    }

    @FXML
    public void handleScriptsButtonAction() {
        final ChoiceDialog<String> dialog = new ChoiceDialog<>(null, model.getScripts());
        dialog.initOwner(application.getPrimaryStage());
        dialog.setTitle("Scripts");
        dialog.setHeaderText("Select script to run");
        dialog.setContentText("Script:");
        final Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            model.runScript(result.get());
        }
    }

    @FXML
    public void handleSettingsButtonAction() {
        showSettings(true);
    }

    @FXML
    public void handleStartButtonAction() {
        model.start();
    }

    @FXML
    public void handleStopButtonAction() {
        model.stop();
    }

    @FXML
    private void initialize() {
        LogHandler.initialize(textArea);
        ScriptManager.instance().setAlert((str) -> alert(str));
        ScriptManager.instance().setConfirm((str) -> confirm(str));
        ScriptManager.instance().setPrompt((str, defValue) -> prompt(str, defValue));
        ScriptManager.instance().setSelect((str, options) -> select(str, options));
        model.initialize(() -> autoAdjustResolution(), () -> updateUI());
        if (Settings.instance().isCheckUpdateOnStartup() && BuildInfo.instance().checkForUpdate() != null) {
            versionLabel.setText(BuildInfo.instance().getFullName() + " (UPDATE AVAILABLE!)");
            githubLink.setText(BuildInfo.instance().getLatestVersionUrl());
        } else {
            versionLabel.setText(BuildInfo.instance().getFullName());
            githubLink.setText(BuildInfo.instance().getRepositoryUrl());
        }
        githubLink.setOnAction(event -> {
            application.getHostServices().showDocument(githubLink.getText());
            githubLink.setVisited(false);
        });
        final Image heartIcon = new Image(getClass().getResourceAsStream("heart.png"));
        donateLink.setGraphic(new ImageView(heartIcon));
        donateLink.setOnAction(event -> {
            application.getHostServices().showDocument(BuildInfo.instance().getDonateUrl());
            donateLink.setVisited(false);
        });
        webView.getEngine().load(BuildInfo.instance().getAdUrl());
        webView.getEngine().locationProperty().addListener((observable, oldLocation, newLocation) -> {
            if (BuildInfo.instance().getAdUrl().equals(oldLocation)) {
                application.getHostServices().showDocument(newLocation);
            }
        });
        final Thread reloadThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    javafx.application.Platform.runLater(() -> {
                        webView.getEngine().reload();
                    });
                } catch (final Exception e) {
                    logger.log(Level.FINE, "Refresh failed", e);
                }
            }
        }, "AdvReloader");
        reloadThread.setDaemon(true);
        reloadThread.start();
        initSettingsPane();
        updateUI();
    }

    private void initSettingsPane() {
        trainTroopsSlider.setMin(0);
        trainTroopsSlider.setMax(Settings.MAX_TRAIN_TROOPS);
        final TroopButton[] availableTroops = model.getAvailableTroops();
        rax1ComboBox.getItems().addAll(availableTroops);
        rax2ComboBox.getItems().addAll(availableTroops);
        rax3ComboBox.getItems().addAll(availableTroops);
        rax4ComboBox.getItems().addAll(availableTroops);
        rax5ComboBox.getItems().addAll(availableTroops);
        rax6ComboBox.getItems().addAll(availableTroops);
        autoAttackComboBox.getItems().addAll(Attack.getAvailableStrategies());
        autoAttackComboBox.setValue(autoAttackComboBox.getItems().get(0));
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
        logLevelComboBox.getItems().addAll(Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG, Level.INFO,
                Level.WARNING, Level.SEVERE);
        logLevelComboBox.setValue(logLevelComboBox.getItems().get(1));
        updateUI();
    }

    private void platformRunNow(final Runnable runnable) {
        final boolean[] done = new boolean[1];
        final Runnable sync = new Runnable() {

            @Override
            public void run() {
                logger.fine("platformRunNow run start");
                runnable.run();
                done[0] = true;
                logger.fine("platformRunNow run complete");
                synchronized (this) {
                    this.notify();
                }
            }
        };
        javafx.application.Platform.runLater(sync);
        logger.fine("platformRunNow wait start");
        synchronized (sync) {
            while (!done[0]) {
                try {
                    sync.wait();
                } catch (final InterruptedException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        logger.fine("platformRunNow wait complete");
    }

    private String prompt(final String msg, final String value) {
        final String[] toReturn = new String[1];
        platformRunNow(() -> {
            final TextInputDialog dialog = new TextInputDialog(value == null ? "" : value);
            dialog.initOwner(application.getPrimaryStage());
            dialog.setHeaderText(msg);
            final Optional<String> result = dialog.showAndWait();
            toReturn[0] = result.isPresent() ? result.get() : null;
        });
        return toReturn[0];
    }

    private String select(final String str, final String[] options) {
        final String[] toReturn = new String[1];
        platformRunNow(() -> {
            final ChoiceDialog<String> dialog = new ChoiceDialog<>(null, options);
            dialog.initOwner(application.getPrimaryStage());
            dialog.setHeaderText(str);
            final Optional<String> result = dialog.showAndWait();
            toReturn[0] = result.isPresent() ? result.get() : null;
        });
        return toReturn[0];
    }

    @Override
    public void setApplication(final Application application) {
        this.application = application;
        showSettings(false);
    }

    private void showSettings(final boolean value) {
        setupPane.setVisible(value);
        controlPane.setVisible(!value);
    }

    private void updateUI() {
        final boolean running = model.isRunning();
        startButton.setDisable(running);
        stopButton.setDisable(!running);
        final Settings settings = model.loadSettings();
        goldField.setText(settings.getGoldThreshold() + "");
        elixirField.setText(settings.getElixirThreshold() + "");
        deField.setText(settings.getDarkElixirThreshold() + "");
        maxThField.setText(settings.getMaxThThreshold() + "");
        detectEmptyCollectorsCheckBox.setSelected(settings.isDetectEmptyCollectors());
        isMatchAllConditionsCheckBox.setSelected(settings.isMatchAllConditions());
        collectResourcesCheckBox.setSelected(settings.isCollectResources());
        trainTroopsSlider.setValue(settings.getTrainMaxTroops());
        logLevelComboBox.getSelectionModel().select(settings.getLogLevel());
        autoAttackComboBox.getSelectionModel().select(settings.getAttackStrategy());
        rax1ComboBox.getSelectionModel().select(settings.getRaxInfo()[0]);
        rax2ComboBox.getSelectionModel().select(settings.getRaxInfo()[1]);
        rax3ComboBox.getSelectionModel().select(settings.getRaxInfo()[2]);
        rax4ComboBox.getSelectionModel().select(settings.getRaxInfo()[3]);
        rax5ComboBox.getSelectionModel().select(settings.getRaxInfo()[4]);
        rax6ComboBox.getSelectionModel().select(settings.getRaxInfo()[5]);
    }
}
