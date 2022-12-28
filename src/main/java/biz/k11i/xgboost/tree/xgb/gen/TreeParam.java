package biz.k11i.xgboost.tree.xgb.gen;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "num_deleted",
    "num_feature",
    "num_nodes",
    "size_leaf_vector"
})
public class TreeParam {

    @JsonProperty("num_deleted")
    private String numDeleted;
    @JsonProperty("num_feature")
    private String numFeature;
    @JsonProperty("num_nodes")
    private String numNodes;
    @JsonProperty("size_leaf_vector")
    private String sizeLeafVector;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("num_deleted")
    public String getNumDeleted() {
        return numDeleted;
    }

    @JsonProperty("num_deleted")
    public void setNumDeleted(String numDeleted) {
        this.numDeleted = numDeleted;
    }

    public TreeParam withNumDeleted(String numDeleted) {
        this.numDeleted = numDeleted;
        return this;
    }

    @JsonProperty("num_feature")
    public String getNumFeature() {
        return numFeature;
    }

    @JsonProperty("num_feature")
    public void setNumFeature(String numFeature) {
        this.numFeature = numFeature;
    }

    public TreeParam withNumFeature(String numFeature) {
        this.numFeature = numFeature;
        return this;
    }

    @JsonProperty("num_nodes")
    public String getNumNodes() {
        return numNodes;
    }

    @JsonProperty("num_nodes")
    public void setNumNodes(String numNodes) {
        this.numNodes = numNodes;
    }

    public TreeParam withNumNodes(String numNodes) {
        this.numNodes = numNodes;
        return this;
    }

    @JsonProperty("size_leaf_vector")
    public String getSizeLeafVector() {
        return sizeLeafVector;
    }

    @JsonProperty("size_leaf_vector")
    public void setSizeLeafVector(String sizeLeafVector) {
        this.sizeLeafVector = sizeLeafVector;
    }

    public TreeParam withSizeLeafVector(String sizeLeafVector) {
        this.sizeLeafVector = sizeLeafVector;
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

    public TreeParam withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TreeParam.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("numDeleted");
        sb.append('=');
        sb.append(((this.numDeleted == null)?"<null>":this.numDeleted));
        sb.append(',');
        sb.append("numFeature");
        sb.append('=');
        sb.append(((this.numFeature == null)?"<null>":this.numFeature));
        sb.append(',');
        sb.append("numNodes");
        sb.append('=');
        sb.append(((this.numNodes == null)?"<null>":this.numNodes));
        sb.append(',');
        sb.append("sizeLeafVector");
        sb.append('=');
        sb.append(((this.sizeLeafVector == null)?"<null>":this.sizeLeafVector));
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
        result = ((result* 31)+((this.numFeature == null)? 0 :this.numFeature.hashCode()));
        result = ((result* 31)+((this.numNodes == null)? 0 :this.numNodes.hashCode()));
        result = ((result* 31)+((this.numDeleted == null)? 0 :this.numDeleted.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.sizeLeafVector == null)? 0 :this.sizeLeafVector.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TreeParam) == false) {
            return false;
        }
        TreeParam rhs = ((TreeParam) other);
        return ((((((this.numFeature == rhs.numFeature)||((this.numFeature!= null)&&this.numFeature.equals(rhs.numFeature)))&&((this.numNodes == rhs.numNodes)||((this.numNodes!= null)&&this.numNodes.equals(rhs.numNodes))))&&((this.numDeleted == rhs.numDeleted)||((this.numDeleted!= null)&&this.numDeleted.equals(rhs.numDeleted))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.sizeLeafVector == rhs.sizeLeafVector)||((this.sizeLeafVector!= null)&&this.sizeLeafVector.equals(rhs.sizeLeafVector))));
    }

}
