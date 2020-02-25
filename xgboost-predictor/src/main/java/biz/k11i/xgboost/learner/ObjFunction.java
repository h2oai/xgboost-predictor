package biz.k11i.xgboost.learner;

import biz.k11i.xgboost.config.PredictorConfiguration;
import net.jafama.FastMath;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Objective function implementations.
 */
public class ObjFunction implements Serializable {

    private static final Map<String, ObjFunction> FUNCTIONS = new HashMap<>();

    static {
        register("rank:pairwise", new ObjFunction());
        register("binary:logistic", new RegLossObjLogistic());
        register("binary:logitraw", new ObjFunction());
        register("multi:softmax", new SoftmaxMultiClassObjClassify());
        register("multi:softprob", new SoftmaxMultiClassObjProb());
        register("reg:linear", new ObjFunction());
        register("reg:squarederror", new ObjFunction());
        register("reg:gamma", new RegObjFunction());
        register("reg:tweedie", new RegObjFunction());
        register("count:poisson", new RegObjFunction());
    }

    /**
     * Gets {@link ObjFunction} from given name.
     *
     * @param name name of objective function
     * @return objective function
     */
    public static ObjFunction fromName(String name) {
        ObjFunction result = FUNCTIONS.get(name);
        if (result == null) {
            throw new IllegalArgumentException(name + " is not supported objective function.");
        }
        return result;
    }

    /**
     * Register an {@link ObjFunction} for a given name.
     *
     * @param name name of objective function
     * @param objFunction objective function
     * @deprecated This method will be made private. Please use {@link PredictorConfiguration.Builder#objFunction(ObjFunction)} instead.
     */
    public static void register(String name, ObjFunction objFunction) {
        FUNCTIONS.put(name, objFunction);
    }

    /**
     * Uses Jafama's {@link FastMath#exp(double)} instead of {@link Math#exp(double)}.
     *
     * @param useJafama {@code true} if you want to use Jafama's {@link FastMath#exp(double)},
     *                  or {@code false} if you don't want to use it but JDK's {@link Math#exp(double)}.
     */
    public static void useFastMathExp(boolean useJafama) {
        if (useJafama) {
            register("binary:logistic", new RegLossObjLogistic_Jafama());
            register("multi:softprob", new SoftmaxMultiClassObjProb_Jafama());

        } else {
            register("binary:logistic", new RegLossObjLogistic());
            register("multi:softprob", new SoftmaxMultiClassObjProb());
        }
    }

    /**
     * Transforms prediction values.
     *
     * @param preds prediction
     * @return transformed values
     */
    public float[] predTransform(float[] preds) {
        // do nothing
        return preds;
    }

    /**
     * Transforms a prediction value.
     *
     * @param pred prediction
     * @return transformed value
     */
    public float predTransform(float pred) {
        // do nothing
        return pred;
    }

    public float probToMargin(float prob) {
        // do nothing
        return prob;
    }

    /**
     * Regression.
     */
    static class RegObjFunction extends ObjFunction {
        @Override
        public float[] predTransform(float[] preds) {
            if (preds.length != 1)
                throw new IllegalStateException(
                    "Regression problem is supposed to have just a single predicted value, got " + preds.length + " instead."
                );
            preds[0] = (float) Math.exp(preds[0]);
            return preds;
        }

        @Override
        public float predTransform(float pred) {
            return (float) Math.exp(pred);
        }

        @Override
        public float probToMargin(float prob) {
            return (float) Math.log(prob);
        }
    }

    /**
     * Logistic regression.
     */
    static class RegLossObjLogistic extends ObjFunction {
        @Override
        public float[] predTransform(float[] preds) {
            for (int i = 0; i < preds.length; i++) {
                preds[i] = sigmoid(preds[i]);
            }
            return preds;
        }

        @Override
        public float predTransform(float pred) {
            return sigmoid(pred);
        }

        float sigmoid(float x) {
            return (1f / (1f + (float) Math.exp(-x)));
        }

        @Override
        public float probToMargin(float prob) {
            return (float) -Math.log(1.0f / prob - 1.0f);
        }
    }

    /**
     * Logistic regression.
     * <p>
     * Jafama's {@link FastMath#exp(double)} version.
     * </p>
     */
    static class RegLossObjLogistic_Jafama extends RegLossObjLogistic {
        @Override
        float sigmoid(float x) {
            return (float) (1 / (1 + FastMath.exp(-x)));
        }
    }

    /**
     * Multiclass classification.
     */
    static class SoftmaxMultiClassObjClassify extends ObjFunction {
        @Override
        public float[] predTransform(float[] preds) {
            int maxIndex = 0;
            float max = preds[0];
            for (int i = 1; i < preds.length; i++) {
                if (max < preds[i]) {
                    maxIndex = i;
                    max = preds[i];
                }
            }

            return new float[]{maxIndex};
        }

        @Override
        public float predTransform(float pred) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Multiclass classification (predicted probability).
     */
    static class SoftmaxMultiClassObjProb extends ObjFunction {
        @Override
        public float[] predTransform(float[] preds) {
            float max = preds[0];
            for (int i = 1; i < preds.length; i++) {
                max = Math.max(preds[i], max);
            }

            double sum = 0;
            for (int i = 0; i < preds.length; i++) {
                preds[i] = exp(preds[i] - max);
                sum += preds[i];
            }

            for (int i = 0; i < preds.length; i++) {
                preds[i] /= (float) sum;
            }

            return preds;
        }

        @Override
        public float predTransform(float pred) {
            throw new UnsupportedOperationException();
        }

        float exp(float x) {
            return (float) Math.exp(x);
        }
    }

    /**
     * Multiclass classification (predicted probability).
     * <p>
     * Jafama's {@link FastMath#exp(double)} version.
     * </p>
     */
    static class SoftmaxMultiClassObjProb_Jafama extends SoftmaxMultiClassObjProb {
        @Override
        float exp(float x) {
            return (float) FastMath.exp(x);
        }
    }
}
