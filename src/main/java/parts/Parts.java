package parts;

import parts.annotations.Sortable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.logging.Logger;

public class Parts implements Serializable {
    private ArrayList<Part> partsList;

    public enum orderType {
        ASC, DESC;
    }

    public Parts() {
        partsList = new ArrayList<>();
    }

    public ArrayList<Part> getPartsList() {
        return partsList;
    }

    public void add(Part partNew) {
        partsList.add(partNew);
    }

//    public void sort(PartHtml field, orderType order) {
//        Logger.getGlobal().info(orderType.ASC + " = " + order.toString() + " -> " + (orderType.ASC == order));
//        Logger.getGlobal().info(PartHtml.PART_NAME + " = " + field.toString() + " -> " + (PartHtml.PART_NAME == field));
//        if (orderType.ASC == order) {
//            if (PartHtml.PART_NAME == field)
//                partsList.sort(Comparator.comparing(Part::getPartName));
//            else if (PartHtml.PART_NUMBER == field)
//                partsList.sort(Comparator.comparing(Part::getPartNumber));
//            else if (PartHtml.VENDOR == field)
//                partsList.sort(Comparator.comparing(Part::getVendor));
//            else if (PartHtml.QTY == field)
//                partsList.sort(Comparator.comparing(Part::getQty));
//            else if (PartHtml.SHIPPED == field)
//                partsList.sort(Comparator.comparing(Part::getShipped));
//            else if (PartHtml.RECEIVED == field)
//                partsList.sort(Comparator.comparing(Part::getReceived));
//
//        } else if (orderType.DESC == order) {
//            if (PartHtml.PART_NAME == field)
//                partsList.sort(Comparator.comparing(Part::getPartName).reversed());
//            else if (PartHtml.PART_NUMBER == field)
//                partsList.sort(Comparator.comparing(Part::getPartNumber).reversed());
//            else if (PartHtml.VENDOR == field)
//                partsList.sort(Comparator.comparing(Part::getVendor).reversed());
//            else if (PartHtml.QTY == field)
//                partsList.sort(Comparator.comparing(Part::getQty).reversed());
//            else if (PartHtml.SHIPPED == field)
//                partsList.sort(Comparator.comparing(Part::getShipped).reversed());
//            else if (PartHtml.RECEIVED == field)
//                partsList.sort(Comparator.comparing(Part::getReceived).reversed());
//        }
//    }
    public void sort(String name, orderType order) {
        //System.out.println(Part.class.getAnnotation(Sortable.class));
        //System.out.println(Part.class.getDeclaredField(field.getFieldName()).getAnnotation(Sortable.class));
        //if (Part.class.getDeclaredAnnotation(Sortable.class) != null) {
            try {
                Method method;
                Field field;
                String fieldGetter;

                /*преобразуем имя Аннотации к имени поля, например. "Part Id" преобразуется к partId*/
                if(name.substring(0,1).toLowerCase() != name.substring(0,1)) {
                    if (name.contains(" ")) {
                        name = name.replace(" ", "");
                    }
                    name = name.substring(0,1).toLowerCase() + name.substring(1,name.length());
                }


                System.out.println(Part.class.getDeclaredField(name).getAnnotation(Sortable.class));
                //Ищем поле(по имени) класса Part с аннотацией Sortable
                if ((field = Part.class.getDeclaredField(name)).getAnnotation(Sortable.class) != null) {

                    fieldGetter = "get"
                            + field.getName().substring(0,1).toUpperCase()
                            + field.getName().substring(1, field.getName().length());

                    if ((method = Part.class.getDeclaredMethod(fieldGetter)) != null ) {
                        //partsList.sort(Comparator.comparing());
                        partsList.sort((o1, o2) -> {
                            int res = 0;
                            try {
//                                System.out.println("int?" + (method.getReturnType().getSimpleName().compareTo("int")));
//                                System.out.println("String?" + (method.getReturnType().getSimpleName().compareTo("String")));
//                                System.out.println("Date?" + (method.getReturnType().getSimpleName().compareTo("Date")) + "\n");
                                if (method.invoke(o1) == null && method.invoke(o2) == null) res = 0;
                                else if (method.invoke(o1) == null && method.invoke(o2) != null) res = -1;
                                else if (method.invoke(o1) != null && method.invoke(o2) == null) res = 1;
                                else {

                                    if (method.getReturnType().getSimpleName().compareTo("Integer") == 0) {
                                        //System.out.println("return " + Integer.compare((Integer)method.invoke(o1), (Integer)method.invoke(o2)));
                                        res = Integer.compare((Integer)method.invoke(o1), (Integer)method.invoke(o2));

                                    } else if (method.getReturnType().getSimpleName().compareTo("String") == 0) {
                                        //System.out.println("return " + method.invoke(o1).toString().compareTo(method.invoke(o2).toString()));
                                        res = method.invoke(o1).toString().compareTo(method.invoke(o2).toString());

                                    } else if (method.getReturnType().getSimpleName().compareTo("Date") == 0) {
                                        //System.out.println("return " + ((Date)method.invoke(o1)).compareTo((Date)method.invoke(o2)));
                                        res = ((Date)method.invoke(o1)).compareTo((Date)method.invoke(o2));
                                    }

                                }
//                                System.out.println("invoke(o2) " + method.invoke(o2).toString() + " " + method.getReturnType().getSimpleName());
//                                System.out.println("invoke(o1) " + method.invoke(o1).toString() + " " + method.getReturnType().getSimpleName());
                                //if (method.invoke(o1) != method.invoke(o2)) return 1;
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                new RuntimeException(fieldGetter + " Invoke Exception. Error: " + e.getMessage());
                            }
                            if (orderType.DESC == order) {
                                res = -res;
                            }
                            return res;
                        });

                        //method.invoke()
                        //method.getReturnType()
                    } else {
                        new RuntimeException(fieldGetter + " NOT FOUND!");
                    }

                    //System.out.println(fieldGetter);
                }
            } catch (NoSuchFieldException | NoSuchMethodException e) {
                throw new RuntimeException("Not found! Error message: " + e.getMessage());
            }
        //}
        //method.getAnnotation();
        //Part.class.getEnclosingMethod().getAnnotation(Part.class);
    }
}
