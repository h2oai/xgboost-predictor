package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.Predictor;
import biz.k11i.xgboost.util.FVec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;


public class PredictorPredictLeafTest {

    @Test
    public void shouldPredictLeafIds() throws IOException {
        Predictor p = new Predictor(getClass().getResourceAsStream("/boosterBytes.bin"));
        float[] input = new float[] { 10, 20, 30, 5, 7, 10 };
        FVec vec = FVec.Transformer.fromArray(input, false);
        int[] preds = p.predictLeaf(vec);
        int[] exp = new int[] { 33, 47, 41, 41, 49 };
        assertArrayEquals(exp, preds);
    }

    @Test
    public void shouldPredictLeafPaths() throws IOException {
        Predictor p = new Predictor(getClass().getResourceAsStream("/boosterBytes.bin"));
        float[] input = new float[] { 10, 20, 30, 5, 7, 10 };
        FVec vec = FVec.Transformer.fromArray(input, false);
        String[] preds = p.predictLeafPath(vec);
        String[] exp = new String[] { "LRRLL", "LLRRLL", "LLRRLL", "LLRRLL", "LLRRLL" };
        assertArrayEquals(exp, preds);
    }

}
