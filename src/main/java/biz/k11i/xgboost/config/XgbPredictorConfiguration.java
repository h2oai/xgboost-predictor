package biz.k11i.xgboost.config;

import biz.k11i.xgboost.learner.ObjFunction;
import biz.k11i.xgboost.tree.reg.DefaultRegTreeFactory;
import biz.k11i.xgboost.tree.reg.RegTreeFactory;

public class XgbPredictorConfiguration {
    public static class Builder {
        private XgbPredictorConfiguration predictorConfiguration;

        Builder() {
            predictorConfiguration = new XgbPredictorConfiguration();
        }

        public Builder objFunction(ObjFunction objFunction) {
            predictorConfiguration.objFunction = objFunction;
            return this;
        }

        public XgbPredictorConfiguration build() {
            XgbPredictorConfiguration result = predictorConfiguration;
            predictorConfiguration = null;
            return result;
        }
    }

    public static final XgbPredictorConfiguration DEFAULT = new XgbPredictorConfiguration();

    private ObjFunction objFunction;

    public ObjFunction getObjFunction() {
        return objFunction;
    }

    public static Builder builder() {
        return new Builder();
    }
}
