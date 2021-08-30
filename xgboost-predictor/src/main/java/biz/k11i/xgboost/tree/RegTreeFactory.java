package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public interface RegTreeFactory {

  RegTree loadTree(ModelReader reader) throws IOException;

}
