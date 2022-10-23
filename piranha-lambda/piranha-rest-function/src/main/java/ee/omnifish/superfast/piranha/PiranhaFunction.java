package ee.omnifish.superfast.piranha;

import cloud.piranha.core.api.AnnotationInfo;
import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.extension.annotationscan.internal.InternalAnnotationScanAnnotationInfo;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PiranhaFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    static final EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
            .extensions(AnnotationScanExtension.class,
                    MyExtension.class,
                    CoreProfileExtension.class)
            //            .servletMapped(PiranhaLambdaSimpleServlet.class, "/")
            .build()
            .start();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent awsRequest, Context context) {

        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath(awsRequest.getPath())
                .build();

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
        final EmbeddedResponse response = piranha.service(request);
        System.out.println("Response: " + response.getResponseAsString());
    }

}
