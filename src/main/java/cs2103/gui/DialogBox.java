package cs2103.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

public class DialogBox {

    @FXML private Label dialog;
    @FXML private ImageView displayPicture;


    public static Node user(String text) {
        return create(text, "/images/user.png", false);
    }

    public static Node bot(String text) {
        return create(text, "/images/paneer.png", true);
    }


    private static Node create(String text, String imagePath, boolean isBot) {
        try {
            FXMLLoader loader = new FXMLLoader(DialogBox.class.getResource("/view/dialog-box.fxml"));
            HBox root = loader.load(); // controller is instantiated by FXML
            DialogBox controller = loader.getController();

            controller.dialog.setText(text);
            Image avatar = new Image(Objects.requireNonNull(
                    DialogBox.class.getResourceAsStream(imagePath),
                    "Missing image resource: " + imagePath));
            controller.displayPicture.setImage(avatar);

            root.setAlignment(isBot ? Pos.TOP_LEFT : Pos.TOP_RIGHT);

            root.getChildren().remove(controller.dialog);
            root.getChildren().remove(controller.displayPicture);
            if (isBot) {
                root.getChildren().addAll(controller.displayPicture, controller.dialog);
            } else {
                root.getChildren().addAll(controller.dialog, controller.displayPicture);
            }

            return root;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load /view/dialog-box.fxml", e);
        }
    }
}