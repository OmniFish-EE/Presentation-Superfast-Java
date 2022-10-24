package helloworld;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationBuilder;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;

/**
 * The main class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloWorld {

    /**
     * Main method.
     * 
     * @param arguments the command-line arguments.
     * @throws Exception when an error occurs.
     */
    public static void main(String[] arguments) throws Exception {
        long millisStart = System.currentTimeMillis();

        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("HelloWorld", new HelloWorldServlet())
                .servletMapping("HelloWorld", "")
                .build();
        
        HttpWebApplicationServer webApplicationServer = new HttpWebApplicationServer();
        webApplicationServer.addWebApplication(webApplication);
        webApplicationServer.initialize();
        webApplicationServer.start();
        
        DefaultHttpServer server = new DefaultHttpServer(8080, webApplicationServer, false);
        server.start();

        System.out.println("INFO: Piranha started in " + (System.currentTimeMillis() - millisStart) + " milliseconds.");
        System.out.println("INFO: Open browser at http://localhost:8080/");
    }
}
