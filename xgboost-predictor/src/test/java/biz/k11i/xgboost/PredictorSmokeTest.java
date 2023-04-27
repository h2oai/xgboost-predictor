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

    //TODO add test that each parameter in each GradBooster implementation works properly
    //TODO add test for multivariate prediction
}
