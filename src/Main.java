import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;

void main() throws IOException {
    ImageEditor editor = new ImageEditor("pobierz.jpg");

    editor.applyFilter(FilterPresets.GRAYSCALE);


    editor.undo();


    editor.redo();

    editor.applyAdjustment(AdjustmentType.SHARPNESS, 90);


    editor.changeCustomFilter(CustomFilterIndex.CUSTOM_FILTER_1);
    editor.changeCustomFilter(CustomFilterIndex.CUSTOM_FILTER_2);

    ImageEditor editor2 = new ImageEditor("pobierz.jpg");

    editor2.applyFilter(CustomFilterIndex.CUSTOM_FILTER_1);


    editor.exportPreview(80);
}


