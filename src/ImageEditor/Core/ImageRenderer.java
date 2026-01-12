package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.OperationTags.FilterType;
import ImageEditor.ImageState;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class ImageRenderer {
    private BufferedImage imageOriginal;

    private final EnumMap<FilterType, FilterStrategy> filterStrategies;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;

    public ImageRenderer(BufferedImage imageOriginal){
        this.imageOriginal = imageOriginal;

        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for(AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        filterStrategies = new EnumMap<>(FilterType.class);
        for(FilterType type : FilterType.values())
            filterStrategies.put(type, Utils.filter_factory(type));
    }

    public BufferedImage renderPreview(ImageState imageState, int scale){
        BufferedImage copiedImage = Utils.scaleImage(Utils.deepCopyImage(imageOriginal), scale);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        if (imageState.getFilter() != FilterType.NOFILTER) {
            filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);
        }

        return copiedImage;
    }

    public BufferedImage render( ImageState imageState) {
        BufferedImage copiedImage = Utils.deepCopyImage(imageOriginal);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        if (imageState.getFilter() != FilterType.NOFILTER) {
            filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);
        }


        return copiedImage;
    }
}
