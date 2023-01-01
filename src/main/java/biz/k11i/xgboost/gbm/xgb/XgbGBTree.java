package biz.k11i.xgboost.gbm.xgb;

import biz.k11i.xgboost.config.PredictorConfiguration;
;
import biz.k11i.xgboost.gbm.GBBase;
import biz.k11i.xgboost.tree.reg.RegTree;
import biz.k11i.xgboost.tree.xgb.XgbTreeImpl;
import biz.k11i.xgboost.tree.xgb.gen.Tree;
import biz.k11i.xgboost.tree.xgb.gen.XgbMainPojo;
import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;
import biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader;
import static biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader.convertToFloat;
import static biz.k11i.xgboost.util.xgb.XgbJsonToPojoReader.convertToInt;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class XgbGBTree extends GBBase {

    ModelParam mparam;
    private XgbTreeImpl[] trees;

    XgbTreeImpl[][] _groupTrees;

    public void loadModel(XgbMainPojo xgbMainPojo) throws IOException {
        mparam = new ModelParam(xgbMainPojo);
        List<Tree> treesList = xgbMainPojo.getLearner().getGradientBooster().getModel().getTrees();
        trees = new XgbTreeImpl[treesList.size()];
        for (int i = 0; i < treesList.size(); i++) {
            trees[i] = new XgbTreeImpl(treesList.get(i));
        }

        List<Integer> tree_info = xgbMainPojo.getLearner().getGradientBooster().getModel().getTreeInfo();

        _groupTrees = new XgbTreeImpl[num_output_group][];
        for (int i = 0; i < num_output_group; i++) {
            int treeCount = 0;
            for (int j = 0; j < tree_info.size(); j++) {
                if (tree_info.get(j) == i) {
                    treeCount++;
                }
            }

            _groupTrees[i] = new XgbTreeImpl[treeCount];
            treeCount = 0;

            for (int j = 0; j < tree_info.size(); j++) {
                if (tree_info.get(j) == i) {
                    _groupTrees[i][treeCount++] = trees[j];
                }
            }
        }
    }

    @Override
    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean with_pbuffer) throws IOException {
        //Not applicable
    }

    @Override
    public float[] predict(FVec feat, int ntree_limit) {
        float[] preds = new float[num_output_group];
        for (int gid = 0; gid < num_output_group; gid++) {
            preds[gid] = pred(feat, gid, 0, ntree_limit);
        }
        return preds;
    }

    @Override
    public float predictSingle(FVec feat, int ntree_limit) {
        if (num_output_group != 1) {
            throw new IllegalStateException(
                    "Can't invoke predictSingle() because this model outputs multiple values: "
                    + num_output_group);
        }
        return pred(feat, 0, 0, ntree_limit);
    }

    float pred(FVec feat, int bst_group, int root_index, int ntree_limit) {
        XgbTreeImpl[] trees = _groupTrees[bst_group];
        int treeleft = ntree_limit == 0 ? trees.length : ntree_limit;
        float psum = 0;
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

    static class ModelParam implements Serializable {
        final int num_trees;
        final int size_leaf_vector;

        ModelParam(XgbMainPojo xgbMainPojo) throws IOException {
            num_trees = convertToInt(xgbMainPojo.getLearner().getGradientBooster().getModel().getGbtreeModelParam()
                    .getNumTrees());
            size_leaf_vector = convertToInt(xgbMainPojo.getLearner().getGradientBooster().getModel().getGbtreeModelParam()
                    .getSizeLeafVector());
        }

    }

    public XgbTreeImpl[][] getGroupedTrees(){
        return _groupTrees;
    }
}
