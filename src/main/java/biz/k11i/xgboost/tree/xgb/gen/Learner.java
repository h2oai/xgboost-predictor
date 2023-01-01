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
    "attributes",
    "feature_names",
    "feature_types",
    "gradient_booster",
    "learner_model_param",
    "objective"
})
public class Learner {

    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonProperty("feature_names")
    private List<Object> featureNames = new ArrayList<Object>();
    @JsonProperty("feature_types")
    private List<Object> featureTypes = new ArrayList<Object>();
    @JsonProperty("gradient_booster")
    private GradientBooster gradientBooster;
    @JsonProperty("learner_model_param")
    private LearnerModelParam learnerModelParam;
    @JsonProperty("objective")
    private Objective objective;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Learner withAttributes(Attributes attributes) {
        this.attributes = attributes;
        return this;
    }

    @JsonProperty("feature_names")
    public List<Object> getFeatureNames() {
        return featureNames;
    }

    @JsonProperty("feature_names")
    public void setFeatureNames(List<Object> featureNames) {
        this.featureNames = featureNames;
    }

    public Learner withFeatureNames(List<Object> featureNames) {
        this.featureNames = featureNames;
        return this;
    }

    @JsonProperty("feature_types")
    public List<Object> getFeatureTypes() {
        return featureTypes;
    }

    @JsonProperty("feature_types")
    public void setFeatureTypes(List<Object> featureTypes) {
        this.featureTypes = featureTypes;
    }

    public Learner withFeatureTypes(List<Object> featureTypes) {
        this.featureTypes = featureTypes;
        return this;
    }

    @JsonProperty("gradient_booster")
    public GradientBooster getGradientBooster() {
        return gradientBooster;
    }

    @JsonProperty("gradient_booster")
    public void setGradientBooster(GradientBooster gradientBooster) {
        this.gradientBooster = gradientBooster;
    }

    public Learner withGradientBooster(GradientBooster gradientBooster) {
        this.gradientBooster = gradientBooster;
        return this;
    }

    @JsonProperty("learner_model_param")
    public LearnerModelParam getLearnerModelParam() {
        return learnerModelParam;
    }

    @JsonProperty("learner_model_param")
    public void setLearnerModelParam(LearnerModelParam learnerModelParam) {
        this.learnerModelParam = learnerModelParam;
    }

    public Learner withLearnerModelParam(LearnerModelParam learnerModelParam) {
        this.learnerModelParam = learnerModelParam;
        return this;
    }

    @JsonProperty("objective")
    public Objective getObjective() {
        return objective;
    }

    @JsonProperty("objective")
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public Learner withObjective(Objective objective) {
        this.objective = objective;
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

    public Learner withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Learner.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("attributes");
        sb.append('=');
        sb.append(((this.attributes == null)?"<null>":this.attributes));
        sb.append(',');
        sb.append("featureNames");
        sb.append('=');
        sb.append(((this.featureNames == null)?"<null>":this.featureNames));
        sb.append(',');
        sb.append("featureTypes");
        sb.append('=');
        sb.append(((this.featureTypes == null)?"<null>":this.featureTypes));
        sb.append(',');
        sb.append("gradientBooster");
        sb.append('=');
        sb.append(((this.gradientBooster == null)?"<null>":this.gradientBooster));
        sb.append(',');
        sb.append("learnerModelParam");
        sb.append('=');
        sb.append(((this.learnerModelParam == null)?"<null>":this.learnerModelParam));
        sb.append(',');
        sb.append("objective");
        sb.append('=');
        sb.append(((this.objective == null)?"<null>":this.objective));
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
        result = ((result* 31)+((this.gradientBooster == null)? 0 :this.gradientBooster.hashCode()));
        result = ((result* 31)+((this.learnerModelParam == null)? 0 :this.learnerModelParam.hashCode()));
        result = ((result* 31)+((this.featureNames == null)? 0 :this.featureNames.hashCode()));
        result = ((result* 31)+((this.featureTypes == null)? 0 :this.featureTypes.hashCode()));
        result = ((result* 31)+((this.attributes == null)? 0 :this.attributes.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.objective == null)? 0 :this.objective.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Learner) == false) {
            return false;
        }
        Learner rhs = ((Learner) other);
        return ((((((((this.gradientBooster == rhs.gradientBooster)||((this.gradientBooster!= null)&&this.gradientBooster.equals(rhs.gradientBooster)))&&((this.learnerModelParam == rhs.learnerModelParam)||((this.learnerModelParam!= null)&&this.learnerModelParam.equals(rhs.learnerModelParam))))&&((this.featureNames == rhs.featureNames)||((this.featureNames!= null)&&this.featureNames.equals(rhs.featureNames))))&&((this.featureTypes == rhs.featureTypes)||((this.featureTypes!= null)&&this.featureTypes.equals(rhs.featureTypes))))&&((this.attributes == rhs.attributes)||((this.attributes!= null)&&this.attributes.equals(rhs.attributes))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.objective == rhs.objective)||((this.objective!= null)&&this.objective.equals(rhs.objective))));
    }

}
