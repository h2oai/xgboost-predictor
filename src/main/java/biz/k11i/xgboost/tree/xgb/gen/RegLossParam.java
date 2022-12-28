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
    "scale_pos_weight"
})
public class RegLossParam {

    @JsonProperty("scale_pos_weight")
    private String scalePosWeight;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("scale_pos_weight")
    public String getScalePosWeight() {
        return scalePosWeight;
    }

    @JsonProperty("scale_pos_weight")
    public void setScalePosWeight(String scalePosWeight) {
        this.scalePosWeight = scalePosWeight;
    }

    public RegLossParam withScalePosWeight(String scalePosWeight) {
        this.scalePosWeight = scalePosWeight;
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

    public RegLossParam withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RegLossParam.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("scalePosWeight");
        sb.append('=');
        sb.append(((this.scalePosWeight == null)?"<null>":this.scalePosWeight));
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
        result = ((result* 31)+((this.scalePosWeight == null)? 0 :this.scalePosWeight.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RegLossParam) == false) {
            return false;
        }
        RegLossParam rhs = ((RegLossParam) other);
        return (((this.scalePosWeight == rhs.scalePosWeight)||((this.scalePosWeight!= null)&&this.scalePosWeight.equals(rhs.scalePosWeight)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}
