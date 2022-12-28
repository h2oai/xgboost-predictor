package biz.k11i.xgboost;

import biz.k11i.xgboost.util.FVec;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class XgbPredictorTest {

    private static final String MODEL = "models/xgboost/pi-xgb-v09.model";
    private static final Logger logger = LoggerFactory.getLogger(XgbPredictorTest.class);

    @Test
    public void testPredictionXGB_v14() throws IOException {
        XgbPredictor p = new XgbPredictor("models/xgboost/pi-xgb-v14.json");
        float[][] input = new float[][] {{1, 90, 62, 12, 43, 27.2f, 0.58f, 24},  {7, 181, 84, 21, 192, 35.9f,  0.586f, 51} };
        float[][] expected = new float[][]{{41259745}, {5698928}};
        for(int i = 0; i < input.length; i++){
            FVec vec = FVec.Transformer.fromArray(input[i], true);
            float[] preds = p.predict(vec, false);
            logger.info("Prediction Results: {}", preds);
            assertPredsInRange(input[i], preds, expected[i]);
        }
    }

    public static boolean assertPredsInRange(float[] input, float[] preds, float[] exp){
        for(int i = 0; i < preds.length; i++){
            if(Math.abs(exp[i] - preds[i]) > (exp[i] * 0.01)) return false;
        }
        return true;
    }

}
