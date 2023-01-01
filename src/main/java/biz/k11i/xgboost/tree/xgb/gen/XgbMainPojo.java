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
    "learner",
    "version"
})
public class XgbMainPojo {

    @JsonProperty("learner")
    private Learner learner;
    @JsonProperty("version")
    private List<Integer> version = new ArrayList<Integer>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("learner")
    public Learner getLearner() {
        return learner;
    }

    @JsonProperty("learner")
    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public XgbMainPojo withLearner(Learner learner) {
        this.learner = learner;
        return this;
    }

    @JsonProperty("version")
    public List<Integer> getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(List<Integer> version) {
        this.version = version;
    }

    public XgbMainPojo withVersion(List<Integer> version) {
        this.version = version;
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

    public XgbMainPojo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(XgbMainPojo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("learner");
        sb.append('=');
        sb.append(((this.learner == null)?"<null>":this.learner));
        sb.append(',');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
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
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.learner == null)? 0 :this.learner.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof XgbMainPojo) == false) {
            return false;
        }
        XgbMainPojo rhs = ((XgbMainPojo) other);
        return ((((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties)))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.learner == rhs.learner)||((this.learner!= null)&&this.learner.equals(rhs.learner))));
    }

}
