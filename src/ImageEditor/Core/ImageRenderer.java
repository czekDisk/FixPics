package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Filters.CustomFilters.CustomFilter;
import ImageEditor.Filters.CustomFilters.CustomFilterManager;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;
import ImageEditor.ImageState;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class ImageRenderer {
    private BufferedImage imageOriginal;

    private final EnumMap<FilterPresets, FilterStrategy> filterStrategies;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;
    private final EnumMap<CustomFilterIndex, CustomFilter> customFilters;

    public ImageRenderer(BufferedImage imageOriginal, CustomFilterManager manager){
        this. customFilters = manager.getCustomFilters();
        this.imageOriginal = imageOriginal;

        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for(AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        filterStrategies = new EnumMap<>(FilterPresets.class);
        for(FilterPresets type : FilterPresets.values())
            filterStrategies.put(type, Utils.filter_factory(type));
    }

    public BufferedImage renderPreview(ImageState imageState, int scale){
        BufferedImage copiedImage = Utils.scaleImage(Utils.deepCopyImage(imageOriginal), scale);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        if (imageState.getFilter() != FilterPresets.NOFILTER) {
            filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);
        }

        return copiedImage;
    }

    public BufferedImage render( ImageState imageState) {
        BufferedImage copiedImage = Utils.deepCopyImage(imageOriginal);  // nie zmieniamy oryginalu

        for (AdjustmentType type : AdjustmentType.values()) {
            adjustmentStrategies.get(type).applyAdjustment(copiedImage, imageState.getAdjustmentState().get(type));
        }

        if (imageState.getFilter().getClass() == FilterPresets.class){
            if (imageState.getFilter() != FilterPresets.NOFILTER)
                filterStrategies.get(imageState.getFilter()).applyFilter(copiedImage);
        }
        else{
            customFilters.get(imageState.getFilter()).applyFilter(copiedImage);
        }


        return copiedImage;
    }
}
