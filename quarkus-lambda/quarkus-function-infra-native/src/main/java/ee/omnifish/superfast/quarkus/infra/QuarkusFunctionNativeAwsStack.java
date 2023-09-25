package ee.omnifish.superfast.quarkus.infra;

import java.util.Map;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;

public class QuarkusFunctionNativeAwsStack extends Stack {

    public QuarkusFunctionNativeAwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public QuarkusFunctionNativeAwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function quarkusFunctionNative = new Function(this, "QuarkusFunctionNative", FunctionProps.builder()
                .functionName("QuarkusFunctionNative")
                .description("Function for the Superfast Java presentation")
                .handler("QuarkusFunctionNative")
                .runtime(Runtime.PROVIDED)
                .environment(Map.of("DISABLE_SIGNAL_HANDLERS", "true"))
                .code(Code.fromAsset("../quarkus-function/target/native/function.zip"))
                .memorySize(256)
                .logRetention(RetentionDays.ONE_DAY)
                .build());
    }
}
