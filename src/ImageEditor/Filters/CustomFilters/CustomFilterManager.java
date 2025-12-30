package ImageEditor.Filters.CustomFilters;

import ImageEditor.ImageState;
import ImageEditor.OperationTags.CustomFilterIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

public class CustomFilterManager {
    private EnumMap<CustomFilterIndex, CustomFilter> customFilters;


    public CustomFilterManager(){
        ObjectMapper mapper = new ObjectMapper();

        File f = new File("Custom.json");
        if(!f.exists() || f.length() == 0){
            customFilters = new EnumMap<>(CustomFilterIndex.class);
        }
        else {
            try {
                customFilters = mapper.readValue(f,
                        new TypeReference<EnumMap<CustomFilterIndex, CustomFilter>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
                customFilters = new EnumMap<>(CustomFilterIndex.class);
            }
        }
    }

    public EnumMap<CustomFilterIndex, CustomFilter> getCustomFilters() {
        return customFilters;
    }

    public void changeFilter(ImageState state, CustomFilterIndex index){
        customFilters.put(index, new CustomFilter(state));
        serialize();
    }



    private void serialize(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("Custom.json"), customFilters);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }





}



