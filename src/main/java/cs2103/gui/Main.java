package cs2103.gui;

import cs2103.Paneer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/main-window.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource("/view/theme.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Paneer");
        stage.setWidth(480);
        stage.setHeight(640);
        stage.setX(200);  // force
        stage.setY(120);

        // Inject Paneer into controller
        MainWindow controller = loader.getController();

        Path cwd = Paths.get(System.getProperty("user.dir"));
        Path projectRoot = cwd.getFileName().toString().equals("text-ui-test") ? cwd.getParent() : cwd;
        String savePath = projectRoot.resolve("data").resolve("paneer.txt").toString();
        controller.setPaneer(new Paneer(savePath));


        stage.centerOnScreen();
        stage.show();


        Platform.runLater(() -> {
            // Show intro/help dialog once the stage is visible
            IntroDialog.showWelcome(stage);
            stage.setAlwaysOnTop(true);
            stage.toFront();
            stage.requestFocus();

            PauseTransition pt = new PauseTransition(Duration.millis(500));
            pt.setOnFinished(e -> stage.setAlwaysOnTop(false));  // setOnFinished returns void
            pt.play();


        controller.greet();
    });
}
}