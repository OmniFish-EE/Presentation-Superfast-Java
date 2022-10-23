package ee.omnifish.superfast.piranha;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
@RequestScoped
public class PiranhaCloudRestResource {

    @GET
    public String helloWorld() {
        return "Hello from Piranha Cloud REST service in AWS Lambda!";
    }
}