package ee.omnifish.superfast.quarkus.infra;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;


public class QuarkusFunctionInfra {
    public static void main(final String[] args) {
        App app = new App();

        new QuarkusFunctionAwsStack(app, "QuarkusFunctionStack", StackProps.builder()
                .build());

        app.synth();
    }
}

