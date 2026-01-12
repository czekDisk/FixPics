import EditorUi.EditorGui;
import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            new EditorGui();
        });
    }
}
