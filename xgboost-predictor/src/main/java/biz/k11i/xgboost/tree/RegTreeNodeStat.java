package biz.k11i.xgboost.tree;

import ai.h2o.algos.tree.INodeStat;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Statistics for node in tree.
 */
public class RegTreeNodeStat implements INodeStat, Serializable {

    final float loss_chg;
    final float sum_hess;
    final float base_weight;
    final int leaf_child_cnt;

    RegTreeNodeStat(ModelReader reader) throws IOException {
        loss_chg = reader.readFloat();
        sum_hess = reader.readFloat();
        base_weight = reader.readFloat();
        leaf_child_cnt = reader.readInt();
    }

    @Override
    public float getWeight() {
        return getCover();
    }

    /**
     * @return loss chg caused by current split
     */
    public float getGain() {
        return loss_chg;
    }

    /**
     * @return sum of hessian values, used to measure coverage of data
     */
    public float getCover() {
        return sum_hess;
    }

    /**
     * @return weight of current node
     */
    public float getBaseWeight() {
        return base_weight;
    }

    /**
     * @return number of child that is leaf node known up to now
     */
    public int getLeafCount() {
        return leaf_child_cnt;
    }

}
