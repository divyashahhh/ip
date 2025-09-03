import javafx.application.Application;

/** A launcher class to workaround classpath issues. */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(cs2103.gui.Main.class, args);
    }
}