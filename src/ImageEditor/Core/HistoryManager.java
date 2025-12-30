package ImageEditor.Core;

import ImageEditor.ImageState;
import ImageEditor.OperationTags.OperationType;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;



class HistoryNode{
    OperationType type;
    int oldValue;
    int newValue;

    public HistoryNode(OperationType type, int oldValue, int newValue){
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}

public class HistoryManager {
    private Stack<HistoryNode> undo;
    private Stack<HistoryNode> redo;


    public HistoryManager(){
        redo = new Stack<>();
        undo = new Stack<>();
    }

    public void update(OperationType type, int oldValue, int newValue){
        if (!redo.empty()) redo.clear();
        undo.push(new HistoryNode(type, oldValue, newValue));
    }

    public boolean isUndoable(){
        return !undo.empty();
    }

    public boolean isRedoable(){
        return !redo.empty();
    }

    public void undo(){

    }



    public void redo(){

    }

}
