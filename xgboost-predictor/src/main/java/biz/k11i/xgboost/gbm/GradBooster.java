package biz.k11i.xgboost.gbm;

import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Interface of gradient boosting model.
 */
public interface GradBooster extends Serializable {

    class Factory {
        /**
         * Creates a gradient booster from given name.
         *
         * @param name name of gradient booster
         * @return created gradient booster
         */
        public static GradBooster createGradBooster(String name) {
            if ("gbtree".equals(name)) {
                return new GBTree();
            } else if ("gblinear".equals(name)) {
                return new GBLinear();
            } else if ("dart".equals(name)) {
                return new Dart();
            }

            throw new IllegalArgumentException(name + " is not supported model.");
        }
    }

    void setNumClass(int numClass);
    void setNumFeature(int numFeature);

    /**
     * Loads model from stream.
     *
     * @param config       predictor configuration
     * @param reader       input stream
     * @param with_pbuffer whether the incoming data contains pbuffer
     * @throws IOException If an I/O error occurs
     */
    void loadModel(PredictorConfiguration config, ModelReader reader, boolean with_pbuffer) throws IOException;

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat        feature vector
     * @param ntree_limit limit the number of trees used in prediction
     * @param base_score base score to initialize prediction
     * @return prediction result
     */
    float[] predict(FVec feat, int ntree_limit, float base_score);

    /**
     * Generates a prediction for given feature vector.
     * <p>
     * This method only works when the model outputs single value.
     * </p>
     *
     * @param feat        feature vector
     * @param ntree_limit limit the number of trees used in prediction
     * @param base_score base score to initialize prediction
     * @return prediction result
     */
    float predictSingle(FVec feat, int ntree_limit, float base_score);

    /**
     * Predicts the leaf index of each tree. This is only valid in gbtree predictor.
     *
     * @param feat        feature vector
     * @param ntree_limit limit the number of trees used in prediction
     * @return predicted leaf indexes
     */
    int[] predictLeaf(FVec feat, int ntree_limit);

    /**
     * Predicts the path to leaf of each tree. This is only valid in gbtree predictor.
     *
     * @param feat        feature vector
     * @param ntree_limit limit the number of trees used in prediction
     * @return predicted path to leaves
     */
    String[] predictLeafPath(FVec feat, int ntree_limit);

}

abstract class GBBase implements GradBooster {
    protected int num_class;
    protected int num_feature;
    protected int num_output_group;

    @Override
    public void setNumClass(int numClass) {
        this.num_class = numClass;
        this.num_output_group = (num_class == 0) ? 1 : num_class;
    }

    @Override
    public void setNumFeature(int numFeature) {
        this.num_feature = numFeature;
    }
}