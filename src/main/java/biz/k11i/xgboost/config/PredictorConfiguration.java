package biz.k11i.xgboost.config;

import biz.k11i.xgboost.learner.ObjFunction;
import biz.k11i.xgboost.tree.reg.DefaultRegTreeFactory;
import biz.k11i.xgboost.tree.reg.RegTreeFactory;

public class PredictorConfiguration {
    public static class Builder {
        private PredictorConfiguration predictorConfiguration;

        Builder() {
            predictorConfiguration = new PredictorConfiguration();
        }

        public Builder objFunction(ObjFunction objFunction) {
            predictorConfiguration.objFunction = objFunction;
            return this;
        }

        public Builder regTreeFactory(RegTreeFactory regTreeFactory) {
            predictorConfiguration.regTreeFactory = regTreeFactory;
            return this;
        }

        public PredictorConfiguration build() {
            PredictorConfiguration result = predictorConfiguration;
            predictorConfiguration = null;
            return result;
        }
    }

    public static final PredictorConfiguration DEFAULT = new PredictorConfiguration();

    private ObjFunction objFunction;
    private RegTreeFactory regTreeFactory;

    public PredictorConfiguration() {
        this.regTreeFactory = DefaultRegTreeFactory.INSTANCE;
    }

    public ObjFunction getObjFunction() {
        return objFunction;
    }

    public RegTreeFactory getRegTreeFactory() {
        return regTreeFactory;
    }

    public static Builder builder() {
        return new Builder();
    }
}
