package parts.view;

import filter.Filter;
import parts.annotations.Printable;
import parts.entity.annotations.ObjectField;
import parts.entity.annotations.ObjectId;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class View {
    private String ANNOTATION_METHOD = "value";

    protected Logger logger = Logger.getGlobal();
    protected String view = "";
    protected String annotationValue = "";
    //protected boolean containts = false;
    protected Object obj;
    ArrayList<Filter> filterProperties = new ArrayList<>();

    public String view(Class source, boolean withHeader) {
        return view;
    }

    public String view(Object source, boolean withHeader){
        return view;
    }

    public void setFilterProperties(ArrayList<Filter> filterProperties) {
        this.filterProperties = filterProperties;
    }

    public ArrayList<Filter> getFilterProperties() {
        return filterProperties;
    }

    protected String getView() {
        return view;
    }

    protected String getValueNew(Class source, Class...annotations) {
//        logger.entering("View","getValueByClass", new Object[]{source, annotations});
        for (int i=0; i < annotations.length; i++) {
            //logger.entering("View","getValueNew", annotations.length + " " + i + " " + source.getDeclaredAnnotation(annotations[i]));
            if (source.getDeclaredAnnotation(annotations[i]) == null) return "";
        }
//        logger.entering("View","getValueNew", " s=" + source.getFieldName() + " a=" + annotations[0].getSimpleName() + " " + source.getDeclaredAnnotation(annotations[0]));

        try {
//            logger.exiting("View","getValueByClass", this);
            annotationValue = source.getDeclaredAnnotation(annotations[0])
                    .getClass().getDeclaredMethod(ANNOTATION_METHOD)
                    .invoke(source.getDeclaredAnnotation(annotations[0]))
                    .toString();
        } catch (NoSuchMethodException e) {
            return "";
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
            logger.throwing("View","getValueByClass.EXCEPTION="+e.getMessage(), e.fillInStackTrace());
            throw new RuntimeException(e.fillInStackTrace());
        }
        return annotationValue;
    }

    protected String getValueNew(Object source, Class...annotations) {
//        logger.entering("View","getValueByObject", new Object[]{source, annotations});

        for (int i=0; i < annotations.length; i++) {
            //logger.entering("View","getValueNew", annotations.length + " " + i + " " + source.getClass().getDeclaredAnnotation(annotations[i]));
            if (source.getClass().getDeclaredAnnotation(annotations[i]) == null) return "";
        }
//        logger.entering("View","getValueNew", " s=" + source.getClass().getFieldName() + " a=" + annotations[0].getSimpleName() + " " + source.getClass().getDeclaredAnnotation(annotations[0]));

        try {
            //logger.exiting("View","getValue", view + "\n" + annotationValue);
            annotationValue = source.getClass().getDeclaredAnnotation(annotations[0])
                    .getClass().getDeclaredMethod(ANNOTATION_METHOD)
                    .invoke(source.getClass().getDeclaredAnnotation(annotations[0]))
                    .toString();
        } catch (NoSuchMethodException e) {
            return "";
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
            logger.throwing("View","getValueByObject.EXCEPTION="+e.getMessage(), e.fillInStackTrace());
            throw new RuntimeException(e.fillInStackTrace());
        }
        return annotationValue;
    }

    protected String getValueNew(Field source, Class...annotations) {
//        logger.entering("View","getValueByField", new Object[]{source, annotations.toString()});

        for (int i=0; i < annotations.length; i++) {
//            logger.exiting("View","getValueByField000", "" + annotation[i]);
            //logger.entering("View","getValueNew", annotations.length + " " + i + " " + " s=" + source.getFieldName() + " a=" + annotations[i].getSimpleName() + " " + source.getDeclaredAnnotation(annotations[i]));
            if (source.getDeclaredAnnotation(annotations[i]) == null) return "";
            //if (annotation[i] == null) return "";
        }
        //logger.entering("View","getValueNew", " fn=" + source.getName() + " a=" + annotations[0].getSimpleName() + " " + source.getDeclaredAnnotation(annotations[0]).getClass());
        //logger.entering("View","getValueNew", " fn=" + source.getName() + " a=" + annotations[0].getSimpleName() + " ");

        try {
//            logger.exiting("View","getValueByField00", "" + source.toString());
//            logger.exiting("View","getValueByField01", "" + source.getDeclaredAnnotation(annotation).getClass().getMethods().toString());
//            logger.exiting("View","getValueByField02", "" + source.getDeclaredAnnotation(annotation[0]));
//            logger.exiting("View","getValueByField03", "" + source.getDeclaredAnnotation(annotation).getClass().getDeclaredMethod(ANNOTATION_METHOD));
//            logger.exiting("View","getValue", "" + this.toString());
            annotationValue = source.getDeclaredAnnotation(annotations[0])
                    .getClass().getDeclaredMethod(ANNOTATION_METHOD)
                    .invoke(source.getDeclaredAnnotation(annotations[0]))
                    .toString();
        } catch (NoSuchMethodException e) {
            return "";
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
            logger.throwing("View","getValueByField.EXCEPTION", e.fillInStackTrace());
            throw new RuntimeException(e.fillInStackTrace());
        }
//        logger.entering("View","getValueNew", " fn=" + source.getName() + " a=" + annotations[0].getSimpleName() + " annotationValue=" + annotationValue);
        return annotationValue;
    }

    protected String toSetter(String name){
        return "set" + name.substring(0,1).toUpperCase() + name.substring(1,name.length());
    }

    protected String toGetter(String name){
        return "get" + name.substring(0,1).toUpperCase() + name.substring(1,name.length());
    }

    protected Object newInstanceByClass(Class source) {
        try {
            return source.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

    protected ArrayList<Filter> getFilterProperties(Class<?> source, Class[] annotations, Class[] excludes) {
        //ArrayList<Filter> filterProperties = new ArrayList<>();
        Filter filter;
        obj = newInstanceByClass(source);

        for(Field f : obj.getClass().getDeclaredFields()) {

            outer:
            for (int i=0; i < annotations.length; i++) {
                //logger.entering("View","getValueNew", annotations.length + " " + i + " " + source.getClass().getDeclaredAnnotation(annotations[i]));

//                logger.entering("View","getFilterProperties1", "i=" + i
//                        + " src=" + obj.getClass().getSimpleName()
//                        + " fn=" + f.getName()
//                        + " a=" + annotations[i].getSimpleName()
//                        + " Annotations=" + source.getDeclaredAnnotationsByType(annotations[i]) );

                if (f.getDeclaredAnnotation(annotations[i]) == null) continue;
                // не добавляем в коллекцию поля из списка классов исключений
                for (int j=0; j < excludes.length; j++) {
                    if (f.getDeclaredAnnotation(excludes[j]) == null) continue outer;
                }

                if (!(annotationValue = getValueNew(f, annotations[i])).isEmpty()) {
                    filter = new Filter();

                    filter.setFieldName(f.getName());
                    filter.setOperatorsByReturnTypes(f.getType().getSimpleName());
                    filter.setAnnotationName(annotationValue);
                    filter.addInputs(f.getName(), "");

                    filterProperties.add(filter);

                    logger.entering("View","getFilterProperties2", "i=" + i
                            + " fn=" + f.getName()
                            + " a=" + annotations[i].getSimpleName() + " annotationValue=" +  annotationValue);
                }
            }
        }

        return filterProperties;
    }

    @Override
    public String toString() {
        return "View{" +
                "view='" + view + '\'' +
                //", annotationValue='" + annotationValue + '\'' +
                '}';
    }
}