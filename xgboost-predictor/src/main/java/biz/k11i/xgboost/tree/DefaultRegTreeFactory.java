package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public final class DefaultRegTreeFactory implements RegTreeFactory {

  public static RegTreeFactory INSTANCE = new DefaultRegTreeFactory();

  @Override
  public RegTree loadTree(int treeId, ModelReader reader) throws IOException {
    RegTreeImpl regTree = new RegTreeImpl();
    regTree.loadModel(reader);
    return regTree;
  }

}
