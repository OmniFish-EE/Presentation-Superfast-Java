package ee.omnifish.superfast.piranha;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PiranhaFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    static final EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
            .extensions(AnnotationScanExtension.class,
                    MyExtension.class,
                    CoreProfileExtension.class)
            .build()
            .start();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent awsRequest, Context context) {

        System.out.println("Request: " + awsRequest);
        
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath(awsRequest.getPath())
                .build();
        
        if (awsRequest.getBody() != null) {
            request.setMethod("POST");
            request.setInputStream(new ByteArrayInputStream(awsRequest.getBody().getBytes(StandardCharsets.UTF_8)));
            request.setContentType(MediaType.APPLICATION_JSON);
            request.setHeader("Content-Type", MediaType.APPLICATION_JSON);
        }

        try {

            final EmbeddedResponse response = piranha.service(request);

            return new APIGatewayProxyResponseEvent()
                    .withMultiValueHeaders(piranhaResponseHeadersAsMap(response))
                    .withIsBase64Encoded(false)
                    .withStatusCode(response.getStatus())
                    .withBody(response.getResponseAsString());
        } catch (IOException | ServletException ex) {
            throw new RuntimeException(ex);
        }

    }

    private static Map<String, List<String>> piranhaResponseHeadersAsMap(final EmbeddedResponse response) {
        return response.getHeaderNames()
                .stream().collect(Collectors.toMap(
                        i -> i,
                        i -> new ArrayList<>(response.getHeaders(i))
                ));
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
