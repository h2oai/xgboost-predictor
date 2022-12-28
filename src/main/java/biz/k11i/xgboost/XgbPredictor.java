package biz.k11i.xgboost;

import biz.k11i.xgboost.config.XgbPredictorConfiguration;
import biz.k11i.xgboost.gbm.GradBooster;
import biz.k11i.xgboost.learner.ObjFunction;
import biz.k11i.xgboost.tree.xgb.gen.XgbMainPojo;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader.convertToFloat;
import static biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader.convertToInt;
/**
 * Predicts using the Xgboost model.
 */
public class XgbPredictor implements Serializable {
    private ModelParam mparam;
    private String name_obj;
    private String name_gbm;
    private float base_score;
    private ObjFunction obj;
    private GradBooster gbm;
    private XgbMainPojo xgbMainPojo;



    public XgbPredictor(String modelFilePath) throws IOException {
        this(modelFilePath, null);
    }

   public XgbPredictor(String modelFilePath, XgbPredictorConfiguration configuration) throws IOException {
        if (configuration == null) {
            configuration = XgbPredictorConfiguration.DEFAULT;
        }
        this.xgbMainPojo = loadJsonToPojos(modelFilePath);
        readParam(xgbMainPojo);
        initObjFunction(configuration);
        initObjGbm();

        gbm.loadModel(xgbMainPojo);

        if (mparam.major_version >= 1) {
            base_score = obj.probToMargin(mparam.base_score);
        } else {
            base_score = mparam.base_score;
        }
    }

    void readParam(XgbMainPojo xgbMainPojo) throws IOException {
        float base_score = convertToFloat(xgbMainPojo.getLearner().getLearnerModelParam().getBaseScore());
        int num_feature = convertToInt(xgbMainPojo.getLearner().getLearnerModelParam().getNumFeature());
        mparam = new ModelParam(base_score, num_feature, xgbMainPojo);
        name_obj = xgbMainPojo.getLearner().getObjective().getName();
        name_gbm = xgbMainPojo.getLearner().getGradientBooster().getName();
    }

    void initObjFunction(XgbPredictorConfiguration configuration) {
        obj = configuration.getObjFunction();
        if (obj == null) {
            obj = ObjFunction.fromName(name_obj);
        }
    }

    void initObjGbm() {
        obj = ObjFunction.fromName(name_obj);
        gbm = GradBooster.Factory.createGradBooster(name_gbm, mparam.major_version);
        gbm.setNumClass(mparam.num_class);
        gbm.setNumFeature(mparam.num_feature);
    }

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat feature vector
     * @return prediction values
     */
    public float[] predict(FVec feat) {
        return predict(feat, false);
    }

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat          feature vector
     * @param output_margin whether to only predict margin value instead of transformed prediction
     * @return prediction values
     */
    public float[] predict(FVec feat, boolean output_margin) {
        return predict(feat, output_margin, 0);
    }

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat          feature vector
     * @param base_margin   predict with base margin for each prediction
     * @return prediction values
     */
    public float[] predict(FVec feat, float base_margin) {
        return predict(feat, base_margin, 0);
    }

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat          feature vector
     * @param base_margin   predict with base margin for each prediction
     * @param ntree_limit   limit the number of trees used in prediction
     * @return prediction values
     */
    public float[] predict(FVec feat, float base_margin, int ntree_limit) {
        float[] preds = predictRaw(feat, base_margin, ntree_limit);
        preds = obj.predTransform(preds);
        return preds;
    }

    /**
     * Generates predictions for given feature vector.
     *
     * @param feat          feature vector
     * @param output_margin whether to only predict margin value instead of transformed prediction
     * @param ntree_limit   limit the number of trees used in prediction
     * @return prediction values
     */
    public float[] predict(FVec feat, boolean output_margin, int ntree_limit) {
        float[] preds = predictRaw(feat, base_score, ntree_limit);
        if (! output_margin) {
            preds = obj.predTransform(preds);
        }
        return preds;
    }

