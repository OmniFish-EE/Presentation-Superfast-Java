package ee.omnifish.superfast.quarkus.infra;

import java.util.Arrays;
import static java.util.Collections.singletonList;
import java.util.Map;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;

public class QuarkusFunctionNativeAwsStack extends Stack {

    public QuarkusFunctionNativeAwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public QuarkusFunctionNativeAwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function example = new Function(this, "Example", FunctionProps.builder()
        .functionName("QuarkusFunctionNative")
        .description("Function for the Superfast Java presentation")
        .handler("io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest")
        .runtime(Runtime.PROVIDED)
        .environment(Map.of("DISABLE_SIGNAL_HANDLERS", "true"))
        .code(Code.fromAsset("../quarkus-function-native/target/function.zip"))
        .memorySize(256)
        .logRetention(RetentionDays.ONE_DAY)
        .build());
    }
}
