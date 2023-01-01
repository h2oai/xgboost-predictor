package biz.k11i.xgboost.gbm;

import biz.k11i.xgboost.config.PredictorConfiguration;
import biz.k11i.xgboost.tree.xgb.gen.XgbMainPojo;
import biz.k11i.xgboost.util.ModelReader;

import java.io.IOException;

public abstract class GBBase implements GradBooster {
    protected int num_class;
    protected int num_feature;
    protected int num_output_group;

    @Override
    public void setNumClass(int numClass) {
        this.num_class = numClass;
        this.num_output_group = (num_class == 0) ? 1 : num_class;
    }

    @Override
    public void setNumFeature(int numFeature) {
        this.num_feature = numFeature;
    }

    public void loadModel(PredictorConfiguration config, ModelReader reader, boolean with_pbuffer) throws IOException{
        //Override in classes for version < 1
    };

    public void loadModel(XgbMainPojo xgbMainPojo) throws IOException {
        //Override in classes for version < 1
    };
}