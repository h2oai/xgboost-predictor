package biz.k11i.xgboost.tree;

import ai.h2o.algos.tree.INodeStat;
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

        // if the tree was loaded with no depth, calculate it and replace the parameter values
        if (param.max_depth == 0) {
            // update the parameters by calculating the tree depth
            param = new Param(param, this);
        }
    }

    /**
     * Recursively find the depth of tree
     * @param root_id: root index of the tree
     * @param seed_depth: depth of tree above the root
     * @return depth of the tree
     */
    private int getTreeDepth(int root_id, int seed_depth) {
        // by default, the depth is the seed depth + 1 (depth 1 if this is a node)
        int depth = seed_depth+1;
        if (!nodes[root_id]._isLeaf) {
            int left = getTreeDepth(nodes[root_id].cleft_, depth);
            int right = getTreeDepth(nodes[root_id].cright_, depth);
            depth = Math.max(left, right);
        }
        return depth;
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

        /**
         * Using previous parameter values, create new ones by calculating the tree depth
         * @param previous  previous parameter values
         * @param tree      loaded tree
         */
        Param(Param previous, RegTreeImpl tree) {
            num_roots = previous.num_roots;
            num_nodes = previous.num_nodes;
            num_deleted = previous.num_deleted;
            max_depth = tree.getTreeDepth(0, 0);
            num_feature = previous.num_feature;
            size_leaf_vector = previous.size_leaf_vector;
            reserved = previous.reserved;
        }

        public String toString() {
            return "Number of roots: " + num_roots + ", "
                    + "Number of nodes: " + num_nodes + ", "
                    + "Number of nodes deleted: " + num_deleted + ", "
                    + "Max tree depth: " + max_depth + ", "
                    + "Number of features: " + num_feature + ", "
                    + "Size of leaf vector: " + size_leaf_vector;
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
            return (int) (sindex_ & ((1L << 31) - 1L));
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

    // extend our decision path with a fraction of one and zero extensions
    private void ExtendPath(PathElement[] parent_unique_path, int unique_path_start, int unique_depth,
                            float zero_fraction, float one_fraction, int feature_index) {
        parent_unique_path[unique_path_start+unique_depth] = new PathElement(feature_index,
                zero_fraction, one_fraction, (unique_depth == 0 ? 1.0f : 0.0f));

        for (int i = unique_depth - 1; i >= 0; i--) {
            parent_unique_path[unique_path_start+i+1].pweight += one_fraction *
                    parent_unique_path[unique_path_start+i].pweight * (i + 1)
                    / (float)(unique_depth + 1);
            parent_unique_path[unique_path_start+i].pweight = zero_fraction *
                    parent_unique_path[unique_path_start+i].pweight * (unique_depth - i)
                    / (float)(unique_depth + 1);
        }
    }

    // undo a previous extension of the decision path
    private void UnwindPath(PathElement[] parent_unique_path, int unique_path_start, int unique_depth, int path_index) {
        final float one_fraction = parent_unique_path[unique_path_start+path_index].one_fraction;
        final float zero_fraction = parent_unique_path[unique_path_start+path_index].zero_fraction;
        float next_one_portion = parent_unique_path[unique_path_start+unique_depth].pweight;

        for (int i = unique_depth - 1; i >= 0; --i) {
            if (one_fraction != 0) {
                final float tmp = parent_unique_path[unique_path_start+i].pweight;
                parent_unique_path[unique_path_start+i].pweight = next_one_portion * (unique_depth + 1)
                        / ((i + 1) * one_fraction);
                next_one_portion = tmp - parent_unique_path[unique_path_start+i].pweight * zero_fraction * (unique_depth - i)
                        / (float)(unique_depth + 1);
            } else {
                parent_unique_path[unique_path_start+i].pweight = (parent_unique_path[unique_path_start+i].pweight *
                        (unique_depth + 1)) / (zero_fraction * (unique_depth - i));
            }
        }

        for (int i = path_index; i < unique_depth; ++i) {
            parent_unique_path[unique_path_start+i].feature_index = parent_unique_path[unique_path_start+i+1].feature_index;
            parent_unique_path[unique_path_start+i].zero_fraction = parent_unique_path[unique_path_start+i+1].zero_fraction;
            parent_unique_path[unique_path_start+i].one_fraction = parent_unique_path[unique_path_start+i+1].one_fraction;
        }
    }

    // determine what the total permuation weight would be if
    // we unwound a previous extension in the decision path
    private float UnwoundPathSum(PathElement[] parent_unique_path, int unique_path_start, int unique_depth,
                                 int path_index) {
        final float one_fraction = parent_unique_path[unique_path_start+path_index].one_fraction;
        final float zero_fraction = parent_unique_path[unique_path_start+path_index].zero_fraction;
        float next_one_portion = parent_unique_path[unique_path_start+unique_depth].pweight;
        float total = 0;
        for (int i = unique_depth - 1; i >= 0; --i) {
            if (one_fraction != 0) {
                final float tmp = next_one_portion * (unique_depth + 1)
                        / ((i + 1) * one_fraction);
                total += tmp;
                next_one_portion = parent_unique_path[unique_path_start+i].pweight - tmp * zero_fraction * ((unique_depth - i)
                        / (float)(unique_depth+1));
            } else {
                total += (parent_unique_path[unique_path_start+i].pweight / zero_fraction) / ((unique_depth - i)
                        / (float)(unique_depth + 1));
            }
        }
        return total;
    }

    /**
     * Used by TreeShap
     * data we keep about our decision path
     * note that pweight is included for convenience and is not tied with the other attributes
     * the pweight of the i'th path element is the permuation weight of paths with i-1 ones in them
     */
    private class PathElement {
        int feature_index;
        float zero_fraction;
        float one_fraction;
        float pweight;

        PathElement(int f, float z, float o, float w) {
            feature_index = f;
            zero_fraction = z;
            one_fraction = o;
            pweight = w;
        }
    }

    // recursive computation of SHAP values for a decision tree
    private void TreeShap(FVec feat, double[] phi, int node_index, int unique_depth, int parent_depth,
                          PathElement[] parent_unique_path, float parent_zero_fraction,
                          float parent_one_fraction, int parent_feature_index) {
        final Node node = nodes[node_index];

        int unique_path_start = parent_depth + unique_depth;
        // copy over a section of the array
        if (unique_depth > 0) {
            for (int i = parent_depth, j = unique_path_start; i < unique_path_start; i++, j++)
            {
                parent_unique_path[j] = parent_unique_path[i];
            }
        }

        ExtendPath(parent_unique_path, unique_path_start, unique_depth, parent_zero_fraction,
                parent_one_fraction, parent_feature_index);
        final int split_index = node.getSplitIndex();

        // leaf node
        if (node._isLeaf) {
            for (int i = 1; i <= unique_depth; ++i) {
                final float w = UnwoundPathSum(parent_unique_path, unique_path_start, unique_depth, i);
                final PathElement el = parent_unique_path[unique_path_start+i];
                phi[el.feature_index] += w * (el.one_fraction - el.zero_fraction) * node.leaf_value;
            }

            // internal node
        } else {
            // find which branch is "hot" (meaning x would follow it)
            int hot_index;
            if (Double.isNaN(feat.fvalue(split_index))) {
                hot_index = node.cdefault();
            } else if (feat.fvalue(split_index) < node.split_cond) {
                hot_index = node.cleft_;
            } else {
                hot_index = node.cright_;
            }
            final int cold_index = (hot_index == node.cleft_ ? node.cright_ : node.cleft_);
            final float w = stats[node_index].sum_hess;
            final float hot_zero_fraction = stats[hot_index].sum_hess / w;
            final float cold_zero_fraction = stats[cold_index].sum_hess / w;
            float incoming_zero_fraction = 1;
            float incoming_one_fraction = 1;

            // see if we have already split on this feature,
            // if so we undo that split so we can redo it for this node
            int path_index = 0;
            for (; path_index <= unique_depth; ++path_index) {
                if ((parent_unique_path[unique_path_start+path_index].feature_index) == split_index) break;
            }
            if (path_index != unique_depth + 1) {
                incoming_zero_fraction = parent_unique_path[unique_path_start+path_index].zero_fraction;
                incoming_one_fraction = parent_unique_path[unique_path_start+path_index].one_fraction;
                UnwindPath(parent_unique_path, unique_path_start, unique_depth, path_index);
                unique_depth -= 1;
            }

            TreeShap(feat, phi, hot_index, unique_depth + 1, unique_path_start, parent_unique_path,
                    hot_zero_fraction*incoming_zero_fraction, incoming_one_fraction, split_index);

            TreeShap(feat, phi, cold_index, unique_depth + 1, unique_path_start, parent_unique_path,
                    cold_zero_fraction*incoming_zero_fraction, 0, split_index);
        }
    }

    /**
     * Get feature contributions for a tree
     * @param feat          feature vector
     * @param root_id       root index of the tree
     * @return  array of feature contributions [num_features+1] with sum in last cell
     */
    public double[] getFeatureContributions(FVec feat, int root_id) {
        double[] out_contribs = new double[param.num_feature+1];
        // find the expected value of the tree's predictions
        double base_value = 0.0f;
        double total_cover = 0.0f;
        for (int i = 0; i < nodes.length; ++i) {
            final Node node = nodes[i];
            if (node._isLeaf) {
                final float cover = stats[i].sum_hess;
                base_value += cover * node.leaf_value;
                total_cover += cover;
            }
        }
        // the last element contains the sum
        out_contribs[out_contribs.length-1] += base_value / total_cover;

        // Preallocate space for the unique path data
        final int maxd = param.max_depth + 1;
        PathElement[] unique_path_data = new PathElement[(maxd * (maxd + 1)) / 2];

        // recursively call tree traversing. will modify out_contribs
        TreeShap(feat, out_contribs, root_id, 0, 0, unique_path_data, 1,
                1, -1);

        return out_contribs;
    }

}
