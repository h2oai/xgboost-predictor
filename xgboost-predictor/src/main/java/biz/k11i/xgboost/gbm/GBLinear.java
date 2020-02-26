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

    private ModelParam mparam;
    private float[] weights;

    GBLinear() {
        // do nothing
    }

    @Override
    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean ignored_with_pbuffer) throws IOException {
        mparam = new ModelParam(reader, num_class);
        reader.readInt(); // read padding
        weights = reader.readFloatArray((num_feature + 1) * mparam.num_output_group);
    }

    @Override
    public float[] predict(FVec feat, int ntree_limit) {
        float[] preds = new float[mparam.num_output_group];
        for (int gid = 0; gid < mparam.num_output_group; ++gid) {
            preds[gid] = pred(feat, gid);
        }
        return preds;
    }

    @Override
    public float predictSingle(FVec feat, int ntree_limit) {
        if (mparam.num_output_group != 1) {
            throw new IllegalStateException(
                    "Can't invoke predictSingle() because this model outputs multiple values: "
                            + mparam.num_output_group);
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
        return weights[(fid * mparam.num_output_group) + gid];
    }

    public float bias(int gid) {
        return weights[(num_feature * mparam.num_output_group) + gid];
    }

    static class ModelParam implements Serializable {
        /*!
         * \brief how many output group a single instance can produce
         *  this affects the behavior of number of output we have:
         *    suppose we have n instance and k group, output will be k*n
         */
        final int num_output_group;
        /*! \brief reserved parameters */
        final int[] reserved;

        ModelParam(ModelReader reader, int num_class) throws IOException {
            reader.readUnsignedInt(); // num_feature deprecated
            reader.readInt(); // num_output_group deprecated
            num_output_group = (num_class == 0) ? 1 : num_class;
            reserved = reader.readIntArray(32);
            reader.readInt(); // read padding
        }
    }

    public int getNumFeature() {
        return num_feature;
    }

    public int getNumOutputGroup() {
        return mparam.num_output_group;
    }

}
