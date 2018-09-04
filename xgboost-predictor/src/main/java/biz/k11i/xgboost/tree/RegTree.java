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
     * @param root_id starting root index
     * @return leaf index
     */
    int getLeafIndex(FVec feat, int root_id);

    /**
     * Retrieves nodes from root to leaf and returns leaf value.
     *
     * @param feat    feature vector
     * @param root_id starting root index
     * @return leaf value
     */
    float getLeafValue(FVec feat, int root_id);

}
