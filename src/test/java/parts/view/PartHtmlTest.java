package parts.view;

import org.junit.jupiter.api.Test;
import parts.Part;
import parts.entity.annotations.Field;
import parts.entity.annotations.Id;
import parts.entity.annotations.ObjectId;
import parts.view.part.Html;

import java.util.Date;

public class PartHtmlTest {
    Html ph = new Html();
    Part p1 = new Part(1, "56H212-01", "HPC Blade 7", "CH-DAL",
            64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

    @Test
    void test() {
        //System.out.println(p1.toString());
        Html view = new Html();
        //System.out.println(view.view(p1, new Class[] {Id.class, Field.class}, true));
        //System.out.println(view.view(p1, Id.class, true));
        //System.out.println(view.view(p1, Id.class, true));
        System.out.println(view.view(p1.getClass(), true));
        System.out.println(((Html) view).toTable());
        //System.out.println(((Html) view).toTable("title=\"Parts\""));
        //ph.view(p1);
    }
}
