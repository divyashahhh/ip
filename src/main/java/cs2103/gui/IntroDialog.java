package cs2103.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class IntroDialog {

    static void showWelcome(Stage owner) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Welcome to Paneer");
        alert.setHeaderText("Hello! I'm Paneer \nYour yummy friendly task sous-chef!");

        VBox content = new VBox(8);
        content.setPadding(new Insets(10));

        Label intro = new Label("Paneer helps you track tasks. Type commands in the box below.\n"
                + "Here are the rules and examples to get you cooking fast:");
        intro.getStyleClass().add("intro-text");

        TextArea cmds = new TextArea(String.join("\n",
                "Valid commands:",
                "  - list",
                "  - todo <desc>",
                "  - deadline <desc> /by yyyy-MM-dd",
                "  - event <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm",
                "  - mark <index> | unmark <index> | delete <index>",
                "  - find <keyword>",
                "  - sort",
                "  - bye",
                "",
                "Examples:",
                "  todo read book",
                "  deadline submit report /by 2019-12-02",
                "  event project meeting /from 2019-12-02 1400 /to 2019-12-02 1600",
                "  mark 1",
                "  find book",
                "",
                "Invalid examples (and why):",
                "  deadline /by 2019-12-02   <- missing description",
                "  event party /from 1800   <- missing /to <end>",
                "  mark zero               <- index must be a number"
        ));
        cmds.setEditable(false);
        cmds.setWrapText(true);
        cmds.getStyleClass().add("intro-commands");
        cmds.setPrefRowCount(12);

        content.getChildren().addAll(intro, new Separator(), cmds);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getStylesheets().add(
                Main.class.getResource("/view/theme.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("paneer-dialog");

        // Size nicely relative to owner
        Stage s = (Stage) alert.getDialogPane().getScene().getWindow();
        if (owner != null) {
            s.setX(owner.getX() + 40);
            s.setY(owner.getY() + 40);
        }

        alert.showAndWait();
    }
}


