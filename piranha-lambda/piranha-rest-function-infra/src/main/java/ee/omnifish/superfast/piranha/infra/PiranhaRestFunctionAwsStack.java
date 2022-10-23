package ee.omnifish.superfast.piranha.infra;

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

public class PiranhaRestFunctionAwsStack extends Stack {

    public PiranhaRestFunctionAwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PiranhaRestFunctionAwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here
        LayerVersion java17layer = new LayerVersion(this, "Java17Layer", LayerVersionProps.builder()
                .layerVersionName("Java17Layer")
                .description("Java 17")
                .compatibleRuntimes(Arrays.asList(Runtime.PROVIDED_AL2))
                .code(Code.fromAsset("java17layer.zip"))
                .build());
        
        Function exampleWithLayer = new Function(this, "ExampleWithLayer", FunctionProps.builder()
        .functionName("PiranhaRestFunction")
        .description("Function for the Superfast Java presentation")
        .handler("ee.omnifish.superfast.piranha.PiranhaFunction::handleRequest")
        .runtime(Runtime.PROVIDED_AL2)
//        .environment(Map.of("JAVA_TOOL_OPTIONS", "-XX:TieredStopAtLevel=1"))
        .code(Code.fromAsset("../piranha-rest-function/target/piranha-rest-function-package.jar"))
        .memorySize(3_000)
//        .memorySize(256)
        .logRetention(RetentionDays.ONE_DAY)
        .layers(singletonList(java17layer))
        .build());
    }
}
