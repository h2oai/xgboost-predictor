package biz.k11i.xgboost.tree.reg;

import biz.k11i.xgboost.tree.xgb.gen.Tree;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public interface RegTreeFactory {

  RegTree loadTree(ModelReader reader) throws IOException;

}
