package ImageEditor;

import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Filters.FilterType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class ImageState {
    private EnumMap<AdjustmentType, Integer> adjustmentState;
    private FilterType filter;

    public ImageState(){
        adjustmentState = new EnumMap<>(AdjustmentType.class);
        for (AdjustmentType type : AdjustmentType.values())
            adjustmentState.put(type, 0);

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

    //to expand in the future
    //Temporary solutions often become permanent problems.
    //
    //Craig Bruce
}
