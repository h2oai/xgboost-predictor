package biz.k11i.xgboost.tree.reg;

import biz.k11i.xgboost.util.FVec;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;
import java.io.Serializable;

/**
 * Regression tree.
 */
public class RegTreeImpl implements RegTree {

    private Param param;
    private Node[] nodes;
    private RegTreeNodeStat[] stats;

    /**
     * Loads model from stream.
     *
     * @param reader input stream
     * @throws IOException If an I/O error occurs
     */
    public void loadModel(ModelReader reader) throws IOException {
        param = new Param(reader);

        nodes = new Node[param.num_nodes];
        for (int i = 0; i < param.num_nodes; i++) {
            nodes[i] = new Node(reader);
        }

        stats = new RegTreeNodeStat[param.num_nodes];
        for (int i = 0; i < param.num_nodes; i++) {
            stats[i] = new RegTreeNodeStat(reader);
        }
    }

    /**
     * Retrieves nodes from root to leaf and returns leaf index.
     *
     * @param feat    feature vector
     * @return leaf index
     */
    @Override
    public int getLeafIndex(FVec feat) {
        int id = 0;
        Node n;
        while (!(n = nodes[id])._isLeaf) {
            id = n.next(feat);
        }
        return id;
    }

    /**
     * Retrieves nodes from root to leaf and returns path to leaf.
     *
     * @param feat    feature vector
     * @param sb      output param, will write path path to leaf into this buffer
     */
    @Override
    public void getLeafPath(FVec feat, StringBuilder sb) {
        int id = 0;
        Node n;
        while (!(n = nodes[id])._isLeaf) {
            id = n.next(feat);
            sb.append(id == n.cleft_ ? "L" : "R");
        }
    }

    /**
     * Retrieves nodes from root to leaf and returns leaf value.
     *
     * @param feat    feature vector
     * @param root_id starting root index
     * @return leaf value
     */
    @Override
    public float getLeafValue(FVec feat, int root_id) {
        Node n = nodes[root_id];
        while (!n._isLeaf) {
            n = nodes[n.next(feat)];
        }

        return n.leaf_value;
    }

    @Override
    public Node[] getNodes() {
        return nodes;
    }

    @Override
    public RegTreeNodeStat[] getStats() {
        return stats;
    }

    /**
     * Parameters.
     */
    static class Param implements Serializable {
        /*! \brief number of start root */
        final int num_roots;
        /*! \brief total number of nodes */
        final int num_nodes;
        /*!\brief number of deleted nodes */
        final int num_deleted;
        /*! \brief maximum depth, this is a statistics of the tree */
        final int max_depth;
        /*! \brief  number of features used for tree construction */
        final int num_feature;
        /*!
         * \brief leaf vector size, used for vector tree
         * used to store more than one dimensional information in tree
         */
        final int size_leaf_vector;
        /*! \brief reserved part */
        final int[] reserved;

        Param(ModelReader reader) throws IOException {
            num_roots = reader.readInt();
            num_nodes = reader.readInt();
            num_deleted = reader.readInt();
            max_depth = reader.readInt();
            num_feature = reader.readInt();

            size_leaf_vector = reader.readInt();
            reserved = reader.readIntArray(31);
        }
    }

    public static class Node extends RegTreeNode implements Serializable {
        // pointer to parent, highest bit is used to
        // indicate whether it's a left child or not
        final int parent_;
        // pointer to left, right
        final int cleft_, cright_;
        // split feature index, left split or right split depends on the highest bit
        final /* unsigned */ int sindex_;
        // extra info (leaf_value or split_cond)
        final float leaf_value;
        final float split_cond;

        private final int _defaultNext;
        private final int _splitIndex;
        final boolean _isLeaf;

        // set parent
        Node(ModelReader reader) throws IOException {
            parent_ = reader.readInt();
            cleft_ = reader.readInt();
            cright_ = reader.readInt();
            sindex_ = reader.readInt();

            if (isLeaf()) {
                leaf_value = reader.readFloat();
                split_cond = Float.NaN;
            } else {
                split_cond = reader.readFloat();
                leaf_value = Float.NaN;
            }

            _defaultNext = cdefault();
            _splitIndex = getSplitIndex();
            _isLeaf = isLeaf();
        }

        public boolean isLeaf() {
            return cleft_ == -1;
        }

        @Override
        public int getSplitIndex() {
            return (int) (sindex_ & ((1l << 31) - 1l));
        }

        public int cdefault() {
            return default_left() ? cleft_ : cright_;
        }

        @Override
        public boolean default_left() {
            return (sindex_ >>> 31) != 0;
        }

        @Override
        public int next(FVec feat) {
            float value = feat.fvalue(_splitIndex);
            if (value != value) {  // is NaN?
                return _defaultNext;
            }
            return (value < split_cond) ? cleft_ : cright_;
        }

        @Override
        public int getParentIndex() {
            return parent_;
        }

        @Override
        public int getLeftChildIndex() {
            return cleft_;
        }

        @Override
        public int getRightChildIndex() {
            return cright_;
        }

        @Override
        public float getSplitCondition() {
            return split_cond;
        }

        @Override
        public float getLeafValue(){
            return leaf_value;
        }
    }

}
