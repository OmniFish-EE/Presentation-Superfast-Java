package ee.omnifish.superfast.piranha;

import static java.lang.System.Logger.Level.INFO;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.embedded.EmbeddedResponseBuilder;
import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.crac.Core;
import org.crac.Resource;

public class PiranhaFunction implements RequestStreamHandler {

    static final EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
            .extensions(AnnotationScanExtension.class,
                    MyExtension.class,
                    CoreProfileExtension.class)
            .build()
            .start();

    static {
        Core.getGlobalContext().register(new Resource() {
            @Override
            public void beforeCheckpoint(org.crac.Context<? extends Resource> cntxt) throws Exception {
                // Preload as much as possible eagerly so that it's prepared to handle requests
                // after Snapstart restoration
                warmupPiranha(piranha);
            }

            @Override
            public void afterRestore(org.crac.Context<? extends Resource> cntxt) throws Exception {
            }

        });
    }

    @Override
    public void handleRequest(InputStream in, OutputStream out, Context ctx) throws IOException {

        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/")
                .method("POST")
                .build();
        request.setInputStream(in);
        request.setContentType(MediaType.APPLICATION_JSON);
        request.setHeader("Content-Type", MediaType.APPLICATION_JSON);

        EmbeddedResponse response = new EmbeddedResponseBuilder().build();
        response.setOutputStream(new DelegatingServletOutputStream(out));

        try {

            piranha.service(request, response);

        } catch (IOException | ServletException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void main(String[] args) throws IOException, ServletException {
        System.Logger logger = Logging.getLoggerForEnclosingClass(new Object() {
        });
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/")
                .method("POST")
                .build();
        request.setInputStream(new ByteArrayInputStream("{ \"name\" : \"John\" }".getBytes(StandardCharsets.UTF_8)));
        request.setContentType(MediaType.APPLICATION_JSON);
        request.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        final EmbeddedResponse response = piranha.service(request);
        logger.log(INFO, () -> "Response: " + response.getResponseAsString());

    }

    // execute a dummy request to initialize Jersey, ...
    private static void warmupPiranha(EmbeddedPiranha piranha) {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/")
                .method("POST")
                .build();
        request.setInputStream(new ByteArrayInputStream("{}".getBytes()));
        request.setContentType(MediaType.APPLICATION_JSON);
        request.setHeader("Content-Type", MediaType.APPLICATION_JSON);

        EmbeddedResponse response = new EmbeddedResponseBuilder().build();
        response.getResponseAsString();

        try {

            piranha.service(request, response);

        } catch (IOException | ServletException ex) {
            throw new RuntimeException(ex);
        }

    }

}
