package biz.k11i.xgboost.gbm;

import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.tree.RegTree;
import biz.k11i.xgboost.tree.RegTreeImpl;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Gradient boosted tree implementation.
 */
public class GBTree extends GBBase {

    ModelParam mparam;
    private RegTree[] trees;

    RegTree[][] _groupTrees;

    @Override
    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean with_pbuffer) throws IOException {
        mparam = new ModelParam(reader);

        trees = new RegTree[mparam.num_trees];
        for (int i = 0; i < mparam.num_trees; i++) {
            trees[i] = config.getRegTreeFactory().loadTree(reader);
        }

        int[] tree_info = mparam.num_trees > 0 ? reader.readIntArray(mparam.num_trees) : new int[0];

        if (mparam.num_pbuffer != 0 && with_pbuffer) {
            reader.skip(4 * predBufferSize());
            reader.skip(4 * predBufferSize());
        }

        _groupTrees = new RegTree[num_output_group][];
        for (int i = 0; i < num_output_group; i++) {
            int treeCount = 0;
            for (int j = 0; j < tree_info.length; j++) {
                if (tree_info[j] == i) {
                    treeCount++;
                }
            }

            _groupTrees[i] = new RegTree[treeCount];
            treeCount = 0;

            for (int j = 0; j < tree_info.length; j++) {
                if (tree_info[j] == i) {
                    _groupTrees[i][treeCount++] = trees[j];
                }
            }
        }
    }

    @Override
    public float[] predict(FVec feat, int ntree_limit, float base_score) {
        float[] preds = new float[num_output_group];
        for (int gid = 0; gid < num_output_group; gid++) {
            preds[gid] += pred(feat, gid, 0, ntree_limit, base_score);
        }
        return preds;
    }

    @Override
    public float predictSingle(FVec feat, int ntree_limit, float base_score) {
        if (num_output_group != 1) {
            throw new IllegalStateException(
                    "Can't invoke predictSingle() because this model outputs multiple values: "
                    + num_output_group);
        }
        return pred(feat, 0, 0, ntree_limit, base_score);
    }

    float pred(FVec feat, int bst_group, int root_index, int ntree_limit, float base_score) {
        RegTree[] trees = _groupTrees[bst_group];
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;

        float psum = base_score;
        for (int i = 0; i < treeleft; i++) {
            psum += trees[i].getLeafValue(feat, root_index);
        }
        return psum;
    }

    @Override
    public int[] predictLeaf(FVec feat, int ntree_limit) {
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;
        int[] leafIndex = new int[treeleft];
        for (int i = 0; i < treeleft; i++) {
            leafIndex[i] = trees[i].getLeafIndex(feat);
        }
        return leafIndex;
    }

    @Override
    public String[] predictLeafPath(FVec feat, int ntree_limit) {
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;
        String[] leafPath = new String[treeleft];
        StringBuilder sb = new StringBuilder(64);
        for (int i = 0; i < treeleft; i++) {
            trees[i].getLeafPath(feat, sb);
            leafPath[i] = sb.toString();
            sb.setLength(0);
        }
        return leafPath;
    }

    private long predBufferSize() {
        return num_output_group * mparam.num_pbuffer * (mparam.size_leaf_vector + 1);
    }

    /**
     * Predict contributions for all classes for given number of trees
     * @param feat        feature vector
     * @param ntree_limit limit the number of trees used in prediction
     * @return array with feature weights (nFeatures + 1 wide) where right-most column is the sum
     */
    public double[][] predictContribution(FVec feat, int ntree_limit) {
        // need to do seperate predictions for each group (class) in the classifier
        double[][] preds = new double[mparam.num_output_group][mparam.num_feature+1];
        for (int gid = 0; gid < mparam.num_output_group; gid++) {
            // each class is predicted independently
            double[] contrib = calculateContributions(feat, gid, 0, ntree_limit);
            System.arraycopy(contrib, 0, preds[gid], 0, contrib.length);
        }
        return preds;
    }

    /**
     * Calculate the contribution of a given class for a given number of trees
     * @param feat          feature vector
     * @param bst_group     class group
     * @param root_index    tree root index
     * @param ntree_limit   number of trees limit
     * @return array with feature weights (nFeatures + 1 wide) where right-most column is the sum
     */
    private double[] calculateContributions(FVec feat, int bst_group, int root_index, int ntree_limit) {
        RegTreeImpl[] trees = (RegTreeImpl[]) _groupTrees[bst_group];
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;

        double[] psum = new double[mparam.num_feature+1];
        for (int i = 0; i < treeleft; i++) {
            double[] contrib = trees[i].getFeatureContributions(feat, root_index);
            for (int j = 0; j < contrib.length; j++) {
                psum[j] += contrib[j];
            }
        }

        return psum;
    }

    static class ModelParam implements Serializable {
        /*! \brief number of trees */
        final int num_trees;
        /*! \brief number of root: default 0, means single tree */
        final int num_roots;
        /*! \brief nnumber of features to be used by trees */
        final int num_feature;
        /*! \brief size of predicton buffer allocated used for buffering */
        final long num_pbuffer;
        /*!
         * \brief how many output group a single instance can produce
         *  this affects the behavior of number of output we have:
         *    suppose we have n instance and k group, output will be k*n
         */
        final int num_output_group;
        /*! \brief size of leaf vector needed in tree */
        final int size_leaf_vector;
        /*! \brief reserved space */
        final int[] reserved;

        ModelParam(ModelReader reader) throws IOException {
            num_trees = reader.readInt();
            num_roots = reader.readInt();
            num_feature = reader.readInt();
            reader.readInt(); // read padding
            num_pbuffer = reader.readLong();
            num_output_group = reader.readInt();
            size_leaf_vector = reader.readInt();
            reserved = reader.readIntArray(31);
            reader.readInt(); // read padding
        }

    }

    /**
     *
     * @return A two-dim array, with trees grouped into classes.
     */
    public RegTree[][] getGroupedTrees(){
        return _groupTrees;
    }
}
