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
    "gbtree_model_param",
    "tree_info",
    "trees"
})
public class Model {

    @JsonProperty("gbtree_model_param")
    private GbtreeModelParam gbtreeModelParam;
    @JsonProperty("tree_info")
    private List<Integer> treeInfo = new ArrayList<Integer>();
    @JsonProperty("trees")
    private List<Tree> trees = new ArrayList<Tree>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("gbtree_model_param")
    public GbtreeModelParam getGbtreeModelParam() {
        return gbtreeModelParam;
    }

    @JsonProperty("gbtree_model_param")
    public void setGbtreeModelParam(GbtreeModelParam gbtreeModelParam) {
        this.gbtreeModelParam = gbtreeModelParam;
    }

    public Model withGbtreeModelParam(GbtreeModelParam gbtreeModelParam) {
        this.gbtreeModelParam = gbtreeModelParam;
        return this;
    }

    @JsonProperty("tree_info")
    public List<Integer> getTreeInfo() {
        return treeInfo;
    }

    @JsonProperty("tree_info")
    public void setTreeInfo(List<Integer> treeInfo) {
        this.treeInfo = treeInfo;
    }

    public Model withTreeInfo(List<Integer> treeInfo) {
        this.treeInfo = treeInfo;
        return this;
    }

    @JsonProperty("trees")
    public List<Tree> getTrees() {
        return trees;
    }

    @JsonProperty("trees")
    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    public Model withTrees(List<Tree> trees) {
        this.trees = trees;
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

    public Model withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Model.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("gbtreeModelParam");
        sb.append('=');
        sb.append(((this.gbtreeModelParam == null)?"<null>":this.gbtreeModelParam));
        sb.append(',');
        sb.append("treeInfo");
        sb.append('=');
        sb.append(((this.treeInfo == null)?"<null>":this.treeInfo));
        sb.append(',');
        sb.append("trees");
        sb.append('=');
        sb.append(((this.trees == null)?"<null>":this.trees));
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
        result = ((result* 31)+((this.gbtreeModelParam == null)? 0 :this.gbtreeModelParam.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.trees == null)? 0 :this.trees.hashCode()));
        result = ((result* 31)+((this.treeInfo == null)? 0 :this.treeInfo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Model) == false) {
            return false;
        }
        Model rhs = ((Model) other);
        return (((((this.gbtreeModelParam == rhs.gbtreeModelParam)||((this.gbtreeModelParam!= null)&&this.gbtreeModelParam.equals(rhs.gbtreeModelParam)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.trees == rhs.trees)||((this.trees!= null)&&this.trees.equals(rhs.trees))))&&((this.treeInfo == rhs.treeInfo)||((this.treeInfo!= null)&&this.treeInfo.equals(rhs.treeInfo))));
    }

}
