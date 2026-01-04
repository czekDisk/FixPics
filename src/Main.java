import ImageEditor.ImageEditor;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;

void main() throws IOException {
    long startTime = System.currentTimeMillis();

    ImageEditor editor = new ImageEditor("pobierz.jpg");
    editor.applyAdjustment(AdjustmentType.TEMPERATURE, 100);
    editor.export("jpg", "export.jpg");

    long endTime = System.currentTimeMillis();

    System.out.println((endTime - startTime)/1000f + "s");
}


