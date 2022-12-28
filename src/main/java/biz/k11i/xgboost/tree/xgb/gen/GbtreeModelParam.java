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
    "num_trees",
    "size_leaf_vector"
})
public class GbtreeModelParam {

    @JsonProperty("num_trees")
    private String numTrees;
    @JsonProperty("size_leaf_vector")
    private String sizeLeafVector;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("num_trees")
    public String getNumTrees() {
        return numTrees;
    }

    @JsonProperty("num_trees")
    public void setNumTrees(String numTrees) {
        this.numTrees = numTrees;
    }

    public GbtreeModelParam withNumTrees(String numTrees) {
        this.numTrees = numTrees;
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

    public GbtreeModelParam withSizeLeafVector(String sizeLeafVector) {
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

    public GbtreeModelParam withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GbtreeModelParam.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("numTrees");
        sb.append('=');
        sb.append(((this.numTrees == null)?"<null>":this.numTrees));
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
        result = ((result* 31)+((this.numTrees == null)? 0 :this.numTrees.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.sizeLeafVector == null)? 0 :this.sizeLeafVector.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GbtreeModelParam) == false) {
            return false;
        }
        GbtreeModelParam rhs = ((GbtreeModelParam) other);
        return ((((this.numTrees == rhs.numTrees)||((this.numTrees!= null)&&this.numTrees.equals(rhs.numTrees)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.sizeLeafVector == rhs.sizeLeafVector)||((this.sizeLeafVector!= null)&&this.sizeLeafVector.equals(rhs.sizeLeafVector))));
    }

}
