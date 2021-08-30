package ai.h2o.algos.tree;

public interface INode<T> {

  boolean isLeaf();

  float getLeafValue();

  int getSplitIndex();

  int next(T value);

  int getLeftChildIndex();

  int getRightChildIndex();

}