package ee.omnifish.superfast.plainjava.infra;

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

public class PlainJavaFunctionAwsStack extends Stack {

    public PlainJavaFunctionAwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PlainJavaFunctionAwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function example = new Function(this, "Example", FunctionProps.builder()
        .functionName("PlainJavaFunction")
        .description("Function for the Superfast Java presentation")
        .handler("ee.omnifish.superfast.plainjava.PlainJavaFunction::handleRequest")
        .runtime(Runtime.JAVA_11)
//        .environment(Map.of("JAVA_TOOL_OPTIONS", "-XX:TieredStopAtLevel=1"))
        .code(Code.fromAsset("../plain-java-function/target/plain-java-function-package.jar"))
        .memorySize(3_000)
        .logRetention(RetentionDays.ONE_DAY)
        .build());
    }
}
