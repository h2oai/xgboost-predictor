package biz.k11i.xgboost.gbm;

import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.tree.RegTree;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.util.Arrays;

/**
 * Gradient boosted DART tree implementation.
 */
public class Dart extends GBTree {
    private float[] weightDrop;

    Dart() {
        // do nothing
    }

    @Override
    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean with_pbuffer) throws IOException {
        super.loadModel(config, reader, with_pbuffer);
        if (mparam.num_trees != 0) {
            long size = reader.readLong();
            weightDrop = reader.readFloatArray((int)size);
        }
    }

    @Override
    float pred(FVec feat, int bst_group, int root_index, int ntree_limit, float base_score) {
        RegTree[] trees = _groupTrees[bst_group];
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;

        float psum = base_score;
        for (int i = 0; i < treeleft; i++) {
            psum += weightDrop[i] * trees[i].getLeafValue(feat, root_index);
        }

        return psum;
    }

    public float weight(int tidx) {
        return weightDrop[tidx];
    }

}
