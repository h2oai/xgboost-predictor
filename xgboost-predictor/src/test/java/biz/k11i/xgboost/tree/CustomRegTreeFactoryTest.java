package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.Predictor;
import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.util.ModelReader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomRegTreeFactoryTest {

    @Test
    public void shouldCallLoadTreeWithSequentialTreeIndices() throws IOException {
        SpyingRegTreeFactory factory = new SpyingRegTreeFactory();
        PredictorConfiguration pc = PredictorConfiguration.builder()
                .regTreeFactory(factory)
                .build();
        Predictor p = new Predictor(getClass().getResourceAsStream("/boosterBytes.bin"), pc);
        assertNotNull(p);

        assertEquals(4, factory.lastTreeId);
    }

    private static class SpyingRegTreeFactory implements RegTreeFactory {
        private int lastTreeId = -1;
        @Override
        public RegTree loadTree(int treeId, ModelReader reader) throws IOException {
            assertEquals("Tree indices are not sequential", lastTreeId + 1, treeId);
            lastTreeId = treeId;
            return DefaultRegTreeFactory.INSTANCE.loadTree(treeId, reader);
        }
    }

}