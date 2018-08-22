xgboost-predictor-java
======================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ai.h2o/xgboost-predictor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ai.h2o/xgboost-predictor)

Pure Java implementation of [XGBoost](https://github.com/dmlc/xgboost/) predictor for online prediction tasks.


# Getting started

## Adding to dependencies

If you use **Maven**:

```xml
<dependencies>
  <dependency>
    <groupId>ai.h2o</groupId>
    <artifactId>xgboost-predictor</artifactId>
    <version>0.3.1</version>
  </dependency>
</dependencies>
```

Or **Gradle**:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile group: 'ai.h2o', name: 'xgboost-predictor', version: '0.3.1'
}
```

Or **sbt**:

```scala
resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "ai.h2o" % "xgboost-predictor" % "0.3.1"
)
```


## Using Predictor in Java

```java
package biz.k11i.xgboost.demo;

import biz.k11i.xgboost.Predictor;
import biz.k11i.xgboost.util.FVec;

public class HowToUseXgboostPredictor {
    public static void main(String[] args) throws java.io.IOException {
        // If you want to use faster exp() calculation, uncomment the line below
        // ObjFunction.useFastMathExp(true);

        // Load model and create Predictor
        Predictor predictor = new Predictor(
                new java.io.FileInputStream("/path/to/xgboost-model-file"));

        // Create feature vector from dense representation by array
        double[] denseArray = {0, 0, 32, 0, 0, 16, -8, 0, 0, 0};
        FVec fVecDense = FVec.Transformer.fromArray(
                denseArray,
                true /* treat zero element as N/A */);

        // Create feature vector from sparse representation by map
        FVec fVecSparse = FVec.Transformer.fromMap(
                new java.util.HashMap<Integer, Double>() {{
                    put(2, 32.);
                    put(5, 16.);
                    put(6, -8.);
                }});

        // Predict probability or classification
        double[] prediction = predictor.predict(fVecDense);

        // prediction[0] has
        //    - probability ("binary:logistic")
        //    - class label ("multi:softmax")

        // Predict leaf index of each tree
        int[] leafIndexes = predictor.predictLeaf(fVecDense);

        // leafIndexes[i] has a leaf index of i-th tree
    }
}
```


# Benchmark

Throughput comparison to [xgboost4j 1.1](https://github.com/dmlc/xgboost/tree/master/java/xgboost4j) by [xgboost-predictor-benchmark](https://github.com/komiya-atsushi/xgboost-predictor-benchmark).

| Feature           | xgboost-predictor | xgboost4j      |
| ----------------- | ----------------: | -------------: |
| Model loading     |    49017.60 ops/s | 39669.36 ops/s |
| Single prediction |  6016955.46 ops/s |  1018.01 ops/s |
| Batch prediction  |    44985.71 ops/s |     5.04 ops/s |
| Leaf prediction   | 11115853.34 ops/s |  1076.54 ops/s |

Xgboost-predictor-java is about **6,000 to 10,000 times faster than** xgboost4j on prediction tasks.


# Supported models, objective functions and API

- Models
    - "gblinear"
    - "gbtree"
    - "dart"
- Objective functions
    - "binary:logistic"
    - "binary:logitraw"
    - "multi:softmax"
    - "multi:softprob"
    - "reg:linear"
    - "rank:pairwise"
- API
    - Predicts probability or classification
        - `Predictor#predict(FVec)`
    - Outputs margin
        - `Predictor#predict(FVec, true /* output margin */)`
    - Predicts leaf index
        - `Predictor#predictLeaf(FVec)`