    float[] predictRaw(FVec feat, float base_score, int ntree_limit) {
        float[] preds = gbm.predict(feat, ntree_limit);
        for (int i = 0; i < preds.length; i++) {
            preds[i] += base_score;
        }
        return preds;
    }

    /**
     * Generates a prediction for given feature vector.
     * <p>
     * This method only works when the model outputs single value.
     * </p>
     *
     * @param feat feature vector
     * @return prediction value
     */
    public float predictSingle(FVec feat) {
        return predictSingle(feat, false);
    }

    /**
     * Generates a prediction for given feature vector.
     * <p>
     * This method only works when the model outputs single value.
     * </p>
     *
     * @param feat          feature vector
     * @param output_margin whether to only predict margin value instead of transformed prediction
     * @return prediction value
     */
    public float predictSingle(FVec feat, boolean output_margin) {
        return predictSingle(feat, output_margin, 0);
    }

    /**
     * Generates a prediction for given feature vector.
     * <p>
     * This method only works when the model outputs single value.
     * </p>
     *
     * @param feat          feature vector
     * @param output_margin whether to only predict margin value instead of transformed prediction
     * @param ntree_limit   limit the number of trees used in prediction
     * @return prediction value
     */
    public float predictSingle(FVec feat, boolean output_margin, int ntree_limit) {
        float pred = predictSingleRaw(feat, ntree_limit);
        if (!output_margin) {
            pred = obj.predTransform(pred);
        }
        return pred;
    }

    float predictSingleRaw(FVec feat, int ntree_limit) {
        return gbm.predictSingle(feat, ntree_limit) + base_score;
    }

    /**
     * Predicts leaf index of each tree.
     *
     * @param feat feature vector
     * @return leaf indexes
     */
    public int[] predictLeaf(FVec feat) {
        return predictLeaf(feat, 0);
    }

    /**
     * Predicts leaf index of each tree.
     *
     * @param feat        feature vector
     * @param ntree_limit limit, 0 for all
     * @return leaf indexes
     */
    public int[] predictLeaf(FVec feat, int ntree_limit) {
        return gbm.predictLeaf(feat, ntree_limit);
    }

    /**
     * Predicts path to leaf of each tree.
     *
     * @param feat        feature vector
     * @return leaf paths
     */
    public String[] predictLeafPath(FVec feat) {
        return predictLeafPath(feat, 0);
    }

    /**
     * Predicts path to leaf of each tree.
     *
     * @param feat        feature vector
     * @param ntree_limit limit, 0 for all
     * @return leaf paths
     */
    public String[] predictLeafPath(FVec feat, int ntree_limit) {
        return gbm.predictLeafPath(feat, ntree_limit);
    }


    /**
     * Returns number of class.
     *
     * @return number of class
     */
    public int getNumClass() {
        return mparam.num_class;
    }

    static class ModelParam implements Serializable {
        final float base_score;
        final int num_feature;
        final int num_class;
        private final int major_version;
        private final int minor_version;

        ModelParam(float base_score, int num_feature, XgbMainPojo xgbMainPojo) throws IOException {
            this.base_score = base_score;
            this.num_feature = num_feature;
            this.num_class = convertToInt(xgbMainPojo.getLearner().getLearnerModelParam().getNumClass());
            List<Integer> version = xgbMainPojo.getVersion();
            this.major_version = version.get(0);
            this.minor_version = version.get(1);
         }
    }

    public GradBooster getBooster(){
        return gbm;
    }

    public String getObjName() {
        return name_obj;
    }

    public float getBaseScore() {
        return base_score;
    }

    XgbMainPojo loadJsonToPojos(String modelFilePath) throws IOException {
        XgbJsonToPojoReader xgbJsonToPojoReader = new XgbJsonToPojoReader();
        XgbMainPojo xgbMainPojo = xgbJsonToPojoReader.readXgbJsonToPojo(modelFilePath);
        return xgbMainPojo;
    }

}
