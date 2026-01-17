package ImageEditor.Core;

import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterType;
import javax.swing.JOptionPane;

public class EdgeDetectionSafetyObserver implements Observer {
    private final ImageEditor editor;
    private final boolean isGui;
    private boolean warningShown = false;

    public EdgeDetectionSafetyObserver(ImageEditor editor, boolean isGui) {
        this.editor = editor;
        this.isGui = isGui;
    }

    @Override
    public void update() {
        ImageState state = editor.getState();

        if (state.getFilter() != FilterType.EDGEDETECTION) {
            warningShown = false;
            return;
        }

        int contrast = state.getAdjustmentState().getOrDefault(AdjustmentType.CONTRAST, 0);
        int sharpness = state.getAdjustmentState().getOrDefault(AdjustmentType.SHARPNESS, 0);

        if (contrast > 25 || sharpness > 20) {
            if (!warningShown) {

                if (isGui) {

                    JOptionPane.showMessageDialog(null,
                            "Warning: Increasing sharpness/contrast could generate false-positive edges!",
                            "Annoying pop-up by: algorithm observer", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    System.out.println("[Warning]: Increasing sharpness/contrast could generate false-positive edges!");
                }

                warningShown = true;
            }
        } else {
            warningShown = false;
        }
    }
}