package ee.omnifish.superfast.piranha.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;


public class PiranhaRestFunctionInfra {
    public static void main(final String[] args) {
        App app = new App();

        new PiranhaRestFunctionAwsStack(app, "PiranhaRestFunctionStack", StackProps.builder()
                .build());

        app.synth();
    }
}

