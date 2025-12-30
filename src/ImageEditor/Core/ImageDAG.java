package ImageEditor.Core;


import ImageEditor.OperationTags.OperationType;

public class ImageDAG {
    private DAGNode root;
    private DAGNode last;
    private DAGNode current;


    public ImageDAG(){
        this.root = null;
        this.last = null;
        this.current = null;
    }

    public DAGNode gethead() {
        return root;
    }
    public DAGNode getlast() {
        return last;
    }
    public DAGNode getCurr() {
        return current;
    }

    public boolean isCurrent(){
        if (current == last) return true;
        return false;
    }

    public void updateCurrent(){
        current = last;
    }

    public void removeNode(DAGNode node){
        if (node == root){
            if (current == node) current = null;
            root = root.child;
        }
        else{
            if (node == last){
                if (current == node) current = current.parent;
                last = node.parent;
            }
            else{
                if (current == node) current = current.parent;
                node.parent.child = node.child;
            }
        }
    }

    public boolean addOperation(OperationType operation){
        if (cleanDirty(operation) == true){
            last.children.add(new DAGNode(operation, last));
            last = last.children.getFirst();
            return true;
        }
        else{
            last.children.add(new DAGNode(operation, last));
            last = last.child;
            current = null;
            return false;
        }


    }




    private boolean cleanDirty(OperationType operation){
        if (root == null) return false;

        boolean dirtyFlag = false;
        DAGNode node = root;
        while(node != null){
            if (node.operation == operation){
                dirtyFlag = true;
                node = node.children.getFirst();
                removeNode(node.parent);
            }
            else{
                node = node.children.getFirst();
            }
        }
        return dirtyFlag;
    }




}
