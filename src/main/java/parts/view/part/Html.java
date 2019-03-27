package parts.view.part;

import filter.Filter;
import parts.entity.annotations.*;
import parts.annotations.Printable;
import parts.view.View;

import java.lang.Object;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class Html extends View/*implements Viewable*/ {

    //private String view = "";
    private String datePattern = "MMM dd, yyyy";
    //private Object src;


    public String getView() {
        return view;
    }

    @Override
    public String view(Object source, boolean withHeader) {
        /*HEADER*/
        Properties thProperty;

        if (withHeader) view += tagWrap("tr", toTag(source,true,new String[]{"th"}, new Map[]{new HashMap()}));
        //DATA
        view += tagWrap("tr", toTag(source,false, new String[]{"td"}, new Map[]{new HashMap()}));

        return view;
    }

    public String toFilterForm(Class<?> source, ArrayList<? extends Filter> values) {
        view = "";

        Map<String,String> table = new HashMap<>();
        //table.put("action","filter");
        table.put("class","filter");

        Map<String,String> form = new HashMap<>();
        form.put("action","filter");
        form.put("class","filter");

        Map<String,String> submit = new HashMap<>();
        submit.put("type","submit");
        //submit.put("name","filter");
        submit.put("class","submit");
        //submit.put("align","center");
        submit.put("value","Filter");

        Map<String,String> reset = new HashMap<>();
        reset.put("type","button");
        reset.put("name","reset");
        reset.put("class","reset");
        //reset.put("align","center");
        //reset.put("value","reset");

        Map<String,String> tdSubmit = new HashMap<>();
        tdSubmit.put("class","submit");
        tdSubmit.put("align","center");

        Map<String,String> label = new HashMap<>();
        label.put("class","submit");
        label.put("align","right");


        Map<String,String> filterClass = new HashMap<>();
        filterClass.put("class","filter");
        filterClass.put("align","center");

        Map<String,String> aReset = new HashMap<>();
        aReset.put("class","filter");
        aReset.put("href","reset");

        ArrayList<Filter> filterProperties = (values == null ? getFilterProperties(source) : getFilterProperties());

        Map<String,String> input = new HashMap<>();
        input.put("type","text");
        input.put("align","center");
        input.put("name","$header-name$");
        input.put("value","");


        view += tagsWrap("Filter", new String[]{"caption"}, new Map[]{filterClass});

        filterProperties.forEach(filter -> {

            view += tagsWrap(filter.getAnnotationName(), new String[]{"th"}, new Map[]{filterClass});

            filter.getInputs().forEach((s, s2) -> {

                view += tagsWrap(s.contains(filter.getFieldName() + filter.PREF_START) ? /*"After"*/filter.PREF_START :
                                s.contains(filter.getFieldName() + filter.PREF_END) ? /*"Before"*/filter.PREF_END : "",
                        new String[]{"label", "td"}, new Map[]{label, new HashMap()}, new boolean[]{false, false});

                input.replace("name", s);
                if (values != null) {
                    //logger.exiting("Html","toFilterForm", "\nname=" + s + " value=" + values + "\n" );

//                    for (int i=0; i<values.size(); i++) {
//                        if (values.get(i).getFieldName() == s) {
//                            logger.exiting("Html","toFilterForm", "name=" + s + " value=" + o.getInputs().getOrDefault(s, ""));
//                        }
//                    }

                    values.forEach(o -> {
                        //input.replace("value", o.getInputs().get(s) == null ? "" : o.getInputs().get(s));
                        o.getInputs().forEach((s1, s21) -> {
                            if (s1.contains(s)) {
                                input.replace("value", o.getInputs().getOrDefault(s, ""));
                                logger.exiting("Html","toFilterForm", "name=" + s1 + " value=" + s21 + " valueNew=" + o.getInputs().getOrDefault(s, ""));
                            }
                        });

                        //logger.exiting("Html","toFilterForm", "\no=" + o);

                        if (o.getInputs().containsKey(s) /*&& o.getInputs().get(s) != null*/) {
                            //input.replace("value", o.getInputs().getOrDefault(s, ""));
                        }
                    });
                }

                view += tagsWrap("", new String[]{"input", "td"}, new Map[]{input, filterClass}, new boolean[]{true, false});
                view += tagsWrap("", new String[]{"td", "td"}, new Map[]{new HashMap(), new HashMap()});

            });

            view += tagsWrap("", new String[]{"tr"}, new Map[]{filterClass});

        });

        view = tagWrap(view, "table", table, false);
        view += tagWrap("", "input", submit, true);
        //view += tagWrap(tagWrap("button", reset, false), "a", aReset, false);
        view += tagWrap(tagWrap("button", reset, "Reset"), "a", aReset, false);
        view = tagWrap(view, "form", form, false);

        //logger.exiting("Html","toFilterForm", "\nview=" + view);
        return view;
    }

    public String ArrayToHtmlTable(ArrayList<?> array, Class target) {
        //if (!array.isEmpty()) {
            /*Преобразуем коллекцию к html полям(tr=th+td)*/
            Map<String,String> main = new HashMap<>();
            main.put("class","data");

            Map<String,String> a = new HashMap<>();
            a.put("href","sort?name=$header-name$");
            //a.put("title","aaatitle");
            for(int i=0; i < array.size(); i++){
                //if (i==0) view += tagWrap("tr", toTag(target,true, new String[]{"a","th"}, new Map[]{a, new HashMap()})) + "\n";

                //tr -> input -> form -> th
                //
                if (i==0) view += tagWrap("tr", toTag(target,true, new String[]{"a","th"}, new Map[]{a, main})) + "\n";

                view +=  tagWrap("tr", toTag(array.get(i), false, new String[]{"td"}, new Map[]{main})) + "\n";

                //logger.exiting("Html","ArrayToHtmlTable", "array["+i+"]=" + array.get(i) + "; view=\n" + view);
            }
            //logger.exiting("Html","ArrayToHtmlTable", "\nview=" + view + "\n");

            /*Преобразуем в таблицу*/
            view = tagWrap("table", main, view);
        //}
        return view;
    }

    public String toTable(){
        return (view = tagWrap("table", view));
    }

    /**Функция добавления параметров в тег
     * */
    public String addAttrByTag(String tag, String[] attr) {
        String attrList = "";

        for (int i=0; i < attr.length; i++) {
            attrList += attr[i];
        }

        if (view.contains("<" + tag + ">") || view.contains("<" + tag + "/>")) {
            view = view.replace("<" + tag + ">","<" + tag + " " + attrList + ">");
        }

        return view;
    }

//    private String tagWrap(String tag, String content) {
//        return "<" + tag + ">" + content + "</" + tag + ">";
//    }

    /**функция оборачивает содержимое тегом*/
    private String tagWrap(String tag, Object content) {
        return "<" + tag + ">" + content + "</" + tag + ">";
    }

//    private String tagWrap(String tag, Object content, boolean isSingleTag) {
//        return isSingleTag ? "<" + tag + "/>" : "<" + tag + ">" + content + "</" + tag + ">";
//    }

    private String tagWrap(String tag, Map<String,String> tagProperty, Object content) {
        String properties = "";
        for(String name: tagProperty.keySet()){
            //properties += " " +  name + "=\"" + tagProperty.get(name) + "\"";
            properties += " " +  name + "=\""
                    + (tagProperty.get(name).contains("$header-name$")
                    ? tagProperty.get(name).replace("$header-name$", content.toString())
                    : tagProperty.get(name)) + "\"";
        }
        //properties.contains("$header-name$") ? properties.replace("header-name", content) : properties

        return "<" + tag + properties + ">" + content + "</" + tag + ">";
    }

    private String tagWrap(Object content, String tag, Map<String,String> tagProperty, boolean isSingleTag) {
        String properties = "";
        for(String name: tagProperty.keySet()){
            properties += " " +  name + "=\"" + tagProperty.get(name) + "\"";
        }
        return  isSingleTag ? "<" + tag + properties + "/>" : "<" + tag + properties + ">" + content + "</" + tag + ">";
    }

    /**вкладывваем содержимое в каждый указанный тег попорядку*/
    private Object tagsWrap(Object content, String[] tags) {
        for(int i=0; i<tags.length; i++) {
            content = tagWrap(tags[i], content);
        }
        return content;
    }

    private Object tagsWrap(Object content, String[] tags, Map<String,String>[] tagsProperty) {
        //logger.exiting("Html","tagsWrap", "length="+tagsProperty.length);
        for(int i=0; i<tags.length; i++) {
//            for(int j=0; j<tagsProperty[i][j].length(); j++){
//                //content = tagWrap(tags[i], content);
//            }
            if(tagsProperty!=null) content = tagWrap(tags[i], tagsProperty[i], content);
            else content = tagWrap(tags[i], content);
            //logger.exiting("Html","tagsWrap", "["+i+"]=" + tagsProperty[i]+" length="+tagsProperty.length+" size="+tagsProperty[i].size());
        }
        return content;
    }

    private Object tagsWrap(Object content, String[] tags, Map<String,String>[] tagsProperty, boolean[] isSingleTag) {
        //logger.exiting("Html","tagsWrap", "length="+tagsProperty.length);
        for(int i=0; i<tags.length; i++) {
//            for(int j=0; j<tagsProperty[i][j].length(); j++){
//                //content = tagWrap(tags[i], content);
//            }
            if(tagsProperty!=null) content = tagWrap(content, tags[i], tagsProperty[i], isSingleTag[i]);
            else content = tagWrap(tags[i], content);
            //logger.exiting("Html","tagsWrap", "["+i+"]=" + tagsProperty[i]+" length="+tagsProperty.length+" size="+tagsProperty[i].size());
        }
        return content;
    }

    public String toTag(Class<?> source, boolean isHeader, String[] tags, Map<String,String>[] tagsProperty) {
        if(source == null) return "";
        obj = newInstanceByClass(source);

        return getField("", obj, source.getDeclaredFields(), isHeader, tags, tagsProperty);
    }

    private String toTag(Object source, boolean isHeader, String[] tags, Map<String,String>[] tagsProperty) {
        if(source == null) return "";

        return getField("", source, source.getClass().getDeclaredFields(), isHeader, tags, tagsProperty);
    }




    private String getField(String view, Object source, java.lang.reflect.Field[] fields, boolean isHeader, String[] tags, Map<String, String>[] tagsProperty) {

        for(java.lang.reflect.Field f : fields) {
            if (f.getDeclaredAnnotation(Printable.class) == null) continue;

            if ((annotationValue = getValueNew(f, ObjectId.class)).isEmpty())
                annotationValue = getValueNew(f, ObjectField.class);

            //logger.exiting("Html","toTag", "tag=" + tag + "; annoValue=" + annotationValue + "; isHeader=" + isHeader);

            if (isHeader == true) {
                view += tagsWrap(annotationValue, tags, tagsProperty);
            }
            else {
                try {
                    Method m = source.getClass().getDeclaredMethod(toGetter(f.getName()));

                    //String content = "";

                    if (m.getReturnType().getSimpleName().compareTo("Date") == 0) {

                        if (m.invoke(source) == null) {
                            view += tagsWrap("", tags, tagsProperty);
                        } else {
                            view += tagsWrap(new SimpleDateFormat(datePattern, Locale.ENGLISH).format(m.invoke(source)), tags, tagsProperty);
                        }

                    } else {

                        if (m.invoke(source) == null) {
                            view += tagsWrap("", tags, tagsProperty);
                        } else {
                            view += tagsWrap(m.invoke(source), tags, tagsProperty);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Getter not found! " + e.getMessage());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Invoke error! " + e.getMessage());
                }
                catch (RuntimeException e) {
                    logger.throwing("Html", "toTag", e.fillInStackTrace());
                    throw new RuntimeException(e.fillInStackTrace());
                }
            }
        }
        //logger.exiting("Html","toTag", view);
        return view;
    }

    public ArrayList<Filter> getFilterProperties(Class<?> source) {
        return getFilterProperties(source, new Class[]{ObjectId.class, ObjectField.class}, new Class[] {Printable.class});
    }

//    public ArrayList<Filter> getFilterProperties() {
//        return getFilterProperties();
//    }

    public void setFilterArray(ArrayList<Filter> array) {
        setFilterProperties(array);
        //return getFilterProperties(source, new Class[]{ObjectId.class, ObjectField.class}, new Class[] {Printable.class});
    }


    //    public ArrayList<Filter> getFilterProperties(Class<?> source) {
//        ArrayList<Filter> filterProperties = new ArrayList<>();
//        Filter filter;
//        obj = newInstanceByClass(source);
//
//        for(java.lang.reflect.Field f : obj.getClass().getDeclaredFields()) {
//            if (f.getDeclaredAnnotation(Printable.class) == null) continue;
//
//            filter = new Filter();
//
//            if ((annotationValue = getValueNew(f, ObjectId.class)).isEmpty())
//                annotationValue = getValueNew(f, ObjectField.class);
//
//            filter.setFieldName(f.getName());
//            filter.setOperatorsByReturnTypes(f.getType().getSimpleName());
//            filter.setAnnotationName(annotationValue);
//
//            filterProperties.add(filter);
//        }
//
//        return filterProperties;
//    }
}