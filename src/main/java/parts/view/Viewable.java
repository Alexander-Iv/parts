package parts.view;

import parts.Part;

import java.lang.annotation.Annotation;

public interface Viewable {
    String view(Object source, boolean withHeader);
    String view(Class source, boolean withHeader);
}
