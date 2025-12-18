package ImageEditor.Filters.CustomFilters;

import ImageEditor.ImageState;

public class CustomFilterManager {
    //TODO tworzy nowe customFiltry, przechowuje/zapisuje/wczytuje już utworzone
    private CustomFilter[] filters;
    private int index = 0;

    public void createCustomFilter(ImageState state){
        filters[index] = new CustomFilter(state);
        index++;
    }

}


