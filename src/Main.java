import EditorUi.EditorCli;
import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import EditorUi.EditorGui;
import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        boolean useCLI = false;

        for (String arg : args) {
            if (arg.equals("-i") || arg.equals("--interactive")) {
                useCLI = true;
                break;
            }
        }

        if (useCLI) {
            EditorCli cli = new EditorCli();
            cli.start();
        }
        else {
            SwingUtilities.invokeLater(() -> {
                new EditorGui();
            });
        }
    }
}
