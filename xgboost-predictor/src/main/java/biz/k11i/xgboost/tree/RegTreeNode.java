package biz.k11i.xgboost.tree;

import ai.h2o.algos.tree.INode;
import biz.k11i.xgboost.util.FVec;

import java.io.Serializable;

public abstract class RegTreeNode implements INode<FVec>, Serializable {

    /**
     *
     * @return Index of node's parent
     */
    public abstract int getParentIndex();

    /**
     *
     * @return Index of node's left child node
     */
    public abstract int getLeftChildIndex();

    /**
     *
     * @return Index of node's right child node
     */
    public abstract int getRightChildIndex();

    /**
     *
     * @return Split condition  on the node, if the node is a split node. Leaf nodes have this value set to NaN
     */
    public abstract float getSplitCondition();

    /**
     *
     * @return Predicted value on the leaf node, if the node is leaf. Otherwise NaN
     */
    public abstract float getLeafValue();

    /**
     *
     * @return True if default direction for unrecognized values is the LEFT child, otherwise false.
     */
    public abstract boolean default_left();

    /**
     *
     * @return Index of domain category used to split on the node
     */
    public abstract int getSplitIndex();

}
