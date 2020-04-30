package biz.k11i.xgboost.tree;

import ai.h2o.algos.tree.INodeStat;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Statistics each node in tree.
 */
public class RegTreeNodeStat implements INodeStat, Serializable {
    /** loss chg caused by current split */
    public final float loss_chg;
    /** sum of hessian values, used to measure coverage of data */
    public final float sum_hess;
    /** weight of current node */
    public final float base_weight;
    /** number of child that is leaf node known up to now */
    public final int leaf_child_cnt;

    RegTreeNodeStat(ModelReader reader) throws IOException {
        loss_chg = reader.readFloat();
        sum_hess = reader.readFloat();
        base_weight = reader.readFloat();
        leaf_child_cnt = reader.readInt();
    }

    @Override
    public float getWeight() {
        return sum_hess;
    }
}
