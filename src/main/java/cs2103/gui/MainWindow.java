package cs2103.gui;

import cs2103.Paneer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainWindow {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Paneer paneer;

    public void setPaneer(Paneer p) {
        this.paneer = p;
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Optional greeting from the bot on startup. */
    public void greet() {
        if (paneer == null) return;
        addBot("Hello! I'm Paneer. What can I cook up for you today?");
    }

    @FXML
    private void handleUserInput() {
        if (paneer == null) return;

        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response = paneer.getResponse(input);

        Node userBubble = DialogBox.user(input);
        Node botBubble  = DialogBox.bot(response);

        dialogContainer.getChildren().addAll(userBubble, botBubble);
        userInput.clear();

        if (paneer.shouldExit(input)) {

            Platform.runLater(() -> {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                Platform.exit();
            });
        }
    }

    private void addBot(String text) {
        dialogContainer.getChildren().add(DialogBox.bot(text));
    }
}