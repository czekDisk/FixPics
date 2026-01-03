import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;

void main() throws IOException {
    ImageEditor editor = new ImageEditor("pobierz.jpg");

    editor.applyFilter(FilterPresets.GRAYSCALE);
    editor.export("jpg", "output1.jpg");

    editor.undo();
    editor.export("jpg", "output2.jpg");

    editor.redo();
    editor.export("jpg", "output3.jpg");

    editor.applyAdjustment(AdjustmentType.SHARPNESS, 90);
    editor.export("jpg", "output4.jpg");

    editor.changeCustomFilter(CustomFilterIndex.CUSTOM_FILTER_1);
    editor.changeCustomFilter(CustomFilterIndex.CUSTOM_FILTER_2);

    ImageEditor editor2 = new ImageEditor("pobierz.jpg");

    editor2.applyFilter(CustomFilterIndex.CUSTOM_FILTER_1);
    editor2.export("jpg", "output5.jpg");

    editor2.applyFilter(FilterPresets.INVERT);
    editor2.export("jpg", "output6.jpg");

    editor2.applyFilter(FilterPresets.PIXELBLUR);
    editor2.export("jpg", "output7.jpg");

    editor2.applyFilter(FilterPresets.SEPIA);
    editor2.export("jpg", "output8.jpg");
}


