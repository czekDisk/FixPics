import EditorUi.EditorCli;
import EditorUi.EditorGui;
import javax.swing.*;

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
