package ImageEditor.Core;

import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;
import javax.swing.JOptionPane;

public class ExposureSafetyObserver implements Observer {
    private final ImageEditor editor;
    private boolean warningShown = false;
    private final boolean isGui;

    public ExposureSafetyObserver(ImageEditor editor, boolean isGui) {
        this.editor = editor;
        this.isGui = isGui;
    }

    @Override
    public void update() {
        ImageState state = editor.getState();

        int brightness = state.getAdjustmentState().getOrDefault(AdjustmentType.BRIGHTNESS, 0);
        int exposure = state.getAdjustmentState().getOrDefault(AdjustmentType.EXPOSURE, 0);

        int totalLight = (brightness+35) + (exposure * 2);

        if (brightness > 70 || exposure > 45 || totalLight > 100) {
            if (!warningShown) {
                if (isGui) {
                    JOptionPane.showMessageDialog(null,
                            "Warning: Too much brightness/exposure could result in clipping!",
                            "Annoying pop-up by: histogram observer", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    System.out.println("[Warning]: Too much brightness/exposure could result in clipping!");
                }

                warningShown = true;
            }
        } else {
            warningShown = false;
        }
    }
}