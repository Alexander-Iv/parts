package filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Filter {

    public enum RETURN_TYPES {
        String("String"), Integer("Integer"), Date("Date");

        private String name;

        RETURN_TYPES(String name) {
            this.name = name;
        }
    }

//    private Logger logger = Logger.getGlobal();
    public final String PREF_START = "After";
    public final String PREF_END = "Before";

    private String fieldName = null;
    private String annotationName = null;
    private RETURN_TYPES returnTypes = null;
    private OPERATORS[] operators = null;

    //private ArrayList<? extends Filter> keyVals = new ArrayList<>();
    private Map<String, String> inputs = new HashMap<>();

    public Map<String, String> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, String> inputs) {
        this.inputs = inputs;
    }

    public void addInputs(String key, String val) {
        if (!inputs.containsKey(key)) {
            for(int i=0; i<operators.length && operators[i].compareTo(OPERATORS.BETWEEN) == 0; i++) {
                inputs.put(key + PREF_START, val);
                inputs.put(key + PREF_END, val);
                return;
            }

            inputs.put(key, val);
        }
    }

    public void replaceInputVal(String key, String val) {
        inputs.replace(key, val);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
//        logger.exiting("Filter","setFieldName", "" + this.fieldName);
    }

    public String getAnnotationName() {
        return annotationName;
    }

    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
//        logger.exiting("Filter","setAnnotationName", "" + this.annotationName);
    }

    public RETURN_TYPES getReturnTypes() {
        return returnTypes;
    }

    public enum OPERATORS {
        GT, GTE, LT, LTE, EQ, LIKE, BETWEEN
    }

    public OPERATORS[] getOperators() {
        return operators;
    }

    public void setOperatorsByReturnTypes (String returnTypes) {
        this.returnTypes = RETURN_TYPES.valueOf(returnTypes);

        switch (this.returnTypes) {
            case String:
                operators = new OPERATORS[]{OPERATORS.LIKE};
                break;
            case Integer:
                operators = new OPERATORS[]{OPERATORS.GT, OPERATORS.GTE};
                break;
            case Date:
                operators = new OPERATORS[]{OPERATORS.BETWEEN};
                break;
        }
//        logger.exiting("Filter","setOperatorsByReturnTypes", "" + returnTypes +" "+ operators);
    }

    @Override
    public String toString() {
        return "Filter{" +
                "fieldName='" + fieldName + '\'' +
                ", annotationName='" + annotationName + '\'' +
                ", returnTypes=" + returnTypes +
                ", operators=" + Arrays.toString(operators) +
                ", inputs=" + inputs.toString() +
                '}';
    }
}
