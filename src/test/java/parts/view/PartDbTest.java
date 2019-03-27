package parts.view;

import db.Connector;
import org.junit.jupiter.api.Test;
import parts.Part;
import parts.view.part.Db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import java.io.File;
import java.net.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

public class PartDbTest {
    Db dbView = new Db();
    Part p1 = new Part(1, "56H212-01", "HPC Blade 7", "CH-DAL",
            64, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

    @Test
    void test() {
//        System.out.println("\nWithout Headers");
//        System.out.println("~~~~~~~~~~~~~~~");
//        System.out.println(new Db().view(p1, false));

        System.out.println("With Headers");
        System.out.println("~~~~~~~~~~~~");
        //System.out.println(dbView.view(p1, true));
        System.out.println(dbView.view(Part.class, true));

//        String userDir = System.getProperty("user.dir");
//        String fileSep = System.getProperty("file.separator");
//        String contextPath = Paths.get(userDir + fileSep + "src" + fileSep + "main" + fileSep + "webapp" + fileSep + "META-INF" + fileSep + "context.xml").toString();
//        System.out.println( "PATH " + contextPath);
//        ClassLoader loader;
//        Context ctx;
//        try {
//            System.out.println( "PATH " + new URI("file:///"+contextPath.replaceAll(fileSep+fileSep, "/")));
//            //System.out.println("URI " + new URI("file:/"+contextPath.replaceAll(fileSep+fileSep, "/")).toString());
//            //URI uri = new URI("file:/"+contextPath.replaceAll(fileSep+fileSep, "/"));
//            try {//file://
//                loader = new URLClassLoader(new URL[] {new URI("file:///"+contextPath.replaceAll(fileSep+fileSep, "/")).toURL()});
//
//                URI uri = new URI("file:///"+contextPath.replaceAll(fileSep+fileSep, "/"));
//                System.out.println(
//                        //new URLClassLoader(new URL[]{uri.toURL()}).getResource("file:///D:/IdeaProjects/parts/src/main/webapp/META-INF/context.xml")
//                        new URLClassLoader(new URL[]{uri.toURL()}).getURLs()
//                        //ClassLoader.getSystemResourceAsStream(uri.toString())
//                        //uri
//                );
//                Class cls = Class.forName("parts.view.PartDbTest");
//
//                System.out.println(cls.getClass().getResource("/parts/view/context.xml"));
//                //System.out.println(new URI("file:///"+contextPath.replaceAll(fileSep+fileSep, "/")).toURL());
//                //ClassLoader cLoader = this.getClass().getClassLoader().getResource("/context.xml");
//                //System.out.println(cLoader.getResourceAsStream("file:/"+contextPath.replaceAll(fileSep+fileSep, "/")));
//
//                Hashtable env = new Hashtable();
//                env.put(Context.INITIAL_CONTEXT_FACTORY, loader.getResource(contextPath));
//                try {
//                    //InitialContext initContext =
//                    ctx = new InitialContext(env);
//                    System.out.println(ctx.getEnvironment());
//                } catch (NamingException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(loader.getResource("jdbc/partsDB"));
//                System.out.println(loader.getResourceAsStream("jdbc/partsDB"));
//            } catch (MalformedURLException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        System.out.println(userDir
//                + fileSep
//                + "src" + fileSep
//                + "main" + fileSep
//                + "webapp" + fileSep
//                + "META-INF" + fileSep
//                + "context.xml"
//        );
        //Class aClass = ClassLoader.getResource();
        //loader.getResource("");

        //System.out.println(Paths.get("."));
//        System.out.println(PartDbTest.class.getResource("PartDbTest.class").toExternalForm());
//        System.out.println(PartDbTest.class.getResource("PartDbTest.class").getPath());
//        System.out.println(PartDbTest.class.getResource("PartDbTest.class").getQuery());

        //Properties properties = new Properties();
        //properties.setProperty("user","postgres");
        //properties.setProperty("password","1111");
        //properties.setProperty("ssl","false");
        //properties.setProperty("sslmode","disable");
        //Connector.setConnection("jdbc:postgresql://appls-srv:5432/dbparts",
        //Connector.setConnection("jdbc:postgresql://appls-srv:5432/postgres", "org.postgresql.Driver", properties).setStatement();
        //Connector.getConnector().setResultSet(dbView.view(new Part(), true));

        //properties.setProperty("user","bis");
        //properties.setProperty("password","employer");
        //Connector.setConnection("jdbc:oracle:thin:@(DESCRIPTION= (ADDRESS= (PROTOCOL=TCP) (HOST=10.27.114.8) (PORT=1521)) (CONNECT_DATA= (SID=cbisdb)))", "oracle.jdbc.driver.OracleDriver", properties).setStatement();

        System.out.println(Connector.printQuery());
    }
}
