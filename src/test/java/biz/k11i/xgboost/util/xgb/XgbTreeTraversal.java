package biz.k11i.xgboost.util.xgb;

import biz.k11i.xgboost.tree.xgb.gen.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XgbTreeTraversal {

    private static final Logger logger = LoggerFactory.getLogger(XgbTreeTraversal.class);
    Tree xgbTree;

    public XgbTreeTraversal(Tree xgbTree){
        this.xgbTree = xgbTree;
    }

    public void traverse() {
        logger.debug("Start with root -> 0");
        traverseDepthFirst(0, 0);
    }

    private void traverseDepthFirst(int index, int level) {
        if(index >= 0 && index < xgbTree.getLeftChildren().size()) {
            traverseDepthFirst(xgbTree.getLeftChildren().get(index), level + 1);
            traverseDepthFirst(xgbTree.getRightChildren().get(index), level + 1);
        }else{

        }
    }

    private String addspace(int i, String str) {
        StringBuilder str1 = new StringBuilder();
        for (int j = 0; j < i; j++) {
            str1.append(" ");
        }
        str1.append(str);
        return str1.toString();
    }
}
