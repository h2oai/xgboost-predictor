package biz.k11i.xgboost.tree.reg;

import biz.k11i.xgboost.tree.xgb.gen.Tree;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public final class DefaultRegTreeFactory implements RegTreeFactory {

  public static RegTreeFactory INSTANCE = new DefaultRegTreeFactory();

  @Override
  public final RegTree loadTree(ModelReader reader) throws IOException {
    RegTreeImpl regTree = new RegTreeImpl();
    regTree.loadModel(reader);
    return regTree;
  }

}
