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
    "best_iteration",
    "best_ntree_limit",
    "best_score"
})
public class Attributes {

    @JsonProperty("best_iteration")
    private String bestIteration;
    @JsonProperty("best_ntree_limit")
    private String bestNtreeLimit;
    @JsonProperty("best_score")
    private String bestScore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("best_iteration")
    public String getBestIteration() {
        return bestIteration;
    }

    @JsonProperty("best_iteration")
    public void setBestIteration(String bestIteration) {
        this.bestIteration = bestIteration;
    }

    public Attributes withBestIteration(String bestIteration) {
        this.bestIteration = bestIteration;
        return this;
    }

    @JsonProperty("best_ntree_limit")
    public String getBestNtreeLimit() {
        return bestNtreeLimit;
    }

    @JsonProperty("best_ntree_limit")
    public void setBestNtreeLimit(String bestNtreeLimit) {
        this.bestNtreeLimit = bestNtreeLimit;
    }

    public Attributes withBestNtreeLimit(String bestNtreeLimit) {
        this.bestNtreeLimit = bestNtreeLimit;
        return this;
    }

    @JsonProperty("best_score")
    public String getBestScore() {
        return bestScore;
    }

    @JsonProperty("best_score")
    public void setBestScore(String bestScore) {
        this.bestScore = bestScore;
    }

    public Attributes withBestScore(String bestScore) {
        this.bestScore = bestScore;
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

    public Attributes withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Attributes.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("bestIteration");
        sb.append('=');
        sb.append(((this.bestIteration == null)?"<null>":this.bestIteration));
        sb.append(',');
        sb.append("bestNtreeLimit");
        sb.append('=');
        sb.append(((this.bestNtreeLimit == null)?"<null>":this.bestNtreeLimit));
        sb.append(',');
        sb.append("bestScore");
        sb.append('=');
        sb.append(((this.bestScore == null)?"<null>":this.bestScore));
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
        result = ((result* 31)+((this.bestIteration == null)? 0 :this.bestIteration.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.bestNtreeLimit == null)? 0 :this.bestNtreeLimit.hashCode()));
        result = ((result* 31)+((this.bestScore == null)? 0 :this.bestScore.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Attributes) == false) {
            return false;
        }
        Attributes rhs = ((Attributes) other);
        return (((((this.bestIteration == rhs.bestIteration)||((this.bestIteration!= null)&&this.bestIteration.equals(rhs.bestIteration)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.bestNtreeLimit == rhs.bestNtreeLimit)||((this.bestNtreeLimit!= null)&&this.bestNtreeLimit.equals(rhs.bestNtreeLimit))))&&((this.bestScore == rhs.bestScore)||((this.bestScore!= null)&&this.bestScore.equals(rhs.bestScore))));
    }

}
