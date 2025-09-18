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
        addBot("Namaste! I'm Paneer  your task tandoor. What shall we simmer today?");
    }

    @FXML
    private void handleUserInput() {
        if (paneer == null) return;

        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response = paneer.getResponse(input);

        // If Paneer requests interactive edit, show a prompt dialog
        if (response != null && response.startsWith("__PANEER_PROMPT_EDIT__")) {
            int idx = Integer.parseInt(response.substring("__PANEER_PROMPT_EDIT__".length()));
            javafx.scene.control.TextInputDialog dlg = new javafx.scene.control.TextInputDialog();
            dlg.setTitle("Edit Task");
            dlg.setHeaderText("Enter a replacement command for task " + (idx + 1));
            dlg.setContentText("Example: todo read book | deadline desc /by yyyy-MM-dd | event desc /from ... /to ...");
            dlg.getDialogPane().getStylesheets().add(Main.class.getResource("/view/theme.css").toExternalForm());
            dlg.getDialogPane().getStyleClass().add("paneer-dialog");
            dlg.showAndWait().ifPresent(newCmd -> {
                if (newCmd != null && !newCmd.trim().isEmpty()) {
                    String userEcho = "edit " + (idx + 1);
                    Node userBubble = DialogBox.user(userEcho);
                    dialogContainer.getChildren().add(userBubble);
                    String resp = paneer.getResponse("__APPLY_EDIT__ " + idx + " " + newCmd.trim());
                    Node botBubble  = DialogBox.bot(resp);
                    dialogContainer.getChildren().add(botBubble);
                }
            });
            userInput.clear();
            return;
        }

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