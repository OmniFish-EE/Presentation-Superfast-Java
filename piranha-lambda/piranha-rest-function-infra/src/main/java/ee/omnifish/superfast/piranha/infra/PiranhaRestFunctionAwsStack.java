package ee.omnifish.superfast.piranha.infra;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.Alias;
import software.amazon.awscdk.services.lambda.AliasProps;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.IVersion;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Version;
import software.amazon.awscdk.services.lambda.VersionProps;
import software.amazon.awscdk.services.logs.RetentionDays;

public class PiranhaRestFunctionAwsStack extends Stack {

    public PiranhaRestFunctionAwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PiranhaRestFunctionAwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here        
        Function myFunction = new Function(this, "myFunction", FunctionProps.builder()
                .functionName("PiranhaRestFunction")
                .description("Function for the Superfast Java presentation")
                .handler("ee.omnifish.superfast.piranha.PiranhaFunction::handleRequest")
                .runtime(Runtime.JAVA_17)
                .environment(Map.of("JAVA_TOOL_OPTIONS", "-XX:+TieredCompilation -XX:TieredStopAtLevel=1"))
                .code(Code.fromAsset("../piranha-rest-function/target/piranha-rest-function-package.jar"))
                .memorySize(3_000)
                .logRetention(RetentionDays.ONE_DAY)
                .build());

        enableSnapstart(myFunction);

        publishFunctionVersion(myFunction, "MyVersion");
    }

    private void publishFunctionVersion(Function myFunction, String version) {
        Version myVersion = new Version(this, "version-" + version, new VersionProps() {
            @Override
            public IFunction getLambda() {
                return myFunction;
            }
        });
        new Alias(this, "alias-" + version, new AliasProps() {
            @Override
            public String getAliasName() {
                return version;
            }

            @Override
            public IVersion getVersion() {
                return myVersion;
            }
        });
    }

    private void enableSnapstart(Function myFunction) {
        ((CfnFunction) myFunction.getNode().getDefaultChild())
                .addPropertyOverride("SnapStart", new Object() {
                    @JsonProperty("ApplyOn")
                    public String getApplyOn() {
                        return "PublishedVersions";
                    }
                });
    }
}
