package ImageEditor.Core;

import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;

import javax.swing.*;
import java.util.EnumMap;

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
                if (presetBox.getSelectedItem() != presetName) {
                    presetBox.setSelectedItem(presetName);
                }
                break;
            }
        }
    }

    private boolean areStatesEqual(ImageState s1, ImageState s2) {
        if (s1.getFilter() != s2.getFilter()) return false;

        EnumMap<AdjustmentType, Integer> map1 = s1.getAdjustmentState();
        EnumMap<AdjustmentType, Integer> map2 = s2.getAdjustmentState();

        for (AdjustmentType type : AdjustmentType.values()) {
            int val1=0, val2=0;

            if (map1.get(type) != null) {
                val1 = map1.get(type);
            }

            if (map2.get(type) != null) {
                val2 = map2.get(type);
            }

            if (val1 != val2) {
                return false;
            }
        }
        return true;
    }
}