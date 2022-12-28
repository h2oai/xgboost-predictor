package biz.k11i.xgboost.tree.xgb.gen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "base_weights",
    "categories",
    "categories_nodes",
    "categories_segments",
    "categories_sizes",
    "default_left",
    "id",
    "left_children",
    "loss_changes",
    "parents",
    "right_children",
    "split_conditions",
    "split_indices",
    "split_type",
    "sum_hessian",
    "tree_param"
})
public class Tree {

    @JsonProperty("base_weights")
    private List<Double> baseWeights = new ArrayList<Double>();
    @JsonProperty("categories")
    private List<Object> categories = new ArrayList<Object>();
    @JsonProperty("categories_nodes")
    private List<Object> categoriesNodes = new ArrayList<Object>();
    @JsonProperty("categories_segments")
    private List<Object> categoriesSegments = new ArrayList<Object>();
    @JsonProperty("categories_sizes")
    private List<Object> categoriesSizes = new ArrayList<Object>();
    @JsonProperty("default_left")
    private List<Boolean> defaultLeft = new ArrayList<Boolean>();
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("left_children")
    private List<Integer> leftChildren = new ArrayList<Integer>();
    @JsonProperty("loss_changes")
    private List<Double> lossChanges = new ArrayList<Double>();
    @JsonProperty("parents")
    private List<Integer> parents = new ArrayList<Integer>();
    @JsonProperty("right_children")
    private List<Integer> rightChildren = new ArrayList<Integer>();
    @JsonProperty("split_conditions")
    private List<Double> splitConditions = new ArrayList<Double>();
    @JsonProperty("split_indices")
    private List<Integer> splitIndices = new ArrayList<Integer>();
    @JsonProperty("split_type")
    private List<Integer> splitType = new ArrayList<Integer>();
    @JsonProperty("sum_hessian")
    private List<Double> sumHessian = new ArrayList<Double>();
    @JsonProperty("tree_param")
    private TreeParam treeParam;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("base_weights")
    public List<Double> getBaseWeights() {
        return baseWeights;
    }

    @JsonProperty("base_weights")
    public void setBaseWeights(List<Double> baseWeights) {
        this.baseWeights = baseWeights;
    }

    public Tree withBaseWeights(List<Double> baseWeights) {
        this.baseWeights = baseWeights;
        return this;
    }

    @JsonProperty("categories")
    public List<Object> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    public Tree withCategories(List<Object> categories) {
        this.categories = categories;
        return this;
    }

    @JsonProperty("categories_nodes")
    public List<Object> getCategoriesNodes() {
        return categoriesNodes;
    }

    @JsonProperty("categories_nodes")
    public void setCategoriesNodes(List<Object> categoriesNodes) {
        this.categoriesNodes = categoriesNodes;
    }

    public Tree withCategoriesNodes(List<Object> categoriesNodes) {
        this.categoriesNodes = categoriesNodes;
        return this;
    }

    @JsonProperty("categories_segments")
    public List<Object> getCategoriesSegments() {
        return categoriesSegments;
    }

    @JsonProperty("categories_segments")
    public void setCategoriesSegments(List<Object> categoriesSegments) {
        this.categoriesSegments = categoriesSegments;
    }

    public Tree withCategoriesSegments(List<Object> categoriesSegments) {
        this.categoriesSegments = categoriesSegments;
        return this;
    }

    @JsonProperty("categories_sizes")
    public List<Object> getCategoriesSizes() {
        return categoriesSizes;
    }

    @JsonProperty("categories_sizes")
    public void setCategoriesSizes(List<Object> categoriesSizes) {
        this.categoriesSizes = categoriesSizes;
    }

    public Tree withCategoriesSizes(List<Object> categoriesSizes) {
        this.categoriesSizes = categoriesSizes;
        return this;
    }

    @JsonProperty("default_left")
    public List<Boolean> getDefaultLeft() {
        return defaultLeft;
    }

    @JsonProperty("default_left")
    public void setDefaultLeft(List<Boolean> defaultLeft) {
        this.defaultLeft = defaultLeft;
    }

