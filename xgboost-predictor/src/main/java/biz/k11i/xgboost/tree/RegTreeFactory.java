package biz.k11i.xgboost.tree;

import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public interface RegTreeFactory {

  /**
   * Loads one tree stored at current position of the provided ModelReader.
   * @param treeId tree index, this is a physical index of the tree in the serialized model. For multiclass models
   *               trees are serialized in groups per-class, class 1 trees, class 2 trees, ... No assumptions can be
   *               made how tree index translates to a particular tree of a given class.
   * @param reader model reader
   * @return instance of a regression tree
   * @throws IOException when model is corrupted/cannot be deserialized
   */
  RegTree loadTree(int treeId, ModelReader reader) throws IOException;

}
