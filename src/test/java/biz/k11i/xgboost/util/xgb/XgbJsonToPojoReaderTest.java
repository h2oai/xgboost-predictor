package biz.k11i.xgboost.util.xgb;

import biz.k11i.xgboost.tree.xgb.gen.Tree;
import biz.k11i.xgboost.tree.xgb.gen.XgbMainPojo;
import biz.k11i.xgboost.util.FVec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XgbJsonToPojoReaderTest {

    private static final Logger logger = LoggerFactory.getLogger(XgbJsonToPojoReaderTest.class);

    @Test
    public void testReadXgbJsonToPojo() throws IOException {
        XgbJsonToPojoReader xgbJsonToPojoReader = new XgbJsonToPojoReader();
        XgbMainPojo xgbMainPojo = xgbJsonToPojoReader.readXgbJsonToPojo("models/xgboost/pi-xgb-v14.json");

        Assertions.assertEquals(xgbMainPojo.getLearner().getAttributes().getBestIteration(), "99");
        Assertions.assertEquals(xgbMainPojo.getLearner().getAttributes().getBestNtreeLimit(), "100");

        Assertions.assertEquals(xgbMainPojo.getLearner().getGradientBooster().getName(), "gbtree");
        Assertions.assertEquals(xgbMainPojo.getLearner().getGradientBooster().getModel().getTreeInfo().size(),
                100);
        Assertions.assertEquals(xgbMainPojo.getLearner().getGradientBooster().getModel().getGbtreeModelParam()
                .getNumTrees(), "100");

        Assertions.assertEquals(xgbMainPojo.getLearner().getLearnerModelParam().getNumFeature(), "8");
        Assertions.assertEquals(xgbMainPojo.getLearner().getObjective().getName(), "binary:logistic");
        Assertions.assertEquals(xgbMainPojo.getLearner().getObjective().getRegLossParam().getScalePosWeight(), "1");
        List<Integer> versionAsList = Arrays.asList(1, 4, 2);
        Assertions.assertIterableEquals(xgbMainPojo.getVersion(), versionAsList);
    }

    @Test
    public void getPredictedVal() throws IOException {
        XgbJsonToPojoReader xgbJsonToPojoReader = new XgbJsonToPojoReader();
        XgbMainPojo xgbMainPojo = xgbJsonToPojoReader.readXgbJsonToPojo("models/xgboost/pi-xgb-v14.json");
        float[] predictions = null;
        float[][] input = new float[][]{{1, 90, 62, 12, 43, 27.2f, 0.58f, 24}, {7, 181, 84, 21, 192, 35.9f, 0.586f, 51}};
        float[][] expected = new float[][]{{41259745}, {5698928}};
        int l_a_bestIteration = convertToInt(xgbMainPojo.getLearner().getAttributes().getBestIteration());
        logger.info("Learners -> Attributes -> BestIteration -> {}", l_a_bestIteration);
        int l_a_bestNTreeLimit = convertToInt(xgbMainPojo.getLearner().getAttributes().getBestNtreeLimit());
        logger.info("Learners -> Attributes -> BestNtreeLimit -> {}", l_a_bestNTreeLimit);

        float l_lmp_baseScore = convertToFloat(xgbMainPojo.getLearner().getLearnerModelParam().getBaseScore());
        logger.info("Base Score -> {}", l_lmp_baseScore);
        int l_lmp_noOfFeatures = convertToInt(xgbMainPojo.getLearner().getLearnerModelParam().getNumFeature());
        logger.info("No of Features -> {}", l_lmp_noOfFeatures);
        int l_lmp_noOfClass = convertToInt(xgbMainPojo.getLearner().getLearnerModelParam().getNumClass());
        logger.info("No of Class -> {}", l_lmp_noOfClass);

        String l_gb_name = xgbMainPojo.getLearner().getGradientBooster().getName();
        logger.info("Learners gradient booster name -> {}", l_gb_name);

        int l_gb_m_gmp_noOfTrees = convertToInt(xgbMainPojo.getLearner().getGradientBooster().getModel()
                .getGbtreeModelParam().getNumTrees());
        logger.info("Learners gradient booster, model, model param number of trees -> {}", l_gb_m_gmp_noOfTrees);
        int l_gb_m_gmp_sizeLeafVector = convertToInt(xgbMainPojo.getLearner().getGradientBooster().getModel()
                .getGbtreeModelParam().getSizeLeafVector());
        logger.info("Learners gradient booster, model, model size leaf vector -> {}", l_gb_m_gmp_sizeLeafVector);

        List<Tree> trees = xgbMainPojo.getLearner().getGradientBooster().getModel().getTrees();
        for (int i = 0; i < 1; i++) {
            XgbTreeTraversal xgbTreeTraversal = new XgbTreeTraversal(trees.get(i));
            xgbTreeTraversal.traverse();
        }
        String l_obj_objFunctnName = xgbMainPojo.getLearner().getObjective().getName();
        logger.info("Learners Objective Function Name -> {}", l_obj_objFunctnName);

        for (int i = 0; i < input.length; i++) {
            FVec vec = FVec.Transformer.fromArray(input[i], true);
            float[] preds = null;
            //assertPredsInRange(input[i], preds, expected[i]);
        }
    }

    public static boolean assertPredsInRange(float[] input, float[] preds, float[] exp) {
        for (int i = 0; i < preds.length; i++) {
            if (Math.abs(exp[i] - preds[i]) > (exp[i] * 0.01)) return false;
        }
        return true;
    }

    /*
            float pred(FVec feat, int bst_group, int root_index, int ntree_limit) {
                RegTree[] trees = _groupTrees[bst_group];
                int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;
                float psum = 0;
                for (int i = 0; i < treeleft; i++) {
                    psum += trees[i].getLeafValue(feat, root_index);
                }
                return psum;
            }

            public float getLeafValue(FVec feat, int root_id) {
                Node n = nodes[root_id];
                while (!n._isLeaf) {
                    n = nodes[n.next(feat)];
                }
                return n.leaf_value;
            }

            public int next(FVec feat) {
            float value = feat.fvalue(_splitIndex);
            if (value != value) {  // is NaN?
                return _defaultNext;
            }
                return (value < split_cond) ? cleft_ : cright_;
            }

     */

    private float convertToFloat(String inputStr) {
        return Float.parseFloat(inputStr);
    }

    private int convertToInt(String inputStr) {
        return Integer.parseInt(inputStr);
    }

    private float[] predTransform(float[] preds) {
        for (int i = 0; i < preds.length; i++) {
            preds[i] = sigmoid(preds[i]);
        }
        return preds;
    }

    private float predTransform(float pred) {
        return sigmoid(pred);
    }

    private float sigmoid(float x) {
        return (1f / (1f + (float) Math.exp(-x)));
    }

    public float probToMargin(float prob) {
        return (float) -Math.log(1.0f / prob - 1.0f);
    }

}