package biz.k11i.xgboost.tree.xgb;

import biz.k11i.xgboost.util.FVec;

import java.io.Serializable;

public interface XgbTree extends Serializable {

    int getLeafIndex(FVec feat);

    void getLeafPath(FVec feat, StringBuilder sb);

    float getLeafValue(FVec feat, int root_id);

    XgbTreeImpl.Node[] getNodes();

}
