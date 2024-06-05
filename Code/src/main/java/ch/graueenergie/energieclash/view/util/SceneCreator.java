package ch.graueenergie.energieclash.view.util;

import ch.graueenergie.energieclash.view.AbstractSynchView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SceneCreator {
    protected static final Logger LOGGER = LogManager.getLogger(SceneCreator.class);

    public static void setViewToStage(Stage stage, AbstractSynchView view, String viewPath, boolean hiddenCursor) {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(SceneCreator.class.getResource(viewPath));
                fxmlLoader.setController(view);
                Parent parent = fxmlLoader.load();
                Scene scene = stage.getScene();
                if (scene == null) {
                    scene = new Scene(parent);
                    stage.setScene(scene);
                } else {
                    scene.setRoot(parent);
                }
                scene.setCursor(hiddenCursor ? Cursor.NONE : Cursor.DEFAULT);
                stage.show();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        });
    }
}
