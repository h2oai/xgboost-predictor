package biz.k11i.xgboost;

import biz.k11i.xgboost.util.FVec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PredictorSmokeTest {

    private Predictor predictor;

    @Before
    public void setup() throws Exception {
        predictor = new Predictor(getClass().getResourceAsStream("/prostate/boosterBytesProstate.bin"));
    }

    @Test
    public void shouldProvideEqualPredictionWithDifferentAPI() {
        float[] rowData = new float[] { 13.2f, 23.6f};
        FVec row = FVec.Transformer.fromArray(rowData, false);
        float[] prediction = predictor.predict(row);
        float[] prediction2 = predictor.predict(row, false);
        float[] prediction3 = predictor.predict(row, predictor.getBaseScore());
        float[] prediction4 = predictor.predict(row, predictor.getBaseScore(), 0);
        float[] prediction5 = predictor.predict(row, false, 0);
        float predictionSingle = predictor.predictSingle(row);
        float predictionSingle2 = predictor.predictSingle(row, false);
        float predictionSingle3 = predictor.predictSingle(row, false, 0);

        float expected = 63.56952667f; // obtain from xgboost version 1.2.0

        Assert.assertEquals(1, prediction.length);
        Assert.assertEquals(expected, prediction[0], 0);
        Assert.assertEquals(expected, prediction2[0], 0);
        Assert.assertEquals(expected, prediction3[0], 0);
        Assert.assertEquals(expected, prediction4[0], 0);
        Assert.assertEquals(expected, prediction5[0], 0);
        Assert.assertEquals(expected, predictionSingle, 0);
        Assert.assertEquals(expected, predictionSingle2, 0);
        Assert.assertEquals(expected, predictionSingle3, 0);
    }

    @Test
    public void shouldProvideDifferentPredictionPrecisionDueTheFloatingPointError() {
        float[] rowData = new float[] { 15.2f, 0};
        FVec row = FVec.Transformer.fromArray(rowData, false);
        float[] prediction = predictor.predict(row);
        float predictionSingle = predictor.predictSingle(row);

        /*
         * obtained from xgboost version 1.3.0 and newer (Current upper bound is 1.6.1)
         * Difference is due the different sequence of floating point addition
         * see: https://github.com/dmlc/xgboost/issues/6350
         * Previous result was 64.41069031f obtained from xgboost version 1.2.0
         */
        float previousExpected = 64.41069031f;
        float expected = 64.41068268f;

        Assert.assertNotEquals(previousExpected, prediction[0], 0);
        Assert.assertNotEquals(previousExpected, predictionSingle, 0);
        Assert.assertEquals(1, prediction.length);
        Assert.assertEquals(expected, prediction[0], 0);
        Assert.assertEquals(expected, predictionSingle, 0);
    }

    @Test
    public void shouldProvidePredictionWithCustomBaseMargin() {
        float[] rowData = new float[] { 13.2f, 23.6f};
        FVec row = FVec.Transformer.fromArray(rowData, false);
        float[] prediction = predictor.predict(row);
        float[] predictionWithCustomBaseMargin = predictor.predict(row, predictor.getBaseScore() + 0.2f);

        Assert.assertEquals(1, prediction.length);
        Assert.assertEquals(1, predictionWithCustomBaseMargin.length);
        Assert.assertEquals(prediction[0] + 0.2f, predictionWithCustomBaseMargin[0], 0);
    }

    //TODO add test that each parameter in each GradBooster implementation works properly
    //TODO add test for multivariate prediction
}
