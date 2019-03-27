package parts.entity.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Field {
    String name();
//    PartHtml name();
//    PartDb nameInDB();
//    boolean print() default true;
    String viewName() default "";
    boolean print() default true;
}