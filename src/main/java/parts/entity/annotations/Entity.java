package parts.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@FunctionalInterface
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Entity {
    //String value();
    String name();
    //String name();
    //PartHtml name();
    //PartDb nameInDB();
    //@Printable print();
    //boolean print() default true;
    //String viewName() default "";
    //boolean print() default true;
}