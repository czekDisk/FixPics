import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;

void main() throws IOException {
    long startTime = System.currentTimeMillis();

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

    editor.applyFilter(FilterPresets.GRAYSCALE);
    editor.export("jpg", "export1.jpg");

    editor.applyFilter(FilterPresets.INVERT);
    editor.export("jpg", "export2.jpg");

    editor.applyFilter(FilterPresets.PIXELBLUR);
    editor.export("jpg", "export3.jpg");

    editor.applyFilter(FilterPresets.SEPIA);
    editor.export("jpg", "export4.jpg");

    long endTime = System.currentTimeMillis();

    System.out.println((endTime - startTime)/1000f + "s");
}


