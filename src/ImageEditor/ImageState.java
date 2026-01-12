package ImageEditor;

import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterType;

import java.util.EnumMap;

public class ImageState {
    private EnumMap<AdjustmentType, Integer> adjustmentState;
    private FilterType filter;

    public ImageState(){
        adjustmentState = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentState.put(type, 0);

        filter = FilterType.NOFILTER;
    }

    public ImageState(ImageState other){
        this.adjustmentState = new EnumMap<>(other.adjustmentState);
        this.filter = other.filter;
    }

    public void copyFrom(ImageState other){
        this.adjustmentState = new EnumMap<>(other.adjustmentState);
        this.filter = other.filter;
    }

    public void clear(){
        for (AdjustmentType type : AdjustmentType.values()){
            adjustmentState.put(type, 0);
        }

        filter = FilterType.NOFILTER;
    }

    public EnumMap<AdjustmentType, Integer> getAdjustmentState() {
        return adjustmentState;
    }
    public FilterType getFilter() {
        return filter;
    }

    public void setFilter(FilterType filter) {
        this.filter = filter;
    }
    public void setAdjustmentState(EnumMap<AdjustmentType, Integer> adjustmentState) {
        this.adjustmentState = adjustmentState;
    }

}


