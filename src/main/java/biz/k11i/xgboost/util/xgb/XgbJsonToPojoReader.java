package biz.k11i.xgboost.util.xgb;

import biz.k11i.xgboost.tree.xgb.gen.XgbMainPojo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class XgbJsonToPojoReader {

    private static final Logger logger = LoggerFactory.getLogger(XgbJsonToPojoReader.class);

    public XgbMainPojo readXgbJsonToPojo(String modelFilePath) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(modelFilePath);
        File file = new File(resource.getFile());
        XgbMainPojo xgbMainPojo = objMapper.readValue(file, XgbMainPojo.class);
        return xgbMainPojo;
    }

    public static float convertToFloat(String inputStr) {
        return Float.parseFloat(inputStr);
    }

    public static int convertToInt(String inputStr) {
        return Integer.parseInt(inputStr);
    }

}
