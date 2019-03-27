package parts.view.part;

import parts.annotations.Printable;
import parts.entity.annotations.*;
import parts.view.View;

import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Db extends View {
    private Logger logger = Logger.getGlobal();
    private Object src;

    @Override
    public String view(Class source, boolean withHeader) {
        logger.entering("Db","view", new Object[]{source, withHeader});
        toQuery(source, withHeader);
        logger.exiting("Db","view", view);
        return view;
    }
    
    /** Генерация запроса по полям класса
     * */
    private String toQuery(Class source, boolean withHeader) {
        //logger.entering("Db","toQuery", new Object[]{source});
        view = "SELECT";

        if (withHeader) {
            int fieldCount = 0;
            for(java.lang.reflect.Field f : source.getDeclaredFields()) {
                if (fieldCount == 0) view += " ";

                view += ( !((annotationValue = getValueNew(f, TableDbId.class)).isEmpty()) && fieldCount != source.getDeclaredFields().length-1 ) ? annotationValue + ", " : annotationValue;
                view += ( !((annotationValue = getValueNew(f, TableDbField.class)).isEmpty()) && fieldCount != source.getDeclaredFields().length-1 ) ? annotationValue + ", " : annotationValue;

                fieldCount++;
            }
        } else view += " *";

        view += " FROM ";

        //for TABLE
        if (source.getDeclaredAnnotation(TableDb.class) != null) {
            view += getValueNew(source, TableDb.class);
        } else {
            throw new RuntimeException("В классе " + source.getClass().getSimpleName() + " необходимо указать аннотацию TableDb");
        }

        view += "\n";

        return view;
    }

    private Object DbToObject(ResultSet resultSet, Class target) throws SQLException {
        /**Создаем экземпляр класса target*/
        try {
            src = target.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.fillInStackTrace());
        }

        /**Выполняем, если запрос содержит какие-либо данные*/
        if (resultSet != null && !resultSet.isBeforeFirst()) {
            for(java.lang.reflect.Field f : src.getClass().getDeclaredFields()) {
                if (f.getDeclaredAnnotation(Printable.class) == null) continue;

                try {
                    getValueNew(f, TableDbId.class/*, Printable.class*/);
                    getValueNew(f, TableDbField.class/*, Printable.class*/);

                    /**Получаем getter из класса ResultSet в зависимости от типа исходного поля
                     * resultSet.getInt; resultSet.getDate; resultSet.getString*/
                    Method method = resultSet.getClass().getDeclaredMethod("get" + (f.getType().getSimpleName().compareTo("Integer") == 0 ? "Int" : f.getType().getSimpleName()), String.class);

                    if (method.invoke(resultSet, annotationValue) != null) {
                        /**Вызываем полученный ранее метод с входным параметром, полученным из аннотации каждого поля.
                         * Например, resultSet.getInt("Part_ID"); resultSet.getInt("QTY"); и т.д.*/
                        src.getClass().getDeclaredMethod(toSetter(f.getName()), f.getType()).invoke(src, method.invoke(resultSet, annotationValue));
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e.fillInStackTrace());
                }
            }
        }
        logger.exiting("Db","DbToObject", src.toString().isEmpty() ? "EQ":"nEQ" + " " + src.toString());
        return src.toString().isEmpty() ? null : src;
    }

    public <T extends ArrayList> T DbToObjects(ResultSet resultSet, T source, Class target) throws SQLException {
        while (resultSet.next()) {
            source.add(DbToObject(resultSet, target));
        }
        logger.exiting("Db","DbToObjects", source.toString());
        return source;
    }

    @Override
    public String toString() {
        return "Db{" +
                "view='" + view + '\'' +
                '}';
    }
}