package ImageEditor.Core;

import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

public class PresetDetectorObserver implements Observer {
    private final ImageEditor editor;
    private final JComboBox<String> presetBox;

    public PresetDetectorObserver(ImageEditor editor, JComboBox<String> presetBox) {
        this.editor = editor;
        this.presetBox = presetBox;
    }

    @Override
    public void update() {
        ImageState currentState = editor.getState();

        for (String presetName : editor.getPresetNames()) {
            ImageState presetState = editor.getPresetState(presetName);

            if (presetState != null && areStatesEqual(currentState, presetState)) {
                if (!Objects.equals(presetBox.getSelectedItem(), presetName)) {
                    presetBox.setSelectedItem(presetName);
                }
                break;
            }
        }
    }

    private boolean areStatesEqual(ImageState s1, ImageState s2) {
        if (s1.getFilter() != s2.getFilter()) return false;

        Map<AdjustmentType, Integer> map1 = s1.getAdjustmentState();
        Map<AdjustmentType, Integer> map2 = s2.getAdjustmentState();

        for (AdjustmentType type : AdjustmentType.values()) {
            if (!Objects.equals(map1.getOrDefault(type, 0), map2.getOrDefault(type, 0))) {
                return false;
            }
        }
        return true;
    }
}