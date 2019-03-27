package parts;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class PartsTest {

    Parts p = new Parts();

    PartsTest(){
        p.add(new Part(1, "56H212-01", "HPC Blade 7", "CH-DAL",
                64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        p.add(new Part(2, "56H212-03", "HPC Blade 7", "CH-DAL",
                64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        p.add(new Part(3, "56H212-02", "HPC Blade 7", "CH-DAL",
                64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        p.add(new Part(4, "56H212-00", "HPC Blade 7", "CH-DAL",
                64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
    }

    //@Test
    public void sortTest() throws NoSuchFieldException {
        System.out.println("BEFIRE\n");
        p.getPartsList().forEach(part -> {System.out.println(part.toString());});

        System.out.println("\n\nAFTER\n");
        p.sort("partName", Parts.orderType.ASC);
        //p.sort("partId", Parts.orderType.ASC);
        //p.sort("received", Parts.orderType.ASC);
        p.getPartsList().forEach(part -> {System.out.println(part.toString());});

        System.out.println("\n\nAFTER DESC\n");
        //p.sort("partName", Parts.orderType.DESC);
        p.sort("partId", Parts.orderType.ASC);
        //p.sort("received", Parts.orderType.DESC);
        p.getPartsList().forEach(part -> {System.out.println(part.toString());});
    }
}
