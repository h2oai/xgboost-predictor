package biz.k11i.xgboost.tree;

import ai.h2o.algos.tree.INodeStat;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Statistics each node in tree.
 */
class RegTreeNodeStat implements INodeStat, Serializable {
    /*! \brief loss chg caused by current split */
    final float loss_chg;
    /*! \brief sum of hessian values, used to measure coverage of data */
    final float sum_hess;
    /*! \brief weight of current node */
    final float base_weight;
    /*! \brief number of child that is leaf node known up to now */
    final int leaf_child_cnt;

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
