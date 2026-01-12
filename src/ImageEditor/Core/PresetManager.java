package ImageEditor.Core;

import Exceptions.PresetAlreadyExistsException;
import ImageEditor.ImageState;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class PresetManager {
    private HashMap<String, ImageState> presets;


    public PresetManager(){
        ObjectMapper mapper = new ObjectMapper();

        File f = new File("Custom.json");
        if(!f.exists() || f.length() == 0){
            presets = new HashMap<>();
        }
        else {
            try {
                presets = mapper.readValue(f,
                        new TypeReference<HashMap<String, ImageState>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
                presets = new HashMap<>();
            }
        }
    }


    public ImageState getPreset(String presetName){
        return presets.get(presetName);
    }

    public void removePreset(String presetName){
        presets.remove(presetName);
        serialize();
    }

    public void createPreset(String presetName, ImageState state) throws PresetAlreadyExistsException {
        if (presets.containsKey(presetName))
            throw new PresetAlreadyExistsException("there's already a preset with that name");

        presets.put(presetName,new ImageState(state));
        serialize();
    }

    public Set<String> getPresetNames(){
        return presets.keySet();
    }

    private void serialize(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("Custom.json"), presets);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }



}







