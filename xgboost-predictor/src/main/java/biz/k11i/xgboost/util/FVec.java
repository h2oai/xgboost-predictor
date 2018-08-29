package biz.k11i.xgboost.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Interface of feature vector.
 */
public interface FVec extends Serializable {
    /**
     * Gets index-th value.
     *
     * @param index index
     * @return value
     */
    float fvalue(int index);

    class Transformer {
        private Transformer() {
            // do nothing
        }

        /**
         * Builds FVec from dense vector.
         *
         * @param values         float values
         * @param treatsZeroAsNA treat zero as N/A if true
         * @return FVec
         */
        public static FVec fromArray(float[] values, boolean treatsZeroAsNA) {
            return new FVecArrayImpl.FVecFloatArrayImpl(values, treatsZeroAsNA);
        }

        /**
         * Builds FVec from dense vector.
         *
         * @param values         double values
         * @param treatsZeroAsNA treat zero as N/A if true
         * @return FVec
         */
        public static FVec fromArray(double[] values, boolean treatsZeroAsNA) {
            return new FVecArrayImpl.FVecDoubleArrayImpl(values, treatsZeroAsNA);
        }

        /**
         * Builds FVec from map.
         *
         * @param map map containing non-zero values
         * @return FVec
         */
        public static FVec fromMap(Map<Integer, ? extends Number> map) {
            return new FVecMapImpl(map);
        }
    }
}

class FVecMapImpl implements FVec {
    private final Map<Integer, ? extends Number> values;

    FVecMapImpl(Map<Integer, ? extends Number> values) {
        this.values = values;
    }

    @Override
    public float fvalue(int index) {
        Number number = values.get(index);
        if (number == null) {
            return Float.NaN;
        }

        return number.floatValue();
    }
}

class FVecArrayImpl {
    static class FVecFloatArrayImpl implements FVec {
        private final float[] values;
        private final boolean treatsZeroAsNA;

        FVecFloatArrayImpl(float[] values, boolean treatsZeroAsNA) {
            this.values = values;
            this.treatsZeroAsNA = treatsZeroAsNA;
        }

        @Override
        public float fvalue(int index) {
            if (values.length <= index) {
                return Float.NaN;
            }

            float result = values[index];
            if (treatsZeroAsNA && result == 0) {
                return Float.NaN;
            }

            return result;
        }
    }

    static class FVecDoubleArrayImpl implements FVec {
        private final double[] values;
        private final boolean treatsZeroAsNA;

        FVecDoubleArrayImpl(double[] values, boolean treatsZeroAsNA) {
            this.values = values;
            this.treatsZeroAsNA = treatsZeroAsNA;
        }

        @Override
        public float fvalue(int index) {
            if (values.length <= index) {
                return Float.NaN;
            }

            final double result = values[index];
            if (treatsZeroAsNA && result == 0) {
                return Float.NaN;
            }

            return (float) result;
        }
    }
}