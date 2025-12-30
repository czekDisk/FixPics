package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.OperationTags.FilterPresets;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.OperationType;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class ImageRenderer {
    private BufferedImage imageOriginal;
    private BufferedImage imagePreview;

    private ImageDAG imageDAG;


    private final EnumMap<FilterPresets, FilterStrategy> filterStrategies;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;


    public ImageRenderer(BufferedImage imageOriginal){
        this.imageOriginal = imageOriginal;
        imagePreview = Utils.scaleDown(imageOriginal);
        previewCache = Utils.deepCopyImage(imagePreview);

        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for(AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        filterStrategies = new EnumMap<>(FilterPresets.class);
        for(FilterPresets type : FilterPresets.values())
            filterStrategies.put(type, Utils.filter_factory(type));
    }



    public void update(OperationType operationType){

    }











    public BufferedImage renderPreview(ImageState imageState){
        BufferedImage copiedImage = Utils.deepCopyImage(imagePreview);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);

        return copiedImage;
    }

    public BufferedImage render( ImageState imageState) {
        BufferedImage copiedImage = Utils.deepCopyImage(imageOriginal);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);

        return copiedImage;
    }
}
