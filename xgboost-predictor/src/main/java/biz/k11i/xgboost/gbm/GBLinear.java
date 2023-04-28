package biz.k11i.xgboost.gbm;

import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Linear booster implementation
 */
public class GBLinear extends GBBase {

    private float[] weights;

    @Override
    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean ignored_with_pbuffer) throws IOException {
        new ModelParam(reader);
        long len = reader.readLong();
        if (len == 0) {
            weights = new float[(num_feature + 1) * num_output_group];
        } else {
            weights = reader.readFloatArray((int) len);
        }
    }

    @Override
    public float[] predict(FVec feat, int ntree_limit, float base_score) {
        float[] preds = new float[num_output_group];
        for (int gid = 0; gid < num_output_group; ++gid) {
            preds[gid] = pred(feat, gid);
        }
        return preds;
    }

    @Override
    public float predictSingle(FVec feat, int ntree_limit, float base_score) {
        if (num_output_group != 1) {
            throw new IllegalStateException(
                    "Can't invoke predictSingle() because this model outputs multiple values: "
                            + num_output_group);
        }
        return pred(feat, 0);
    }

    float pred(FVec feat, int gid) {
        float psum = bias(gid);
        float featValue;
        for (int fid = 0; fid < num_feature; ++fid) {
            featValue = feat.fvalue(fid);
            if (!Float.isNaN(featValue)) {
                psum += featValue * weight(fid, gid);
            }
        }
        return psum;
    }

    @Override
    public int[] predictLeaf(FVec feat, int ntree_limit) {
        throw new UnsupportedOperationException("gblinear does not support predict leaf index");
    }

    @Override
    public String[] predictLeafPath(FVec feat, int ntree_limit) {
        throw new UnsupportedOperationException("gblinear does not support predict leaf path");
    }

    public float weight(int fid, int gid) {
        return weights[(fid * num_output_group) + gid];
    }

    public float bias(int gid) {
        return weights[(num_feature * num_output_group) + gid];
    }

    static class ModelParam implements Serializable {
        /*! \brief reserved space */
        final int[] reserved;

        ModelParam(ModelReader reader) throws IOException {
            reader.readUnsignedInt(); // num_feature deprecated
            reader.readInt(); // num_output_group deprecated
            reserved = reader.readIntArray(32);
        }
    }

    public int getNumFeature() {
        return num_feature;
    }

    public int getNumOutputGroup() {
        return num_output_group;
    }

}
