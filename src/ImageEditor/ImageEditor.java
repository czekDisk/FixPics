package ImageEditor;

import Exceptions.NonExistantPresetException;
import Exceptions.PresetAlreadyExistsException;
import ImageEditor.Core.PresetManager;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Core.ImageRenderer;
import ImageEditor.OperationTags.FilterType;
import ImageEditor.Core.Observer;
import ImageEditor.Core.Subject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
public class ImageEditor implements Subject {
    private final BufferedImage originalImage;
    private ImageState state;
    private HistoryManager history;
    private IOManager io;
    private ImageRenderer imageRenderer;
    private PresetManager presetManager;
    private final List<Observer> observers = new ArrayList<>();

    public ImageEditor(String filepath) throws IOException {
        this.io = new IOManager(filepath);

        this.originalImage = io.getImage();
        this.state = new  ImageState();
        this.history = new HistoryManager(state);
        // register history manager as observer so it receives automatic updates
        registerObserver(history);
        this.presetManager = new PresetManager();
        this.imageRenderer = new ImageRenderer(originalImage);

    }

    public void applyAdjustment(AdjustmentType type, int value){
        state.getAdjustmentState().put(type, value);
        notifyObservers();
    }

    public void applyFilter(FilterType type){
            state.setFilter(type);
            notifyObservers();
    }

    public void undo(){
        history.undo();
    }

    public boolean isUndoable(){
        return history.isUndoable();
    }

    public void redo(){
        history.redo();
    }

    public boolean isRedoable(){
        return history.isRedoable();
    }

    public void exportPreview(int scale) throws IOException {
        io.exportImage(imageRenderer.renderPreview(state, scale));
    }
    public void exportPreview(String fileFormat, String filename, int scale)throws IOException{
        io.exportImage(imageRenderer.renderPreview(state, scale), fileFormat, filename);
    }

    public void export() throws IOException{
        io.exportImage(imageRenderer.render(state));
    }
    public void export(String fileFormat, String filename) throws IOException{
        io.exportImage(imageRenderer.render(state), fileFormat, filename);
    }

    
    public BufferedImage getPreview(int scale){
        return imageRenderer.renderPreview(state, scale);
    }

    // Return a copy of the current state so callers can inspect it without mutating internal state
    public ImageState getState(){
        return new ImageState(state);
    }





    public void applyPreset(String presetName) throws NonExistantPresetException {
        if (!presetManager.containsPreset(presetName)){
            throw new NonExistantPresetException("there's no such preset");
        }
        ImageState preset = presetManager.getPreset(presetName);
        if (preset == null){
            throw new NonExistantPresetException("there's no such preset");
        }

        state.copyFrom(preset);
        notifyObservers();
    }

    public void savePreset(String presetName) throws PresetAlreadyExistsException {
        presetManager.createPreset(presetName, state);
    }

    public void removePreset(String presetName){
        presetManager.removePreset(presetName);
    }

    public Set<String> getPresetNames(){
        return presetManager.getPresetNames();
    }

    @Override
    public void registerObserver(Observer o) {
        if (o == null) return;
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : new ArrayList<>(observers)) {
            o.update();
        }
    }


}



