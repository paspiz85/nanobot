package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.parsing.Clickable;
import it.paspiz85.nanobot.util.Config;
import it.paspiz85.nanobot.util.Constants;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
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

public class MainController implements ApplicationAwareController, Constants {

	Application application;

	@FXML
	ComboBox<String> autoAttackComboBox;

	@FXML
	Button cancelButton;

	@FXML
	GridPane configGridPane;

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
	final MainModel model = new MainModel();
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
	Button setupButton;
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

	@FXML
	public void handleCancelButtonAction() {
		showSettings(false);
	}

	@FXML
	public void handleSaveButtonAction() {
		if (!goldField.getText().isEmpty()) {
			Config.instance().setGoldThreshold(
					Integer.parseInt(goldField.getText()));
		}

		if (!elixirField.getText().isEmpty()) {
			Config.instance().setElixirThreshold(
					Integer.parseInt(elixirField.getText()));
		}

		if (!deField.getText().isEmpty()) {
			Config.instance().setDarkElixirThreshold(
					Integer.parseInt(deField.getText()));
		}

		if (!maxThField.getText().isEmpty()) {
			Config.instance().setMaxThThreshold(
					Integer.parseInt(maxThField.getText()));
		}

		Config.instance().setMatchAllConditions(
				isMatchAllConditionsCheckBox.isSelected());
		Config.instance().setDetectEmptyCollectors(
				detectEmptyCollectorsCheckBox.isSelected());
		Config.instance().setPlaySound(playSoundCheckBox.isSelected());
		Config.instance().setAttackStrategy(autoAttackComboBox.getValue());
		Config.instance().getRaxInfo()[0] = Clickable
				.fromDescription(rax1ComboBox.getValue());
		Config.instance().getRaxInfo()[1] = Clickable
				.fromDescription(rax2ComboBox.getValue());
		Config.instance().getRaxInfo()[2] = Clickable
				.fromDescription(rax3ComboBox.getValue());
		Config.instance().getRaxInfo()[3] = Clickable
				.fromDescription(rax4ComboBox.getValue());

		Config.instance().save();
		showSettings(false);
	}

	@FXML
	public void handleSetupButtonAction() {
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
	void initialize() {
		LogHandler.initialize(textArea);
		model.initialize();

		initializeLinks();
		initializeLabels();
		initializeTextFields();
		githubLink.setText(REPOSITORY_URL);
		githubLink.setVisible(true);

		initializeComboBox();
		updateConfigGridPane();
		if (model.checkForUpdate()) {
			updateLabel.setVisible(true);
		}
	}

	void initializeComboBox() {
		autoAttackComboBox.getItems().addAll(
				Config.instance().getAttackStrategies());
		autoAttackComboBox.setValue(autoAttackComboBox.getItems().get(0));

		Clickable[] availableTroops = Config.instance().getAvailableTroops();
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

		Image heartIcon = new Image(getClass().getResourceAsStream(
				"/images/heart.png"));
		donateLink.setGraphic(new ImageView(heartIcon));

		donateLink.setOnAction(event -> {
			application.getHostServices().showDocument(
					REPOSITORY_URL + "#donate");
			donateLink.setVisited(false);
		});
	}

	void initializeTextFields() {
		ChangeListener<String> intFieldListener = (observable, oldValue,
				newValue) -> {
					try {
						if (!newValue.isEmpty()) {
							Integer.parseInt(newValue);
						}
					} catch (NumberFormatException e) {
						((TextField) ((StringProperty) observable).getBean())
						.setText(oldValue);
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
	}

	private void showSettings(boolean value) {
		setupPane.setVisible(value);
		controlPane.setVisible(!value);
	}

	void updateConfigGridPane() {
		goldField.setText(Config.instance().getGoldThreshold() + "");
		elixirField.setText(Config.instance().getElixirThreshold() + "");
		deField.setText(Config.instance().getDarkElixirThreshold() + "");
		maxThField.setText(Config.instance().getMaxThThreshold() + "");

		isMatchAllConditionsCheckBox.setSelected(Config.instance()
				.isMatchAllConditions());
		detectEmptyCollectorsCheckBox.setSelected(Config.instance()
				.isDetectEmptyCollectors());
		playSoundCheckBox.setSelected(Config.instance().isPlaySound());
		autoAttackComboBox.getSelectionModel().select(
				Config.instance().getAttackStrategy().getClass()
						.getSimpleName());
		rax1ComboBox.getSelectionModel().select(
				Config.instance().getRaxInfo()[0].getDescription());
		rax2ComboBox.getSelectionModel().select(
				Config.instance().getRaxInfo()[1].getDescription());
		rax3ComboBox.getSelectionModel().select(
				Config.instance().getRaxInfo()[2].getDescription());
		rax4ComboBox.getSelectionModel().select(
				Config.instance().getRaxInfo()[3].getDescription());

		configGridPane.setVisible(true);
	}

}
