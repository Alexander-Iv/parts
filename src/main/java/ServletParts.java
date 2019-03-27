import db.Connector;
import filter.Filter;
import parts.Part;
import parts.Parts;
import parts.view.part.Db;
import parts.view.part.Html;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;

@WebServlet(
        urlPatterns = {"/list", "/filter", "/sort", "/reset"},
        initParams = {
                @WebInitParam(name = "db.user.name.default", value = "uparts", description = "Default database user name"),
                @WebInitParam(name = "db.user.password.default", value = "1111", description = "Default database user password")
        })
public class ServletParts extends HttpServlet {
    Logger logger = Logger.getGlobal();
    //Html filter = new Html();
    Html filter;
    Db dbView;
    PrintWriter out;
    Map<String, String> prop = new HashMap<>();

    public ServletParts() {
        try{
            //handler.setEncoding("UTF-8");
            //handler = new FileHandler(this.getServletName() + ".log");
            //logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            //logger.addHandler(handler);

        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        Parts parts = new Parts();
        out = resp.getWriter();

        out.printf("<html>");
        out.printf("<head>");
        out.printf("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/styles.css\">");
        out.printf("<title>"+ this.getServletContext().getServletContextName() +"</title>");
        out.printf("</head>");
        out.printf("<body>");

        //out.println("uri!!!" + req.getRequestURI() + "<br>");
        //out.println("uri!!!" + System.getProperties() + "<br>");

        try {
            Connector.setConnection().setStatement();
            Connector.getConnector().setResultSet((dbView = new Db()).view(/*new Part()*/Part.class, true));
//            if (getServletContext().getAttribute("Connection") == null) {
//                getServletContext().setAttribute("Connection", Connector.getConnection());
//            }
            //out.println("DB View:"+ dbView +"<br>");
            if (req.getRequestURI().contains("reset")) {

                (getServletContext().getAttribute("filter") == null ? filter.getFilterProperties(Part.class) : ((Html) getServletContext().getAttribute("filter")).getFilterProperties()).forEach(fff -> {
                    //Меняем значениями из параметров запроса
                    fff.getInputs().forEach((s, s2) -> {
                        fff.replaceInputVal(s, "");
                    });
                    //выводим информацию по фильтрам
                    fff.getInputs().forEach((s, s2) -> {
                        //out.println("k=" + s + " v=" + s2 + "<br>");
                    });
                });

                //prop.replaceAll((s, s2) -> s2="");
                //if (prop != null) prop.forEach((s, s2) -> /*prop.remove(s));//*/prop.replace(s, ""));
                //out.println("" + req.getRequestURI() + " " + getServletContext().getContextPath() + " " + getServletContext().getContext("reset"));
                resp.sendRedirect(getServletContext().getContextPath());
            }

            if (req.getRequestURI().contains("filter")) {

                // получаем значения параметров запроса
                req.getParameterMap().forEach((s, strings) -> {
                    if (prop.containsKey(s)) prop.replace(s, strings[0]);
                    else prop.put(s, strings[0]);
                });

                (getServletContext().getAttribute("filter") == null ? filter.getFilterProperties(Part.class) : ((Html) getServletContext().getAttribute("filter")).getFilterProperties()).forEach(fff -> {
                    //Меняем значениями из параметров запроса
                    fff.getInputs().forEach((s, s2) -> {
                        fff.replaceInputVal(s, prop.get(s));
                    });
                    //выводим информацию по фильтрам
                    fff.getInputs().forEach((s, s2) -> {
                        //out.println("k=" + s + " v=" + s2 + "<br>");
                    });
                });
            }

            //генерируем фильтр
            filter = (getServletContext().getAttribute("filter") == null ?  new Html() : ((Html) getServletContext().getAttribute("filter")));
            //filter = new Html();
            //filter = (getServletContext().getAttribute("filter") == null ? new Html() : (Html) getServletContext().getAttribute("filter"));

            out.println(filter.toFilterForm(Part.class, (getServletContext().getAttribute("filter") == null ? null : filter.getFilterProperties())));
//            out.println(getServletContext().getAttribute("filter") == null
//                    ? filter.toFilterForm(Part.class, new ArrayList<>())
//                    : ((Html) getServletContext().getAttribute("filter")).toFilterForm(Part.class, ((Html) getServletContext().getAttribute("filter")).getFilterProperties() ));
            //out.println(filter.toFilterForm(Part.class, getServletContext().getAttribute("filter") == null ? new ArrayList<>() : ((Html) getServletContext().getAttribute("filter")).getFilterProperties(Part.class) ) );

            dbView.DbToObjects(Connector.getResultSet(), parts.getPartsList(), Part.class);

            if (req.getRequestURI().contains("sort")) {
                Integer sortCounter = (Integer)getServletContext().getAttribute("sortCounter-"+req.getParameter("name"));
                if(sortCounter==null) sortCounter=1;
                else sortCounter++;

                parts.sort(req.getParameter("name"), sortCounter%2==0 ? Parts.orderType.ASC : Parts.orderType.DESC);

                //out.println("</br>req.parameter=" + req.getParameter("name"));
                getServletContext().setAttribute("sortCounter-"+req.getParameter("name"), sortCounter);
                //out.println("</br>sortCounter=" + "sortCounter-"+req.getParameter("name") + " count=" + getServletContext().getAttribute("sortCounter-"+req.getParameter("name")) + " " + (sortCounter%2==0));

                //out.println("</br>parts=" + new Html().ArrayToHtmlTable(parts.getPartsList(), Part.class));
            }

            out.println(new Html().ArrayToHtmlTable(parts.getPartsList(), Part.class));

            getServletContext().setAttribute("filter", filter);
//            out.println("HTML:\n<br>" + ((Html) getServletContext().getAttribute("filter")));
//            out.println("FILTER PROPERTIES:\n<br>" + ((Html) getServletContext().getAttribute("filter")).getFilterProperties() );

        } catch (Exception e) {
            out.printf("ERROR " + e.fillInStackTrace());
            logger.throwing("ServletParts", "doGet", e.fillInStackTrace());
            throw new RuntimeException("Errors in ServletParts!!! " + e.getMessage() + "\n\n" + e.fillInStackTrace());
        }
        finally {
            //Connector.close();
        }
        //out.println("\nOK");


        out.printf("</body>");
        out.printf("</html>");
    }

//    @Override
//    public void destroy() {
//        super.destroy();
//        Connector.close();
//    }
}