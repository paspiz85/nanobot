package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.util.Constants;
import it.paspiz85.nanobot.util.Logging;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for GUI.
 *
 * @author paspiz85
 *
 */
public class Application extends javafx.application.Application implements Constants {

    public static void main(final String[] args) {
        Logging.initialize();
        try {
            launch(Application.class, args);
        } finally {
            Logging.close();
        }
    }

    private Stage primaryStage;

    Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        primaryStage.setTitle(NAME);
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Application.class.getResource("MainView.fxml"));
        final Parent parent = loader.load();
        final Object controller = loader.getController();
        if (controller instanceof ApplicationAwareController) {
            ((ApplicationAwareController) controller).setApplication(this);
        }
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
