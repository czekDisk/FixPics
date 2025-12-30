package ImageEditor.Core;

import ImageEditor.OperationTags.OperationType;

public class DAGNode{
    OperationType operation;
    DAGNode parent;
    DAGNode child;

    public DAGNode(OperationType operation, DAGNode parent){
        this.operation = operation;
        this.parent = parent;
        this.child = null;
    }
}
