package biz.k11i.xgboost.tree.xgb;

import ai.h2o.algos.tree.INode;
import ai.h2o.algos.tree.INodeStat;
import biz.k11i.xgboost.tree.xgb.gen.Tree;
import biz.k11i.xgboost.util.FVec;

import java.io.Serializable;

public class XgbTreeImpl implements XgbTree{

    private XgbTreeParam param;
    private Tree xgbTree;
    private Node[] nodes;


    public XgbTreeImpl(Tree xgbTree) {
        this.xgbTree = xgbTree;
        this.nodes = getNodesViewFromArray();
    }

    public Node[] getNodesViewFromArray() {
        param = new XgbTreeParam();
        nodes = new Node[param.num_nodes];
        for (int i = 0; i < param.num_nodes; i++) {
            nodes[i] = new Node(i);
        }
        return nodes;
    }

    public int getLeafIndex(FVec feat) {
        int index = 0;
        Node n;
        while (!(n = nodes[index]).isLeaf()) {
            index = n.next(feat);
        }
        return index;
    }

    public void getLeafPath(FVec feat, StringBuilder sb) {
        int index = 0;
        Node n;
        while (!(n = nodes[index]).isLeaf()) {
            index = n.next(feat);
            sb.append(index == n.getLeftChildIndex() ? "L" : "R");
        }
    }

    public float getLeafValue(FVec feat, int root_id) {
        Node n = nodes[root_id];
        while (!n.isLeaf()) {
            n = nodes[n.next(feat)];
        }
        return n.getLeafValue();
    }

    public Node[] getNodes() {
        return nodes;
    }

    class XgbTreeParam implements Serializable {
        final int num_deleted;
        final int num_feature;
        final int num_nodes;
        final int size_leaf_vector;

        XgbTreeParam() {
            num_deleted = convertToInt(xgbTree.getTreeParam().getNumDeleted());
            num_feature = convertToInt(xgbTree.getTreeParam().getNumFeature());
            num_nodes = convertToInt(xgbTree.getTreeParam().getNumNodes());
            size_leaf_vector = convertToInt(xgbTree.getTreeParam().getSizeLeafVector());
        }
    }

    public class Node implements INode<FVec>, INodeStat, Serializable {

        final float baseWeight;
        final boolean defaultLeft;
        final int leftChild;
        final float lossChange;
        final int parent;
        final int rightChild;
        final float splitCondition;
        final int splitIndex;
        final int splitType;
        final float sumHessian;

        Node(int nodeIndex) {
            baseWeight = (float) xgbTree.getBaseWeights().get(nodeIndex).doubleValue();
            defaultLeft = xgbTree.getDefaultLeft().get(nodeIndex);
            leftChild = xgbTree.getLeftChildren().get(nodeIndex);
            lossChange = (float) xgbTree.getLossChanges().get(nodeIndex).doubleValue();
            parent = xgbTree.getParents().get(nodeIndex);
            rightChild = xgbTree.getRightChildren().get(nodeIndex);
            splitCondition = (float) xgbTree.getSplitConditions().get(nodeIndex).doubleValue();
            splitIndex = xgbTree.getSplitIndices().get(nodeIndex);
            splitType = xgbTree.getSplitType().get(nodeIndex);
            sumHessian = (float) xgbTree.getSumHessian().get(nodeIndex).doubleValue();
        }

        @Override
        public boolean isLeaf() {
            return leftChild == -1;
        }

        @Override
        public float getLeafValue() {
            return isLeaf() ? splitCondition : Float.NaN;
        }

        @Override
        public int getSplitIndex() {
            return splitIndex;
        }

        public boolean isDefaultLeft() {
            return defaultLeft;
        }

        @Override
        public int next(FVec feat) {
            float value = feat.fvalue(getSplitIndex());
            return (value < splitCondition) ? leftChild : rightChild;
        }

        public int getParentIndex() {
            return parent;
        }

        @Override
        public int getLeftChildIndex() {
            return leftChild;
        }

        @Override
        public int getRightChildIndex() {
            return rightChild;
        }

        public int getDefaultChildIndex() {
            return defaultLeft ? leftChild : rightChild;
        }

        @Override
        public float getWeight() {
            return getCover();
        }

        public float getGain() {
            return lossChange;
        }

        public float getCover() {
            return sumHessian;
        }

        public float getBaseWeight() {
            return baseWeight;
        }

    }

    private static float convertToFloat(String inputStr) {
        return Float.parseFloat(inputStr);
    }

    private static int convertToInt(String inputStr) {
        return Integer.parseInt(inputStr);
    }

}