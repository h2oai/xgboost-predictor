package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.util.FVec;

import java.io.Serializable;

/**
 * Regression tree.
 */
public interface RegTree extends Serializable {

    /**
     * Retrieves nodes from root to leaf and returns leaf index.
     *
     * @param feat    feature vector
     * @return leaf index
     */
    int getLeafIndex(FVec feat);

    /**
     * Retrieves nodes from root to leaf and returns path to leaf.
     *
     * @param feat    feature vector
     * @param sb      output param, will write path path to leaf into this buffer
     */
    void getLeafPath(FVec feat, StringBuilder sb);

    /**
     * Retrieves nodes from root to leaf and returns leaf value.
     *
     * @param feat    feature vector
     * @param root_id starting root index
     * @return leaf value
     */
    float getLeafValue(FVec feat, int root_id);

    /**
     *
     * @return Tree's nodes
     */
    RegTreeNode[] getNodes();

    /**
     * @return Tree's nodes stats
     */
    RegTreeNodeStat[] getStats();

}
