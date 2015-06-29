package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.attack.Attack;
import it.paspiz85.nanobot.os.OS;
import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Point;
import it.paspiz85.nanobot.util.Settings;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

/**
 * Main GUI controller.
 *
 * @author paspiz85
 *
 */
public class MainController implements ApplicationAwareController, Constants {

    private static final OS DEFAULT_OS = OS.instance();

    private Application application;

    private final Model model = Model.instance();

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
    private ComboBox<Clickable> rax5ComboBox;

    @FXML
    private ComboBox<Clickable> rax6ComboBox;

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

    private final OS os = DEFAULT_OS;

    @FXML
    public void handleCancelButtonAction() {
        showSettings(false);
    }

    @FXML
    public void handleResetBarracksButtonAction() {
        final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(application.getPrimaryStage());
        alert.setTitle("Reset Settings");
        alert.setHeaderText("This operation delete previous barracks settings");
        alert.setContentText("Are you sure?");
        final Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            model.resetBarracks();
        }
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
            settings.setPlaySound(playSoundCheckBox.isSelected());
            settings.setLogLevel(logLevelComboBox.getValue());
            settings.setLogEnemyBase(saveEnemyCheckBox.isSelected());
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
        model.initialize(() -> setupResolution(), () -> setupBarracksStep1(), () -> updateButtons());
        initLabels();
        initLinks();
        initSettingsPane();
        if (model.checkForUpdate()) {
            updateLabel.setVisible(true);
        }
    }

    private void initLabels() {
        final String version = getClass().getPackage().getImplementationVersion();
        if (version != null) {
            versionLabel.setText(NAME + " v" + version);
        }
    }

    private void initLinks() {
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

    private void initSettingsPane() {
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
        final Clickable[] availableTroops = model.getAvailableTroops();
        rax1ComboBox.getItems().addAll(availableTroops);
        rax2ComboBox.getItems().addAll(availableTroops);
        rax3ComboBox.getItems().addAll(availableTroops);
        rax4ComboBox.getItems().addAll(availableTroops);
        rax5ComboBox.getItems().addAll(availableTroops);
        rax6ComboBox.getItems().addAll(availableTroops);
        updateSettingsPane(model.loadSettings());
    }

    private void platformRunNow(final Runnable runnable) throws InterruptedException {
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
        Platform.runLater(sync);
        logger.fine("platformRunNow wait start");
        synchronized (sync) {
            while (!done[0]) {
                sync.wait();
            }
        }
        logger.fine("platformRunNow wait complete");
    }

    @Override
    public void setApplication(final Application application) {
        this.application = application;
        showSettings(false);
    }

    private Point setupBarracksStep1() {
        Point point = null;
        try {
            final boolean[] configure = new boolean[1];
            platformRunNow(() -> {
                try {
                    final Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.initOwner(application.getPrimaryStage());
                    alert.setTitle("Barracks configuration");
                    alert.setHeaderText("You must configure the location " + "of first Barracks");
                    alert.setContentText("Click Yes to start configuration.");
                    final Optional<ButtonType> result = alert.showAndWait();
                    configure[0] = result.get() == ButtonType.OK;
                } catch (final Exception e) {
                    logger.log(Level.SEVERE, "Unable to setup barracks", e);
                }
            });
            if (configure[0]) {
                point = setupBarracksStep2();
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to setup barracks", e);
        }
        return point;
    }

    private Point setupBarracksStep2() throws InterruptedException {
        final Point[] point = new Point[1];
        os.zoomUp();
        platformRunNow(() -> {
            try {
                final Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initOwner(application.getPrimaryStage());
                alert.setTitle("Barracks configuration");
                alert.setHeaderText("You must configure the location " + "of first Barracks");
                alert.setContentText("Make sure you are max zoomed out.\n\n"
                        + "First Barracks is the leftmost one when you "
                        + "scroll through your barracks via orange next arrow on the right. For example, if you "
                        + "have 4 barracks, when you select the first one and click 'Train Troops', all "
                        + "3 'next' views should also be barracks.\n\n"
                        + "Click Yes and click on your first barracks. Do "
                        + "NOT click anything else in between, click Yes and click barracks." + "");
                final Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    logger.info("Waiting for user to click on first barracks.");
                    point[0] = os.waitForClick();
                }
            } catch (final Exception e) {
                logger.log(Level.SEVERE, "Unable to setup barracks", e);
            }
        });
        return point[0];
    }

    private boolean setupResolution() {
        final boolean[] ret = new boolean[1];
        try {
            platformRunNow(() -> {
                final Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initOwner(application.getPrimaryStage());
                alert.setTitle("Resolution");
                alert.setHeaderText("Confirm changing resolution");
                alert.setContentText(String.format("%s must run in resolution %dx%d.\n"
                        + "Click YES to change it automatically, NO to do it later.\n", BS_WINDOW_NAME, BS_RES_X,
                        BS_RES_Y));
                final Optional<ButtonType> result = alert.showAndWait();
                ret[0] = result.get() == ButtonType.OK;
            });
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Unable to setup resolution", e);
        }
        return ret[0];
    }

    private void showSettings(final boolean value) {
        setupPane.setVisible(value);
        controlPane.setVisible(!value);
    }

    private void updateButtons() {
        final boolean value = model.isRunning();
        startButton.setDisable(value);
        stopButton.setDisable(!value);
    }

    private void updateSettingsPane(final Settings settings) {
        goldField.setText(settings.getGoldThreshold() + "");
        elixirField.setText(settings.getElixirThreshold() + "");
        deField.setText(settings.getDarkElixirThreshold() + "");
        maxThField.setText(settings.getMaxThThreshold() + "");
        detectEmptyCollectorsCheckBox.setSelected(settings.isDetectEmptyCollectors());
        isMatchAllConditionsCheckBox.setSelected(settings.isMatchAllConditions());
        playSoundCheckBox.setSelected(settings.isPlaySound());
        logLevelComboBox.getSelectionModel().select(settings.getLogLevel());
        saveEnemyCheckBox.setSelected(settings.isLogEnemyBase());
        autoAttackComboBox.getSelectionModel().select(settings.getAttackStrategy());
        rax1ComboBox.getSelectionModel().select(settings.getRaxInfo()[0]);
        rax2ComboBox.getSelectionModel().select(settings.getRaxInfo()[1]);
        rax3ComboBox.getSelectionModel().select(settings.getRaxInfo()[2]);
        rax4ComboBox.getSelectionModel().select(settings.getRaxInfo()[3]);
        rax5ComboBox.getSelectionModel().select(settings.getRaxInfo()[4]);
        rax6ComboBox.getSelectionModel().select(settings.getRaxInfo()[5]);
    }
}
