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
    "base_score",
    "num_class",
    "num_feature"
})
public class LearnerModelParam {

    @JsonProperty("base_score")
    private String baseScore;
    @JsonProperty("num_class")
    private String numClass;
    @JsonProperty("num_feature")
    private String numFeature;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("base_score")
    public String getBaseScore() {
        return baseScore;
    }

    @JsonProperty("base_score")
    public void setBaseScore(String baseScore) {
        this.baseScore = baseScore;
    }

    public LearnerModelParam withBaseScore(String baseScore) {
        this.baseScore = baseScore;
        return this;
    }

    @JsonProperty("num_class")
    public String getNumClass() {
        return numClass;
    }

    @JsonProperty("num_class")
    public void setNumClass(String numClass) {
        this.numClass = numClass;
    }

    public LearnerModelParam withNumClass(String numClass) {
        this.numClass = numClass;
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

    public LearnerModelParam withNumFeature(String numFeature) {
        this.numFeature = numFeature;
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

    public LearnerModelParam withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LearnerModelParam.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("baseScore");
        sb.append('=');
        sb.append(((this.baseScore == null)?"<null>":this.baseScore));
        sb.append(',');
        sb.append("numClass");
        sb.append('=');
        sb.append(((this.numClass == null)?"<null>":this.numClass));
        sb.append(',');
        sb.append("numFeature");
        sb.append('=');
        sb.append(((this.numFeature == null)?"<null>":this.numFeature));
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
        result = ((result* 31)+((this.baseScore == null)? 0 :this.baseScore.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.numClass == null)? 0 :this.numClass.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LearnerModelParam) == false) {
            return false;
        }
        LearnerModelParam rhs = ((LearnerModelParam) other);
        return (((((this.numFeature == rhs.numFeature)||((this.numFeature!= null)&&this.numFeature.equals(rhs.numFeature)))&&((this.baseScore == rhs.baseScore)||((this.baseScore!= null)&&this.baseScore.equals(rhs.baseScore))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.numClass == rhs.numClass)||((this.numClass!= null)&&this.numClass.equals(rhs.numClass))));
    }

}
