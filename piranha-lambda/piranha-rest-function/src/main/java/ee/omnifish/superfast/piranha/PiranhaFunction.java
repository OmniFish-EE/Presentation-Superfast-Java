package ee.omnifish.superfast.piranha;

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
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.xml.sax.InputSource;

public class PiranhaFunction implements RequestStreamHandler {

    static final EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
            .extensions(AnnotationScanExtension.class,
                    MyExtension.class,
                    CoreProfileExtension.class)
            .build()
            .start();

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
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/")
                .build();
        EmbeddedResponse response = piranha.service(request);
        System.out.println("Response: " + response.getResponseAsString());

        request = new EmbeddedRequestBuilder()
                .servletPath("/")
                .method("POST")
                .build();
        request.setInputStream(new ByteArrayInputStream("{ \"name\" : \"John\" }".getBytes(StandardCharsets.UTF_8)));
        request.setContentType(MediaType.APPLICATION_JSON);
        request.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        response = piranha.service(request);
        System.out.println("Response: " + response.getResponseAsString());

    }

}
