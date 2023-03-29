package biz.k11i.xgboost;

import biz.k11i.xgboost.learner.ObjFunction;
import biz.k11i.xgboost.util.FVec;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PredictorTest {

    private Predictor predictor;
    private FVec row;

    @Before
    public void setup() throws Exception {
        predictor = new Predictor(getClass().getResourceAsStream("/boosterBytes.bin"));
        float[] rowData = new float[] { 10, 20, 30, 5, 7, 10 };
        row = FVec.Transformer.fromArray(rowData, false);

    }

    @Test
    public void shouldPredictWithDefaultBaseScore() {
        final float baseScoreOffset = 0.5f;
        final float modelBaseScore = predictor.getBaseScore();

        float defPrediction = predictor.predictSingle(row, true);
        predictor.setBaseScore(modelBaseScore + baseScoreOffset);

        float modPrediction = predictor.predictSingle(row, true);
        assertEquals(baseScoreOffset, modPrediction - defPrediction, 0);
    }

    @Test
    public void shouldIgnoreDefaultBaseScore() {
        final float baseScoreOffset = 0.5f;
        final float modelBaseScore = predictor.getBaseScore();

        float defPrediction = predictor.predictSingle(row, true);
        float custPrediction = predictor.predictSingle(row, modelBaseScore, true);
        assertEquals(defPrediction, custPrediction, 0);

        float actual = predictor.predictSingle(row, modelBaseScore + baseScoreOffset, true);
        assertEquals(defPrediction + baseScoreOffset, actual, 0);
    }

    @Test
    public void shouldReturnObjectiveFunction() {
        final ObjFunction objFunction = predictor.getObjective();
        assertNotNull(objFunction);

        float expected = predictor.predictSingle(row);
        float rawActual = predictor.predictSingle(row, true);
        float actual = objFunction.predTransform(rawActual);

        assertEquals(expected, actual, 0);
    }

}