    public Tree withDefaultLeft(List<Boolean> defaultLeft) {
        this.defaultLeft = defaultLeft;
        return this;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Tree withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("left_children")
    public List<Integer> getLeftChildren() {
        return leftChildren;
    }

    @JsonProperty("left_children")
    public void setLeftChildren(List<Integer> leftChildren) {
        this.leftChildren = leftChildren;
    }

    public Tree withLeftChildren(List<Integer> leftChildren) {
        this.leftChildren = leftChildren;
        return this;
    }

    @JsonProperty("loss_changes")
    public List<Double> getLossChanges() {
        return lossChanges;
    }

    @JsonProperty("loss_changes")
    public void setLossChanges(List<Double> lossChanges) {
        this.lossChanges = lossChanges;
    }

    public Tree withLossChanges(List<Double> lossChanges) {
        this.lossChanges = lossChanges;
        return this;
    }

    @JsonProperty("parents")
    public List<Integer> getParents() {
        return parents;
    }

    @JsonProperty("parents")
    public void setParents(List<Integer> parents) {
        this.parents = parents;
    }

    public Tree withParents(List<Integer> parents) {
        this.parents = parents;
        return this;
    }

    @JsonProperty("right_children")
    public List<Integer> getRightChildren() {
        return rightChildren;
    }

    @JsonProperty("right_children")
    public void setRightChildren(List<Integer> rightChildren) {
        this.rightChildren = rightChildren;
    }

    public Tree withRightChildren(List<Integer> rightChildren) {
        this.rightChildren = rightChildren;
        return this;
    }

    @JsonProperty("split_conditions")
    public List<Double> getSplitConditions() {
        return splitConditions;
    }

    @JsonProperty("split_conditions")
    public void setSplitConditions(List<Double> splitConditions) {
        this.splitConditions = splitConditions;
    }

    public Tree withSplitConditions(List<Double> splitConditions) {
        this.splitConditions = splitConditions;
        return this;
    }

    @JsonProperty("split_indices")
    public List<Integer> getSplitIndices() {
        return splitIndices;
    }

    @JsonProperty("split_indices")
    public void setSplitIndices(List<Integer> splitIndices) {
        this.splitIndices = splitIndices;
    }

    public Tree withSplitIndices(List<Integer> splitIndices) {
        this.splitIndices = splitIndices;
        return this;
    }

    @JsonProperty("split_type")
    public List<Integer> getSplitType() {
        return splitType;
    }

    @JsonProperty("split_type")
    public void setSplitType(List<Integer> splitType) {
        this.splitType = splitType;
    }

    public Tree withSplitType(List<Integer> splitType) {
        this.splitType = splitType;
        return this;
    }

    @JsonProperty("sum_hessian")
    public List<Double> getSumHessian() {
        return sumHessian;
    }

    @JsonProperty("sum_hessian")
    public void setSumHessian(List<Double> sumHessian) {
        this.sumHessian = sumHessian;
    }

    public Tree withSumHessian(List<Double> sumHessian) {
        this.sumHessian = sumHessian;
        return this;
    }

    @JsonProperty("tree_param")
    public TreeParam getTreeParam() {
        return treeParam;
    }

    @JsonProperty("tree_param")
    public void setTreeParam(TreeParam treeParam) {
        this.treeParam = treeParam;
    }

    public Tree withTreeParam(TreeParam treeParam) {
        this.treeParam = treeParam;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Tree withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Tree.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("baseWeights");
        sb.append('=');
        sb.append(((this.baseWeights == null)?"<null>":this.baseWeights));
        sb.append(',');
        sb.append("categories");
        sb.append('=');
        sb.append(((this.categories == null)?"<null>":this.categories));
        sb.append(',');
        sb.append("categoriesNodes");
        sb.append('=');
        sb.append(((this.categoriesNodes == null)?"<null>":this.categoriesNodes));
        sb.append(',');
        sb.append("categoriesSegments");
        sb.append('=');
        sb.append(((this.categoriesSegments == null)?"<null>":this.categoriesSegments));
        sb.append(',');
        sb.append("categoriesSizes");
        sb.append('=');
        sb.append(((this.categoriesSizes == null)?"<null>":this.categoriesSizes));
        sb.append(',');
        sb.append("defaultLeft");
        sb.append('=');
        sb.append(((this.defaultLeft == null)?"<null>":this.defaultLeft));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("leftChildren");
        sb.append('=');
        sb.append(((this.leftChildren == null)?"<null>":this.leftChildren));
        sb.append(',');
        sb.append("lossChanges");
        sb.append('=');
        sb.append(((this.lossChanges == null)?"<null>":this.lossChanges));
        sb.append(',');
        sb.append("parents");
        sb.append('=');
        sb.append(((this.parents == null)?"<null>":this.parents));
        sb.append(',');
        sb.append("rightChildren");
        sb.append('=');
        sb.append(((this.rightChildren == null)?"<null>":this.rightChildren));
        sb.append(',');
        sb.append("splitConditions");
        sb.append('=');
        sb.append(((this.splitConditions == null)?"<null>":this.splitConditions));
        sb.append(',');
        sb.append("splitIndices");
        sb.append('=');
        sb.append(((this.splitIndices == null)?"<null>":this.splitIndices));
        sb.append(',');
        sb.append("splitType");
        sb.append('=');
        sb.append(((this.splitType == null)?"<null>":this.splitType));
        sb.append(',');
        sb.append("sumHessian");
        sb.append('=');
        sb.append(((this.sumHessian == null)?"<null>":this.sumHessian));
        sb.append(',');
        sb.append("treeParam");
        sb.append('=');
        sb.append(((this.treeParam == null)?"<null>":this.treeParam));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.splitConditions == null)? 0 :this.splitConditions.hashCode()));
        result = ((result* 31)+((this.treeParam == null)? 0 :this.treeParam.hashCode()));
        result = ((result* 31)+((this.sumHessian == null)? 0 :this.sumHessian.hashCode()));
        result = ((result* 31)+((this.categoriesSizes == null)? 0 :this.categoriesSizes.hashCode()));
        result = ((result* 31)+((this.rightChildren == null)? 0 :this.rightChildren.hashCode()));
        result = ((result* 31)+((this.baseWeights == null)? 0 :this.baseWeights.hashCode()));
        result = ((result* 31)+((this.categoriesNodes == null)? 0 :this.categoriesNodes.hashCode()));
        result = ((result* 31)+((this.splitType == null)? 0 :this.splitType.hashCode()));
        result = ((result* 31)+((this.leftChildren == null)? 0 :this.leftChildren.hashCode()));
        result = ((result* 31)+((this.splitIndices == null)? 0 :this.splitIndices.hashCode()));
        result = ((result* 31)+((this.defaultLeft == null)? 0 :this.defaultLeft.hashCode()));
        result = ((result* 31)+((this.categories == null)? 0 :this.categories.hashCode()));
        result = ((result* 31)+((this.categoriesSegments == null)? 0 :this.categoriesSegments.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.lossChanges == null)? 0 :this.lossChanges.hashCode()));
        result = ((result* 31)+((this.parents == null)? 0 :this.parents.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tree) == false) {
            return false;
        }
        Tree rhs = ((Tree) other);
        return ((((((((((((((((((this.splitConditions == rhs.splitConditions)||((this.splitConditions!= null)&&this.splitConditions.equals(rhs.splitConditions)))&&((this.treeParam == rhs.treeParam)||((this.treeParam!= null)&&this.treeParam.equals(rhs.treeParam))))&&((this.sumHessian == rhs.sumHessian)||((this.sumHessian!= null)&&this.sumHessian.equals(rhs.sumHessian))))&&((this.categoriesSizes == rhs.categoriesSizes)||((this.categoriesSizes!= null)&&this.categoriesSizes.equals(rhs.categoriesSizes))))&&((this.rightChildren == rhs.rightChildren)||((this.rightChildren!= null)&&this.rightChildren.equals(rhs.rightChildren))))&&((this.baseWeights == rhs.baseWeights)||((this.baseWeights!= null)&&this.baseWeights.equals(rhs.baseWeights))))&&((this.categoriesNodes == rhs.categoriesNodes)||((this.categoriesNodes!= null)&&this.categoriesNodes.equals(rhs.categoriesNodes))))&&((this.splitType == rhs.splitType)||((this.splitType!= null)&&this.splitType.equals(rhs.splitType))))&&((this.leftChildren == rhs.leftChildren)||((this.leftChildren!= null)&&this.leftChildren.equals(rhs.leftChildren))))&&((this.splitIndices == rhs.splitIndices)||((this.splitIndices!= null)&&this.splitIndices.equals(rhs.splitIndices))))&&((this.defaultLeft == rhs.defaultLeft)||((this.defaultLeft!= null)&&this.defaultLeft.equals(rhs.defaultLeft))))&&((this.categories == rhs.categories)||((this.categories!= null)&&this.categories.equals(rhs.categories))))&&((this.categoriesSegments == rhs.categoriesSegments)||((this.categoriesSegments!= null)&&this.categoriesSegments.equals(rhs.categoriesSegments))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.lossChanges == rhs.lossChanges)||((this.lossChanges!= null)&&this.lossChanges.equals(rhs.lossChanges))))&&((this.parents == rhs.parents)||((this.parents!= null)&&this.parents.equals(rhs.parents))));
    }

}
