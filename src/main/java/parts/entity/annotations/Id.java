package parts.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Id {
    String name();
    //PartHtml name();
    //PartDb nameInDB();
    //boolean print() default false;
    String viewName() default "";
    boolean print() default false;
}