package ai.h2o.algos.tree;

public interface INodeStat {

  float getGain();

  float getCover();

  float getBaseWeight();

  int getLeafCount();

}
